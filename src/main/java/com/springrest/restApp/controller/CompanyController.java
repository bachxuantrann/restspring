package com.springrest.restApp.controller;

import com.springrest.restApp.domain.Company;
import com.springrest.restApp.domain.dto.ResultPaginationDTO;
import com.springrest.restApp.service.CompanyService;
import com.springrest.restApp.util.annotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
//    Create a new company
    @PostMapping("/companies")
    public ResponseEntity<?> createCompany(@Valid @RequestBody Company company){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.handleCreateCompany(company));
    }
//    Get list companies
    @GetMapping("/companies")
    @ApiMessage("get all companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            @Filter Specification<Company> spec,
            Pageable pageable
            ){
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.handleGetAllCompany(spec,pageable));
    }
//    Delete a company
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long id){
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
//    Update a company
    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company){
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.handleUpdateCompany(company));
    }
}
