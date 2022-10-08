package com.inspur.labor.security.config.ueditor.upload;


import com.inspur.labor.security.config.ueditor.define.State;
import com.inspur.labor.security.service.FileService;
import com.inspur.labor.security.util.Constant;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 耿鹏
 */
public class Uploader {
    private HttpServletRequest request = null;
    private Map<String, Object> conf = null;
    private FileService fileService = null;

    public Uploader(HttpServletRequest request, Map<String, Object> conf, FileService fileService) {
        this.request = request;
        this.conf = conf;
        this.fileService = fileService;
    }

    public final State doExec() {
        String filedName = (String) this.conf.get("fieldName");
        State state = null;

        if (Constant.TRUE.equals(this.conf.get(Constant.IS_BASE_64))) {
            state = Base64Uploader.save(this.request.getParameter(filedName),
                    this.conf);
        } else {
            state = BinaryUploader.save(this.request, this.conf, fileService);
        }

        return state;
    }
}
