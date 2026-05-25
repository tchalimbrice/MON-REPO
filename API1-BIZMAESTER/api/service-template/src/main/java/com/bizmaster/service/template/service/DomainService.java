package com.bizmaster.service.template.service;

import com.bizmaster.service.template.dto.AuditDto;
import com.bizmaster.service.template.dto.CompanyDto;
import com.bizmaster.service.template.dto.DashboardSummaryDto;
import com.bizmaster.service.template.dto.InviteRequestDto;
import com.bizmaster.service.template.dto.InviteResponseDto;
import com.bizmaster.service.template.dto.ProductDto;
import com.bizmaster.service.template.dto.ReportDto;

import java.util.List;

public interface DomainService {
    List<CompanyDto> getCompanies();
    CompanyDto createCompany(CompanyDto companyDto, String createdBy);
    CompanyDto updateCompany(Long companyId, CompanyDto companyDto, String updatedBy);
    void deleteCompany(Long companyId, String deletedBy);
    List<CompanyDto> getCompaniesByDomain(String domain);
    CompanyDto getCompany(Long companyId);
    List<ProductDto> getProducts();
    ProductDto createProduct(ProductDto productDto, String createdBy);
    ProductDto updateProduct(Long productId, ProductDto productDto, String updatedBy);
    void deleteProduct(Long productId, String deletedBy);
    List<ProductDto> getProductsByCompany(Long companyId);
    List<AuditDto> getAudits();
    List<AuditDto> getAuditsByCompany(Long companyId);
    InviteResponseDto createInvite(InviteRequestDto inviteRequestDto, String createdBy);
    InviteResponseDto getInvite(String token);
    ReportDto saveReport(ReportDto reportDto, String createdBy);
    ReportDto saveReportForCompany(Long companyId, ReportDto reportDto, String createdBy);
    List<ReportDto> getReports();
    List<ReportDto> getReportsByCompany(Long companyId);
    DashboardSummaryDto getDashboardSummary(String username);
    DashboardSummaryDto getDashboardSummaryByCompany(Long companyId, String username);
}
