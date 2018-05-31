package com.soco.shipping.domains;

import com.google.common.base.MoreObjects;

public class ShipmentRequest {

  private ShipmentAddress origin;
  private ShipmentAddress destination;
  private ShipmentParcel parcel;
  private String reference;

  public ShipmentAddress getOrigin() {
    return origin;
  }

  public void setOrigin(ShipmentAddress origin) {
    this.origin = origin;
  }

  public ShipmentAddress getDestination() {
    return destination;
  }

  public void setDestination(ShipmentAddress destination) {
    this.destination = destination;
  }

  public ShipmentParcel getParcel() {
    return parcel;
  }

  public void setParcel(ShipmentParcel parcel) {
    this.parcel = parcel;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }
  
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("reference", reference)
        .add("origin", origin)
        .add("destination", destination)
        .add("parcel", parcel)
          .toString();
  }

}