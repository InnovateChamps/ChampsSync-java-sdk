package com.innovatechamps.sync.client.merchantorder;

import lombok.Builder;
import lombok.Getter;

/** Country information. */
@Getter
@Builder
public class MerchantOrderReceiverAddressCountryRequest {
  /** Country ID. */
  private final String id;

  /** Country name. */
  private final String name;
}
