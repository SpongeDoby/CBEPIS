package com.example.cbepis.vo;

import com.example.cbepis.entity.Approval;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalVo extends Approval {
    private Integer page;
    private Integer limit;
}
