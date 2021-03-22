package com.paypal.bfs.test.employeeserv.api;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.api.model.EmployeeResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Interface for employee resource operations.
 */
public interface EmployeeResource {

  /**
   * Retrieves the {@link Employee} resource by id.
   *
   * @param id employee id.
   * @return {@link Employee} resource.
   */
  @GetMapping(value = "/v1/bfs/employees/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Employee> employeeGetById(@NotNull @Pattern(regexp = "^\\d{1,9}$") @PathVariable("id") String id);

  @PostMapping(value = "/v1/bfs/employee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<EmployeeResponse> employeeCreate(@Valid @RequestBody Employee employee);
}
