package com.crediya.applications.logger;

import com.crediya.common.logging.Slf4jLogger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = Slf4jLoggerConfig.class)
public class Slf4jLoggerConfigTest {

  @Autowired
  private ApplicationContext context;

  @Test
  void slf4jLoggerIsLoaded() {
    Slf4jLogger logger = context.getBean(Slf4jLogger.class);
    assertNotNull(logger, "GlobalExceptionFilter bean should be loaded in the context");
  }
}
