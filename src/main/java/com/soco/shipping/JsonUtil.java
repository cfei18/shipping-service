package com.soco.shipping;

import java.io.InputStream;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soco.shipping.exceptions.DeserializationException;
import com.soco.shipping.exceptions.SerializationException;

public class JsonUtil {

  private static final ObjectMapper mapper = new ObjectMapper();
  
  static {
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setSerializationInclusion(Include.NON_NULL);
  }
  
  public static String serialize(final Object obj) throws SerializationException {
    try {
      return mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new SerializationException(obj, e);
    }
  }
  
  public static <T> T deserialize(final String payload, Class<T> classToDeserialize) {
    try {
      return mapper.readValue(payload, classToDeserialize);
    } catch (Exception e) {
      throw new DeserializationException(payload, classToDeserialize, e);
    }
  }
  
  public static <T> T deserialize(final InputStream payload, Class<T> classToDeserialize) {
    try {
      return mapper.readValue(payload, classToDeserialize);
    } catch (Exception e) {
      throw new DeserializationException(classToDeserialize, e);
    }
  }
  
}