package com.soco.shipping.clients;

import java.util.List;
import java.util.Map;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.soco.shipping.comm.HttpClientManager;
import com.soco.shipping.comm.ShippoShipmentCreateComm;
import com.soco.shipping.domains.ShipmentAddress;
import com.soco.shipping.domains.ShipmentParcel;
import com.soco.shipping.domains.ShipmentRequest;
import com.soco.shipping.domains.ShippingRate;
import com.soco.shipping.domains.ShippoRate;
import com.soco.shipping.domains.ShippoShipment;

public class ShippoClient implements ShippingClient {
  
  private static final Logger logger = LoggerFactory.getLogger(ShippoClient.class);
  
  private final HttpClientManager httpClientManager;
  private final String apiKey;
  
  protected int weightInOunces;
  
  public ShippoClient(final HttpClientManager httpClientManager, final String apiKey) {
    this.httpClientManager = httpClientManager;
    this.apiKey = apiKey;
  }

  @Override
  public List<ShippingRate> retrieveRates(final ShipmentRequest request) throws Exception {
    Map<String, Object> shipmentMap = buildShipmentMap(request);
    
    try (CloseableHttpClient httpClient = httpClientManager.buildHttpClient()) {
      ShippoShipmentCreateComm comm = new ShippoShipmentCreateComm(
          httpClient, httpClientManager.getUrl(), apiKey, shipmentMap);
      ShippoShipment shippoShipment = comm.execute();
      return transform(shippoShipment);
    }
  }
  
  protected Map<String, Object> buildShipmentMap(final ShipmentRequest shipmentRequest) {
    logger.info(
        "Starting EasyPost shipment creation for ShipmentRequest: {}",
        shipmentRequest);

    Map<String, Object> shipment = Maps.newHashMap();
    shipment.put("address_from", buildAddress(shipmentRequest.getOrigin()));
    shipment.put("address_to", buildAddress(shipmentRequest.getDestination()));
    shipment.put("parcels", getParcel(shipmentRequest.getParcel()));
    shipment.put("async", false);
    shipment.put("validate", false);
    
    logger.info("Created EasyPost shipment map {}", shipment);
    return shipment;
  }
  
  private Map<String, Object> buildAddress(final ShipmentAddress address) {
    Map<String, Object> fromAddressMap = Maps.newHashMap();
    if (address.getName() != null) {
      fromAddressMap.put("name", address.getName());
    }
    if (address.getCompany() != null) {
      fromAddressMap.put("company", address.getCompany());
    }
    fromAddressMap.put("street1", address.getStreet1());
    if (address.getStreet2() != null) {
      fromAddressMap.put("street2", address.getStreet2());
    }
    fromAddressMap.put("city", address.getCity());
    fromAddressMap.put("state", address.getRegion());
    fromAddressMap.put("country", address.getCountry());
    fromAddressMap.put("zip", address.getPostalCode());
    fromAddressMap.put("phone", address.getPhone());
    return fromAddressMap;
  }
  
  private Map<String, Object> getParcel(final ShipmentParcel parcel) {
    this.weightInOunces = parcel.getWeight();
    Map<String, Object> parcelMap = Maps.newHashMap();
    parcelMap.put("length", 6);
    parcelMap.put("height", 6);
    parcelMap.put("width", 6);
    parcelMap.put("distance_unit", "in");
    parcelMap.put("weight", weightInOunces);
    parcelMap.put("mass_unit", "oz");
    return parcelMap;
  }
  
  protected List<ShippingRate> transform(final ShippoShipment shippoShipment) {
    List<ShippoRate> shippoRates = shippoShipment.getRates();
    List<ShippingRate> rates = Lists.newArrayList();
    
    if (shippoRates != null) {
      for (ShippoRate shippoRate : shippoRates) {
        ShippingRate rate = new ShippingRate();
        rate.setCarrier(shippoRate.getProvider());
        rate.setCurrency(shippoRate.getCurrency());
        rate.setRate(shippoRate.getAmount());
        rate.setServiceLevel(shippoRate.getServiceLevel().getName());
        rate.setShippingZone(shippoRate.getZone());
        rate.setEstimatedDays(shippoRate.getEstimatedDays());
        rate.setWeightInOunces(weightInOunces);
        rates.add(rate);
      }
    }
    return rates;
  }

}