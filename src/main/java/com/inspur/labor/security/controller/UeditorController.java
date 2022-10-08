package com.inspur.labor.security.controller;

import com.inspur.labor.security.config.ueditor.ActionEnter;
import com.inspur.labor.security.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * @ClassName UeditorController
 * @Description ueditor配置
 * @Date 2022/3/8 14:47
 * @Author gengpeng
 */
@RestController
@CrossOrigin
@RequestMapping("/ueditor")
public class UeditorController extends BaseController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/exec")
    @ResponseBody
    public String exec(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        String rootPath = request.getSession().getServletContext().getRealPath(File.separator);
        return new ActionEnter(request, rootPath, fileService).exec();
    }
}
