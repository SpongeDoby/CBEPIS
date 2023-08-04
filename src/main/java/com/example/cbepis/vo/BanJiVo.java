package com.example.cbepis.vo;

import com.example.cbepis.entity.BanJi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BanJiVo extends BanJi {
    private Integer page;
    private Integer limit;
}
