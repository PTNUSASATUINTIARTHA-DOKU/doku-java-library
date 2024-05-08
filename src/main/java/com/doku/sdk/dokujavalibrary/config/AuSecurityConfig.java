package com.doku.sdk.dokujavalibrary.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = {
        "com.doku.au.security.module.encryption",
        "com.doku.au.security.module.snap"
})
@Configuration
@PropertySource("${doku.au-security-common.properties}")
public class AuSecurityConfig {
}
