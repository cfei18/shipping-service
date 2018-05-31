package com.soco.shipping.clients;

import java.util.List;
import com.soco.shipping.domains.ShipmentRequest;
import com.soco.shipping.domains.ShippingRate;

public interface ShippingClient {

  /**
   * Retrieves the shipping rates for a shipment request
   * @param request
   * @return A list of shipping rates, may also be an empty list
   */
  public List<ShippingRate> retrieveRates(ShipmentRequest request) throws Exception;
  
}