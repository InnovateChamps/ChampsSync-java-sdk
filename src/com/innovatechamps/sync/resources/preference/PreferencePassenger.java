package com.innovatechamps.sync.resources.preference;

import com.innovatechamps.sync.client.common.IdentificationRequest;

import lombok.Getter;

/** Passenger info. */
@Getter
public class PreferencePassenger {
  /** First name. */
  private String firstName;

  /** Last name. */
  private String lastName;

  /** Identification. */
  private IdentificationRequest identification;
}
