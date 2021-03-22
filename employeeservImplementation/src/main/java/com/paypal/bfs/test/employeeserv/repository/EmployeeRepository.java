package com.paypal.bfs.test.employeeserv.repository;

import com.paypal.bfs.test.employeeserv.domain.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

}
