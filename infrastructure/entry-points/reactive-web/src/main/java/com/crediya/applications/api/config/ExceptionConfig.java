package com.crediya.applications.api.config;

import com.crediya.common.api.handling.GlobalExceptionFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ GlobalExceptionFilter.class })
public class ExceptionConfig {
}

