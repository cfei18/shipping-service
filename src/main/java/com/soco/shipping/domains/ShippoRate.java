package com.soco.shipping.domains;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShippoRate {

  private String amount;
  private String currency;
  private String provider;
  @JsonProperty("estimated_days")
  private Integer estimatedDays;
  @JsonProperty("servicelevel")
  private ServiceLevel serviceLevel;
  private String zone;

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public Integer getEstimatedDays() {
    return estimatedDays;
  }

  public void setEstimatedDays(Integer estimatedDays) {
    this.estimatedDays = estimatedDays;
  }

  public ServiceLevel getServiceLevel() {
    return serviceLevel;
  }

  public void setServiceLevel(ServiceLevel serviceLevel) {
    this.serviceLevel = serviceLevel;
  }

  public String getZone() {
    return zone;
  }

  public void setZone(String zone) {
    this.zone = zone;
  }

  public static class ServiceLevel {

    private String name;
    private String token;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getToken() {
      return token;
    }

    public void setToken(String token) {
      this.token = token;
    }

  }

}