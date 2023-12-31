package com.innovatechamps.sync.resources.paymentmethod;

import java.math.BigDecimal;
import java.util.List;

import com.innovatechamps.sync.network.DCResource;

import lombok.Getter;

/** Payment Method resource. */
@Getter
public class PaymentMethod extends DCResource {
  /** Payment method ID. */
  private String id;

  /** Payment method name. */
  private String name;

  /** Types of payment method. */
  private String paymentTypeId;

  /** Payment method status. */
  private String status;

  /** Logo to display in secure sites. */
  private String secureThumbnail;

  /** Logo to show. */
  private String thumbnail;

  /** Whether the capture can be delayed or not. */
  private String deferredCapture;

  /** Payment method settings. */
  private List<PaymentMethodSettings> settings;

  /** List of additional information that must be supplied by the payer. */
  private List<String> additionalInfoNeeded;

  /** Minimum amount that can be processed with this payment method. */
  private BigDecimal minAllowedAmount;

  /** Maximum amount that can be processed with this payment method. */
  private BigDecimal maxAllowedAmount;

  /** How many time in minutes could happen until processing of the payment. */
  private Long accreditationTime;

  /** Financial institution processing this payment method. */
  private List<PaymentMethodFinancialInstitutions> financialInstitutions;

  /** Processing modes. */
  private List<String> processingModes;
}
