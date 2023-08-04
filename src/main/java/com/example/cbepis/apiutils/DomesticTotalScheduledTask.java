package com.example.cbepis.apiutils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cbepis.entity.ConfirmedData;
import com.example.cbepis.entity.DomesticTotal;
import com.example.cbepis.service.DomesticTotalService;
import com.example.cbepis.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DomesticTotalScheduledTask {
    @Autowired
    private DomesticTotalService domesticTotalService;
    @Autowired
    private IndexService indexService;

    //定时执行并更新数据库
//    @Scheduled(cron="0 0 0 * * ?")//每天凌晨0点更新
//    @Scheduled(fixedDelay = 20000)//测试用
    public void updateDbScheduledTask() throws ParseException {
        HttpClient httpClient = new HttpClient();
        String apiData = httpClient.getData();


        /*
        * 国内总体数据
        * */
        //FastJson拿数据
        JSONObject jsonObject = JSONObject.parseObject(apiData);
        Object data = jsonObject.get("data");
        //拿到的数据转换成FastJson对象,提取全国的数据ChinaTotal
        JSONObject jsonObjectData = JSONObject.parseObject(data.toString());
        Object chinaTotal = jsonObjectData.get("chinaTotal");
        Object overseaLastUpdateTime = jsonObjectData.get("overseaLastUpdateTime");
        //提取整体数据total
        JSONObject jsonObjectTotal = JSONObject.parseObject(chinaTotal.toString());
        Object total = jsonObjectTotal.get("total");
        //提取total中的数据
        JSONObject jsonObjectTotalData = JSONObject.parseObject(total.toString());
        Object confirm = jsonObjectTotalData.get("confirm");
        Object input = jsonObjectTotalData.get("input");
        Object severe = jsonObjectTotalData.get("severe");
        Object heal = jsonObjectTotalData.get("heal");
        Object dead = jsonObjectTotalData.get("dead");
        Object suspect = jsonObjectTotalData.get("suspect");

        //数据封装
        DomesticTotal dataEntity = new DomesticTotal();
        dataEntity.setConfirm((Integer) confirm);
        dataEntity.setInput((Integer) input);
        dataEntity.setSevere((Integer) severe);
        dataEntity.setHeal((Integer) heal);
        dataEntity.setDead((Integer) dead);
        dataEntity.setSuspect((Integer) suspect);
        //日期类型转换（报错了，处理一下）
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dataEntity.setUpdateTime(format.parse(String.valueOf(overseaLastUpdateTime)));
        //插入数据库
        domesticTotalService.save(dataEntity);
        //清除Redis缓存 保持Redis和mysql的数据一致性
        Jedis jedis=new Jedis("127.0.0.1");
        if(jedis!=null){
            jedis.flushDB();
        }


        /*各省份数据
        * */
        //获取json中的areatree
        JSONArray areaTree = jsonObjectData.getJSONArray("areaTree");
        Object[] objects=areaTree.toArray();//object中包含了全世界所有国家的数据，第三条是中国的
        JSONObject jsonObject1 = JSONObject.parseObject(objects[2].toString());
        //拿到各省份数据
        JSONArray children=jsonObject1.getJSONArray("children");
        Object[] objects1 = children.toArray();
        List<ConfirmedData> list=new ArrayList<>();
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0;i<objects1.length;i++){
            ConfirmedData confirmedData=new ConfirmedData();
            JSONObject jsonObject2 = JSONObject.parseObject(objects1[i].toString());
            Object name = jsonObject2.get("name");
            Object lastUpdateTime = jsonObject2.get("lastUpdateTime");
            Object total1 = jsonObject2.get("total");
            JSONObject jsonObject3 = JSONObject.parseObject(total1.toString());
            Object confirm1 = jsonObject3.get("confirm");
            //计算现存确诊
            Object heal1 = jsonObject3.get("heal");
            Object dead1 = jsonObject3.get("dead");
            int Confirm_now=Integer.parseInt(confirm1.toString())-Integer.parseInt(heal1.toString())-Integer.parseInt(dead1.toString());
            //封装数据
            confirmedData.setName(name.toString());
            confirmedData.setValue(Confirm_now);
            if(lastUpdateTime==null){
                confirmedData.setUpdateTime(new Date());
            }else{
                confirmedData.setUpdateTime(format1.parse(String.valueOf(lastUpdateTime)));
            }
            list.add(confirmedData);
        }
        indexService.saveBatch(list);
        //清除Redis缓存 保持Redis和mysql的数据一致性
        Jedis jedis1=new Jedis("127.0.0.1");
        if(jedis1!=null){
            jedis1.flushDB();//删指定键值对，这里偷懒一下
        }
    }
}
