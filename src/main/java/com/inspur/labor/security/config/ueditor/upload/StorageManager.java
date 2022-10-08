package com.inspur.labor.security.config.ueditor.upload;

import com.inspur.labor.security.config.ueditor.define.AppInfo;
import com.inspur.labor.security.config.ueditor.define.BaseState;
import com.inspur.labor.security.config.ueditor.define.State;
import com.inspur.labor.security.service.FileService;
import com.inspur.labor.security.util.Constant;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @author 耿鹏
 */
@Component
@ConfigurationProperties(prefix = "nginx")
public class StorageManager {

    public static final int BUFFER_SIZE = 8192;

    private static String fileurl;

    public static String getFileurl() {
        return fileurl;
    }

    public static void setFileurl(String fileurl) {
        StorageManager.fileurl = fileurl;
    }

    public static int getBufferSize() {
        return BUFFER_SIZE;
    }

    public StorageManager() {
    }

    public static State saveBinaryFile(byte[] data, String path) {
        File file = new File(path);

        State state = valid(file);

        if (!state.isSuccess()) {
            return state;
        }

        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bos.write(data);
            bos.flush();
            bos.close();
        } catch (IOException ioe) {
            return new BaseState(false, AppInfo.IO_ERROR);
        }

        state = new BaseState(true, file.getAbsolutePath());
        state.putInfo("size", data.length);
        state.putInfo("title", file.getName());
        return state;
    }

    public static State saveFileByInputStream(HttpServletRequest request, InputStream is, String path, String picName,
                                              long maxSize, MultipartFile file, FileService fileService) {

        State state = null;
        File tmpFile = getTmpFile();
        byte[] dataBuf = new byte[2048];

        try {
            //转成字节流
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            int rc = 0;
            while ((rc = is.read(dataBuf, 0, Constant.BUF_LENGTH)) > 0) {
                swapStream.write(dataBuf, 0, rc);
            }

            dataBuf = swapStream.toByteArray();
            swapStream.flush();
            swapStream.close();

            if (tmpFile.length() > maxSize) {
                tmpFile.delete();
                return new BaseState(false, AppInfo.MAX_SIZE);
            }
            //调用DFS的存储服务上传文件
            String url = fileService.ueditorUploadFile(file);

            boolean success = true;
            //如果上传成功
            if (success) {
                state = new BaseState(true);
                state.putInfo("size", tmpFile.length());
                //文件名填入此处
                state.putInfo("title", picName);
                //所属group填入此处
                state.putInfo("group", "");
                //文件访问的url填入此处
                state.putInfo("url", url);
                tmpFile.delete();
            } else {
                state = new BaseState(false, 4);
                tmpFile.delete();
            }

            return state;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BaseState(false, AppInfo.IO_ERROR);
    }

    public static State saveFileByInputStream(InputStream is, String path, String picName) {
        State state = null;

        File tmpFile = getTmpFile();

        byte[] dataBuf = new byte[2048];
        BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);

        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

            int count = 0;
            while ((count = bis.read(dataBuf)) != -1) {
                bos.write(dataBuf, 0, count);
            }
            bos.flush();
            bos.close();


            boolean success = true;

            //如果上传成功
            if (success) {
                state = new BaseState(true);
                state.putInfo("size", tmpFile.length());
                state.putInfo("title", tmpFile.getName());
                tmpFile.delete();
            } else {
                state = new BaseState(false, 4);
                tmpFile.delete();
            }

            return state;
        } catch (IOException e) {
        }
        return new BaseState(false, AppInfo.IO_ERROR);
    }

    private static File getTmpFile() {
        File tmpDir = FileUtils.getTempDirectory();
        String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
        return new File(tmpDir, tmpFileName);
    }

    private static State saveTmpFile(File tmpFile, String path) {
        State state = null;
        File targetFile = new File(path);

        if (targetFile.canWrite()) {
            return new BaseState(false, AppInfo.PERMISSION_DENIED);
        }
        try {
            FileUtils.moveFile(tmpFile, targetFile);
        } catch (IOException e) {
            return new BaseState(false, AppInfo.IO_ERROR);
        }

        state = new BaseState(true);
        state.putInfo("size", targetFile.length());
        state.putInfo("title", targetFile.getName());

        return state;
    }

    private static State valid(File file) {
        File parentPath = file.getParentFile();

        if ((!parentPath.exists()) && (!parentPath.mkdirs())) {
            return new BaseState(false, AppInfo.FAILED_CREATE_FILE);
        }

        if (!parentPath.canWrite()) {
            return new BaseState(false, AppInfo.PERMISSION_DENIED);
        }

        return new BaseState(true);
    }
}
