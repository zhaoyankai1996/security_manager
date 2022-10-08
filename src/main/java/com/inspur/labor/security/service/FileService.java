package com.inspur.labor.security.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * @Title: PubFilesService
 * @Description: 附件服务类
 * @Author: liuzq
 * @CreateDate: 2019/12/11 15:04
 * @Version: 1.0
 */
public interface FileService {

    /**
     * 上传附件
     *
     * @param file
     * @throws IOException
     */
    void upload(MultipartFile file) throws IOException;

    /**
     * 富文本上传文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    String ueditorUploadFile(MultipartFile file) throws IOException;

    /**
     * 替换ueditor保存的url前缀
     *
     * @param content
     * @param prefix
     * @return
     */
    String contentUrlReplace(String content, String prefix);

    /**
     * 还原ueditor保存的url
     *
     * @param content 富文本内容
     * @param prefix  要替换的前缀，如
     * @return
     */
    String contentUrlReduction(String content, String prefix);

    /**
     * 还原url
     *
     * @param url
     * @return
     */
    String reductionUrl(String url);
}



