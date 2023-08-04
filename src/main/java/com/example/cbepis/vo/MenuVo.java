package com.example.cbepis.vo;

import com.example.cbepis.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MenuVo extends Menu {
    private Integer page;
    private Integer limit;
}
