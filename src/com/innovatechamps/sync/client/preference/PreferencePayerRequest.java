package com.innovatechamps.sync.client.preference;

import java.time.OffsetDateTime;

import com.innovatechamps.sync.client.common.AddressRequest;
import com.innovatechamps.sync.client.common.IdentificationRequest;
import com.innovatechamps.sync.client.common.PhoneRequest;

import lombok.Builder;
import lombok.Getter;

/** Payer information. */
@Getter
@Builder
public class PreferencePayerRequest {
  /** Payer's name. */
  private final String name;

  /** Payer's surname. */
  private final String surname;

  /** Payer's email. */
  private final String email;

  /** Payer's phone. */
  private final PhoneRequest phone;

  /** Payer's identification. */
  private final IdentificationRequest identification;

  /** Payer's address. */
  private final AddressRequest address;

  /** Date of creation of the payer user. */
  private final OffsetDateTime dateCreated;
}
