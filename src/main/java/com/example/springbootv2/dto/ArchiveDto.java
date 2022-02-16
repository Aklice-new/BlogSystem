package com.example.springbootv2.dto;

import com.example.springbootv2.model.ContentMD;
import lombok.Data;

import java.util.List;
@Data
public class ArchiveDto {
    private String date;
    private String count;
    private List<ContentMD> articles;

}
