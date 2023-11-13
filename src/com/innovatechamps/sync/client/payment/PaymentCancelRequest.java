package com.innovatechamps.sync.client.payment;

import static com.innovatechamps.sync.resources.payment.PaymentStatus.CANCELLED;

import lombok.Builder;
import lombok.Getter;

/** PaymentCancelRequest class. */
@Getter
@Builder
public class PaymentCancelRequest {
  /** Status cancelled. */
  private final String status = CANCELLED;
}
