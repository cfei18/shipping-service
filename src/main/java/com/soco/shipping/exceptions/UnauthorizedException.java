package com.soco.shipping.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class UnauthorizedException extends ShippingException {

  private static final long serialVersionUID = 1L;

  @Override
  public String displayMessage() {
    return null;
  }

  @Override
  public String errorCode() {
    return "Unauthorized";
  }

  @Override
  public int httpStatus() {
    return HttpStatus.FORBIDDEN_403;
  }
  
}