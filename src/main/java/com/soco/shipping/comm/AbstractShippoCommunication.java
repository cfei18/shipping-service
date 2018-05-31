package com.soco.shipping.comm;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;

public abstract class AbstractShippoCommunication<T> extends AbstractCommunicationManager<T> {

  protected final String baseUrl;
  private final String apiKey;
  
  public AbstractShippoCommunication(final CloseableHttpClient httpClient,
      final String baseUrl, final String apiKey) {
    super(httpClient);
    this.baseUrl = baseUrl;
    this.apiKey = apiKey;
  }
  
  @Override
  protected HttpResponse executeRequest(final CloseableHttpClient httpClient) throws Exception {
    HttpRequestBase request = buildHttpRequestBase();
    request.addHeader(HttpHeaders.AUTHORIZATION, "ShippoToken " + apiKey);
    request.addHeader(HttpHeaders.CONTENT_TYPE, 
        ContentType.APPLICATION_JSON.toString());
    
    return httpClient.execute(request);
  }
  
  protected abstract HttpRequestBase buildHttpRequestBase() throws Exception;

}