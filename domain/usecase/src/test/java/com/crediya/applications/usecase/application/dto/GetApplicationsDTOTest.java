package com.crediya.applications.usecase.application.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetApplicationsDTOTest {

  @Test
  void testToStringWithValues() {
    GetApplicationsDTO dto = GetApplicationsDTO.builder()
      .page(1)
      .pageSize(10)
      .applicationStatuses(List.of("PENDING", "APPROVED"))
      .build();

    String expected = "[page=1][pageSize=10][applicationStatuses=[PENDING, APPROVED]]";

    assertEquals(expected, dto.toString());
  }
}
