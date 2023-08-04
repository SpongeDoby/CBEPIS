package com.example.cbepis.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 请假审批节点状态枚举类型
 */
@AllArgsConstructor
@NoArgsConstructor
public enum ApprovalNodeStatusEnum {
    NO_SUBMIT(0,"未提交"),
    TEACHER_ING(1,"教师审批中"),
    TEACHER_REJECTED(2,"教师已驳回"),
    TEACHER_PASSED(3,"教师审核通过"),
    COLLEGE_ING(4,"院系审核中"),
    COLLEGE_REJECTED(5,"院系已驳回"),
    COLLEGE_PASSED(6,"院系审核通过"),
    TIME_OUT(7,"已超时");

    private Integer code;
    private String describe;

    public Integer getCode() {
        return code;
    }

    public String getDescribe() {
        return describe;
    }
}
