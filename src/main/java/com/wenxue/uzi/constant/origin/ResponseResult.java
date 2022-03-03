package com.wenxue.uzi.constant.origin;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;


/**
 * @author yl
 */
@Data
@Slf4j
public class ResponseResult<T> implements Serializable {
    private int code;
    private String msg;
    private T data;


    public ResponseResult() {
        super();
    }

    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(ResultCodeEnum code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    public ResponseResult(ResultCodeEnum code, T data) {
        this.code = code.getCode();
        this.msg = code.getMsg();
        this.data = data;
    }

    public static <T>  ResponseResult<T> genSuccessResult(T data) {
        return new ResponseResult(ResultCodeEnum.SUCCESS,data);
    }
    public static <T>  ResponseResult<T> genSuccessResult() {
        return new  ResponseResult(ResultCodeEnum.SUCCESS);
    }

    public static <T>  ResponseResult<T> genErrorResult(ResultCodeEnum code) {
        return new  ResponseResult(code);
    }

    public static <T>  ResponseResult<T> genErrorResult(int code, String msg) {
        return new ResponseResult(code,msg);
    }
}
