package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.date.BetweenFormatter.Level;
import cn.hutool.core.date.DateUtil;
import cn.hutool.system.HostInfo;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.web.basic.GenericLogic;
import io.gitee.dqcer.mcdull.system.provider.web.service.IServerMonitorService;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.VirtualMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;
import oshi.util.Util;

import java.lang.management.RuntimeMXBean;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Server Monitor Service
 *
 * @author dqcer
 * @since 2024/05/31
 */
@Service
public class ServerMonitorServiceImpl
        extends GenericLogic implements IServerMonitorService {


    private final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public Map<String,Object> getServers(){
        Map<String, Object> resultMap = new LinkedHashMap<>(8);
        try {
            SystemInfo si = new SystemInfo();
            OperatingSystem os = si.getOperatingSystem();
            HardwareAbstractionLayer hal = si.getHardware();
            // 系统信息
            resultMap.put("sys", getSystemInfo());
            // cpu 信息
            resultMap.put("cpu", getCpuInfo(hal.getProcessor()));
            // 内存信息
            resultMap.put("memory", getMemoryInfo(hal.getMemory()));
            // 交换区信息
            resultMap.put("swap", getSwapInfo(hal.getMemory()));
            // 磁盘
            resultMap.put("disk", getDiskInfo(os));
            resultMap.put("time", DateUtil.format(new Date(), "HH:mm:ss"));
        } catch (Exception e) {
            LogHelp.error(log, "getServers error", e);
        }
        return resultMap;
    }

    private Map<String,Object> getDiskInfo(OperatingSystem os) {
        Map<String,Object> diskInfo = new LinkedHashMap<>();
        FileSystem fileSystem = os.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        String osName = System.getProperty("os.name");
        long available = 0, total = 0;
        for (OSFileStore fs : fsArray){
            // windows 需要将所有磁盘分区累加，linux 和 mac 直接累加会出现磁盘重复的问题，待修复
            if(osName.toLowerCase().startsWith("win")) {
                available += fs.getUsableSpace();
                total += fs.getTotalSpace();
            } else {
                available = fs.getUsableSpace();
                total = fs.getTotalSpace();
                break;
            }
        }
        long used = total - available;
        diskInfo.put("total", total > 0 ? this.getSize(total) : "?");
        diskInfo.put("available", this.getSize(available));
        diskInfo.put("used", this.getSize(used));
        if(total != 0){
            diskInfo.put("usageRate", df.format(used/(double)total * 100));
        } else {
            diskInfo.put("usageRate", 0);
        }
        return diskInfo;
    }

    public String getSize(long size) {
        final int gb = 1024 * 1024 * 1024;
        final int mb = 1024 * 1024;
        final int kb = 1024;

        String resultSize;
        if (size / gb >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = df.format(size / (float) gb) + "GB   ";
        } else if (size / mb >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = df.format(size / (float) mb) + "MB   ";
        } else if (size / kb >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = df.format(size / (float) kb) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }


    private Map<String,Object> getSwapInfo(GlobalMemory memory) {
        Map<String,Object> swapInfo = new LinkedHashMap<>();
        VirtualMemory virtualMemory = memory.getVirtualMemory();
        long total = virtualMemory.getSwapTotal();
        long used = virtualMemory.getSwapUsed();
        swapInfo.put("total", FormatUtil.formatBytes(total));
        swapInfo.put("used", FormatUtil.formatBytes(used));
        swapInfo.put("available", FormatUtil.formatBytes(total - used));
        if(used == 0){
            swapInfo.put("usageRate", 0);
        } else {
            swapInfo.put("usageRate", df.format(used/(double)total * 100));
        }
        return swapInfo;
    }

    private Map<String,Object> getMemoryInfo(GlobalMemory memory) {
        Map<String,Object> memoryInfo = new LinkedHashMap<>();
        memoryInfo.put("total", FormatUtil.formatBytes(memory.getTotal()));
        memoryInfo.put("totalNum", FormatUtil.formatBytes(memory.getTotal()));
        memoryInfo.put("available", FormatUtil.formatBytes(memory.getAvailable()));
        memoryInfo.put("used", FormatUtil.formatBytes(memory.getTotal() - memory.getAvailable()));
        memoryInfo.put("usageRate", df.format((memory.getTotal() - memory.getAvailable())/(double)memory.getTotal() * 100));
        return memoryInfo;
    }

    private Map<String,Object> getCpuInfo(CentralProcessor processor) {
        Map<String,Object> cpuInfo = new LinkedHashMap<>();
        cpuInfo.put("name", processor.getProcessorIdentifier().getName());
        cpuInfo.put("package", processor.getPhysicalPackageCount() + "个物理CPU");
        cpuInfo.put("core", processor.getPhysicalProcessorCount() + "个物理核心");
        cpuInfo.put("coreNumber", processor.getPhysicalProcessorCount());
        cpuInfo.put("logic", processor.getLogicalProcessorCount() + "个逻辑CPU");
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 默认等待300毫秒...
        long time = 300;
        Util.sleep(time);
        long[] ticks = processor.getSystemCpuLoadTicks();
        while (Arrays.toString(prevTicks).equals(Arrays.toString(ticks)) && time < 1000){
            time += 25;
            Util.sleep(25);
            ticks = processor.getSystemCpuLoadTicks();
        }
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;
        cpuInfo.put("used", df.format(100d * user / totalCpu + 100d * sys / totalCpu));
        cpuInfo.put("idle", df.format(100d * idle / totalCpu));
        return cpuInfo;
    }

    private Map<String,Object> getSystemInfo(){
        Map<String,Object> systemInfo = new LinkedHashMap<>();
        // jvm 运行时间
        RuntimeMXBean runtimeMXBean = SystemUtil.getRuntimeMXBean();
        long time =  runtimeMXBean.getStartTime();
        Date date = new Date(time);
        // 计算项目运行时间
        String formatBetween = DateUtil.formatBetween(date, new Date(), Level.SECOND);
        OsInfo osInfo = SystemUtil.getOsInfo();
        // 系统信息
        systemInfo.put("os", osInfo.toString());
        systemInfo.put("day", formatBetween);

        HostInfo hostInfo = SystemUtil.getHostInfo();
        systemInfo.put("ip",  hostInfo.getAddress());

        return systemInfo;
    }
}
