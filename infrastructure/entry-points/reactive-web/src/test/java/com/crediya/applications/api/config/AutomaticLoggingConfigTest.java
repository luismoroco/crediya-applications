package com.crediya.applications.api.config;

import com.crediya.common.logging.Logger;
import com.crediya.common.logging.aspect.AutomaticLoggingAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@SpringBootTest(classes = {
  AutomaticLoggingConfig.class,
  AutomaticLoggingConfigTest.TestConfig.class
})
class AutomaticLoggingConfigTest {

  @Autowired
  private ApplicationContext context;

  @Test
  void AutomaticLoggerIsLoaded() {
    AutomaticLoggingAspect logger = context.getBean(AutomaticLoggingAspect.class);
    assertNotNull(logger, "AutomaticLoggingAspect bean should be loaded in the context");
  }

  @TestConfiguration
  static class TestConfig {
    @Bean
    Logger logger() {
      return mock(Logger.class);
    }
  }
}
