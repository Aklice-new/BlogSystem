package com.example.springbootv2.model;

import lombok.Data;

@Data
public class OptionMD {
    /** 名称 */
    private String name;
    /** 内容 */
    private String value;
    /** 备注 */
    private String description;
}
