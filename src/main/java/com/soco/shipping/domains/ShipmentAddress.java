package com.soco.shipping.domains;

import com.google.common.base.MoreObjects;

public class ShipmentAddress {

  private String phone;
  private String name;
  private String company;
  private String street1;
  private String street2;
  private String city;
  private String country;
  private String postalCode;
  private String region;
  private boolean residential;

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getStreet1() {
    return street1;
  }

  public void setStreet1(String street1) {
    this.street1 = street1;
  }

  public String getStreet2() {
    return street2;
  }

  public void setStreet2(String street2) {
    this.street2 = street2;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public boolean isResidential() {
    return residential;
  }

  public void setResidential(boolean residential) {
    this.residential = residential;
  }
  
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
          .add("name", name)
          .add("company", company)
          .add("street1", street1)
          .add("street2", street2)
          .add("city", city)
          .add("region", region)
          .add("postalCode", postalCode)
          .add("country", country)
          .add("phone", phone)
          .add("residential", residential)
            .toString();
  }

}