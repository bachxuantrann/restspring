package com.springrest.restApp.domain.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO {
    private MetaDTO metaDTO;
    private Object result;
}
