package com.bizmaster.service.template.controller;

import com.bizmaster.service.template.dto.AuditDto;
import com.bizmaster.service.template.dto.CompanyDto;
import com.bizmaster.service.template.dto.DashboardSummaryDto;
import com.bizmaster.service.template.dto.InviteRequestDto;
import com.bizmaster.service.template.dto.InviteResponseDto;
import com.bizmaster.service.template.dto.ProductDto;
import com.bizmaster.service.template.dto.ReportDto;
import com.bizmaster.service.template.service.DomainService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${service.base-path:/api/template}")
public class DomainController {

    private final DomainService domainService;

    public DomainController(DomainService domainService) {
        this.domainService = domainService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/dashboard/summary")
    public ResponseEntity<DashboardSummaryDto> getDashboardSummary(Authentication authentication) {
        return ResponseEntity.ok(domainService.getDashboardSummary(getUserEmail(authentication)));
    }

    @GetMapping("/companies")
    public ResponseEntity<List<CompanyDto>> getCompanies() {
        return ResponseEntity.ok(domainService.getCompanies());
    }

    @GetMapping("/companies/domain/{domain}")
    public ResponseEntity<List<CompanyDto>> getCompaniesByDomain(@PathVariable String domain) {
        return ResponseEntity.ok(domainService.getCompaniesByDomain(domain));
    }

    @GetMapping("/companies/{companyId}")
    public ResponseEntity<CompanyDto> getCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(domainService.getCompany(companyId));
    }

    @PostMapping("/companies")
    public ResponseEntity<CompanyDto> createCompany(@Valid @RequestBody CompanyDto companyDto, Authentication authentication) {
        return ResponseEntity.ok(domainService.createCompany(companyDto, getUserEmail(authentication)));
    }

    @PutMapping("/companies/{companyId}")
    public ResponseEntity<CompanyDto> updateCompany(@PathVariable Long companyId,
                                                    @Valid @RequestBody CompanyDto companyDto,
                                                    Authentication authentication) {
        return ResponseEntity.ok(domainService.updateCompany(companyId, companyDto, getUserEmail(authentication)));
    }

    @DeleteMapping("/companies/{companyId}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long companyId, Authentication authentication) {
        domainService.deleteCompany(companyId, getUserEmail(authentication));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getProducts() {
        return ResponseEntity.ok(domainService.getProducts());
    }

    @GetMapping("/companies/{companyId}/products")
    public ResponseEntity<List<ProductDto>> getProductsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(domainService.getProductsByCompany(companyId));
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto, Authentication authentication) {
        return ResponseEntity.ok(domainService.createProduct(productDto, getUserEmail(authentication)));
    }

    @PostMapping("/companies/{companyId}/products")
    public ResponseEntity<ProductDto> createProductForCompany(@PathVariable Long companyId,
                                                             @Valid @RequestBody ProductDto productDto,
                                                             Authentication authentication) {
        productDto.setCompanyId(companyId);
        return ResponseEntity.ok(domainService.createProduct(productDto, getUserEmail(authentication)));
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId,
                                                    @Valid @RequestBody ProductDto productDto,
                                                    Authentication authentication) {
        return ResponseEntity.ok(domainService.updateProduct(productId, productDto, getUserEmail(authentication)));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId, Authentication authentication) {
        domainService.deleteProduct(productId, getUserEmail(authentication));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/audits")
    public ResponseEntity<List<AuditDto>> getAudits() {
        return ResponseEntity.ok(domainService.getAudits());
    }

    @GetMapping("/companies/{companyId}/audits")
    public ResponseEntity<List<AuditDto>> getAuditsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(domainService.getAuditsByCompany(companyId));
    }

    @PostMapping("/invites")
    public ResponseEntity<InviteResponseDto> createInvite(@Valid @RequestBody InviteRequestDto inviteRequestDto, Authentication authentication) {
        return ResponseEntity.ok(domainService.createInvite(inviteRequestDto, getUserEmail(authentication)));
    }

    @GetMapping("/invites/{token}")
    public ResponseEntity<InviteResponseDto> getInvite(@PathVariable String token) {
        return ResponseEntity.ok(domainService.getInvite(token));
    }

    @PostMapping("/reports")
    public ResponseEntity<ReportDto> saveReport(@Valid @RequestBody ReportDto reportDto, Authentication authentication) {
        return ResponseEntity.ok(domainService.saveReport(reportDto, getUserEmail(authentication)));
    }

    @PostMapping("/companies/{companyId}/reports")
    public ResponseEntity<ReportDto> saveReportForCompany(@PathVariable Long companyId,
                                                          @Valid @RequestBody ReportDto reportDto,
                                                          Authentication authentication) {
        return ResponseEntity.ok(domainService.saveReportForCompany(companyId, reportDto, getUserEmail(authentication)));
    }

    @GetMapping("/reports")
    public ResponseEntity<List<ReportDto>> getReports() {
        return ResponseEntity.ok(domainService.getReports());
    }

    @GetMapping("/companies/{companyId}/reports")
    public ResponseEntity<List<ReportDto>> getReportsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(domainService.getReportsByCompany(companyId));
    }

    @GetMapping("/companies/{companyId}/dashboard/summary")
    public ResponseEntity<DashboardSummaryDto> getDashboardSummaryByCompany(@PathVariable Long companyId, Authentication authentication) {
        return ResponseEntity.ok(domainService.getDashboardSummaryByCompany(companyId, getUserEmail(authentication)));
    }

    private String getUserEmail(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return "anonymous";
        }
        return authentication.getName();
    }
}
