package com.crediya.applications.api.config;

import com.crediya.common.api.handling.GlobalExceptionFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = ExceptionConfig.class)
class ExceptionConfigTest {

  @Autowired
  private ApplicationContext context;

  @Test
  void globalExceptionFilterBeanIsLoaded() {
    GlobalExceptionFilter filter = context.getBean(GlobalExceptionFilter.class);
    assertNotNull(filter, "GlobalExceptionFilter bean should be loaded in the context");
  }
}
