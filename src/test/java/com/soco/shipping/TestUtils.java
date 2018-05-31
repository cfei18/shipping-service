package com.soco.shipping;

import com.soco.shipping.domains.ShipmentAddress;
import com.soco.shipping.domains.ShipmentParcel;
import com.soco.shipping.domains.ShipmentRequest;

public class TestUtils {

  public static ShipmentRequest fullShipmentRequest() {
    ShipmentRequest request = new ShipmentRequest();
    
    ShipmentAddress origin = new ShipmentAddress();
    origin.setStreet1("1 Main St");
    origin.setCity("San Francisco");
    origin.setCountry("US");
    origin.setName("Jane Doe");
    origin.setPostalCode("94103");
    origin.setRegion("CA");
    origin.setResidential(false);
    origin.setPhone("555-555-5555");

    ShipmentAddress destination = new ShipmentAddress();
    destination.setCompany("Does Co.");
    destination.setStreet1("5 Oak Ln");
    destination.setCity("New York City");
    destination.setCountry("US");
    destination.setName("John Doe");
    destination.setPostalCode("10001");
    destination.setRegion("NY");
    destination.setStreet2("Apt 1");
    destination.setResidential(false);
    
    ShipmentParcel parcel = new ShipmentParcel();
    parcel.setWeight(15);
    
    request.setOrigin(origin);
    request.setDestination(destination);
    request.setParcel(parcel);
    request.setReference("reference");
    return request;
  }
  
}