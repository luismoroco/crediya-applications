package com.crediya.applications.model.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationStatusTest {

  @Test
  void testEnumValues() {
    ApplicationStatus active = ApplicationStatus.ACTIVE;
    ApplicationStatus pending = ApplicationStatus.PENDING;

    assertNotNull(active);
    assertNotNull(pending);

    assertEquals(1, active.getCode());
    assertEquals(2, pending.getCode());
  }

  @Test
  void testValuesCount() {
    ApplicationStatus[] values = ApplicationStatus.values();
    assertEquals(2, values.length, "Debe haber exactamente 2 estados");
  }

  @Test
  void testValueOf() {
    assertEquals(ApplicationStatus.ACTIVE, ApplicationStatus.valueOf("ACTIVE"));
    assertEquals(ApplicationStatus.PENDING, ApplicationStatus.valueOf("PENDING"));
  }
}
