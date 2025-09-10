package com.crediya.applications.usecase.application.dto;

import com.crediya.applications.model.application.ApplicationStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UpdateApplicationDTO {

  private Long applicationId;
  private ApplicationStatus applicationStatus;

  @Override
  public String toString() {
    return String.format("[applicationId=%s][applicationStatus=%s]", applicationId, applicationStatus);
  }
}
