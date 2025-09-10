package com.crediya.applications.usecase.application.dto;

import com.crediya.applications.model.application.ApplicationStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateApplicationDTOTest {

  @Test
  void testBuilderAndGetters() {
    UpdateApplicationDTO dto = UpdateApplicationDTO.builder()
      .applicationId(10L)
      .applicationStatus(ApplicationStatus.PENDING)
      .build();

    assertThat(dto.getApplicationId()).isEqualTo(10L);
    assertThat(dto.getApplicationStatus()).isEqualTo(ApplicationStatus.PENDING);
  }

  @Test
  void testSetters() {
    UpdateApplicationDTO dto = new UpdateApplicationDTO();

    dto.setApplicationId(20L);
    dto.setApplicationStatus(ApplicationStatus.REJECTED);

    assertThat(dto.getApplicationId()).isEqualTo(20L);
    assertThat(dto.getApplicationStatus()).isEqualTo(ApplicationStatus.REJECTED);
  }

  @Test
  void testToString_returnsFormattedString() {
    UpdateApplicationDTO dto = UpdateApplicationDTO.builder()
      .applicationId(5L)
      .applicationStatus(ApplicationStatus.PENDING)
      .build();

    String result = dto.toString();

    assertThat(result).isEqualTo("[applicationId=5][applicationStatus=PENDING]");
  }
}
