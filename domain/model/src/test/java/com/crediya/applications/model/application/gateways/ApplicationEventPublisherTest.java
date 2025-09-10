package com.crediya.applications.model.application.gateways;

import com.crediya.applications.model.application.ApplicationStatus;
import com.crediya.applications.model.application.gateways.event.ApplicationUpdatedEvent;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class ApplicationEventPublisherTest {

  @Test
  void testNotifyApplicationUpdated_returnsExpectedMono() {
    ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
    ApplicationUpdatedEvent event = ApplicationUpdatedEvent.builder()
      .applicationId(1L)
      .amount(5000L)
      .deadline(12)
      .applicationStatus(ApplicationStatus.PENDING)
      .email("test@example.com")
      .build();

    when(publisher.notifyApplicationUpdated(event)).thenReturn(Mono.just("12345678"));

    StepVerifier.create(publisher.notifyApplicationUpdated(event))
      .expectNext("12345678")
      .verifyComplete();

    verify(publisher, times(1)).notifyApplicationUpdated(event);
  }
}
