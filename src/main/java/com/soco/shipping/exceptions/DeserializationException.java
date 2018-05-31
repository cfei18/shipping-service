package com.soco.shipping.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class DeserializationException extends ShippingException {

  private static final long serialVersionUID = 1L;

  private String payload;
  private String classToDeserialize;
  
  public DeserializationException(final String payload,
      final Class<?> classToDeserialize, final Throwable t) {
    super(t);
    this.classToDeserialize = classToDeserialize.getSimpleName();
    this.payload = payload;
  }
  
  public DeserializationException(
      final Class<?> classToDeserialize, final Throwable t) {
    super(t);
    this.classToDeserialize = classToDeserialize.getSimpleName();
    this.payload = "";
  }
  
  @Override
  public String displayMessage() {
    return "Unable to deserialize " + payload  + " to " + classToDeserialize;
  }

  @Override
  public String errorCode() {
    return "Deserialization";
  }

  @Override
  public int httpStatus() {
    return HttpStatus.BAD_REQUEST_400;
  }

}