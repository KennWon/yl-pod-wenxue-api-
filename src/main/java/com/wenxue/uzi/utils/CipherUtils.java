package com.wenxue.uzi.utils;

import sun.security.pkcs.PKCS8Key;
import sun.security.rsa.RSAPublicKeyImpl;
import sun.security.util.DerValue;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.util.Base64;

/**
 * @author yl
 */
public class CipherUtils {


    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public static final String PREFERRED_ENCODING = "UTF-8";
    private static final String CIPHER = "AES/CBC/PKCS5Padding";
    private static final String CRYPT_MODE = "AES";
    /**
     * Used to get the number of Quadruples.
     */
    private static final int FOURBYTE = 4;
    /**
     * Byte used to pad output.
     */
    private static final byte PAD = (byte) '=';
    /**
     * The base length.
     */
    private static final int BASELENGTH = 255;
    private static final byte[] BASE64_ALPHABET = new byte[BASELENGTH];
    private static final int HEX_FF = 0xFF;
    private static final char FILL = '0';
    private static final int RADIX = 16;
    /**
     * Lookup length.
     */
    private static final int LOOKUPLENGTH = 64;
    private static final byte[] LOOKUP_BASE64_ALPHABET = new byte[LOOKUPLENGTH];
    private static String ALGORITHM_MODEL = "RSA/ECB/PKCS1Padding";

    static {
        for (int i = 0; i < BASELENGTH; i++) {
            BASE64_ALPHABET[i] = (byte) -1;
        }
        for (int i = 'Z'; i >= 'A'; i--) {
            BASE64_ALPHABET[i] = (byte) (i - 'A');
        }
        for (int i = 'z'; i >= 'a'; i--) {
            BASE64_ALPHABET[i] = (byte) (i - 'a' + 26);
        }
        for (int i = '9'; i >= '0'; i--) {
            BASE64_ALPHABET[i] = (byte) (i - '0' + 52);
        }

        BASE64_ALPHABET['+'] = 62;
        BASE64_ALPHABET['/'] = 63;

        for (int i = 0; i <= 25; i++) {
            LOOKUP_BASE64_ALPHABET[i] = (byte) ('A' + i);
        }

        for (int i = 26, j = 0; i <= 51; i++, j++) {
            LOOKUP_BASE64_ALPHABET[i] = (byte) ('a' + j);
        }

        for (int i = 52, j = 0; i <= 61; i++, j++) {
            LOOKUP_BASE64_ALPHABET[i] = (byte) ('0' + j);
        }

        LOOKUP_BASE64_ALPHABET[62] = (byte) '+';
        LOOKUP_BASE64_ALPHABET[63] = (byte) '/';
    }


    /**
     * RSA算法将content加密
     *
     * @param content 待加密内容
     * @param publicKey base64格式的秘钥
     * @return base64格式的加密后字符串
     */
    public static String rsaEncrypt(String content, String publicKey) throws Exception {
        return encrypt(content, decode(publicKey));
    }

    /**
     * RSA算法解密
     *
     * @param encryptContent 待解密的内容，格式为base64
     * @param privateKey base64格式的私钥
     * @return 原字符串
     */
    public static String rsaDecrypt(String encryptContent, String privateKey) throws Exception {
        return decrypt(encryptContent, decode(privateKey));
    }

    /**
     * aes算法加密
     *
     * @param content 待加密内容
     * @param key aes key
     * @param iv aes iv
     * @return 加密后的字符串 16进制
     */
    public static String aesEncrypt(String content, String key, String iv) throws Exception {
        return encryptHex(content, key, iv);
    }

    public static String encrypt(String source, byte[] publicKey) throws Exception {
        return Base64.getEncoder().encodeToString(encrypt(source.getBytes(UTF_8), publicKey));
    }

    public static byte[] encrypt(byte[] source, byte[] publicKey) throws Exception {
//        RSAPublicKey key = RSAPublicKeyImpl.newKey(publicKey);
        RSAPublicKeyImpl key = new RSAPublicKeyImpl(publicKey);
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODEL);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(source);
    }

    /**
     * aes算法解密
     *
     * @param content 加密后的内容 16进制
     * @param key aes key
     * @param iv aes iv
     * @return 解密后的字符串
     */
    public static String aesDecrypt(String content, String key, String iv) throws Exception {
        return decryptHex(content, key, iv);
    }

    public static String decryptHex(String value, String sk, String iv) throws Exception {
        return new String(decrypt(hex2Byte(value), sk.getBytes(UTF_8), iv.getBytes(UTF_8)), UTF_8);
    }


    public static byte[] decrypt(byte[] value, byte[] sk, byte[] iv)
            throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(sk, CRYPT_MODE);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(value);
    }

    private static byte[] hex2Byte(String hex) {
        int byteSize = hex.length() / 2;
        byte[] bytes = new byte[byteSize];
        for (int idx = 0; idx < byteSize; idx++) {
            int high = Integer.parseInt(hex.substring(idx * 2, idx * 2 + 1), RADIX);
            int low = Integer.parseInt(hex.substring(idx * 2 + 1, idx * 2 + 2), RADIX);
            bytes[idx] = (byte) (high * RADIX + low);
        }

        return bytes;
    }


    public static String decrypt(String source, byte[] privateKey) throws Exception {
        return new String(decrypt(Base64.getDecoder().decode(source), privateKey), UTF_8);
    }

    public static byte[] decrypt(byte[] source, byte[] privateKey) throws Exception {
        PrivateKey key = PKCS8Key.parseKey(new DerValue(privateKey));
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODEL);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(source);
    }


    public static byte[] decode(String base64Encoded) throws UnsupportedEncodingException {
        return decode(toBytes(base64Encoded));
    }

    public static byte[] toBytes(String source, String encoding) throws UnsupportedEncodingException {
        return source.getBytes(encoding);
    }

    public static byte[] toBytes(String source) throws UnsupportedEncodingException {
        return toBytes(source, PREFERRED_ENCODING);
    }

    /**
     * Decodes Base64 data into octets
     *
     * @param base64Data Byte array containing Base64 data
     * @return Array containing decoded data.
     */
    public static byte[] decode(byte[] base64Data) {
        // RFC 2045 requires that we discard ALL non-Base64 characters
        if (!isBase64(base64Data)) {
            throw new IllegalArgumentException("不是有效的Base64字符");
        }

        int numberQuadruple = base64Data.length / FOURBYTE;
        byte[] decodedData;
        {
            // this sizes the output array properly - rlw
            int lastData = base64Data.length;
            // ignore the '=' padding
            while (base64Data[lastData - 1] == PAD) {
                if (--lastData == 0) {
                    return new byte[0];
                }
            }
            decodedData = new byte[lastData - numberQuadruple];
        }

        int dataIndex;
        int encodedIndex = 0;
        for (int i = 0; i < numberQuadruple; i++) {
            dataIndex = i * 4;
            byte marker0 = base64Data[dataIndex + 2];
            byte marker1 = base64Data[dataIndex + 3];

            byte b1 = BASE64_ALPHABET[base64Data[dataIndex]];
            byte b2 = BASE64_ALPHABET[base64Data[dataIndex + 1]];

            if (marker0 != PAD && marker1 != PAD) {
                // No PAD e.g 3cQl
                byte b3 = BASE64_ALPHABET[marker0];
                byte b4 = BASE64_ALPHABET[marker1];

                decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                decodedData[encodedIndex + 1] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
                decodedData[encodedIndex + 2] = (byte) (b3 << 6 | (b4 & 0xff));
            } else if (marker0 == PAD) {
                // Two PAD e.g. 3c[Pad][Pad]
                decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
            } else {
                // One PAD e.g. 3cQ[Pad]
                byte b3 = BASE64_ALPHABET[marker0];
                decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                decodedData[encodedIndex + 1] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            }
            encodedIndex += 3;
        }
        return decodedData;
    }

    private static boolean isBase64(byte octect) {
        if (octect == PAD) {
            return true;
        } else //noinspection RedundantIfStatement
            if (octect < 0 || BASE64_ALPHABET[octect] == -1) {
                return false;
            } else {
                return true;
            }
    }

    public static boolean isBase64(byte[] arrayOctect) {

        arrayOctect = discardWhitespace(arrayOctect);

        int length = arrayOctect.length;
        if (length == 0) {
            // shouldn't a 0 length array be valid base64 data?
            // return false;
            return true;
        }

        for (int i = 0; i < length; i++) {
            if (!isBase64(arrayOctect[i])) {
                return false;
            }
        }

        return true;
    }


    static byte[] discardWhitespace(byte[] data) {
        byte[] groomedData = new byte[data.length];
        int bytesCopied = 0;
        for (byte b : data) {
            switch (b) {
                case (byte) ' ':
                case (byte) '\n':
                case (byte) '\r':
                case (byte) '\t':
                    break;
                default:
                    groomedData[bytesCopied++] = b;
            }
        }

        byte[] packedData = new byte[bytesCopied];
        System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);
        return packedData;
    }

    public static String encryptHex(String value, String sk, String iv) throws Exception {
        return byte2Hex(encrypt(value.getBytes(UTF_8), sk.getBytes(UTF_8),
                iv.getBytes(UTF_8)));
    }


    private static String byte2Hex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            String value = Integer.toHexString(b & HEX_FF);
            if (value.length() == 1) {
                builder.append(FILL);
            }

            builder.append(value);
        }

        return builder.toString().toUpperCase();
    }


    public static byte[] encrypt(byte[] value, byte[] sk, byte[] iv) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(sk, CRYPT_MODE);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(value);
    }



    public static void main(String[] args) throws Exception {
        String content = "你好，我是张三，我是李四，我是王五，and you?";
//        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDETUJPf9lpcLLcT22ZUfb0wTQhMrc+PEjaeqAM8ilolTcuvW3mbBRP0xvulLgqoYA7AeLa3Nj8ZBBL3yaA4bl6+3yt4fm4UpqtwO3RcHiBMoNh6veV8hAeMt4DvRoIooVxgdiFyzIOAVZUoHR7eYsDmhnTAsgU2YVedOrMgr47DwIDAQAB";
//        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMRNQk9/2WlwstxPbZlR9vTBNCEytz48SNp6oAzyKWiVNy69beZsFE/TG+6UuCqhgDsB4trc2PxkEEvfJoDhuXr7fK3h+bhSmq3A7dFweIEyg2Hq95XyEB4y3gO9GgiihXGB2IXLMg4BVlSgdHt5iwOaGdMCyBTZhV506syCvjsPAgMBAAECgYA3WY17+xGjgGp+YksF8y2N/7giMDmo67ijGfZtEA8K9R/1JuaJFSp5A46ZrAdtxdHxUWvUyHbKW7CYpBgoOP1iKHvbAY7aYKr2f/0hxZwcQ/8WahX24LF+kCKCGa/gWn135qVZ8vXix9Q6rR56L9ZIqgPbydk8L9e9agJjV5hFOQJBAOSQAmN/xIPO/PF3ErcK4jJ3ddBQ7zHSxblbs9AVS8ExIStaKCTbjzgCYim78ENSF69kKmKGSJJ4KKzZwAHUn2MCQQDb3db9Du86mXED8j0u6HZdv0dXXavakfIdpSMGFHYHKnh74YHtjCdYwPQtQNhCzwglcQRawNzDIAiUr7yODRNlAkEAtS8ztYxjS9mZyhCKVW/bQAZS7oA6HVToSLfZIuq+0rcPhD5D9HPYZJ3/EvZY0AUToYXV06R/gxydoeg7vTe/4QJAWbcd4MjVST4PZaLU4G0uz3YbsESiuV9QQlR+BB8ZQjdMTpBoKfxEmeyyNjz9gdVk55JESpvj+SL7HWDQOc6QGQJBAKd+0p9r+I1KXAZIn6BhkvUtySS6qCZHaFspsYvU9tU8guyaCRDMnuB/8dDRBVy6KjbYKjnCBJ4uZVdlSnvCCz0=";
        String publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCz2T615jYbPeFhi6bGjW/BQVxU3jiNR70CqR0mZZ3GtZ9K8VoaoDtE+Qzcd/HkENukOTSAaylCiPbPChsLDZpWjpWqB9zzYhXQpnVDqqTuUgxeApZmSoPkJ1k6A5HKSnC0sI3v8qjy21/Yo09m2uxcbdFULs314yHa/VUXcnQOSwIDAQAB";
//        String privateKey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI8zZ175hOg2evUOts6+ucFnz/CSeVb+9hB+eKtWRkP6eEc17cOzCZ4ji6RZK/ZOjwYSGanjE3P9PMS2c2w+gYfyof5TATgDZsc31dYJzKyvUNMqUpYK+OJJGHS/gT3re73eXK9GpoYBHhxB8Y9E1sMxzz3LrVpVG3hBfoaTQ0szAgMBAAECgYBki/NI/GoVoiszZeF1yPUkl3+pqy8Hpn2fly2jIDC2CmfpmDQZ9NwL7WlfPD1h9Eb4PD1Iy+gcyPKiLHlex4SqojX3dUtK+R6ELxMYO81dlXGiC0xFeB7FlTI7/8ldfpegGN0nFBG7o++FY4EekiNykR0ij9kWOjnjiSHOT5Z6AQJBAM9qdBrt459VRdyIzAtfLzxe+Pnqp2wp2wO+YUjljOqp+lBs9lYJvp41aMoYbwNrHGRR36m8qRo5r58a2AxCSKUCQQCwvlcQLr54IeYLD2bXDZJDJj0wJfg1koM+RsDv/cxf8f5jlO4S1U2sui9Eim1pSXHQm1gvfothmg1VzxQVOCT3AkA61bmFggFVSvz8J5mpiCCAAXiie5tuJRlJIJG7+dFVJ04nziC6Gx2FByVoXjHvSEzPcCH/pdJZ7A8TFKxAHfU5AkAS5OYtxrF56jXLbOQTjVHbd9UGtqhoIbeCCtJJVZppj1cgkiU5QjBBjM3Mx/eWT9Go+VJeEWsZDfzq9W4yLoRVAkEApu5evKvBmPxoabVfs7q6a1Kw3YO4nmfs/mmnCzTVXLQPQj9x1oQrIb+487HzdJ+of68CMC/lPul/pRdo7oSl1A==";
        System.out.println(decode(publicKey));
        String en = rsaEncrypt(content,publicKey);
        System.out.println(en);
//        String de = rsaDecrypt(en,privateKey);
//        System.out.println(de);
    }
}
