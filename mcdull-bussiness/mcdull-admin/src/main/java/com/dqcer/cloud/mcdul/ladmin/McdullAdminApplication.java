package com.dqcer.cloud.mcdul.ladmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 麦兜管理应用程序
 *
 * @author QinDong
 * @date 2022/12/26
 */
@Controller
@SpringBootApplication
public class McdullAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(McdullAdminApplication.class, args);
    }


    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("index")
    public String index() {
        return "index";
    }
}
