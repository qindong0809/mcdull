package com.dqcer.cloud;

import cn.hutool.db.Db;
import net.bytebuddy.agent.VirtualMachine;
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
 *  参考资料： https://github.com/fuzhengwei/CodeGuide/tree/master/docs/md/bytecode/byte-buddy
 *
 * @author dqcer
 * @version 2022/11/10
 */
public class ServiceLogAgent {

    private static final Logger log = LoggerFactory.getLogger(ServiceLogAgent.class);

    public static void premain(String agentArgs, Instrumentation inst) {
        log.info("Agent start...");

        Db use = Db.use();

        //动态构建操作，根据transformer规则执行拦截操作
        // 匹配上的具体的类型描述
        AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule) -> {
            //构建拦截规则
            return builder
                    //method()指定哪些方法需要被拦截，ElementMatchers.any()表示拦截所有方法
//                    .method(ElementMatchers.any())
                    .method(ElementMatchers.not(ElementMatchers.isHashCode()).and(ElementMatchers.not(ElementMatchers.isToString())))
                    //intercept()指定拦截上述方法的拦截器
                    .intercept(MethodDelegation.to(new ServiceLogInterceptor(use)));
        };

        //采用Byte Buddy的AgentBuilder结合Java Agent处理程序
        new AgentBuilder
                //采用ByteBuddy作为默认的Agent实例
                .Default()
                //拦截匹配方式
                .type(ElementMatchers.nameStartsWith("com"))
                .and(ElementMatchers.isAnnotatedWith(RestController.class))
                //拦截到的类由transformer处理
                .transform(transformer)
                //安装到 Instrumentation
                //.with(new McdullListener())
                .installOn(inst);
    }


}
