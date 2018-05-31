package com.soco.shipping.domains;

import java.util.List;

public class ShippoShipment {

  private List<ShippoRate> rates;

  public List<ShippoRate> getRates() {
    return rates;
  }

  public void setRates(List<ShippoRate> rates) {
    this.rates = rates;
  }
  
}