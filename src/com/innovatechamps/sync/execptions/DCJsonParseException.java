package com.innovatechamps.sync.execptions;

/** DCJsonParseException class. */
@SuppressWarnings("serial")
public class DCJsonParseException extends DCException {
  /**
   * DCJsonParseException constructor.
   *
   * @param message message
   */
  public DCJsonParseException(String message) {
    super(message);
  }

  /**
   * DCJsonParseException constructor.
   *
   * @param message message
   * @param throwable throwable
   */
  public DCJsonParseException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
