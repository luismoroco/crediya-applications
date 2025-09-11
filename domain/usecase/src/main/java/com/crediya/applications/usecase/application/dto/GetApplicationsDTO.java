package com.crediya.applications.usecase.application.dto;

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
  private List<String> emails;

  @Override
  public String toString() {
    return String.format("[page=%s][pageSize=%s][applicationStatuses=%s]", page, pageSize, applicationStatuses);
  }
}
