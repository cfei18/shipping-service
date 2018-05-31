package com.soco.shipping.exceptions;

public class UnsuccessfulHttpResponseException extends GatewayException {

  private static final long serialVersionUID = 1L;

  public UnsuccessfulHttpResponseException(int statusCode, String message) {
    super("http error status of " + String.valueOf(statusCode) + "-" + message);
  }
  
}