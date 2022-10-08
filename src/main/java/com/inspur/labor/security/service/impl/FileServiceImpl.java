package com.inspur.labor.security.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.inspur.labor.security.service.FileService;
import com.inspur.labor.security.util.Constant;
import com.inspur.labor.security.util.ErrorCodeEnum;
import com.inspur.labor.security.util.ResponseDTO;
import com.inspur.msy.component.greenchannel.utils.GreenChannelConstants;
import com.inspur.msy.component.greenchannel.utils.ProtoTokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @ClassName FileServiceImpl
 * @Description 附件服务
 * @Date 2022/8/2 10:34
 * @Author gengpeng
 */
@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${oss.uploadFileUrl}")
    private String uploadFileUrl;

    @Value("${oss.downloadUrl}")
    private String downloadFileUrl;

    @Value("${oss.fileGroup}")
    private String fileGroup;


    /**
     * 上传附件
     *
     * @param file
     */
    @Override
    public void upload(MultipartFile file) throws IOException {
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);

        Map ext = new HashMap(10);
        // 业务id，用于文件追踪
        ext.put("bsnId", "");
        // 业务类型，用于文件追踪
        ext.put("bsnType", "");
        // 业务描述，可不填
        ext.put("bsnDesc", "");
        ext.put("group", fileGroup);
        // 业务编码，用于文件追踪
        ext.put("source", "demo");
        String encode = ProtoTokenUtils.newPayload()
                .ext(JSONObject.toJSONString(ext))
                .createToken();
        headers.set(GreenChannelConstants.HEADER_GREEN_CHANNEL_AUTH, encode);

        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        String parentPath = FileUtil.getTmpDirPath() + IdUtil.fastSimpleUUID();
        String tmpFilePath = parentPath + File.separator
                + Objects.requireNonNull(file.getOriginalFilename());
        File localFile = FileUtil.touch(tmpFilePath);
        file.transferTo(localFile);

        param.add("file", new FileSystemResource(localFile));

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param, headers);
        ResponseDTO result = restTemplate.postForObject(uploadFileUrl, httpEntity, ResponseDTO.class);

        if (localFile.exists()) {
            logger.debug("删除临时文件" + localFile.getName());
            FileUtil.del(parentPath);
        }
        if (null != result && ErrorCodeEnum.P_SUCCESS.getCode().equals(result.getCode())) {
            List<LinkedHashMap> pathList = Convert.toList(LinkedHashMap.class, result.getData());
            if (!pathList.isEmpty()) {
                // oss服务返回的地址为相对路径，数据表中存储相对路径，用时拼接为全路径，defautgroup/M00/00/C3/Ct0CSmKW4dKAffqzAAgIGctfOKQ335.png
                logger.debug("文件路径：{}", Convert.toStr(pathList.get(0).get("filePath")));
            }
        }
    }

    /**
     * 富文本上传文件
     *
     * @param file
     * @return
     */
    @Override
    public String ueditorUploadFile(MultipartFile file) throws IOException {
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
        Map ext = new HashMap(10);
        ext.put("bsnDesc", "富文本上传");
        ext.put("group", fileGroup);
        ext.put("source", "demo");
        String encode = ProtoTokenUtils.newPayload()
                .ext(JSONObject.toJSONString(ext))
                .createToken();
        headers.set(GreenChannelConstants.HEADER_GREEN_CHANNEL_AUTH, encode);


        String parentPath = FileUtil.getTmpDirPath() + IdUtil.fastSimpleUUID();
        String tmpFilePath = parentPath + File.separator
                + Objects.requireNonNull(file.getOriginalFilename());
        File parentFile = new File(parentPath);
        if (!parentFile.exists() || !parentFile.isDirectory()) {
            parentFile.mkdirs();
        }
        int loc = file.getOriginalFilename().lastIndexOf(".");
        Path tempFile = Files.createTempFile(parentFile.toPath(), file.getOriginalFilename().substring(0, loc), file.getOriginalFilename().substring(loc));
        file.transferTo(tempFile);
        File localFile = tempFile.toFile();
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("file", new FileSystemResource(localFile));

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param, headers);
        ResponseDTO result = restTemplate.postForObject(uploadFileUrl, httpEntity, ResponseDTO.class);
        if (localFile.exists()) {

            logger.debug("删除临时文件" + localFile.getName());
            FileUtil.del(parentPath);
        }
        if (null != result && ErrorCodeEnum.P_SUCCESS.getCode().equals(result.getCode())) {
            List<LinkedHashMap> pathList = Convert.toList(LinkedHashMap.class, result.getData());
            if (!pathList.isEmpty()) {
                return downloadFileUrl + Convert.toStr(pathList.get(0).get("filePath"));
            }
        }
        return null;
    }

    /**
     * 替换url前缀
     * ueditor
     *
     * @param content
     * @param prefix
     * @return
     */
    @Override
    public String contentUrlReplace(String content, String prefix) {
        //替换图片url前缀
        if (StringUtils.isNotBlank(prefix) && StringUtils.isNotBlank(content) && content.contains(prefix)) {
            content = content.replace(prefix, "_oss_flag_");
        }
        return content;
    }

    /**
     * 还原ueditor保存的url
     *
     * @param content
     * @param prefix
     * @return
     */
    @Override
    public String contentUrlReduction(String content, String prefix) {
        //还原图片url前缀
        if (StringUtils.isNotBlank(content) && content.contains(Constant.OSS_FLAG)) {
            content = content.replace(Constant.OSS_FLAG, prefix);
        }
        return content;
    }

    /**
     * 还原url
     *
     * @return
     */
    @Override
    public String reductionUrl(String url) {
        return StringUtils.isNotBlank(url) ? downloadFileUrl + url : "";
    }
}
