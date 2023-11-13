package com.innovatechamps.sync.execptions;

/** DCMalformedRequestException class. */
@SuppressWarnings("serial")
public class DCMalformedRequestException extends DCException {
  /**
   * DCMalformedRequestException constructor.
   *
   * @param message message
   */
  public DCMalformedRequestException(String message) {
    super(message);
  }

  /**
   * DCMalformedRequestException constructor.
   *
   * @param cause cause
   */
  public DCMalformedRequestException(Throwable cause) {
    super(cause);
  }
}
