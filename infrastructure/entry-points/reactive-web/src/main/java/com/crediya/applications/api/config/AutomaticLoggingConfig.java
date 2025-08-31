package com.crediya.applications.api.config;

import com.crediya.common.logging.aspect.AutomaticLoggingAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ AutomaticLoggingAspect.class })
public class AutomaticLoggingConfig {
}
