package com.crediya.applications.logger;

import com.crediya.common.logging.Slf4jLogger;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ Slf4jLogger.class })
public class Slf4jLoggerConfig {
}
