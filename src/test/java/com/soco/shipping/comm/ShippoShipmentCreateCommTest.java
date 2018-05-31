package com.soco.shipping.comm;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.io.EmptyInputStream;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.google.common.collect.Maps;
import com.soco.shipping.exceptions.DeserializationException;
import com.soco.shipping.exceptions.UnauthorizedException;
import com.soco.shipping.exceptions.UnsuccessfulHttpResponseException;

public class ShippoShipmentCreateCommTest {
  
  @Rule public ExpectedException expectedException = ExpectedException.none();
  
  private ShippoShipmentCreateComm comm;
  private CloseableHttpClient httpClient;
  private Map<String, Object> paramMap;
  private CloseableHttpResponse httpResponse;
  private StatusLine statusLine;
  private HttpEntity entity;
  
  @Before
  public void setup() throws Exception {
    httpClient = mock(CloseableHttpClient.class);
    httpResponse = mock(CloseableHttpResponse.class);
    statusLine = mock(StatusLine.class);
    entity = mock(HttpEntity.class);
    paramMap = Maps.newHashMap();

    comm = new ShippoShipmentCreateComm(httpClient, null, null, paramMap);
    
    when(httpResponse.getStatusLine()).thenReturn(statusLine);
    when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);
  }

  @Test
  public void execute_badApiKey_expectUnauthorizedException() throws Exception {
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.UNAUTHORIZED_401);
    
    expectedException.expect(UnauthorizedException.class);
    comm.execute();
  }
  
  @Test
  public void execute_badRequest_expectUnsuccessfulHttpResponseException() throws Exception {
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST_400);

    expectedException.expect(UnsuccessfulHttpResponseException.class);
    expectedException.expectMessage("http error status of 400");
    comm.execute();
  }
  
  @Test
  public void execute_badResponse_expectDeserializationException() throws Exception {
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.OK_200);
    when(httpResponse.getEntity()).thenReturn(entity);
    when(entity.getContent()).thenReturn(EmptyInputStream.INSTANCE);
    
    expectedException.expect(DeserializationException.class);
    comm.execute();
  }
  
  @Test
  public void execute_timeoutException_expectGatewayException() throws Exception {
    when(httpClient.execute(any(HttpPost.class))).thenThrow(new ConnectTimeoutException());
    
    expectedException.expect(ConnectTimeoutException.class);
    comm.execute();
  }
  
  @Test
  public void execute_validRequest_expectValidResponse() throws Exception {
    when(statusLine.getStatusCode()).thenReturn(HttpStatus.OK_200);
    when(httpResponse.getEntity()).thenReturn(entity);
    when(entity.getContent()).thenReturn(
        new ByteArrayInputStream("{}".getBytes(StandardCharsets.UTF_8)));
    
    assertNotNull(comm.execute());
  }
  
}