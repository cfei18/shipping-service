package com.soco.shipping;

import static com.soco.shipping.ConfigConstants.PORT;
import static com.soco.shipping.TestUtils.fullShipmentRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import com.soco.shipping.comm.HttpClientManager;
import com.soco.shipping.comm.PooledHttpClientManager;
import com.soco.shipping.exceptions.ShippingException;
import com.soco.shipping.exceptions.ShippingExceptionHandler;
import spark.ResponseTransformer;
import spark.Spark;

@Category(CommunicationTest.class)
public class ShippingControllerIntTest {
  
  private static Properties properties;
  private static HttpClientManager shippoConnectionManager;
  private static CloseableHttpClient httpClient;
  private static CloseableHttpResponse httpResponse;
  private static StatusLine statusLine;
  private static HttpEntity entity;
  
  private HttpClientManager httpClientManager;
  
  @BeforeClass
  public static void init() throws Exception {
    properties = ShippingDaemon.initProperties("./config/default.properties");
    shippoConnectionManager = mock(HttpClientManager.class);
    httpClient = mock(CloseableHttpClient.class);
    httpResponse = mock(CloseableHttpResponse.class);
    statusLine = mock(StatusLine.class);
    entity = mock(HttpEntity.class);
    
    when(shippoConnectionManager.buildHttpClient()).thenReturn(httpClient);
    when(httpResponse.getStatusLine()).thenReturn(statusLine);
    when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);
    
    Spark.port(Integer.valueOf(properties.getProperty(PORT)));
    Spark.exception(ShippingException.class, new ShippingExceptionHandler());
    ResponseTransformer jsonTransformer = new JsonTransformer();
    ShippingController controller = new ShippingController(properties,
        jsonTransformer, shippoConnectionManager);
    controller.initializeRoutes();
  }
  
  @AfterClass
  public static void shutdown() throws Exception {
    Spark.stop();
  }
  
  @Before
  public void setup() {
    httpClientManager = new PooledHttpClientManager(
        "http://localhost:8888", 10000, 10000, 10000);
  }

  @Test
  public void testBadRequest_expectValidationErrorResponse() throws Exception {
    HttpPost post = new HttpPost("http://localhost:8888/rates");
    try (CloseableHttpClient httpClient = httpClientManager.buildHttpClient()) {
      CloseableHttpResponse response = httpClient.execute(post);
      StatusLine statusLine = response.getStatusLine();
      
      assertNotNull(statusLine);
      assertEquals(HttpStatus.BAD_REQUEST_400, statusLine.getStatusCode());
      
      InputStream is = response.getEntity().getContent();
      String payload = new BufferedReader(new InputStreamReader(is)).readLine();
      assertTrue(payload.contains("Validation Error"));
    }
  }
  
  @Test
  public void testBadShippoApiKeyt_expectErrorResponse() throws Exception {
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.UNAUTHORIZED_401);
    
    HttpPost post = new HttpPost("http://localhost:8888/rates");
    HttpEntity entity = new StringEntity("{\"origin\":{},\"destination\":{},\"parcel\":{}}") ;
    post.setEntity(entity);
    
    try (CloseableHttpClient httpClient = httpClientManager.buildHttpClient()) {
      CloseableHttpResponse response = httpClient.execute(post);
      StatusLine statusLine = response.getStatusLine();
      
      assertNotNull(statusLine);
      assertEquals(HttpStatus.FORBIDDEN_403, statusLine.getStatusCode());
    }
  }
  
  @Test
  public void testValidRequest_expectSuccessfulResponse() throws Exception {
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.OK_200);
    when(httpResponse.getEntity()).thenReturn(entity);
    when(entity.getContent()).thenReturn(httpClientManager
        .getClass().getClassLoader().getResourceAsStream("shippoShipmentResponse.txt"));
    
    
    HttpPost post = new HttpPost("http://localhost:8888/rates");
    HttpEntity entity = new StringEntity(JsonUtil.serialize(fullShipmentRequest())) ;
    post.setEntity(entity);
    
    try (CloseableHttpClient httpClient = httpClientManager.buildHttpClient()) {
      CloseableHttpResponse response = httpClient.execute(post);
      StatusLine statusLine = response.getStatusLine();
      
      assertNotNull(statusLine);
      assertEquals(HttpStatus.OK_200, statusLine.getStatusCode());
      
      InputStream is = response.getEntity().getContent();
      String payload = new BufferedReader(new InputStreamReader(is)).readLine();
      assertNotNull(payload);
    }
  }
  
}