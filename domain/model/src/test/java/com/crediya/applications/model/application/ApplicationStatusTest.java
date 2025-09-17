package com.crediya.applications.model.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationStatusTest {

  @Test
  void testEnumValues() {
    ApplicationStatus active = ApplicationStatus.REJECTED;
    ApplicationStatus pending = ApplicationStatus.PENDING;

    assertNotNull(active);
    assertNotNull(pending);

    assertEquals(1, active.getCode());
    assertEquals(2, pending.getCode());
  }

  @Test
  void testValuesCount() {
    ApplicationStatus[] values = ApplicationStatus.values();
    assertEquals(4, values.length, "Only 4 status values");
  }

  @Test
  void testValueOf() {
    assertEquals(ApplicationStatus.REJECTED, ApplicationStatus.valueOf("REJECTED"));
    assertEquals(ApplicationStatus.PENDING, ApplicationStatus.valueOf("PENDING"));
  }

  @Test
  void testFromCodeInvalidValueThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> ApplicationStatus.fromCode(99));
  }
}
