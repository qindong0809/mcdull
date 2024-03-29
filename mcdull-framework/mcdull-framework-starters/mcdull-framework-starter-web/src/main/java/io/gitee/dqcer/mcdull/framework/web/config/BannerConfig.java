package io.gitee.dqcer.mcdull.framework.web.config;

import cn.hutool.core.thread.ThreadUtil;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.concurrent.TimeUnit;

/**
 * banner
 *
 * @author dqcer
 * @since 2023/05/04
 */
public class BannerConfig implements ApplicationRunner {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ThreadUtil.execute( () -> {
            ThreadUtil.sleep(1, TimeUnit.SECONDS); // 延迟 1 秒
            LogHelp.info(log,  "\n----------------------------------------------------------\n\t" +
                            "Druid monitor paged: \t{} \n\t" +
                            "H2 console: \t{} \n\t" +
                            "Swagger: \t{} \n\t" +
                            "----------------------------------------------------------",
                    "http://localhost:8090/druid","http://localhost:8090/h2", "http://localhost:8090/doc.html"
            );
        });
    }
}
