package com.innovatechamps.sync.resources;


import java.time.OffsetDateTime;

import com.innovatechamps.sync.network.DCResource;
import com.innovatechamps.sync.resources.customer.CustomerCardCardholder;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Send customer card data to Mercado Pago server and receive a token to complete the payments
 * transactions. For testing only. .
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class CardToken extends DCResource {

  /** Card token. */
  private String id;

  /** Card id. */
  private String cardId;

  /** First six digits of card number. */
  private String firstSixDigits;

  /** Expiration month of the card. */
  private Integer expirationMonth;

  /** Expiration year of the card. */
  private Integer expirationYear;

  /** Last four digits of the card. */
  private String lastFourDigits;

  /** Cardholder information. */
  private CustomerCardCardholder cardholder;

  /** Current status of card. E.g. active. */
  private String status;

  /** Date token was created. */
  private OffsetDateTime dateCreated;

  /** Date token was last updated. */
  private OffsetDateTime dateLastUpdated;

  /** Date token expires. */
  private OffsetDateTime dateDue;

  /** Flag indicating if Luhn validation is used. */
  private Boolean luhnValidation;

  /** Flag indicating if this is a production card token. */
  private Boolean liveMode;

  /** Require esc. */
  private Boolean requireEsc;

  /** Security code of the card. */
  private Integer cardNumberLength;

  /** Security code of the card. */
  private Integer securityCodeLength;
}
