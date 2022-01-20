package com.wenxue.uzi.exception;

import com.wenxue.uzi.constant.origin.ResultCodeEnum;
import lombok.Data;

/**
 * @author yl
 */
@Data
public class CommonException extends RuntimeException {
    private int code;

    public CommonException(ResultCodeEnum responseCode) {
        super(responseCode.getMsg());
        this.code = responseCode.getCode();
    }

    public CommonException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
