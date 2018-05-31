package com.soco.shipping;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import com.soco.shipping.clients.ShippoClientTest;
import com.soco.shipping.comm.ShippoShipmentCreateCommTest;
import com.soco.shipping.exceptions.ShippingExceptionHandlerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  JsonTransformerTest.class,
  ShippingControllerIntTest.class,
  ShippingControllerTest.class,
  ShippoClientTest.class,
  ShippoShipmentCreateCommTest.class,
  ShippingExceptionHandlerTest.class
})
public class AllTests {

}