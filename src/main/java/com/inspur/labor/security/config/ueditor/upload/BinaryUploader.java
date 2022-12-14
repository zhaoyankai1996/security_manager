package com.inspur.labor.security.config.ueditor.upload;

import com.inspur.labor.security.config.ueditor.PathFormat;
import com.inspur.labor.security.config.ueditor.define.AppInfo;
import com.inspur.labor.security.config.ueditor.define.BaseState;
import com.inspur.labor.security.config.ueditor.define.FileType;
import com.inspur.labor.security.config.ueditor.define.State;
import com.inspur.labor.security.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 耿鹏
 */
public class BinaryUploader {
    static Logger logger = LoggerFactory.getLogger(BinaryUploader.class);
    private static final String ALLOW_FILES = "allowFiles";

    public static final State save(HttpServletRequest request, Map<String, Object> conf, FileService fileService) {


        boolean isAjaxUpload = request.getHeader("X_Requested_With") != null;


        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("upfile");

            String savePath = (String) conf.get("savePath");
            String localSavePathPrefix = (String) conf.get("localSavePathPrefix");
            String originFileName = file.getOriginalFilename();
            String suffix = FileType.getSuffixByFilename(originFileName);

            originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
            savePath = savePath + suffix;

            long maxSize = (Long) conf.get("maxSize");

            if (!validType(suffix, (String[]) conf.get(ALLOW_FILES))) {
                return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
            }
            savePath = PathFormat.parse(savePath, originFileName);
            localSavePathPrefix = localSavePathPrefix + savePath;
            String physicalPath = localSavePathPrefix;
            logger.info("BinaryUploader physicalPath:{},savePath:{}", localSavePathPrefix, savePath);
            InputStream is = file.getInputStream();

            //在此处调用ftp的上传图片的方法将图片上传到文件服务器
            String path = physicalPath.substring(0, physicalPath.lastIndexOf("/"));
            String picName = physicalPath.substring(physicalPath.lastIndexOf("/") + 1);
            State storageState = StorageManager.saveFileByInputStream(request, is, path, picName, maxSize, file, fileService);

            is.close();

            if (storageState.isSuccess()) {
                storageState.putInfo("type", suffix);
                storageState.putInfo("original", originFileName + suffix);
            }

            return storageState;
        } catch (Exception e) {
            return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
        }
    }

    private static boolean validType(String type, String[] allowTypes) {
        List<String> list = Arrays.asList(allowTypes);

        return list.contains(type);
    }
}
