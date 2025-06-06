package com.springrest.restApp.service;

import com.springrest.restApp.domain.Company;
import com.springrest.restApp.domain.dto.MetaDTO;
import com.springrest.restApp.domain.dto.ResultPaginationDTO;
import com.springrest.restApp.repository.CompanyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    public Company handleCreateCompany(Company company){
        return this.companyRepository.save(company);
    }
    public ResultPaginationDTO handleGetAllCompany(Specification spec, Pageable pageable){
        Page<Company> companies = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        MetaDTO mt = new MetaDTO();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotalPages(companies.getTotalPages());
        mt.setTotal(companies.getTotalElements());
        rs.setMetaDTO(mt);
        rs.setResult(companies.getContent());
        return rs;
    }
    public Void handleDeleteCompany(Long id){
        Company deletedCompany = this.companyRepository.getReferenceById(id);
        this.companyRepository.delete(deletedCompany);
        return null;
    }
    public Company handleUpdateCompany(Company company){
        Optional<Company> companyOptional = this.companyRepository.findById(company.getId());
        if (companyOptional.isPresent()){
            Company updatedCompany = companyOptional.get();
            updatedCompany.setName(company.getName());
            updatedCompany.setLogo(company.getLogo());
            updatedCompany.setDescription(company.getDescription());
            updatedCompany.setAddress(company.getAddress());
            return this.companyRepository.save(updatedCompany);
        }
        return null;
    }
}
