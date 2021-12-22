package com.qzero.tunnel.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:prefer.properties")
@ConfigurationProperties(prefix = "prefer")
public class PreferenceConfig {

    private boolean checkServerAlive;

    public PreferenceConfig() {
    }

    public PreferenceConfig(boolean checkServerAlive) {
        this.checkServerAlive = checkServerAlive;
    }

    public boolean isCheckServerAlive() {
        return checkServerAlive;
    }

    public void setCheckServerAlive(boolean checkServerAlive) {
        this.checkServerAlive = checkServerAlive;
    }

    @Override
    public String toString() {
        return "PreferenceConfig{" +
                "checkServerAlive=" + checkServerAlive +
                '}';
    }
}
