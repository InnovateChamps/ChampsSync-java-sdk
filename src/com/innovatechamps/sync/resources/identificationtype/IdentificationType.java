package com.innovatechamps.sync.resources.identificationtype;

import com.innovatechamps.sync.network.DCResource;

import lombok.Getter;

/** Identification Type resource. */
@Getter
public class IdentificationType extends DCResource {
  /** Identification type ID. */
  private String id;

  /** Identification type name. */
  private String name;

  /** Identification number data type. */
  private String type;

  /** Identification type min length. */
  private Integer minLength;

  /** Identification type max length. */
  private Integer maxLength;
}
