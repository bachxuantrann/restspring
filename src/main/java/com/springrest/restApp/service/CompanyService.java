package com.springrest.restApp.service;

import com.springrest.restApp.domain.Company;
import com.springrest.restApp.repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    public Company handleCreateCompany(Company company){
        return this.companyRepository.save(company);
    }
}
