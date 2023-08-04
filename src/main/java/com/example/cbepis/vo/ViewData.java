package com.example.cbepis.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewData {
//    状态码、消息、行数、数据实体
    private Integer code=0;//设置初始值为0，不然Layui会报错。
    private String msg="";
    private Long count=0L;
    private Object data;

    public ViewData(Long count, Object data) {
        this.count = count;
        this.data = data;
    }

    public ViewData(Object data) {
        this.data = data;
    }
}
