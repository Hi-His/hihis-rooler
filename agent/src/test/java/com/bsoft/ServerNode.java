package com.bsoft;

import com.google.common.collect.Lists;
import com.sun.management.OperatingSystemMXBean;
import ctd.mvc.resource.service.ResourceLocateService;
import ctd.spring.AppDomainContext;
import ctd.util.AppContextHolder;
import ctd.util.NetUtils;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import org.apache.commons.lang3.StringUtils;

@Entity(
        name = "Base_ServerNode"
)
public class ServerNode {
    private static List<ServerNodeExDataBuilder> exDataBuilders;
    private static ServerNode instance;
    private String id;
    private String domain;
    private String osName;
    private String osVersion;
    private String arch;
    private int processors;
    private int phyMemSize;
    private int swapSpace;
    private Date jvmStartTime;
    private String jvmName;
    private String jvmVersion;
    private String ipAddress;
    private List<ServerNodeExData> exDatas;
    private String ip;
    private String httpPath;
    public Map<String,String> domainMapping;

    public ServerNode() {
    }

    public static void setExDataBuilders(List<ServerNodeExDataBuilder> builders) {
        exDataBuilders = builders;
    }

    public static ServerNode instance() {
        if (instance == null) {
            OperatingSystemMXBean os = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
            instance = new ServerNode();
            instance.id = AppDomainContext.getServerId();
            instance.domain = AppContextHolder.getName();
            instance.osName = os.getName();
            instance.osVersion = os.getVersion();
            instance.arch = os.getArch();
            instance.processors = os.getAvailableProcessors();
            instance.phyMemSize = (int)os.getTotalPhysicalMemorySize() >> 20;
            instance.swapSpace = (int)os.getTotalSwapSpaceSize() >> 20;
            RuntimeMXBean jvm = ManagementFactory.getRuntimeMXBean();
            instance.jvmStartTime = new Date(jvm.getStartTime());
            instance.jvmName = jvm.getVmName();
            instance.jvmVersion = jvm.getSpecVersion() + " " + jvm.getVmVersion();
            instance.ipAddress = StringUtils.join(NetUtils.getAllLocalhosts(), ',');
            if (exDataBuilders != null && !exDataBuilders.isEmpty()) {
                List<ServerNodeExData> exDatas = Lists.newArrayList();
                Iterator var3 = exDataBuilders.iterator();

                while(var3.hasNext()) {
                    ServerNodeExDataBuilder exDataBuilder = (ServerNodeExDataBuilder)var3.next();
                    exDatas.add(exDataBuilder.create());
                }

                instance.exDatas = exDatas;
            }




            String rpcServer = AppContextHolder.getRpcServerWorkUrl();
            String ip = rpcServer.replace("tcp://", "").split(":")[0];

            Class c = AppContextHolder.getBean("resourceLocator").getClass();
            String path = (String)c.getDeclaredMethod("getHttpContextPath").invoke(c);

            String port  = path.replace("http://", "").split(":")[1];

            instance.httpPath = "http://" + ip  + port;
            instance.ip = ip;
        }

        return instance;
    }

    @Id
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getOsName() {
        return this.osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getArch() {
        return this.arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public int getProcessors() {
        return this.processors;
    }

    public void setProcessors(int processors) {
        this.processors = processors;
    }

    public int getPhyMemSize() {
        return this.phyMemSize;
    }

    public void setPhyMemSize(int phyMemSize) {
        this.phyMemSize = phyMemSize;
    }

    public int getSwapSpace() {
        return this.swapSpace;
    }

    public void setSwapSpace(int swapSpace) {
        this.swapSpace = swapSpace;
    }

    public Date getJvmStartTime() {
        return this.jvmStartTime;
    }

    public void setJvmStartTime(Date jvmStartTime) {
        this.jvmStartTime = jvmStartTime;
    }

    public String getJvmName() {
        return this.jvmName;
    }

    public void setJvmName(String jvmName) {
        this.jvmName = jvmName;
    }

    public String getJvmVersion() {
        return this.jvmVersion;
    }

    public void setJvmVersion(String jvmVersion) {
        this.jvmVersion = jvmVersion;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Transient
    public List<ServerNodeExData> getExDatas() {
        return this.exDatas;
    }

    public void setExDatas(List<ServerNodeExData> exDatas) {
        this.exDatas = exDatas;
    }

    public <T extends ServerNodeExData> T findExData(Class<T> clz) {
        if (this.exDatas != null && !this.exDatas.isEmpty()) {
            Iterator var2 = this.exDatas.iterator();

            ServerNodeExData exData;
            do {
                if (!var2.hasNext()) {
                    return null;
                }

                exData = (ServerNodeExData)var2.next();
            } while(exData.getClass() != clz);

            return exData;
        } else {
            return null;
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHttpPath() {
        return httpPath;
    }

    public void setHttpPath(String httpPath) {
        this.httpPath = httpPath;
    }

    public Map<String, String> getDomainMapping() {
        return domainMapping;
    }

    public void setDomainMapping(Map<String, String> domainMapping) {
        this.domainMapping = domainMapping;
    }
}
