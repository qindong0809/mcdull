package com.dqcer.cloud.mcdul.ladmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
