package io.gitee.dqcer.mcdull.framework.oss.utils.file;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.config.properties.OssProperties;

import java.io.File;
import java.util.Date;

/**
 * 文件上传工具类
 *
 * @author ruoyi
 */
public class FileUploadUtils {

    /**
     * 默认上传的地址
     */
    private static String defaultBaseDir = OssProperties.getLocalPath();

    public static void setDefaultBaseDir(String defaultBaseDir) {
        FileUploadUtils.defaultBaseDir = defaultBaseDir;
    }

    public static String getDefaultBaseDir() {
        return defaultBaseDir;
    }



    public static final String upload(byte[] bytes, String fileName) {
        String defaultBaseDirPath = getDefaultBaseDir();
        String extension = getExtension(fileName);
        String aafileName = extractFilename(fileName);
        String absPath = getAbsoluteFile(defaultBaseDirPath, aafileName).getAbsolutePath();
        FileUtil.writeBytes(bytes, absPath);
        return getPathFileName(defaultBaseDirPath, aafileName);
    }


    public static final String extractFilename(String fileName) {
        return StrUtil.format("{}/{}_{}.{}", DateUtil.format(new Date(), "yyyy/MM/dd"),
                FileNameUtil.getSuffix(fileName), RandomUtil.randomBytes(32), getExtension(fileName));
    }

    public static final File getAbsoluteFile(String uploadDir, String fileName) {
        File desc = new File(uploadDir + File.separator + fileName);
        if (!desc.exists()) {
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
        }
        return desc;
    }

    public static final String getPathFileName(String uploadDir, String fileName) {
        int dirLastIndex = OssProperties.getLocalPath().length() + 1;
        String currentDir = StrUtil.subPre(uploadDir, dirLastIndex);
        return "/profile" + "/" + currentDir + "/" + fileName;
    }


    public static final String getExtension(String fileName) {
        String extension = FileNameUtil.extName(fileName);
        return extension;
    }
}
