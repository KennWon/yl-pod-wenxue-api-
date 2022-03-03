package com.wenxue.uzi.helper;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yl
 */
@Data
public class Path {

    @NotNull(message = "ID不能为空")
    private Integer id;
    private String url;


}
