package com.wenxue.uzi.helper;

import com.wenxue.uzi.utils.RequestParamsUtil;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * 云路供应链科技有限公司 版权所有 © Copyright 2019<br>
 * <p>
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_
 * //                         o8888888o
 * //                         88" . "88
 * //                         (| ^_^ |)
 * //                         O\  =  /O
 * //                      ____/`---'\____
 * //                    .'  \\|     |//  `.
 * //                   /  \\|||  :  |||//  \
 * //                  /  _||||| -:- |||||-  \
 * //                  |   | \\\  -  /// |   |
 * //                  | \_|  ''\---/''  |   |
 * //                  \  .-\__  `-`  ___/-. /
 * //                ___`. .'  /--.--\  `. . ___
 * //              ."" '<  `.___\_<|>_/___.'  >'"".
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /
 * //      ========`-.____`-.___\_____/___.-`____.-'========
 * //                           `=---='
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * //         佛祖保佑       永无BUG     永不修改
 * ////////////////////////////////////////////////////////////////////
 *
 * @Description:
 * @date 2020-07-16 12:25
 * @Author: <a href= "765741668@qq.com">yangzonghua</a>
 */
@Slf4j
public class InterceptorLocalBloomHelper {

    @Value("${yyt.carrier.id:41}")
    private Integer carrierId;

    public static void validateSingle(RoutingContext ctx) {
        Map<String, String> bizContent = RequestParamsUtil.getBizContent(ctx);
        String waybillNo = bizContent.get("waybillNo");
        List<String> filter = new ArrayList<String>(){{add("123");}};
        byte result;
        if (filter != null) {
            boolean contains;
            try {
                contains = filter.contains(waybillNo);
                result = contains ? (byte) 1 : (byte) 0;
            } catch (Exception e) {
                result = -1;
                log.error("local布隆校验异常, 单号: {}", waybillNo, e);
            }
        } else {
            log.warn("local布隆没有进行初始化, 忽略处理: {}", waybillNo);
            result = 0;
        }

        //极端的高并发情况下,数据结构的正确使用能为我们省下非常可观的内存申请空间
        //使用byte只占用1个字节的申请空间，而盲目使用int则占用4个字节的申请空间
        ctx.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .setStatusCode(HTTP_OK)
                .end(result + "");
    }
}
