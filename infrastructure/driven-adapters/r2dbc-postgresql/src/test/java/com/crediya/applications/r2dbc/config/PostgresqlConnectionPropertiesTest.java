package com.crediya.applications.r2dbc.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostgresqlConnectionPropertiesTest {

  @Mock
  private PostgresqlConnectionProperties properties;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    properties = new PostgresqlConnectionProperties(
      "localhost",
      5432,
      "crediya",
      "public",
      "admin",
      "admin123"
    );
  }

  @Test
  void testPropertiesValues() {
    assertEquals("localhost", properties.host());
    assertEquals(5432, properties.port());
    assertEquals("crediya", properties.database());
    assertEquals("public", properties.schema());
    assertEquals("admin", properties.username());
    assertEquals("admin123", properties.password());
  }
}
