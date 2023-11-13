package com.innovatechamps.sync.resources.customer;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import com.innovatechamps.sync.network.DCResource;
import com.innovatechamps.sync.resources.common.Identification;
import com.innovatechamps.sync.resources.common.Phone;

import lombok.Getter;

/**
 * This class allows you to store customers data safely to improve the shopping experience. This
 * will allow your customer to complete their purchases much faster and easily when used in
 * conjunction with the Cards class.
 */
@Getter
public class Customer extends DCResource {

  /** Customer ID. */
  private String id;

  /** Customer's email. */
  private String email;

  /** Customer's first name. */
  private String firstName;

  /** Customer's last name. */
  private String lastName;

  /** Customer's phone information. */
  private Phone phone;

  /** Customer's identification information. */
  private Identification identification;

  /** Customer's default address. */
  private String defaultAddress;

  /** Customer's address. */
  private CustomerDefaultAddress address;

  /** Customer's date registered. */
  private OffsetDateTime dateRegistered;

  /** Customer's description. */
  private String description;

  /** Customer's date created. */
  private OffsetDateTime dateCreated;

  /** Customer's date last update. */
  private OffsetDateTime dateLastUpdated;

  /** Metadata. */
  private Map<String, Object> metadata;

  /** Default card. */
  private String defaultCard;

  /** List of cards. */
  private List<CustomerCard> cards;

  /** Addresses. */
  private List<CustomerAddress> addresses;

  /** Live mode. */
  private Boolean liveMode;
}
