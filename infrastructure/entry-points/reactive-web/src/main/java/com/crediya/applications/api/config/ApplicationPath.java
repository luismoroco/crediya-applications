package com.crediya.applications.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "route.path")
public record ApplicationPath(
  String startApplication,
  String getApplications
) {}
