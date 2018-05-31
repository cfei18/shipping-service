package com.soco.shipping.comm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.impl.client.CloseableHttpClient;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.soco.shipping.exceptions.UnauthorizedException;
import com.soco.shipping.exceptions.UnsuccessfulHttpResponseException;

public abstract class AbstractCommunicationManager<T> {

  private static final Logger logger = LoggerFactory.getLogger(AbstractCommunicationManager.class);

  private final CloseableHttpClient httpClient;

  public AbstractCommunicationManager(final CloseableHttpClient httpClient) {
    this.httpClient = httpClient;
  }

  /**
   * @return <T>
   * @throws UnauthorizedException This is thrown if the credentials are incorrect. 
   * @throws Exception These are exceptions that require developer intervention and need additional
   *         research to resolve.
   */
  public T execute() throws Exception {
    HttpResponse response = executeRequest(httpClient);

    StatusLine status = response.getStatusLine();
    logger.info("status: {}, reason: {}", status.getStatusCode(), status.getReasonPhrase());

    if (notSuccessful(status.getStatusCode())) {
      if (status.getStatusCode() == HttpStatus.UNAUTHORIZED_401) {
        throw new UnauthorizedException();
      }

      throw new UnsuccessfulHttpResponseException(status.getStatusCode(), status.getReasonPhrase());
    }

    try (InputStream is = response.getEntity().getContent()) {
      return deserialize(is);
    }
  }
  
  protected boolean notSuccessful(int statusCode) {
    return statusCode < HttpStatus.OK_200
        || statusCode >= HttpStatus.MULTIPLE_CHOICES_300;
  }
  
  protected String convertToString(final InputStream is) {
    try {
      return new BufferedReader(new InputStreamReader(is)).readLine();
    } catch (Exception e) {
      logger.error("", e);
      return null;
    }
  }

  protected abstract HttpResponse executeRequest(CloseableHttpClient httpClient) throws Exception;

  protected abstract T deserialize(InputStream is) throws Exception;
  
}