package com.soco.shipping.comm;

import org.apache.http.impl.client.CloseableHttpClient;

public interface HttpClientManager {

  public String getUrl();
  public CloseableHttpClient buildHttpClient();
  
}