package com.soco.shipping.comm;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class PooledHttpClientManager implements HttpClientManager {

  private final String url;
  private final RequestConfig requestConfig;
  private final PoolingHttpClientConnectionManager poolingClientManager;
  
  public PooledHttpClientManager(final String url, 
      final int connectionRequestTimeout, final int connectTimeout,
      final int socketTimeoutInMillis) {
    this.url = url;
    
    this.requestConfig = RequestConfig.custom()
        //Time (in millis) to wait for a connection from the manager
        .setConnectionRequestTimeout(connectionRequestTimeout)
        //Time (in millis) to establish the connection with the remote host
        .setConnectTimeout(connectTimeout)
        //Time (in millis) to wait for the data from the remote host
        .setSocketTimeout(socketTimeoutInMillis)
        .build();
   
    this.poolingClientManager = new PoolingHttpClientConnectionManager();
  }
  
  @Override
  public String getUrl() {
    return url;
  }

  @Override
  public CloseableHttpClient buildHttpClient() {
    return HttpClientBuilder.create()
        .setConnectionManagerShared(true)
        .setConnectionManager(poolingClientManager)
        .setDefaultRequestConfig(requestConfig)
        .build();
  }

}