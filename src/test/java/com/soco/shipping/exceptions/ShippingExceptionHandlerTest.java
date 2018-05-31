package com.soco.shipping.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import com.soco.shipping.JsonUtil;
import spark.Request;
import spark.Response;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(JsonUtil.class)
public class ShippingExceptionHandlerTest {
  
  private static final String DISPLAY_MESSAGE = "displayMessage";
  private static final String ERROR_CODE = "errorCode";
  private static final int STATUS = 501;
  
  private ShippingExceptionHandler handler;
  private Request request;
  private Response response;
  
  @Before
  public void setup() {
    this.handler = new ShippingExceptionHandler();
    this.response = new TestResponse();
  }

 @Test
 public void handle_success_expectExpectedResponse() {
   handler.handle(new TestShippingException(), request, response);
   
   assertEquals(STATUS, response.status());
   assertEquals("{\"errorCode\":\"errorCode\",\"errorMessage\":\"displayMessage\"}", response.body());
 }
 
 @Test
 public void handle_serializationException_expectBadResponse() {
   PowerMockito.mockStatic(JsonUtil.class);
   when(JsonUtil.serialize(any())).thenThrow(new SerializationException("aabb", new RuntimeException()));
   
   handler.handle(new TestShippingException(), request, response);
   assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, response.status());
   assertNull(response.body());
 }
 
 static class TestShippingException extends ShippingException {

  private static final long serialVersionUID = 1L;

  @Override
  public String displayMessage() {
    return DISPLAY_MESSAGE;
  }

  @Override
  public String errorCode() {
    return ERROR_CODE;
  }

  @Override
  public int httpStatus() {
    return STATUS;
  }
   
 }
 
 public static class TestResponse extends Response {
   
   private String body;
   private int status;
   
   public TestResponse() {
     super();
   }
   
   @Override
   public void status(int statusCode) {
     this.status = statusCode;
   }
   
   @Override
   public int status() {
     return this.status;
   }
   
   @Override
   public String body() {
     return this.body;
   }
   
   @Override 
   public void body(String body) {
     this.body = body;
   }
   
 }
  
}