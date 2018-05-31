package com.soco.shipping.exceptions;

public abstract class ShippingException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public abstract String displayMessage();
  public abstract String errorCode();
  public abstract int httpStatus();

  public ShippingException() {}
  
  public ShippingException(final Throwable throwable) {
    super(throwable);
  }
  
  public ShippingException(final String message, final Throwable throwable) {
    super(message, throwable);
  }
  
  public ShippingException(final String message) {
    super(message);
  }
  
}