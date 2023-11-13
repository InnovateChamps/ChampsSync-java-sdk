package com.innovatechamps.sync.resources.payment;


import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.innovatechamps.sync.network.DCResource;
import com.innovatechamps.sync.resources.common.Source;

import lombok.Getter;
/** PaymentRefund class. */
@Getter
public class PaymentRefund extends DCResource {
  /** Refund id. */
  private Long id;

  /** ID of the refunded payment. */
  private Long paymentId;

  /** Amount refunded. */
  private BigDecimal amount;

  /** Adjustment amount. */
  private BigDecimal adjustmentAmount;

  /** Refund status. */
  private String status;

  /** Refund mode. */
  private String refundMode;

  /** Date of creation. */
  private OffsetDateTime dateCreated;

  /** Refund reason. */
  private String reason;

  /** Unique sequence number. */
  private String uniqueSequenceNumber;

  /** Source of the refund. */
  private Source source;
}
