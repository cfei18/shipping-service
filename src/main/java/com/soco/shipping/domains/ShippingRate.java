package com.soco.shipping.domains;

import com.google.common.base.MoreObjects;

public class ShippingRate {

  private String carrier;
  private String serviceLevel;
  private int weightInOunces;
  private String currency;
  private String rate;
  private String shippingZone;
  private Integer estimatedDays;

  public String getCarrier() {
    return carrier;
  }

  public void setCarrier(String carrier) {
    this.carrier = carrier;
  }

  public String getServiceLevel() {
    return serviceLevel;
  }

  public void setServiceLevel(String serviceLevel) {
    this.serviceLevel = serviceLevel;
  }

  public int getWeightInOunces() {
    return weightInOunces;
  }

  public void setWeightInOunces(int weightInOunces) {
    this.weightInOunces = weightInOunces;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getRate() {
    return rate;
  }

  public void setRate(String rate) {
    this.rate = rate;
  }

  public String getShippingZone() {
    return shippingZone;
  }

  public void setShippingZone(String shippingZone) {
    this.shippingZone = shippingZone;
  }
  
  public Integer getEstimatedDays() {
    return estimatedDays;
  }

  public void setEstimatedDays(Integer estimatedDays) {
    this.estimatedDays = estimatedDays;
  }
  
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
          .add("carrier", carrier)
          .add("serviceLevel", serviceLevel)
          .add("weightInOunces", weightInOunces)
          .add("currency", currency)
          .add("rate", rate)
          .add("estimatedDays", estimatedDays)
          .add("shippingZone", shippingZone)
            .toString();
  }

}