package com.paypal.bfs.test.employeeserv.functest;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.api.model.EmployeeResponse;
import com.paypal.bfs.test.employeeserv.domain.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.repository.EmployeeRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation class for employee resource.
 */
@RestController
@Validated
@Slf4j
public class EmployeeResourceImpl implements EmployeeResource {

  @Autowired
  EmployeeRepository employeeRepository;


  @Override
  public ResponseEntity<Employee> employeeGetById(String id) {

    Optional<EmployeeEntity> optEntity = employeeRepository.findById(Integer.parseInt(id));

    if (optEntity.isPresent()) {
      EmployeeEntity entity = optEntity.get();
      Employee employee = new Employee();
      employee.setId(entity.getEmpId());
      employee.setFirstName(entity.getFirstName());
      employee.setLastName(entity.getLastName());
      employee.setDateOfBirth(entity.getDateOfBirth());

      Address address = new Address();
      address.setCity(entity.getCity());
      address.setCountry(entity.getCountry());
      address.setLine1(entity.getAddressLine1());
      address.setLine2(entity.getAddressLine2());
      address.setState(entity.getState());
      address.setZipCode(entity.getZipCode());
      employee.setAddress(address);

      return new ResponseEntity<>(employee, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseEntity<EmployeeResponse> employeeCreate(Employee employee) {
    EmployeeEntity entity = EmployeeEntity.builder()
        .firstName(employee.getFirstName())
        .lastName(employee.getLastName())
        .dateOfBirth(employee.getDateOfBirth())
        .city(employee.getAddress().getCity())
        .country(employee.getAddress().getCountry())
        .addressLine1(employee.getAddress().getLine1())
        .addressLine2(employee.getAddress().getLine2())
        .zipCode(employee.getAddress().getZipCode())
        .state(employee.getAddress().getZipCode())
        .build();

    EmployeeEntity out = employeeRepository.save(entity);

    EmployeeResponse response = new EmployeeResponse();
    response.setEmployeeId(String.valueOf(out.getEmpId()));
    response.setTimestamp(LocalDateTime.now().toString());
    return new ResponseEntity(response, HttpStatus.CREATED);
  }

}
