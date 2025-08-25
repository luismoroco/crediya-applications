package com.crediya.applications.model.application;

import lombok.Getter;

@Getter
public enum ApplicationStatus {

  ACTIVE(1),
  PENDING(2);

  private final int code;

  ApplicationStatus(int code) {
    this.code = code;
  }
}
