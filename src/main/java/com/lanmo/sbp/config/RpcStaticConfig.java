package com.lanmo.sbp.config;

import com.google.common.collect.Maps;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rpc")
@EnableConfigurationProperties(RpcStaticConfig.class)
public class RpcStaticConfig {


    private Map<String, String> path = Maps.newHashMap();

    public Map<String, String> getPath() {
        return path;
    }

    public void setPath(Map<String, String> path) {
        this.path = path;
    }

}
