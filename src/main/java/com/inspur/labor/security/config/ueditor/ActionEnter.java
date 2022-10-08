package com.inspur.labor.security.config.ueditor;


import com.alibaba.fastjson.JSONException;
import com.inspur.labor.security.config.ueditor.define.ActionMap;
import com.inspur.labor.security.config.ueditor.define.AppInfo;
import com.inspur.labor.security.config.ueditor.define.BaseState;
import com.inspur.labor.security.config.ueditor.define.State;
import com.inspur.labor.security.config.ueditor.hunter.FileManager;
import com.inspur.labor.security.config.ueditor.hunter.ImageHunter;
import com.inspur.labor.security.config.ueditor.upload.Uploader;
import com.inspur.labor.security.service.FileService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 耿鹏
 */
public class ActionEnter {

    private HttpServletRequest request = null;

    private String rootPath = null;
    private String contextPath = null;

    private String actionType = null;

    private ConfigManager configManager = null;

    private FileService fileService = null;

    private static final String REG_STR = "^[a-zA-Z_]+[\\w0-9_]*$";

    public ActionEnter(HttpServletRequest request, String rootPath, FileService fileService) {

        this.request = request;
        this.rootPath = rootPath;
        this.actionType = request.getParameter("action");
        this.contextPath = request.getContextPath();
        this.fileService = fileService;
        this.configManager = ConfigManager.getInstance(this.rootPath, this.contextPath, request.getRequestURI());

    }

    public String exec() throws JSONException {

        String callbackName = this.request.getParameter("callback");

        if (callbackName != null) {

            if (!validCallbackName(callbackName)) {
                return new BaseState(false, AppInfo.ILLEGAL).toJsonString();
            }

            return this.invoke();

        } else {
            return this.invoke();
        }

    }

    public String invoke() throws JSONException {

        if (actionType == null || !ActionMap.MAPPING.containsKey(actionType)) {
            return new BaseState(false, AppInfo.INVALID_ACTION).toJsonString();
        }

        if (this.configManager == null || !this.configManager.valid()) {
            return new BaseState(false, AppInfo.CONFIG_ERROR).toJsonString();
        }

        State state = null;

        int actionCode = ActionMap.getType(this.actionType);

        Map<String, Object> conf = null;

        switch (actionCode) {

            case ActionMap.CONFIG:
                return this.configManager.getAllConfig().toJSONString();

            case ActionMap.UPLOAD_IMAGE:
            case ActionMap.UPLOAD_SCRAWL:
            case ActionMap.UPLOAD_VIDEO:
            case ActionMap.UPLOAD_FILE:
                conf = this.configManager.getConfig(actionCode);
                state = new Uploader(request, conf, fileService).doExec();
                break;

            case ActionMap.CATCH_IMAGE:
                conf = configManager.getConfig(actionCode);
                String[] list = this.request.getParameterValues((String) conf.get("fieldName"));
                state = new ImageHunter(conf).capture(list);
                break;

            case ActionMap.LIST_IMAGE:
            case ActionMap.LIST_FILE:
                conf = configManager.getConfig(actionCode);
                int start = this.getStartIndex();
                state = new FileManager(conf).listFile(start);
                break;
            default:
                break;
        }

        return state.toJsonString();

    }

    public int getStartIndex() {

        String start = this.request.getParameter("start");

        try {
            return Integer.parseInt(start);
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * callback参数验证
     */
    public boolean validCallbackName(String name) {

        if (name.matches(REG_STR)) {
            return true;
        }

        return false;

    }

}