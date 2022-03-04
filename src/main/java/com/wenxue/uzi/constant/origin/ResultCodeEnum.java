package com.wenxue.uzi.constant.origin;

/**
 * @description: 自定义状态码枚举类
 * 参考： http://www.cnblogs.com/zhanghengscnc/p/8824820.html
 * @author: linhaibo
 * @date: 2019-04-30 15:16
 */
public enum ResultCodeEnum {


    SUCCESS(200, "请求成功"),
    FAIL(500, "系统开小差克了，请稍后重试"),

    /**
     * 参数错误
     */
    PARAMS_IS_NULL(600, "参数为空错误"),
    PARAMS_NOT_COMPLETE(601, "参数不全"),
    PARAMS_TYPE_ERROR(602, "参数类型匹配错误"),
    PARAMS_IS_INVALID(603, "参数无效"),
    JSON_PARSING_ERROR(604, "數據解析错误"),

    /**
     * 数据错误
     */
    DATA_NOT_FOUND(999002000, "数据未找到"),
    DATA_IS_WRONG(999002010, "数据有误"),
    DATA_ALREADY_EXISTED(999002020, "数据已存在"),

    /**
     * 用户错误
     */
    USER_NOT_EXIST(999003000, "用户不存在"),
    USER_NOT_LOGGED_IN(999003010, "用户未登陆"),
    USER_ACCOUNT_ERROR(999003020, "用户名或密码错误"),
    USER_ACCOUNT_FORBIDDEN(999003030, "用户账户已被禁用"),
    USER_HAS_EXIST(999003040, "该用户已存在"),
    USER_CODE_ERROR(999003050, "验证码错误"),



    /**
     * 接口错误，系统错误
     */
    INTERFACE_INNER_INVOKE_ERROR(999005000, "系统内部接口调用异常"),
    INTERFACE_OUTER_INVOKE_ERROR(999005010, "系统外部接口调用异常"),
    INTERFACE_FORBIDDEN(999005020, "接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(999005030, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(999005040, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(999005050, "接口负载过高"),
    SYSTEM_INNER_ERROR(999005060, " 系统内部错误"),
    SERVICE_TRANSFER_ERROR(999005070, "跨服务调用错误"),
    SERVICE_HYSTRIX_ERROR(999005080, "服务不可用"),

    /**
     * 业务错误
     */
    BUSINESS_ERROR(999006000, "系统业务出现问题"),
    ID_GENERATOR_ERROR(999006010, "ID生成异常"),
    HTTP_REQUEST_METHOD_ERROR(999006020, "请求方式异常"),
    CODE_IS_EXIST_ERROR(999006030, "该编码已存在"),
    MONGO_QUERY_TYPE_ERROR(999006040,"查询mongo异常"),
    MONGO_ADD_TYPE_ERROR(999006050,"新增mongo异常，主键重复"),
    NETWORK_RANGE_PLACE_NAME_REPEAT(999006051,"网点场地名称重复"),
    CHAR_LEN_MAX(999006052,"字符长度超过最大限制"),

    /**
     * 权限错误
     */
    PERMISSION_NO_ACCESS(999007000, "当前按钮没有访问权限"),

    /**
     * 基础数据
     */

    CHECK_SIGN_FAIL(999009010,"签名验证失败"),
    DATA_DECODE_ERROR(999009020,"数据解密异常"),
    DATA_ENCRYPT_ERROR(999009030,"数据加密异常"),
    SYSTEM_ERROR(999009040,"系统异常，请稍后重试"),
    COOPERATE_ERROR(999009050,"合作商务号错误"),
    VEHICLE_TYPE_NOT_MATCH(999009060,"车型不匹配"),
    DATE_NOT_MATCH(999009070,"日期格式不匹配"),
    PHONE_NOT_MATCH(999009080,"手机号格式不匹配"),
    PLATE_NUMBER_NOT_MATCH(999009090,"车牌号格式不匹配"),
    VEHICLE_ID_NOT_EMPTY(999009091,"车辆的ID不能为空"),
    PLATE_NUMBER_ERROR(999009092,"车牌号错误"),
    ZCS_CODE_IS_EXIST(999009093,"招采系统编码已存在"),
    CARRIER_IS_ERROR(999009094,"承运商ID错误"),

    /***
     * 文件上传1
     */
    FILE_UPLOAD_ERROR(999008010, "文件上传失败"),
    FILE_DOWNLOAD_ERROR(999008020, "文件下载失败"),
    FILE_DELETE_ERROR(999008030, "文件删除失败"),
    FILE_GET_ERROR(999008040, "获取文件失败"),
    FILE_TYPE_ERROR(999008050, "文件类型错误"),
    MODULE_TYPE_ERROR(999008060, "模块类型错误"),


    /**
     * 加班车 加班价格
     *
     */
    OVER_TIME_CAR_NOT_NULL(999010001, "加班车申请数据为空"),
    OVER_TIME_CAR_STATUS_NULL(999010002, "加班运输任务状态数据为空"),
    BIDDING_DEMAND_CODE_NULL(999010003, "招标需求编码不能为空"),
    ;


    private int code;
    private String msg;

    ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    @Override
    public String toString() {
        return "ResultCodeEnum [code=" + code + ", msg=" + msg + "]";
    }
}
