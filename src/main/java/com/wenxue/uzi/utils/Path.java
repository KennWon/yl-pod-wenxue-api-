package com.wenxue.uzi.utils;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author yl
 */
@Data
public class Path {

    private Integer id;
    private String url;
    private LocalDate date;
    private LocalDateTime time;
}
