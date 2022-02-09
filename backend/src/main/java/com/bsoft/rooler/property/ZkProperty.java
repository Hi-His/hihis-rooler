package com.bsoft.rooler.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: 何胜豪
 * @Title: TODO
 * @Package: com.bsoft.rooler.property
 * @Description:
 * @date : 2021/12/13 13:29
 */

@Data
@ConfigurationProperties(prefix = "zookeeper")
public class ZkProperty {

    private String connectString;

    private String auth;

}
