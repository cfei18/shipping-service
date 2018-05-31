package com.soco.shipping.exceptions;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.MoreObjects;
import com.soco.shipping.JsonUtil;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

public class ShippingExceptionHandler implements ExceptionHandler<ShippingException> {
  
  private static final Logger logger = LoggerFactory.getLogger(ShippingExceptionHandler.class);
  
  @Override
  public void handle(ShippingException exception, Request request, Response response) {
    try {
      response.status(exception.httpStatus());
      ErrorMessage message = new ErrorMessage(exception.errorCode(), exception.displayMessage());
      
      response.body(JsonUtil.serialize(message));
    } catch (Exception e) {
      logger.error(String.format(
          "Error in trying to handle exception that had error code %s and message %s", 
          exception.errorCode(), exception.displayMessage()), exception);
      logger.error("error processing exception " + 
          exception.getClass(), e);
      response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
      response.body(null);
    }
  }
  
  public static class ErrorMessage {

    private String errorCode;
    private String errorMessage;
    
    public ErrorMessage(String errorCode, String errorMessage) {
      this.errorCode = errorCode;
      this.errorMessage = errorMessage;
    }
    
    public String getErrorCode() {
      return errorCode;
    }

    public String getErrorMessage() {
      return errorMessage;
    }
    
    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
          .add("errorCode", errorCode)
          .add("errorMessage", errorMessage)
            .toString();
    }
    
  }

}