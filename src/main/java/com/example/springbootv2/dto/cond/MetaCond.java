package com.example.springbootv2.dto.cond;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaCond {
    /**
     * meta Name
     */
    private String name;
    /**
     * 类型
     */
    private String type;
}
