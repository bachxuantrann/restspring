package com.springrest.restApp.domain.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetaDTO {
    private int page;
    private int pageSize;
    private int totalPages;
    private Long total;
}
