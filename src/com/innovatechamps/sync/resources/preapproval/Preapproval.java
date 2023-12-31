package com.innovatechamps.sync.resources.preapproval;

import java.time.OffsetDateTime;

import com.innovatechamps.sync.network.DCResource;

import lombok.Getter;

/** Preapproval resource. */
@Getter
public class Preapproval extends DCResource {
  /** Preapproval ID. */
  private String id;

  /** Payer ID. */
  private Long payerId;

  /** Payer email. */
  private String payerEmail;

  /** Return URL. */
  private String backUrl;

  /** Seller ID. */
  private Long collectorId;

  /** Application ID. */
  private Long applicationId;

  /** Preapproval status. */
  private String status;

  /** Preapproval title. */
  private String reason;

  /** Preapproval reference value. */
  private String externalReference;

  /** Creation date. */
  private OffsetDateTime dateCreated;

  /** Last modified date. */
  private OffsetDateTime lastModified;

  /** Preapproval checkout link. */
  private String initPoint;

  /** Preapproval sandbox checkout link. */
  private String sandboxInitPoint;

  /** Payment method ID. */
  private String paymentMethodId;

  /** Recurring data. */
  private PreapprovalAutoRecurring autoRecurring;

  /** Version. */
  private Long version;
}
