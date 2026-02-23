package com.campus.common.result;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {

    private List<T> records;
    private Long total;
    private Integer page;
    private Integer size;

}