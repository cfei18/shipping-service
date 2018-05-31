package com.soco.shipping;

import static com.soco.shipping.ConfigConstants.*;

import java.io.FileInputStream;
import java.util.Properties;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.soco.shipping.comm.HttpClientManager;
import com.soco.shipping.comm.PooledHttpClientManager;
import com.soco.shipping.exceptions.ShippingException;
import com.soco.shipping.exceptions.ShippingExceptionHandler;
import spark.ResponseTransformer;
import spark.Spark;

public class ShippingDaemon {

  private static final Logger logger = LoggerFactory.getLogger(ShippingDaemon.class);
  
  public static void main(String[] args) {
    try {
      Properties props = initProperties("./config/default.properties");
      logger.debug(props.getProperty(PORT));
      Spark.port(Integer.valueOf(props.getProperty(PORT)));
      Spark.staticFileLocation("/public");
      Spark.exception(ShippingException.class, new ShippingExceptionHandler());
      
      ResponseTransformer jsonTransformer = new JsonTransformer();
      HttpClientManager httpClientManager = new PooledHttpClientManager(
          props.getProperty(SHIPPO_BASE_URL), 
          Integer.valueOf(props.getProperty(SHIPPO_CONNECTION_REQUEST_TIMEOUT)), 
          Integer.valueOf(props.getProperty(SHIPPO_CONNECT_TIMEOUT)), 
          Integer.valueOf(props.getProperty(SHIPPO_SOCKET_TIMEOUT)));
      ShippingController controller = new ShippingController(props,
          jsonTransformer, httpClientManager);
      controller.initializeRoutes();
    } catch (Exception e) {
      logger.error("error starting application", e);
    }
  }
  
  public static Properties initProperties(final String configFilePath) throws Exception {
    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    encryptor.setPassword("dogsAreSuperCool.WouldRate20outOf10"); // could be got from web, env variable...
    Properties props = new EncryptableProperties(encryptor);
    FileInputStream fis = new FileInputStream(configFilePath);
    props.load(fis);

    return props;
  }
  
}