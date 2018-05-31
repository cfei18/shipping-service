package com.soco.shipping.clients;

import static com.soco.shipping.ConfigConstants.SHIPPO_API_KEY;
import static com.soco.shipping.ConfigConstants.SHIPPO_BASE_URL;
import static com.soco.shipping.ConfigConstants.SHIPPO_CONNECTION_REQUEST_TIMEOUT;
import static com.soco.shipping.ConfigConstants.SHIPPO_CONNECT_TIMEOUT;
import static com.soco.shipping.ConfigConstants.SHIPPO_SOCKET_TIMEOUT;
import static com.soco.shipping.TestUtils.fullShipmentRequest;
import static org.junit.Assert.assertNotNull;
import java.util.List;
import java.util.Properties;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import com.soco.shipping.CommunicationTest;
import com.soco.shipping.ShippingDaemon;
import com.soco.shipping.comm.HttpClientManager;
import com.soco.shipping.comm.PooledHttpClientManager;
import com.soco.shipping.domains.ShippingRate;
import com.soco.shipping.exceptions.UnauthorizedException;

@Category(CommunicationTest.class)
public class ShippoClientCommunicationIntTest {
  
  private static Properties properties;
  
  @Rule public ExpectedException expectedException = ExpectedException.none();
  
  private static HttpClientManager httpClientManager;
  
  @BeforeClass
  public static void init() throws Exception {
    properties = ShippingDaemon.initProperties("./config/default.properties");
    httpClientManager = new PooledHttpClientManager(
        properties.getProperty(SHIPPO_BASE_URL), 
        Integer.valueOf(properties.getProperty(SHIPPO_CONNECTION_REQUEST_TIMEOUT)), 
        Integer.valueOf(properties.getProperty(SHIPPO_CONNECT_TIMEOUT)), 
        Integer.valueOf(properties.getProperty(SHIPPO_SOCKET_TIMEOUT)));
  }
  
  @Test
  public void execute_badApiKey_expectException() throws Exception {
    ShippoClient client = new ShippoClient(httpClientManager, "badKey");
    
    expectedException.expect(UnauthorizedException.class);
    client.retrieveRates(fullShipmentRequest());
  }
  
  @Test
  public void execute_validRequest_validResponse() throws Exception {
    ShippoClient client = new ShippoClient(httpClientManager, properties.getProperty(SHIPPO_API_KEY));
    List<ShippingRate> rates = client.retrieveRates(fullShipmentRequest());
    //I don't validate the rates themselves since they change
    assertNotNull(rates);
  }
  
}