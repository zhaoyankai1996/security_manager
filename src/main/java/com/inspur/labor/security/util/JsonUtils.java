package com.inspur.labor.security.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * jackson工具类
 *
 * @Title: JsonUtils
 * @Author: gengpeng
 * @CreateDate: 2020/10/20 9:46
 * @Version: 1.0
 */
public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();

        // 如果存在未知属性，则忽略不报错
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许key没有双引号
        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许key有单引号
        MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 允许整数以0开头
        MAPPER.configure(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS.mappedFeature(), true);
        // 允许字符串中存在回车换行控制符
        MAPPER.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
    }

    public static String toJsonString(Object obj) {
        return obj != null ? toJsonString(obj, () -> "", false) : "";
    }

    public static String toJsonString(Object obj, Supplier<String> defaultSupplier, boolean format) {
        try {
            if (obj == null) {
                return defaultSupplier.get();
            }
            if (obj instanceof String) {
                return obj.toString();
            }
            if (obj instanceof Number) {
                return obj.toString();
            }
            if (format) {
                return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("toJSONString error", e);
        }
        return defaultSupplier.get();
    }

    public static <T> T toJavaObject(String value, Class<T> tClass) {
        return StringUtils.isNotBlank(value) ? toJavaObject(value, tClass, () -> null) : null;
    }

    public static <T> T toJavaObject(Object obj, Class<T> tClass) {
        return obj != null ? toJavaObject(toJsonString(obj), tClass, () -> null) : null;
    }

    public static <T> T toJavaObject(String value, Class<T> tClass, Supplier<T> defaultSupplier) {
        try {
            if (StringUtils.isBlank(value)) {
                return defaultSupplier.get();
            }
            return MAPPER.readValue(value, tClass);
        } catch (JsonProcessingException e) {
            logger.error("toJavaObject error", e);
        }
        return defaultSupplier.get();
    }

    public static <T> List<T> toJavaObjectList(String value, Class<T> tClass) {
        return StringUtils.isNotBlank(value) ? toJavaObjectList(value, tClass, () -> null) : null;
    }

    public static <T> List<T> toJavaObjectList(Object obj, Class<T> tClass) {
        return obj != null ? toJavaObjectList(toJsonString(obj), tClass, () -> null) : null;
    }

    public static <T> List<T> toJavaObjectList(String value, Class<T> tClass, Supplier<List<T>> defaultSupplier) {
        try {
            if (StringUtils.isBlank(value)) {
                return defaultSupplier.get();
            }
            JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, tClass);
            return MAPPER.readValue(value, javaType);
        } catch (JsonProcessingException e) {
            logger.error("toJavaObjectList error", e);
        }
        return defaultSupplier.get();
    }

    public static Map<String, Object> toMap(String value) {
        return StringUtils.isNotBlank(value) ? toMap(value, () -> null) : null;
    }

    public static Map<String, Object> toMap(Object value) {
        return value != null ? toMap(value, () -> null) : null;
    }

    public static Map<String, Object> toMap(Object value, Supplier<Map<String, Object>> defaultSupplier) {
        if (value == null) {
            return defaultSupplier.get();
        }
        try {
            if (value instanceof Map) {
                return (Map<String, Object>) value;
            }
        } catch (Exception e) {
            logger.info("fail to convert {}", toJsonString(value), e);
        }
        return toMap(toJsonString(value), defaultSupplier);
    }

    public static Map<String, Object> toMap(String value, Supplier<Map<String, Object>> defaultSupplier) {
        if (StringUtils.isBlank(value)) {
            return defaultSupplier.get();
        }
        try {
            return toJavaObject(value, LinkedHashMap.class);
        } catch (Exception e) {
            logger.error("toMap error", e);
        }
        return defaultSupplier.get();
    }


    public static List<Object> toList(String value) {
        return StringUtils.isNotBlank(value) ? toList(value, () -> null) : null;
    }

    public static List<Object> toList(Object value) {
        return value != null ? toList(value, () -> null) : null;
    }

    public static List<Object> toList(String value, Supplier<List<Object>> defaultSuppler) {
        if (StringUtils.isBlank(value)) {
            return defaultSuppler.get();
        }
        try {
            return toJavaObject(value, List.class);
        } catch (Exception e) {
            logger.error("toList error", e);
        }
        return defaultSuppler.get();
    }

    public static List<Object> toList(Object value, Supplier<List<Object>> defaultSuppler) {
        if (value == null) {
            return defaultSuppler.get();
        }
        if (value instanceof List) {
            return (List<Object>) value;
        }
        return toList(toJsonString(value), defaultSuppler);
    }

    public static long getLong(Map<String, Object> map, String key) {
        if (MapUtils.isEmpty(map)) {
            return 0L;
        }
        String valueStr = String.valueOf(map.get(key));
        if (StringUtils.isBlank(valueStr) || !StringUtils.isNumeric(valueStr)) {
            return 0L;
        }
        return Long.parseLong(valueStr);
    }

    public static int getInt(Map<String, Object> map, String key) {
        if (MapUtils.isEmpty(map)) {
            return 0;
        }
        String valueStr = String.valueOf(map.get(key));
        if (StringUtils.isBlank(valueStr) || !StringUtils.isNumeric(valueStr)) {
            return 0;
        }
        return Integer.parseInt(valueStr);
    }
}
