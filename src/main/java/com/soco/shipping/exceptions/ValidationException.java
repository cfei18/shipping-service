package com.soco.shipping.exceptions;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import com.google.common.base.Joiner;

public class ValidationException extends ShippingException {

  private static final long serialVersionUID = 1L;
  
  private String fieldsInError;
  private final Joiner joiner = Joiner.on(", ");
  
  public ValidationException(final String fieldsInError) {
    this.fieldsInError = fieldsInError;
  }
  
  public ValidationException(final List<String> fieldsInError) {
    this.fieldsInError = joiner.join(fieldsInError);
  }
  
  @Override
  public String getMessage() {
    return displayMessage();
  }

  @Override
  public String displayMessage() {
    return "Invalid field(s) " + fieldsInError;
  }

  @Override
  public String errorCode() {
    return "Validation Error";
  }

  @Override
  public int httpStatus() {
    return HttpStatus.BAD_REQUEST_400;
  }

}