package com.paypal.bfs.test.employeeserv.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.api.model.EmployeeResponse;
import com.paypal.bfs.test.employeeserv.utils.JsonDateDeserializer;
import com.paypal.bfs.test.employeeserv.utils.JsonDateSerializer;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeResourceImplTest {

  @LocalServerPort
  private int port;

  public static ObjectMapper mapper = new ObjectMapper();
  public static TestRestTemplate restTemplate = new TestRestTemplate();


  @BeforeClass
  public static void init() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleModule module = new SimpleModule();
    module.addDeserializer(LocalDate.class, new JsonDateDeserializer());
    module.addSerializer(LocalDate.class, new JsonDateSerializer());
    mapper.registerModule(module);
  }

  @Test
  public void testCreateEmployeePositive() throws IOException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("content-type", MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(genEmployee(false)), headers);

    ResponseEntity<String> response = restTemplate.exchange(
        genUrl("/v1/bfs/employee"), HttpMethod.POST, entity, String.class);
    EmployeeResponse empResp = mapper.readValue(response.getBody(), EmployeeResponse.class);

    assertEquals(201, response.getStatusCodeValue());
    assertNotNull(empResp);
    assertNotNull(empResp.getEmployeeId());

  }

  @Test
  public void testCreateEmployeeBadRequest() throws IOException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("content-type", MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(genEmployee(true)), headers);

    ResponseEntity<String> response = restTemplate.exchange(
        genUrl("/v1/bfs/employee"), HttpMethod.POST, entity, String.class);

    assertEquals(400, response.getStatusCodeValue());
  }

  @Test
  public void testCreateEmployeeBadUrl() throws IOException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("content-type", MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(genEmployee(false)), headers);

    ResponseEntity<String> response = restTemplate.exchange(
        genUrl("/v1/bfs/employeed"), HttpMethod.POST, entity, String.class);

    assertEquals(404, response.getStatusCodeValue());
  }


  @Test
  public void testGetEmployeePositive() throws IOException {
    //create Entity first
    HttpHeaders headers = new HttpHeaders();
    headers.add("content-type", MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<String> entity = new HttpEntity<String>(mapper.writeValueAsString(genEmployee(false)), headers);
    ResponseEntity<String> response = restTemplate.exchange(
        genUrl("/v1/bfs/employee"), HttpMethod.POST, entity, String.class);
    EmployeeResponse empResp = mapper.readValue(response.getBody(), EmployeeResponse.class);

    //Get call
    entity = new HttpEntity<String>(null, null);
    response = restTemplate.exchange(
        genUrl("/v1/bfs/employees/" + empResp.getEmployeeId()), HttpMethod.GET, entity, String.class);

    Employee actualResp = mapper.readValue(response.getBody(), Employee.class);
    assertEquals(200, response.getStatusCodeValue());
    assertNotNull(actualResp);
    assertNotNull(actualResp.getId());
    assertNotNull(actualResp.getFirstName());
  }


  @Test
  public void testGetEmployeeResourceNotFount() throws IOException {
    //Get call
    HttpEntity entity = new HttpEntity<String>(null, null);
    ResponseEntity<String> response = restTemplate.exchange(
        genUrl("/v1/bfs/employees/" + 500), HttpMethod.GET, entity, String.class);
    assertEquals(404, response.getStatusCodeValue());

  }

  public Employee genEmployee(boolean isError) {
    Employee employee = new Employee();
    if (isError) {
      return employee;
    }
    LocalDate.parse("1977-03-01");
    employee.setFirstName("brue");
    employee.setLastName("wayne");
    employee.setDateOfBirth(LocalDate.parse("1977-03-01"));
    Address address = new Address();
    address.setCity("Gotham");
    address.setLine1("1007 Mountain Drive");
    address.setLine2("Wayne Manor");
    address.setState("Gotham State");
    address.setCountry("USA");
    address.setZipCode("111034");
    employee.setAddress(address);

    return employee;

  }

  public String genUrl(String url) {
    return "http://localhost:" + port + url;
  }

}
