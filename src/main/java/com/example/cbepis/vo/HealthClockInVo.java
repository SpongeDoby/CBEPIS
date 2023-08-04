package com.example.cbepis.vo;

import com.example.cbepis.entity.HealthClockIn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HealthClockInVo extends HealthClockIn {
    private Integer page;
    private Integer limit;
}
