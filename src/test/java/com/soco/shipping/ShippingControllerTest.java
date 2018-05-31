package com.soco.shipping;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Properties;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.google.common.collect.Lists;
import com.soco.shipping.clients.ShippingClient;
import com.soco.shipping.comm.HttpClientManager;
import com.soco.shipping.domains.ShipmentAddress;
import com.soco.shipping.domains.ShipmentParcel;
import com.soco.shipping.domains.ShipmentRequest;
import com.soco.shipping.domains.ShippingRate;
import com.soco.shipping.exceptions.DeserializationException;
import com.soco.shipping.exceptions.GatewayException;
import com.soco.shipping.exceptions.ValidationException;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;

public class ShippingControllerTest {
  
  @Rule public ExpectedException expectedException = ExpectedException.none();
  
  private ResponseTransformer transformer = new JsonTransformer();
  private ShippingController shippingController;
  private HttpClientManager httpClientManager;
  private ShippingClient shippingClient;
  private Response response;
  private Request request;
  private Properties properties;
  
  @Before
  public void setup() {
    request = mock(Request.class);
    response = mock(Response.class);
    shippingClient = mock(ShippingClient.class);
    properties = mock(Properties.class);
    
    shippingController = new ShippingController(properties, transformer, httpClientManager);
  }

  @Test
  public void post_noRequest_expectValidationException() throws Exception {
    expectedException.expect(ValidationException.class);
    expectedException.expectMessage("shipment request");
    shippingController.post(request, response);
  }
  
  @Test
  public void post_emptyRequest_expectValidationException() throws Exception {
    when(request.body()).thenReturn("{}");
    
    expectedException.expect(ValidationException.class);
    expectedException.expectMessage("destination info missing");
    shippingController.post(request, response);
  }
  
  @Test
  public void post_badRequest_expectDeserializationException() throws Exception {
    when(request.body()).thenReturn("15");
    
    expectedException.expect(DeserializationException.class);
    shippingController.post(request, response);
  }
  
  @Test
  public void post_shippingClientIssue_expectException() throws Exception {
    when(shippingClient.retrieveRates(any(ShipmentRequest.class))).thenThrow(new GatewayException("gateway exception"));
    shippingController.shippingClient = shippingClient;
    
    when(request.body()).thenReturn(JsonUtil.serialize(buildFullShipmentRequest()));
    
    expectedException.expect(GatewayException.class);
    expectedException.expectMessage("gateway exception");
    shippingController.post(request, response);
  }
  
  @Test
  public void post_validRequest_successfulResponse() throws Exception {
    List<ShippingRate> list = Lists.newArrayList();
    
    when(shippingClient.retrieveRates(any(ShipmentRequest.class)))
      .thenReturn(list);
    shippingController.shippingClient = shippingClient;
    
    when(request.body()).thenReturn(JsonUtil.serialize(buildFullShipmentRequest()));
    
    List<ShippingRate> obj = shippingController.post(request, response);
    assertEquals(list, obj);
  }
  
  @Test
  public void validate_noDestination_expectValidationException() {
    ShipmentRequest request = new ShipmentRequest();
    
    expectedException.expect(ValidationException.class);
    expectedException.expectMessage("destination info missing");
    shippingController.validate(request);
  }
  
  @Test
  public void validate_noOrigin_expectValidationException() {
    ShipmentRequest request = new ShipmentRequest();
    request.setDestination(new ShipmentAddress());
    
    expectedException.expect(ValidationException.class);
    expectedException.expectMessage("origin info missing");
    shippingController.validate(request); 
  }
  
  @Test
  public void validate_noParcel_expectValidationException() {
    ShipmentRequest request = new ShipmentRequest();
    request.setDestination(new ShipmentAddress());
    request.setOrigin(new ShipmentAddress());
    
    expectedException.expect(ValidationException.class);
    expectedException.expectMessage("parcel info missing");
    shippingController.validate(request); 
  }
  
  @Test
  public void validate_validRequest_expectNothing() {
    ShipmentRequest request = new ShipmentRequest();
    request.setDestination(new ShipmentAddress());
    request.setOrigin(new ShipmentAddress());
    request.setParcel(new ShipmentParcel());
    
    shippingController.validate(request);
  }
  
  private ShipmentRequest buildFullShipmentRequest() {
    ShipmentRequest request = new ShipmentRequest();
    request.setDestination(new ShipmentAddress());
    request.setOrigin(new ShipmentAddress());
    request.setParcel(new ShipmentParcel());
    return request;
  }
  
}