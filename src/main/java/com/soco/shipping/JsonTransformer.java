package com.soco.shipping;

import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

  @Override
  public String render(Object model) throws Exception {
    return JsonUtil.serialize(model);
  }

}