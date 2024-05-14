package logic;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
  private String version;
  private int statusCode;
  private Map<String, String> headers;
  private String body;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getStatusText() {
    Map<Integer, String> statusCodeMap = new HashMap<>();

    // Initialize the map with status codes and their corresponding descriptions
    statusCodeMap.put(200, "OK");
    statusCodeMap.put(201, "Created");
    statusCodeMap.put(202, "Accepted");
    statusCodeMap.put(204, "No Content");
    statusCodeMap.put(400, "Bad Request");
    statusCodeMap.put(401, "Unauthorized");
    statusCodeMap.put(403, "Forbidden");
    statusCodeMap.put(404, "Not Found");
    statusCodeMap.put(500, "Internal Server Error");
    statusCodeMap.put(502, "Bad Gateway");
    statusCodeMap.put(503, "Service Unavailable");

    return statusCodeMap.getOrDefault(this.getStatusCode(), "Unknown Status Code");
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public HttpResponse(String version, int statusCode, Map<String, String> headers, String body) {
    this.version = version;
    this.statusCode = statusCode;
    this.headers = headers;
    this.body = body;
  }

  @Override
  public String toString() {
    String headerText = "";
    if (this.headers != null) {
      for (String key : this.headers.keySet()) {
        headerText += String.format("%s: %s\r\n", key, this.headers.get(key));
      }
    }
    return String.format("HTTP/%s %s %s\r\n%s\r\n%s",
        this.getVersion(),
        this.getStatusCode(),
        this.getStatusText(),
        headerText,
        this.getBody());
  }

  public void SendResponse(Socket clientSocket) {
    String httpResponse = this.toString();
    try {
      OutputStream clientOutputStream = clientSocket.getOutputStream();
      clientOutputStream.write(httpResponse.getBytes(StandardCharsets.UTF_8));
      clientOutputStream.flush();
      clientOutputStream.close();
    } catch (IOException e) {
      System.err.println(e.toString());
    }
  }

}
