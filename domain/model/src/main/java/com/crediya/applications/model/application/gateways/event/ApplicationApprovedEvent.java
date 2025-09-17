package com.crediya.applications.model.application.gateways.event;

import com.crediya.applications.model.application.Application;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApplicationApprovedEvent {

  private Long applicationId;
  private Long amount;

  public static ApplicationApprovedEvent of(Application application) {
    return ApplicationApprovedEvent.builder()
      .applicationId(application.getApplicationId())
      .amount(application.getAmount())
      .build();
  }
}
