package com.springrest.restApp.repository;

import com.springrest.restApp.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository  extends JpaRepository<Company,Long>, JpaSpecificationExecutor<Company> {
}
