package com.crediya.applications.usecase.application.dto;

import com.crediya.applications.model.application.ApplicationStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GetApplicationsDTO {

  private Integer page;
  private Integer pageSize;
  private List<String> applicationStatuses;

  @Override
  public String toString() {
    return String.format("GetApplicationsDTO: page=%d, pageSize=%d", page, pageSize);
  }
}
