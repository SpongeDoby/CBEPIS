package com.example.cbepis.vo;

import com.example.cbepis.entity.ConfirmedData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ConfirmedDataVo extends ConfirmedData {
    private Integer page;
    private Integer limit;
}
