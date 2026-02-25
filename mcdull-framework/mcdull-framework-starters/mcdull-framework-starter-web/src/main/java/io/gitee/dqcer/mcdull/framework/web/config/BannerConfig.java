package io.gitee.dqcer.mcdull.framework.web.config;

import cn.hutool.core.thread.ThreadUtil;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;

import java.util.concurrent.TimeUnit;

/**
 * banner
 *
 * @author dqcer
 * @since 2023/05/04
 */
public class BannerConfig implements ApplicationRunner {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private Environment environment;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ThreadUtil.execute( () -> {
            ThreadUtil.sleep(1, TimeUnit.SECONDS); // 延迟 1 秒
            String port = environment.getProperty("server.port", "8080");
            LogHelp.info(log,  "\n----------------------------------------------------------\n\t" +
                            "Druid monitor url: \t{} \n\t" +
                            "API Doc url: \t{} \n\t" +
                            "Monitoring url: \t{} \n\t" +
                            "----------------------------------------------------------",
                    "http://localhost:" + port + "/druid",
                "http://localhost:" + port + "/doc.html",
                "http://localhost:" + port + "/monitoring"
            );
        });
    }
}
