package com.inspur.labor.security.util;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * rsa加解密工具类
 *
 * @author 赵彦凯
 * @version 1.0
 * @date 2022/8/29 10:15
 */
public class RsaUtils {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static String privteKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIe7Rm/+HXJCxl7Bb1u7luz0lpdBfUus9ann7U9fOg4dVabj9NQJc/71TVYRLKj6iBDukwa+Gn33W7Oek1OouCNLxfqkCFVoiFWZVyP2AkjBmNapyYUkuqAi3dZUMtkFEzT+uixflGvjOizhHVLbjpdRzcv9hkdzfMtELB7q1klfAgMBAAECgYB1IRRQ0t7AWoVrAcxCQiilBRDrKDWi13gMDjvI0el8gq4GXhNJ43p5G2CIlDuQ+tnumqLAH88mww4cfAyxFhkIAcCITovo1wnHlUAjvV7jtvrbBhOhnR/RPX+ZGWTahu4D0i3+kyWg69HMQgvhEQxbJeKCei6vsFYLrUx/Of8W8QJBAMnLBr5X0PvNdq9Mz9hfsr6FRALZlvYz+T0AkNweGCmdlklFYxWUOit4uIoYKP9CLVA52FtTPBpdekeRWJ9w9TkCQQCsMU62oDX5m4V/DBEbT2RObqJAPpup3SqkaAj8MGuKoEeKMlZfWgYMSBL/LnfKTbCPiFpdjHnum9+0DCYttYtXAkEAnfQI1Edi8ybyBufGLaVDAbTI9r4hV5h5OJHP0izQVwreT347v/Fl0w9My/N7a8r+MzUEMQAnJLAU/h48U5mUqQJANfxDhGhYY8iJoxpiZqCOfzSac+/tF+V6UA85+e8oWE8F/uLnR3/O85zJL+4eqnOPDoEea8P2GoQOL8G7WotXgwJAWs9coXdsxge9lvi7rHQdSMEQKQQYH+hfegtJQ/NN2BNccuAHnq7IjuGs9UbjXCjHbH0F26SPuo+SGI8Zw/soIg==";
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCHu0Zv/h1yQsZewW9bu5bs9JaXQX1LrPWp5+1PXzoOHVWm4/TUCXP+9U1WESyo+ogQ7pMGvhp991uznpNTqLgjS8X6pAhVaIhVmVcj9gJIwZjWqcmFJLqgIt3WVDLZBRM0/rosX5Rr4zos4R1S246XUc3L/YZHc3zLRCwe6tZJXwIDAQAB";

    /**
     * 生成密钥对(公钥和私钥)
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /** */
    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64Utils.encode(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64Utils.decode(sign));
    }

    /**
     * 私钥解密
     *
     * @param encrypStr 已加密数据
     * @param data      私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String decrypt(String encrypStr, String data) {
        try {
            byte[] encryptedData = Base64Utils.decode(encrypStr);
            byte[] keyBytes = Base64Utils.decode(privteKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypStr;
    }

    /**
     * 公钥解密
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 私钥加密
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 获取私钥
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64Utils.encode(key.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64Utils.encode(key.getEncoded());
    }

    public static void main(String[] args) throws Exception {

//        String str = "B08V8NeqI9v6U5CeW+TWaULE3aFBDIRn1tuGhpYejgNLlGyYWQ6mAYgXuSplBzhbkwjTQYzVP4ZrBBNQA9rt7R4OG8xbRe32rXeur6Kn8zDNPLKLNJisIxrPfGHe3YlNGULyz23nwipeXLz0EbiA5occkLpwenUB03R8mLSRGKk=";
//        System.err.println(decrypt(str,""));
        //        Map<String, Object> keyMap = TestRSA.genKeyPair();
//        String publicKey = TestRSA.getPublicKey(keyMap);
//        String privateKey = TestRSA.getPrivateKey(keyMap);
//        System.err.println(publicKey);
//        System.err.println("===============");
//        System.err.println(privateKey);
        //密码解密
//        byte[] decryptByPrivateKey = TestRSA.decrypt(Base64Utils.decode("K+J2YMDG/moc6k8i5Ax+7mBqEtGuESfegUG3K8aQdIjMktwnU7pthVs26SNExJm2bvCxHdxOsHe2+THd7jXkpwkirMcUCWDc6q5OFrSNzBuEUdeC3o3V0WWZmmk8i50g9AKpx4MlNKLfdwSiFKA+ahfW7QjQwJH/wVkC9tfaT9g="), privte);
//        String decodePassword = new String(decryptByPrivateKey);
//        System.err.println(decodePassword);
    }
}
