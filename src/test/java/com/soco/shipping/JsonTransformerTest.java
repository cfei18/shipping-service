package com.soco.shipping;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import com.soco.shipping.exceptions.SerializationException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JsonUtil.class)
public class JsonTransformerTest {

  @Rule public ExpectedException expectedException = ExpectedException.none();
  
  private JsonTransformer transformer;
  
  @Before
  public void setup() {
    this.transformer = new JsonTransformer();
  }
  
  @Test
  public void render_success() throws Exception {
    assertEquals("{\"number\":10}", 
        transformer.render(new Holder(10)));
  }
  
  @Test
  public void render_serializationException_expectException() throws Exception {
    PowerMockito.mockStatic(JsonUtil.class);
    when(JsonUtil.serialize(any())).thenThrow(
        new SerializationException("", new RuntimeException()));
    
    expectedException.expect(SerializationException.class);
    transformer.render(new Holder(10));
  }
  
  class Holder {
    
    private int number;
    
    public Holder(int number) {
      this.number = number;
    }

    public int getNumber() {
      return number;
    }
    
  }
  
}