package com.soco.shipping;

import static com.soco.shipping.ConfigConstants.SHIPPO_API_KEY;

import java.util.List;
import java.util.Properties;
import com.google.common.base.Strings;
import com.soco.shipping.clients.ShippingClient;
import com.soco.shipping.clients.ShippoClient;
import com.soco.shipping.comm.HttpClientManager;
import com.soco.shipping.domains.ShipmentRequest;
import com.soco.shipping.domains.ShippingRate;
import com.soco.shipping.exceptions.ValidationException;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import spark.Spark;

public class ShippingController {
  
  private final ResponseTransformer transformer;
  ShippingClient shippingClient;

  public ShippingController(final Properties properties,
      final ResponseTransformer transformer,
      final HttpClientManager httpClientManager) {
    this.transformer = transformer;
    this.shippingClient = new ShippoClient(httpClientManager, 
        properties.getProperty(SHIPPO_API_KEY));
  }
  
  public void initializeRoutes() {
    Spark.post("/rates", (req, res) -> post(req, res), transformer);
  }
  
  protected List<ShippingRate> post(Request request, Response response) throws Exception {
    String payload = request.body();
    if (Strings.isNullOrEmpty(payload)) {
      throw new ValidationException("shipment request");
    }
    
    ShipmentRequest shipmentRequest = JsonUtil.deserialize(payload, ShipmentRequest.class);
    validate(shipmentRequest);
    
    return shippingClient.retrieveRates(shipmentRequest);
  }
  
  protected void validate(final ShipmentRequest shipmentRequest) {
    if (shipmentRequest.getDestination() == null) {
      throw new ValidationException("destination info missing");
    }
    
    if (shipmentRequest.getOrigin() == null) {
      throw new ValidationException("origin info missing");
    }
    
    if (shipmentRequest.getParcel() == null) {
      throw new ValidationException("parcel info missing");
    }
  }
  
}