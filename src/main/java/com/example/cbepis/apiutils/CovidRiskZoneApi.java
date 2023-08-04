package com.example.cbepis.apiutils;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CovidRiskZoneApi {
    /**
     *入参：token、province、city、county
     * 出参：end_update_time、high_count、middle_count、high_list、middle_list
     */
    private static final String token="uc8O8er5SuVKUY5A";
    private static final String url="https://v2.alapi.cn/api/springTravel/risk";
    public static String getCovidRiskZoneData(String province,String city,String county) throws IOException {
        OkHttpClient client=new OkHttpClient();
        MediaType mediaType=MediaType.parse("application/x-www-form-urlencoded");
        //空值校验
        if(StringUtils.isEmpty(province) && StringUtils.isEmpty(city) &&StringUtils.isEmpty(county)){
            return null;
        }
        //只有省份
        if(StringUtils.isNotEmpty(province) && StringUtils.isEmpty(city) &&StringUtils.isEmpty(county)){
            RequestBody body=RequestBody.create(mediaType,"token="+token+"&province="+province);
            Request request=new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type","application/x-www-form-urlencoded")
                    .build();
            Response response=client.newCall(request).execute();
            String string = response.body().string();
            return string;
        }
        //省+市
        if(StringUtils.isNotEmpty(province) && StringUtils.isNotEmpty(city) &&StringUtils.isEmpty(county)){
            RequestBody body=RequestBody.create(mediaType,"token="+token+"&province="+province+"&city="+city);
            Request request=new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type","application/x-www-form-urlencoded")
                    .build();
            Response response=client.newCall(request).execute();
            String string = response.body().string();
            return string;
        }
        //省市区
        if(StringUtils.isNotEmpty(province) && StringUtils.isNotEmpty(city) &&StringUtils.isNotEmpty(county)){
            RequestBody body=RequestBody.create(mediaType,"token="+token+"&province="+province+"&city="+city+"&county="+county);
            Request request=new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type","application/x-www-form-urlencoded")
                    .build();
            Response response=client.newCall(request).execute();
            String string = response.body().string();
            return string;
        }
        return null;
    }
}
