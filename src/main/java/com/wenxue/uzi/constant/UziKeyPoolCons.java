package com.wenxue.uzi.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yl
 * 密钥配置常量
 */
@Component
public class UziKeyPoolCons {

    public static String SECRET_KEY;
    public static Long TIME_THRESHOLD;
    public static String PRIVATE_KEY;
    public static String PUBLIC_KEY;

    @Value("${secret.key:DuW452Oe62ei25XV}")
    public void setSecretKey(String secretKey) {SECRET_KEY = secretKey;}
    @Value("${time.threshold:300000}")
    public void setTimeThreshold(Long timeThreshold) {TIME_THRESHOLD = timeThreshold;}
    @Value("${private.key:MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI8zZ175hOg2evUOts6+ucFnz/CSeVb+9hB+eKtWRkP6eEc17cOzCZ4ji6RZK/ZOjwYSGanjE3P9PMS2c2w+gYfyof5TATgDZsc31dYJzKyvUNMqUpYK+OJJGHS/gT3re73eXK9GpoYBHhxB8Y9E1sMxzz3LrVpVG3hBfoaTQ0szAgMBAAECgYBki/NI/GoVoiszZeF1yPUkl3+pqy8Hpn2fly2jIDC2CmfpmDQZ9NwL7WlfPD1h9Eb4PD1Iy+gcyPKiLHlex4SqojX3dUtK+R6ELxMYO81dlXGiC0xFeB7FlTI7/8ldfpegGN0nFBG7o++FY4EekiNykR0ij9kWOjnjiSHOT5Z6AQJBAM9qdBrt459VRdyIzAtfLzxe+Pnqp2wp2wO+YUjljOqp+lBs9lYJvp41aMoYbwNrHGRR36m8qRo5r58a2AxCSKUCQQCwvlcQLr54IeYLD2bXDZJDJj0wJfg1koM+RsDv/cxf8f5jlO4S1U2sui9Eim1pSXHQm1gvfothmg1VzxQVOCT3AkA61bmFggFVSvz8J5mpiCCAAXiie5tuJRlJIJG7+dFVJ04nziC6Gx2FByVoXjHvSEzPcCH/pdJZ7A8TFKxAHfU5AkAS5OYtxrF56jXLbOQTjVHbd9UGtqhoIbeCCtJJVZppj1cgkiU5QjBBjM3Mx/eWT9Go+VJeEWsZDfzq9W4yLoRVAkEApu5evKvBmPxoabVfs7q6a1Kw3YO4nmfs/mmnCzTVXLQPQj9x1oQrIb+487HzdJ+of68CMC/lPul/pRdo7oSl1A==}")
    public void setPrivateKey(String privateKey) {PRIVATE_KEY = privateKey;}
    @Value("${public.key:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCPM2de+YToNnr1DrbOvrnBZ8/wknlW/vYQfnirVkZD+nhHNe3DswmeI4ukWSv2To8GEhmp4xNz/TzEtnNsPoGH8qH+UwE4A2bHN9XWCcysr1DTKlKWCvjiSRh0v4E963u93lyvRqaGAR4cQfGPRNbDMc89y61aVRt4QX6Gk0NLMwIDAQAB}")
    public void setPublicKey(String publicKey) {PUBLIC_KEY = publicKey;}
}
