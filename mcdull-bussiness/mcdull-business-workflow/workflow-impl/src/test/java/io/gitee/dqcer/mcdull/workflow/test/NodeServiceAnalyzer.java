package io.gitee.dqcer.mcdull.workflow.test;

import org.dromara.warm.flow.core.service.NodeService;
import java.lang.reflect.Method;

public class NodeServiceAnalyzer {
    public static void main(String[] args) {
        Class<NodeService> nodeServiceClass = NodeService.class;
        Method[] methods = nodeServiceClass.getMethods();
        
        System.out.println("NodeService接口的方法列表：");
        System.out.println("=================================");
        
        for (Method method : methods) {
            if (method.getDeclaringClass() == NodeService.class) {
                System.out.println("方法名：" + method.getName());
                System.out.println("返回类型：" + method.getReturnType().getName());
                
                Class<?>[] parameterTypes = method.getParameterTypes();
                System.out.print("参数类型：");
                for (int i = 0; i < parameterTypes.length; i++) {
                    System.out.print(parameterTypes[i].getName());
                    if (i < parameterTypes.length - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
                System.out.println("---------------------------------");
            }
        }
    }
}