package io.gitee.dqcer.mcdull.gateway.satoken;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.convert.Convert;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.gateway.utils.SpringUtils;
import io.gitee.dqcer.mcdull.uac.client.service.AuthClientService;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author dqcer
 */
//@Component
public class StpInterfaceImpl implements StpInterface {

    private static final ExecutorService executorService = new ThreadPoolExecutor(1, 1, 3000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000), new ThreadFactoryBuilder()
            .setNameFormat("auth-pool-%d").build());

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的权限列表
        return permissionList(loginId);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的角色列表
        return this.roleList(loginId);
    }

    private List<String> permissionList(Object userId) {
        AuthClientService authClientService = SpringUtils.getBean(AuthClientService.class);

        RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
        // WebFlux异步调用，同步会报错
        Future<Result<List<String>>> future = executorService.submit(() -> authClientService.getPermissionList(Convert.toLong(userId)));
        Result<List<String>> result;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return result.getData();
    }

    private List<String> roleList(Object userId) {
        AuthClientService authClientService = SpringUtils.getBean(AuthClientService.class);

        RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
        // WebFlux异步调用，同步会报错
        Future<Result<List<String>>> future = executorService.submit(() -> authClientService.getRoleList(Convert.toLong(userId)));
        Result<List<String>> result;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return result.getData();
    }

}