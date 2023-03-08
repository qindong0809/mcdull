package io.gitee.dqcer.mcdull.business.common.pdf;

import cn.hutool.core.io.FileUtil;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.layout.font.FontProvider;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;

/**
 * 字体工具类
 *
 * @author dqcer
 * @since 2023/03/08
 */
public class FontUtils {

    private static final Logger log = LoggerFactory.getLogger(FontUtils.class);


    public static final String Arial = "Arial.ttf";

    public static final String STSONG = "STSONG.TTF";

    public static final String FONTS = "fonts";
    public static final String CACHE_FOLD = String.join(File.separator, GlobalConstant.TMP_DIR, GlobalConstant.ROOT_PREFIX, Long.toString(System.currentTimeMillis()));
    public final static String FONT_DIR = String.join(File.separator, CACHE_FOLD, FONTS);

    {
        try {
            // 解析类路径下的资源文件
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(FONTS + "/*");
            if (resources != null) {
                FileUtil.touch(FONT_DIR);
                for (Resource resource : resources) {
                    FileUtil.copy(resource.getFile(), new File(String.join(File.separator, FONT_DIR, resource.getFilename())), true);
                }
            }
            FontProgramFactory.registerFontDirectory(FONT_DIR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static PdfFont getFont(Integer languageCode) {
        String path;
        switch (languageCode) {
            case 1:
                path = "stsong";
                break;
            case 2:
            default:
                path = "arial";
                break;
        }
        try {
            return PdfFontFactory.createRegisteredFont(path, PdfName.IdentityH.getValue(), PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * 提供多种字体
     *
     * @return {@link FontProvider}
     */
    public static FontProvider getFontProvider() {
        FontProvider pro = new DefaultFontProvider();
        try {
            pro.addFont(FontProgramFactory.createFont(FontUtils.FONT_DIR + File.separator + Arial));
            pro.addFont(FontProgramFactory.createFont(FontUtils.FONT_DIR + File.separator + STSONG));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("Set Font Provider error");
        }
        return pro;
    }
}
