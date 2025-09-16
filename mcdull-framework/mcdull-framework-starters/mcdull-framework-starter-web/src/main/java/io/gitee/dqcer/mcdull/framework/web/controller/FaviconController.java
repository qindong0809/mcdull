package io.gitee.dqcer.mcdull.framework.web.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Favicon处理控制器
 *
 * @author dqcer
 * @since 2025/09/16
 */
@Controller
public class FaviconController {

    @GetMapping("/favicon.ico")
    public ResponseEntity<Resource> favicon() {
        try {
            Resource resource = new ClassPathResource("static/favicon.ico");
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(resource);
            } else {
                // 如果文件不存在，返回204 No Content
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            // 发生异常时返回204 No Content
            return ResponseEntity.noContent().build();
        }
    }
}
