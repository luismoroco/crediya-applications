package com.crediya.applications.api.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationPathTest {

  @Test
  void shouldReturnValuesFromRecord() {
    ApplicationPath path = new ApplicationPath("/start", "/get");

    assertThat(path.startApplication()).isEqualTo("/start");
    assertThat(path.getApplications()).isEqualTo("/get");
  }
}
