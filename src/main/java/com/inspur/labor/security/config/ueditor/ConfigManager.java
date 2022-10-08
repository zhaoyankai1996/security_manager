package com.inspur.labor.security.config.ueditor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.inspur.labor.security.config.ueditor.define.ActionMap;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * 配置管理器
 *
 * @author hancong03@baidu.com
 */
public final class ConfigManager {

    private final String rootPath;
    private static final String CONFIG_FILE_NAME = "config.json";
    private final String parentPath = null;
    private JSONObject jsonConfig = null;
    /**
     * 涂鸦上传filename定义
     */
    private final static String SCRAWL_FILE_NAME = "scrawl";
    /**
     * 远程图片抓取filename定义
     */
    private final static String REMOTE_FILE_NAME = "remote";

    /**
     * 通过一个给定的路径构建一个配置管理器， 该管理器要求地址路径所在目录下必须存在config.properties文件
     *
     * @param rootPath
     * @param contextPath
     * @param uri
     * @throws FileNotFoundException
     * @throws IOException
     */
    private ConfigManager(String rootPath, String contextPath, String uri) throws FileNotFoundException, IOException {

        rootPath = rootPath.replace("\\", "/");

        this.rootPath = rootPath;

        this.initEnv();

    }

    /**
     * 配置管理器构造工厂
     *
     * @param rootPath    服务器根路径
     * @param contextPath 服务器所在项目路径
     * @param uri         当前访问的uri
     * @return 配置管理器实例或者null
     */
    public static ConfigManager getInstance(String rootPath, String contextPath, String uri) {

        try {
            return new ConfigManager(rootPath, contextPath, uri);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 验证配置文件加载是否正确
     *
     * @return
     */
    public boolean valid() {
        return this.jsonConfig != null;
    }

    public JSONObject getAllConfig() {

        return this.jsonConfig;

    }

    public Map<String, Object> getConfig(int type) throws JSONException {

        Map<String, Object> conf = new HashMap<String, Object>(10);
        String savePath = null;
        String localSavePathPrefix = null;

        switch (type) {

            case ActionMap.UPLOAD_FILE:
                conf.put("isBase64", "false");
                conf.put("maxSize", this.jsonConfig.getLong("fileMaxSize"));
                conf.put("allowFiles", this.getArray("fileAllowFiles"));
                conf.put("fieldName", this.jsonConfig.getString("fileFieldName"));
                savePath = this.jsonConfig.getString("filePathFormat");
                break;

            case ActionMap.UPLOAD_IMAGE:
                conf.put("isBase64", "false");
                conf.put("maxSize", this.jsonConfig.getLong("imageMaxSize"));
                conf.put("allowFiles", this.getArray("imageAllowFiles"));
                conf.put("fieldName", this.jsonConfig.getString("imageFieldName"));
                savePath = this.jsonConfig.getString("imagePathFormat");
                localSavePathPrefix = this.jsonConfig.getString("localSavePathPrefix");
                break;

            case ActionMap.UPLOAD_VIDEO:
                conf.put("maxSize", this.jsonConfig.getLong("videoMaxSize"));
                conf.put("allowFiles", this.getArray("videoAllowFiles"));
                conf.put("fieldName", this.jsonConfig.getString("videoFieldName"));
                savePath = this.jsonConfig.getString("videoPathFormat");
                localSavePathPrefix = this.jsonConfig.getString("localSavePathPrefix");
                break;

            case ActionMap.UPLOAD_SCRAWL:
                conf.put("filename", ConfigManager.SCRAWL_FILE_NAME);
                conf.put("maxSize", this.jsonConfig.getLong("scrawlMaxSize"));
                conf.put("fieldName", this.jsonConfig.getString("scrawlFieldName"));
                conf.put("isBase64", "true");
                savePath = this.jsonConfig.getString("scrawlPathFormat");
                break;

            case ActionMap.CATCH_IMAGE:
                conf.put("filename", ConfigManager.REMOTE_FILE_NAME);
                conf.put("filter", this.getArray("catcherLocalDomain"));
                conf.put("maxSize", this.jsonConfig.getLong("catcherMaxSize"));
                conf.put("allowFiles", this.getArray("catcherAllowFiles"));
                conf.put("fieldName", this.jsonConfig.getString("catcherFieldName") + "[]");
                savePath = this.jsonConfig.getString("catcherPathFormat");
                localSavePathPrefix = this.jsonConfig.getString("localSavePathPrefix");
                break;

            case ActionMap.LIST_IMAGE:
                conf.put("allowFiles", this.getArray("imageManagerAllowFiles"));
                conf.put("dir", this.jsonConfig.getString("imageManagerListPath"));
                conf.put("count", this.jsonConfig.getIntValue("imageManagerListSize"));
                break;

            case ActionMap.LIST_FILE:
                conf.put("allowFiles", this.getArray("fileManagerAllowFiles"));
                conf.put("dir", this.jsonConfig.getString("fileManagerListPath"));
                conf.put("count", this.jsonConfig.getIntValue("fileManagerListSize"));
                break;
            default:
                break;

        }

        conf.put("savePath", savePath);
        conf.put("localSavePathPrefix", localSavePathPrefix);
        conf.put("rootPath", this.rootPath);

        return conf;

    }

    private void initEnv() throws FileNotFoundException, IOException {
        //更改获取配置文件的方式
        ClassPathResource resource = new ClassPathResource("config.json");
        InputStream inputStream = resource.getInputStream();

        try {
            this.jsonConfig = JSONObject.parseObject(IOUtils.toString(inputStream, Charset.defaultCharset()));
        } catch (Exception e) {
            this.jsonConfig = null;
        }
    }

    private String getConfigPath() {
        return this.parentPath + File.separator + ConfigManager.CONFIG_FILE_NAME;
    }

    private String[] getArray(String key) throws JSONException {

        JSONArray jsonArray = this.jsonConfig.getJSONArray(key);
        String[] result = new String[jsonArray.size()];

        for (int i = 0, len = jsonArray.size(); i < len; i++) {
            result[i] = jsonArray.getString(i);
        }

        return result;

    }

    private String readFile(String path) throws IOException {

        StringBuilder builder = new StringBuilder();

        try {

            InputStreamReader reader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
            BufferedReader bfReader = new BufferedReader(reader);

            String tmpContent = null;

            while ((tmpContent = bfReader.readLine()) != null) {
                builder.append(tmpContent);
            }

            bfReader.close();

        } catch (UnsupportedEncodingException e) {
            // 忽略
        }

        return this.filter(builder.toString());

    }

    /**
     * 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
     *
     * @param input
     * @return
     */
    private String filter(String input) {

        return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");

    }

}
