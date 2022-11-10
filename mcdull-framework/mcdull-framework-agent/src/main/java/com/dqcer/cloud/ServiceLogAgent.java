package com.dqcer.cloud;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.lang.instrument.Instrumentation;

/**
 * 服务日志代理
 *
 * @author dqcer
 * @date 2022/11/10
 */
public class ServiceLogAgent {

    private static final Logger log = LoggerFactory.getLogger(ServiceLogAgent.class);

    public static void premain(String agentArgs, Instrumentation inst) {
        log.info("Agent start...");

        //动态构建操作，根据transformer规则执行拦截操作
        // 匹配上的具体的类型描述
        AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule) -> {
            //构建拦截规则
            return builder
                    //method()指定哪些方法需要被拦截，ElementMatchers.any()表示拦截所有方法
                    .method(ElementMatchers.any())
                    //intercept()指定拦截上述方法的拦截器
                    .intercept(MethodDelegation.to(new ServiceLogInterceptor()));
        };

        //采用Byte Buddy的AgentBuilder结合Java Agent处理程序
        new AgentBuilder
                //采用ByteBuddy作为默认的Agent实例
                .Default()
                //拦截匹配方式：类以com.itheima.driver开始（其实就是com.itheima.driver包下的所有类）
                .type(ElementMatchers.nameStartsWith("com.dqcer"))
                .and(ElementMatchers.isAnnotatedWith(RestController.class))
                //拦截到的类由transformer处理
                .transform(transformer)
                //安装到 Instrumentation
                //.with(new McdullListener())
                .installOn(inst);
    }


}
