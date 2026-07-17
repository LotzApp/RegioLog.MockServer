package org.lotzapp.backend.service.mock;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MockService {
  private final Map<String, HttpStatus> mockResponses = new HashMap<>();

  public void setResponse(String endpoint, int status) {
    this.mockResponses.put(endpoint, HttpStatus.valueOf(status));
  }

  public HttpStatus getResponse(String endpoint) {
    return this.mockResponses.get(endpoint);
  }

  public void reset(String endpoint) {
    this.mockResponses.remove(endpoint);
  }
}
