package com.innovatechamps.sync.execptions;

import lombok.Getter;

/** DCException Class. */
@SuppressWarnings("serial")
@Getter
public class DCException extends Exception {

  /**
   * DCException constructor.
   *
   * @param message message
   */
  public DCException(String message) {
    super(message);
  }

  /**
   * DCException constructor.
   *
   * @param message message
   * @param cause cause
   */
  public DCException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * DCException class.
   *
   * @param cause cause
   */
  public DCException(Throwable cause) {
    super(cause);
  }
}
