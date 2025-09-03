package com.crediya.applications.model.application;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AggregatedApplication {

  private Long applicationId;
  private Long amount;
  private Integer deadline;
}
