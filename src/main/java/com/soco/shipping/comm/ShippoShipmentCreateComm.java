package com.soco.shipping.comm;

import java.io.InputStream;
import java.util.Map;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import com.soco.shipping.JsonUtil;
import com.soco.shipping.domains.ShippoShipment;

public class ShippoShipmentCreateComm extends AbstractShippoCommunication<ShippoShipment> {
  
  private static final String RESOURCE = "/shipments";
  
  private final Map<String, Object> paramMap;

  public ShippoShipmentCreateComm(final CloseableHttpClient httpClient, 
      final String baseUrl, final String apiKey, 
      final Map<String, Object> paramMap) {
    super(httpClient, baseUrl, apiKey);
    this.paramMap = paramMap;
  }

  @Override
  protected HttpRequestBase buildHttpRequestBase() throws Exception {
    HttpPost post = new HttpPost(baseUrl + RESOURCE);
    post.setEntity(new StringEntity(JsonUtil.serialize(paramMap)));
    return post;
  }

  @Override
  protected ShippoShipment deserialize(InputStream is) throws Exception {
    return JsonUtil.deserialize(is, ShippoShipment.class);
  }

}