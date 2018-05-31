package com.soco.shipping.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class SerializationException extends ShippingException {

  private static final long serialVersionUID = 1L;

  private Object obj;
  
  public SerializationException(final Object obj, final Throwable t) {
    super(t);
    this.obj = obj;
  }
  
  @Override
  public String displayMessage() {
    return "Unable to serialize object " + obj.toString();
  }

  @Override
  public String errorCode() {
    return "Serialization";
  }

  @Override
  public int httpStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR_500;
  }

}