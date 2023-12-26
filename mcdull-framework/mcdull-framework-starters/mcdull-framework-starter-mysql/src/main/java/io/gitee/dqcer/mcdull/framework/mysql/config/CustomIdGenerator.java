package io.gitee.dqcer.mcdull.framework.mysql.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.Snowflake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * 自定义id生成器
 *
 * @author dqcer
 * @since 2022/12/24
 */
public class CustomIdGenerator implements IdentifierGenerator {

    private Snowflake snowflake;

    private static final Logger log = LoggerFactory.getLogger(CustomIdGenerator.class);

    @Override
    public Long nextId(Object entity) {

        try {
            String hostAddress = null;
            List<String> ipAddress = getIpAddress();
            if (ipAddress.isEmpty()) {
                InetAddress localHost = InetAddress.getLocalHost();
                hostAddress = localHost.getHostAddress();
            } else {
                hostAddress = ipAddress.get(0);
            }

            String lastStrOfIp = hostAddress.substring(hostAddress.lastIndexOf('.') + 1);
            Long num = Long.valueOf(lastStrOfIp);

            long dataCenterId = num / 32;
            long workId = num % 32;

            if (snowflake == null) {
                snowflake = new Snowflake(workId, dataCenterId);
            }
            long nextId = snowflake.nextId();
            if (log.isDebugEnabled()) {
                log.debug("Custom Id Generator ip: {}  dataCenterId: {}  workId: {} nextId: {} ", hostAddress, dataCenterId, workId, nextId);
            }

            return nextId;
        } catch (Exception e) {
            throw new BusinessException(e);
        }


    }

    private static List<String> getIpAddress() throws SocketException {
        List<String> list = new LinkedList<>();
        Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
        while (enumeration.hasMoreElements()) {
            NetworkInterface network = (NetworkInterface) enumeration.nextElement();
            Enumeration addresses = network.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = (InetAddress) addresses.nextElement();
                if (address != null) {
                    if ((address instanceof Inet4Address || address instanceof Inet6Address)) {
                        list.add(address.getHostAddress());
                    }
                }
            }
        }
        return list;
    }
}
