package com.innovatechamps.sync.resources.payment;

import com.innovatechamps.sync.resources.common.Identification;

import lombok.Getter;

/** PaymentCardholder class. */
@Getter
public class PaymentCardholder {
  /** Cardholder Name. */
  private String name;

  /** Cardholder identification. */
  private Identification identification;
}
