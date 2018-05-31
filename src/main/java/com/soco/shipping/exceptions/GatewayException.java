package com.soco.shipping.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class GatewayException extends ShippingException {

  private static final long serialVersionUID = 1L;

  public GatewayException(final String displayMessage) {
    super(displayMessage);
  }
  
  public GatewayException(final String displayMessage, final Throwable t) {
    super(displayMessage, t);
  }

  @Override
  public String displayMessage() {
    return getMessage();
  }

  @Override
  public String errorCode() {
    return "Gateway error";
  }

  @Override
  public int httpStatus() {
    return HttpStatus.BAD_GATEWAY_502;
  }

}