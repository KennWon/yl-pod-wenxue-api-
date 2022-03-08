package com.wenxue.uzi.utils;


import com.wenxue.uzi.constant.origin.ResultCodeEnum;
import com.wenxue.uzi.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yl
 */
@Slf4j
@Component
public class AesUtils {

    public static String getDecryptText(String key,String iv,String data,String privateKey){
        try {
            String iv2 = CipherUtils.rsaDecrypt(iv, privateKey);
            String key2 = CipherUtils.rsaDecrypt(key, privateKey);
            String contentDecrypt = CipherUtils.aesDecrypt(data, key2, iv2);
            return contentDecrypt;
        } catch (Exception e) {
            log.error("数据解密异常：{}",e.getMessage());
            return StringUtils.EMPTY;
        }
    }

    public static Map<String,Object> getRequestEncryptMap(String data,String PublicKey,String SecretKey){
        Map<String,Object> params = new HashMap<>(16);
        String key = RandomStringUtils.randomAlphanumeric(16);
        String iv = RandomStringUtils.randomAlphanumeric(16);
        String content;
        Long timestamp;
        try {
            timestamp=System.currentTimeMillis();
            content = CipherUtils.aesEncrypt(data, key, iv);
            key = CipherUtils.rsaEncrypt(key, PublicKey);
            iv = CipherUtils.rsaEncrypt(iv, PublicKey);
        } catch (Exception e) {
            log.error("数据加密异常：{}",e.getMessage());
            throw new CommonException(ResultCodeEnum.DATA_ENCRYPT_ERROR);
        }
        String waitMd5 = String.format(Md5Utils.MD5_FORMAT, content, iv, key, SecretKey, timestamp);
        String md5String = Md5Utils.md5(waitMd5);
        params.put("iv",iv);
        params.put("key",key);
        params.put("data",content);
        params.put("digest",md5String);
        params.put("timestamp",timestamp);
        return params;
    }


}
