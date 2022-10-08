package com.inspur.labor.security.config.ueditor.define;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 耿鹏
 */
public class MimeType {

    public static final Map<String, String> TYPES = new HashMap<String, String>() {{
        put("image/gif", ".gif");
        put("image/jpeg", ".jpg");
        put("image/jpg", ".jpg");
        put("image/png", ".png");
        put("image/bmp", ".bmp");
    }};

    public static String getSuffix(String mime) {
        return MimeType.TYPES.get(mime);
    }

}
