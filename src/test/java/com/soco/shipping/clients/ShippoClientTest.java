package com.soco.shipping.clients;

import static com.soco.shipping.TestUtils.fullShipmentRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.google.common.collect.Lists;
import com.soco.shipping.comm.HttpClientManager;
import com.soco.shipping.domains.ShipmentRequest;
import com.soco.shipping.domains.ShippingRate;
import com.soco.shipping.domains.ShippoRate;
import com.soco.shipping.domains.ShippoRate.ServiceLevel;
import com.soco.shipping.domains.ShippoShipment;
import com.soco.shipping.exceptions.UnsuccessfulHttpResponseException;

public class ShippoClientTest {
  
  @Rule public ExpectedException expectedException = ExpectedException.none();
  
  private HttpClientManager httpClientManager;
  private CloseableHttpClient httpClient;
  private CloseableHttpResponse httpResponse;
  private StatusLine statusLine;
  private HttpEntity entity;
  
  private ShippoClient shippoClient;

  @Before
  public void setup() throws Exception {
    httpClientManager = mock(HttpClientManager.class);
    httpClient = mock(CloseableHttpClient.class);
    httpResponse = mock(CloseableHttpResponse.class);
    statusLine = mock(StatusLine.class);
    entity = mock(HttpEntity.class);
    
    when(httpClientManager.buildHttpClient()).thenReturn(httpClient);
    when(httpResponse.getStatusLine()).thenReturn(statusLine);
    when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);
    
    this.shippoClient = new ShippoClient(httpClientManager, null);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void buildShipmentMap_validateMap() {
    ShipmentRequest request = fullShipmentRequest();
    
    Map<String, Object> shipmentMap = shippoClient.buildShipmentMap(request);
   
    assertFalse((Boolean) shipmentMap.get("async"));
    assertFalse((Boolean) shipmentMap.get("validate"));

    Map<String, Object> parcel = (Map<String, Object>) shipmentMap.get("parcels");
    assertEquals(6, parcel.get("length"));
    assertEquals(6, parcel.get("height"));
    assertEquals(6, parcel.get("width"));
    assertEquals("in", parcel.get("distance_unit"));
    assertEquals("oz", parcel.get("mass_unit"));
    assertEquals(15, parcel.get("weight"));
    
    Map<String, Object> origin = (Map<String, Object>) shipmentMap.get("address_from");
    assertEquals("Jane Doe", origin.get("name"));
    assertNull(origin.get("company"));
    assertEquals("1 Main St", origin.get("street1"));
    assertNull(origin.get("street2"));
    assertEquals("San Francisco", origin.get("city"));
    assertEquals("CA", origin.get("state"));
    assertEquals("US", origin.get("country"));
    assertEquals("94103", origin.get("zip"));
    assertEquals("555-555-5555", origin.get("phone"));
    
    Map<String, Object> destination = (Map<String, Object>) shipmentMap.get("address_to");
    assertEquals("John Doe", destination.get("name"));
    assertEquals("Does Co.", destination.get("company"));
    assertEquals("5 Oak Ln", destination.get("street1"));
    assertEquals("Apt 1", destination.get("street2"));
    assertEquals("New York City", destination.get("city"));
    assertEquals("NY", destination.get("state"));
    assertEquals("US", destination.get("country"));
    assertEquals("10001", destination.get("zip"));
    assertNull(destination.get("phone"));
  }
  
  @Test
  public void transform_validate() {
    ShippoShipment shippoShipment = new ShippoShipment();
    
    ServiceLevel level = new ServiceLevel();
    level.setName("Priority Mail");
    
    ShippoRate rate1 = new ShippoRate();
    rate1.setAmount("5.50");
    rate1.setCurrency("USD");
    rate1.setEstimatedDays(4);
    rate1.setProvider("USPS");
    rate1.setServiceLevel(level);
    rate1.setZone("Zone1");
    
    shippoShipment.setRates(Lists.newArrayList(rate1));
    shippoClient.weightInOunces = 50;
    List<ShippingRate> rates = shippoClient.transform(shippoShipment);
    
    assertEquals(1, rates.size());
    ShippingRate srate = rates.get(0);
    
    assertEquals("5.50", srate.getRate());
    assertEquals("USD", srate.getCurrency());
    assertEquals(4, (int) srate.getEstimatedDays());
    assertEquals("USPS", srate.getCarrier());
    assertEquals("Priority Mail", srate.getServiceLevel());
    assertEquals(50, srate.getWeightInOunces());
    assertEquals("Zone1", srate.getShippingZone());
  }
  
  @Test
  public void transform_noRatesReturned_expectEmptyList() {
    ShippoShipment shippoShipment = new ShippoShipment();
    
    List<ShippingRate> rates = shippoClient.transform(shippoShipment);
    assertEquals(0, rates.size());
  }
  
  @Test
  public void retrieveRates_exceptionThrown_expectException() throws Exception {
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST_400);
    
    ShipmentRequest request = fullShipmentRequest();
    
    expectedException.expect(UnsuccessfulHttpResponseException.class);
    expectedException.expectMessage("http error status of 400");
    shippoClient.retrieveRates(request);
  }
  
  @Test
  public void retrieveRates_validRequest_successfulResponse() throws Exception {
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.OK_200);
    when(httpResponse.getEntity()).thenReturn(entity);
    when(entity.getContent()).thenReturn(
        new ByteArrayInputStream("{}".getBytes(StandardCharsets.UTF_8)));
    
    ShipmentRequest request = fullShipmentRequest();
    
    List<ShippingRate> rates = shippoClient.retrieveRates(request);
    assertTrue(rates.isEmpty());
  }
  
}