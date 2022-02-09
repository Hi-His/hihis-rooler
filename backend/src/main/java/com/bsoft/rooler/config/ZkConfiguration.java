package com.bsoft.rooler.config;

import com.bsoft.rooler.property.ZkProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 何胜豪
 * @Title: TODO
 * @Package: com.bsoft.rooler.property
 * @Description:
 * @date : 2021/12/13 13:28
 */
@Configuration
@EnableConfigurationProperties(value = ZkProperty.class)
public class ZkConfiguration {

}
