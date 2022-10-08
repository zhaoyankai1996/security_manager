package com.inspur.labor.security.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

/**
 * @author 赵彦凯
 * @version 1.0
 * @date 2022/8/9 14:30
 */
public class QrCodeUtils {

    private static final String CHARSET = "utf-8";

    private static final int QRCODE_WIDTH = 398;

    private static final int QRCODE_HEIGHT = 398;

    public static BufferedImage createImage(String content) {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_WIDTH, QRCODE_HEIGHT,
                    hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //0xFF000000黑色 0xFFFFFFFF白色
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

    public static BufferedImage createEmptyImg(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //0xFF000000黑色 0xFFFFFFFF白色
                image.setRGB(x, y, 0xFFFFFFFF);
            }
        }
        return image;
    }

    public static BufferedImage drawString(BufferedImage image, String text, int x, int y, Font font, Color color) {
        //绘制文字
        Graphics2D g = image.createGraphics();
        //设置颜色
        g.setColor(color);
        //消除锯齿状
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        //设置字体
        g.setFont(font);
        //绘制文字
        if (x == -1) {
            FontMetrics fontMetrics = g.getFontMetrics(font);
            x = (image.getWidth() - fontMetrics.stringWidth(text)) / 2;
        }
        g.drawString(text, x, y);
        return image;
    }

    public static BufferedImage createActQrCode(String actIdStr, String actNameStr) {
        //生成空白图片
        BufferedImage emptyImg = createEmptyImg(488, 610);
        //添加文字
        String pressText = "请打开”北疆工惠“，使用”扫一扫“功能扫码签到！";
        Font fontBottom = new Font("仿宋", Font.PLAIN, 20);
        drawString(emptyImg, pressText, 0, 600, fontBottom, Color.BLACK);
        //添加文字
//        String actId = "活动编号："+actIdStr;
//        Font fontTop1 = new Font("仿宋", Font.BOLD, 22);
//        drawString(emptyImg,actId,-1,50,fontTop1,Color.BLACK);
        //添加文字
        if (StringUtils.isNotBlank(actNameStr) && actNameStr.length() > Constant.QRCODE_CONTENT_LENGTH) {
            actNameStr = actNameStr.substring(0, 10) + "....";
        }
        String actName = "活动名称：" + actNameStr;
        Font fontTop2 = new Font("仿宋", Font.BOLD, 22);
        drawString(emptyImg, actName, -1, 85, fontTop2, Color.BLACK);
        //嵌入二维码
        BufferedImage qrcode = createImage(actIdStr);
        Graphics2D g = emptyImg.createGraphics();
        g.drawImage(qrcode, 45, 120,
                qrcode.getWidth(), qrcode.getHeight(), null);
        return emptyImg;
    }

}
