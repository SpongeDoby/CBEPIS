package com.example.cbepis.apiutils;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpClient {
    public String getData(){
        //初始化请求参数
        RequestConfig requestConfig=RequestConfig.custom()
                .setSocketTimeout(10000)
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .build();
        CloseableHttpClient closeableHttpClient=null;
        HttpGet httpGet=null;
        CloseableHttpResponse closeableHttpResponse=null;

        //创建HttpClient，发送请求，配置初始化参数，执行请求
        try{
            closeableHttpClient= HttpClients.createDefault();
            httpGet=new HttpGet("https://c.m.163.com/ug/api/wuhan/app/data/list-total");
            httpGet.setConfig(requestConfig);
            closeableHttpResponse=closeableHttpClient.execute(httpGet);
            int statusCode=closeableHttpResponse.getStatusLine().getStatusCode();
            //解析数据
            if(statusCode==200){
                HttpEntity httpEntity=closeableHttpResponse.getEntity();
                return EntityUtils.toString(httpEntity,"utf-8");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try{
                if (closeableHttpResponse!=null){
                    closeableHttpResponse.close();
                }
                if(httpGet!=null){
                    httpGet.releaseConnection();
                }
                if (closeableHttpClient!=null){
                    closeableHttpClient.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
