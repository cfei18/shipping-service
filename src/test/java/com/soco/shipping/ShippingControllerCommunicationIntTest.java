package com.soco.shipping;

import static com.soco.shipping.TestUtils.fullShipmentRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import spark.Spark;

@Category(CommunicationTest.class)
public class ShippingControllerCommunicationIntTest {
  
  private HttpClientManager httpClientManager;
  
  @BeforeClass
  public static void init() throws Exception {
    ShippingDaemon.main(null);
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
  public void testBadShippoRequest_expectErrorResponse() throws Exception {
    HttpPost post = new HttpPost("http://localhost:8888/rates");
    HttpEntity entity = new StringEntity("{\"origin\":{},\"destination\":{},\"parcel\":{}}") ;
    post.setEntity(entity);
    
    try (CloseableHttpClient httpClient = httpClientManager.buildHttpClient()) {
      CloseableHttpResponse response = httpClient.execute(post);
      StatusLine statusLine = response.getStatusLine();
      
      assertNotNull(statusLine);
      assertEquals(HttpStatus.BAD_GATEWAY_502, statusLine.getStatusCode());
    }
  }
  
  @Test
  public void testValidRequest_expectSuccessfulResponse() throws Exception {
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