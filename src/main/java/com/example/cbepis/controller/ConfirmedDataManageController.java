package com.example.cbepis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cbepis.entity.ConfirmedData;
import com.example.cbepis.service.IndexService;
import com.example.cbepis.vo.ConfirmedDataVo;
import com.example.cbepis.vo.ViewData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.View;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ConfirmedDataManageController {
    @Autowired
    private IndexService indexService;


    //对所有省份确诊数据进行模糊查询并返回
    @RequestMapping("/getDataByPage")
    public ViewData getDataByPage(ConfirmedDataVo confirmedDataVo){
        //创建分页对象
        IPage<ConfirmedData> page=new Page<>(confirmedDataVo.getPage(),confirmedDataVo.getLimit());
        //模糊查询条件，按名字查询并按确诊数降序排序
        QueryWrapper<ConfirmedData> queryWrapper=new QueryWrapper<>();
        //传入有名字的就就按名字进行全模糊查询，传入名字为空就查全部
        queryWrapper.like(!(confirmedDataVo.getName()==null),"name",confirmedDataVo.getName());
        queryWrapper.orderByDesc("value");
        //查询
        indexService.page(page,queryWrapper);
        ViewData viewData=new ViewData(page.getTotal(),page.getRecords());
        return  viewData;
    }

    //删除数据
    @RequestMapping("/deleteById")
    public ViewData deleteById(Integer id) {
        indexService.removeById(id);
        ViewData viewData = new ViewData();
        viewData.setCode(200);
        viewData.setMsg("数据删除成功！");
        return viewData;
    }

    //新增或更新
    @RequestMapping("/addorUpdateConfirmedData")
    public ViewData addorUpdateConfirmedData(ConfirmedData confirmedData){
        ViewData viewData = new ViewData();
        boolean save=indexService.saveOrUpdate(confirmedData);
        if(save){
            viewData.setCode(200);
            viewData.setMsg("数据新增成功!");
            return viewData;
        }
        viewData.setCode(100);
        viewData.setMsg("数据新增失败!");
        return viewData;
    }

    //Excel文件拖拽上传
    @RequestMapping("/ConfirmedDataUpload")
    @ResponseBody
    public ViewData ConfirmedDataUpload(@RequestParam("file")MultipartFile multipartFile) throws IOException {
        ViewData viewData = new ViewData();
        //POI获取文件数据
        XSSFWorkbook xssfWorkbook=new XSSFWorkbook(multipartFile.getInputStream());
        //拿到工作表
        XSSFSheet xssfSheet=xssfWorkbook.getSheetAt(0);
        //解析数据，使用集合接收文件数据
        List<ConfirmedData> confirmedDataList=new ArrayList<>();
        XSSFRow xssfRow=null;
        for(int i=0;i<xssfSheet.getPhysicalNumberOfRows();i++){
            ConfirmedData confirmedData=new ConfirmedData();
            xssfRow=xssfSheet.getRow(i);
            confirmedData.setName(xssfRow.getCell(0).getStringCellValue());
            confirmedData.setValue((int)xssfRow.getCell(1).getNumericCellValue());
            confirmedDataList.add(confirmedData);
        }
        indexService.saveBatch(confirmedDataList);
        viewData.setCode(200);
        viewData.setMsg("文件已上传，数据已更新");
        return viewData;
    }

    //数据导出
    @RequestMapping("/ExportConfirmedData")
    public void ExportConfirmedData(HttpServletResponse httpServletResponse) throws IOException {
        List<ConfirmedData> confirmedDataList=indexService.list();
        httpServletResponse.setCharacterEncoding("UTF-8");
        HSSFWorkbook hssfWorkbook=new HSSFWorkbook();
        HSSFSheet hssfSheet=hssfWorkbook.createSheet("各省份确诊数据");
        HSSFRow hssfRow=hssfSheet.createRow(0);
        hssfRow.createCell(0).setCellValue("省份名称");
        hssfRow.createCell(1).setCellValue("确诊数");
        for(ConfirmedData i:confirmedDataList){
            HSSFRow row=hssfSheet.createRow(hssfSheet.getLastRowNum()+1);
            row.createCell(0).setCellValue(i.getName());
            row.createCell(1).setCellValue(i.getValue());
        }
        OutputStream outputStream=null;
        try{
            outputStream=httpServletResponse.getOutputStream();
            httpServletResponse.setContentType("application/octet-stream;chartset=utf8");
            httpServletResponse.setHeader("Content-Disposition","attachment;filename="+new String("各省份疫情数据".getBytes(),"iso-8859-1")+".xls");
            hssfWorkbook.write(outputStream);
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                outputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
