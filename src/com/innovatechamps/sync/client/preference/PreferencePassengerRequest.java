package com.innovatechamps.sync.client.preference;

import com.innovatechamps.sync.client.common.IdentificationRequest;

import lombok.Builder;
import lombok.Getter;

/** Passenger info. */
@Getter
@Builder
public class PreferencePassengerRequest {
  /** First name. */
  private final String firstName;

  /** Last name. */
  private final String lastName;

  /** Identification. */
  private final IdentificationRequest identification;
}
