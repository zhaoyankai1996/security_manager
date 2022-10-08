package com.inspur.labor.security.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;

/**
 * @Title: AESUtil
 * @Description: AES加解密工具类
 * @Author: gengpeng
 * @CreateDate: 2019/5/23 17:50
 * @Version: 1.0
 */
public class AesUtil {

    private static final Logger logger = LoggerFactory.getLogger(AesUtil.class);
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String KEY_AES = "AES";
    private static final String KEY = "inspur";
    private static SecretKey SECRET_KEY = new SecretKeySpec(Arrays.copyOf(KEY.getBytes(StandardCharsets.US_ASCII), 16), KEY_AES);

    private AesUtil() throws Exception {
        throw new Exception();
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * AES加密
     *
     * @param content
     * @return
     */
    public static String aesEncrypt(String content) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(KEY_AES, "BC");
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);

            byte[] cleartext = content.getBytes(DEFAULT_CHARSET);
            byte[] ciphertextBytes = cipher.doFinal(cleartext);
            return new String(Hex.encodeHex(ciphertextBytes)).toUpperCase();
        } catch (UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException |
                NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException e) {
            logger.error("aesEncrypt error", e);
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param content
     * @return
     */
    public static String aesDecrypt(String content) {
        if (StringUtils.isEmpty(content)) {
            return "";
        }
        try {
            Cipher cipher = Cipher.getInstance(KEY_AES, "BC");
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);

            byte[] decodeContentBytes = Hex.decodeHex(content.toCharArray());
            byte[] ciphertextBytes = cipher.doFinal(decodeContentBytes);
            return new String(ciphertextBytes, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException | DecoderException |
                NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException e) {
            logger.error("aesDecrypt error", e);
        }
        return null;
    }

    public static void main(String[] args) {

    }
}
