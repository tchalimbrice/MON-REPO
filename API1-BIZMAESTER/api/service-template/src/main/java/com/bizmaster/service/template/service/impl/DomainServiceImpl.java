package com.bizmaster.service.template.service.impl;

import com.bizmaster.service.template.dto.AuditDto;
import com.bizmaster.service.template.dto.CompanyDto;
import com.bizmaster.service.template.dto.DashboardMetricDto;
import com.bizmaster.service.template.dto.DashboardSummaryDto;
import com.bizmaster.service.template.dto.InviteRequestDto;
import com.bizmaster.service.template.dto.InviteResponseDto;
import com.bizmaster.service.template.dto.ProductDto;
import com.bizmaster.service.template.dto.ReportDto;
import com.bizmaster.service.template.entity.AuditLogEntity;
import com.bizmaster.service.template.entity.CompanyEntity;
import com.bizmaster.service.template.entity.InviteTokenEntity;
import com.bizmaster.service.template.entity.ProductEntity;
import com.bizmaster.service.template.entity.ReportEntity;
import com.bizmaster.service.template.mapper.AuditMapper;
import com.bizmaster.service.template.mapper.CompanyMapper;
import com.bizmaster.service.template.mapper.InviteMapper;
import com.bizmaster.service.template.mapper.ProductMapper;
import com.bizmaster.service.template.mapper.ReportMapper;
import com.bizmaster.service.template.repository.AuditLogRepository;
import com.bizmaster.service.template.repository.CompanyRepository;
import com.bizmaster.service.template.repository.InviteTokenRepository;
import com.bizmaster.service.template.repository.ProductRepository;
import com.bizmaster.service.template.repository.ReportRepository;
import com.bizmaster.service.template.service.DomainService;
import com.bizmaster.service.template.service.SseService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DomainServiceImpl implements DomainService {

    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final AuditLogRepository auditLogRepository;
    private final InviteTokenRepository inviteTokenRepository;
    private final ReportRepository reportRepository;
    private final SseService sseService;

    public DomainServiceImpl(CompanyRepository companyRepository,
                             ProductRepository productRepository,
                             AuditLogRepository auditLogRepository,
                             InviteTokenRepository inviteTokenRepository,
                             ReportRepository reportRepository,
                             SseService sseService) {
        this.companyRepository = companyRepository;
        this.productRepository = productRepository;
        this.auditLogRepository = auditLogRepository;
        this.inviteTokenRepository = inviteTokenRepository;
        this.reportRepository = reportRepository;
        this.sseService = sseService;
    }

    @Override
    public List<CompanyDto> getCompanies() {
        return companyRepository.findAll().stream()
                .map(CompanyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyDto> getCompaniesByDomain(String domain) {
        return companyRepository.findByDomainIgnoreCase(domain).stream()
                .map(CompanyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDto getCompany(Long companyId) {
        return CompanyMapper.toDto(requireCompany(companyId));
    }

    @Override
    @Transactional
    public CompanyDto createCompany(CompanyDto companyDto, String createdBy) {
        CompanyEntity entity = CompanyMapper.toEntity(companyDto);
        CompanyEntity saved = companyRepository.save(entity);
        saveAudit(saved, "COMPANY_CREATED", createdBy, "Company created: " + saved.getName());
        sseService.publishEvent(saved.getId(), "company-created", Map.of("company", CompanyMapper.toDto(saved)));
        return CompanyMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CompanyDto updateCompany(Long companyId, CompanyDto companyDto, String updatedBy) {
        CompanyEntity entity = requireCompany(companyId);
        entity.setName(companyDto.getName());
        entity.setAddress(companyDto.getAddress());
        entity.setEmail(companyDto.getEmail());
        entity.setPhone(companyDto.getPhone());
        entity.setManagerName(companyDto.getManagerName());
        entity.setMode(companyDto.getMode());
        entity.setDomain(companyDto.getDomain());
        entity.setUpdatedAt(Instant.now());
        CompanyEntity saved = companyRepository.save(entity);
        saveAudit(saved, "COMPANY_UPDATED", updatedBy, "Company updated: " + saved.getName());
        sseService.publishEvent(saved.getId(), "company-updated", Map.of("company", CompanyMapper.toDto(saved)));
        return CompanyMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteCompany(Long companyId, String deletedBy) {
        CompanyEntity company = requireCompany(companyId);
        saveAudit(company, "COMPANY_DELETED", deletedBy, "Company deleted: " + company.getName());
        companyRepository.delete(company);
    }

    @Override
    public List<ProductDto> getProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsByCompany(Long companyId) {
        return productRepository.findByCompanyId(companyId).stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto, String createdBy) {
        CompanyEntity company = requireCompany(productDto.getCompanyId());
        ProductEntity entity = ProductMapper.toEntity(productDto, company);
        ProductEntity saved = productRepository.save(entity);
        saveAudit(company, "PRODUCT_CREATED", createdBy, "Product created: " + saved.getName());
        sseService.publishEvent(company.getId(), "product-created", Map.of("product", ProductMapper.toDto(saved)));
        return ProductMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long productId, ProductDto productDto, String updatedBy) {
        ProductEntity entity = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        CompanyEntity company = entity.getCompany();
        entity.setReference(productDto.getReference());
        entity.setName(productDto.getName());
        entity.setCategory(productDto.getCategory());
        entity.setPurchasePrice(productDto.getPurchasePrice());
        entity.setSalePrice(productDto.getSalePrice());
        entity.setStock(productDto.getStock());
        entity.setMinStock(productDto.getMinStock());
        entity.setSupplier(productDto.getSupplier());
        entity.setUnit(productDto.getUnit());
        ProductEntity saved = productRepository.save(entity);
        saveAudit(company, "PRODUCT_UPDATED", updatedBy, "Product updated: " + saved.getName());
        sseService.publishEvent(company.getId(), "product-updated", Map.of("product", ProductMapper.toDto(saved)));
        return ProductMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId, String deletedBy) {
        ProductEntity entity = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        CompanyEntity company = entity.getCompany();
        saveAudit(company, "PRODUCT_DELETED", deletedBy, "Product deleted: " + entity.getName());
        productRepository.delete(entity);
    }

    @Override
    public List<AuditDto> getAudits() {
        return auditLogRepository.findAll().stream().map(AuditMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AuditDto> getAuditsByCompany(Long companyId) {
        return auditLogRepository.findByCompanyIdOrderByCreatedAtDesc(companyId).stream()
                .map(AuditMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InviteResponseDto createInvite(InviteRequestDto inviteRequestDto, String createdBy) {
        CompanyEntity company = requireCompany(Long.valueOf(inviteRequestDto.getCompanyId()));
        InviteTokenEntity token = new InviteTokenEntity();
        token.setCompany(company);
        token.setEmail(inviteRequestDto.getEmail());
        token.setMode(inviteRequestDto.getMode());
        token.setRole(inviteRequestDto.getRole());
        token.setToken(UUID.randomUUID().toString());
        token.setCreatedAt(Instant.now());
        token.setExpiresAt(Instant.now().plusSeconds(86400));
        InviteTokenEntity saved = inviteTokenRepository.save(token);
        saveAudit(company, "INVITE_GENERATED", createdBy, "Invite generated for " + saved.getEmail());
        sseService.publishEvent(company.getId(), "invite-created", Map.of("invite", InviteMapper.toDto(saved)));
        return InviteMapper.toDto(saved);
    }

    @Override
    public InviteResponseDto getInvite(String token) {
        return inviteTokenRepository.findByToken(token)
                .map(InviteMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Invite token not found"));
    }

    @Override
    @Transactional
    public ReportDto saveReport(ReportDto reportDto, String createdBy) {
        CompanyEntity company = requireCompany(reportDto.getCompanyId());
        ReportEntity entity = ReportMapper.toEntity(reportDto, company);
        ReportEntity saved = reportRepository.save(entity);
        saveAudit(company, "REPORT_STORED", createdBy, "Report stored: " + saved.getName());
        sseService.publishEvent(company.getId(), "report-created", Map.of("reportId", saved.getId()));
        return ReportMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ReportDto saveReportForCompany(Long companyId, ReportDto reportDto, String createdBy) {
        reportDto.setCompanyId(companyId);
        return saveReport(reportDto, createdBy);
    }

    @Override
    public List<ReportDto> getReports() {
        return reportRepository.findAll().stream().map(ReportMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ReportDto> getReportsByCompany(Long companyId) {
        return reportRepository.findByCompanyId(companyId).stream().map(ReportMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public DashboardSummaryDto getDashboardSummary(String username) {
        long totalCompanies = companyRepository.count();
        long totalProducts = productRepository.count();
        long totalReports = reportRepository.count();
        long totalInvites = inviteTokenRepository.count();

        DashboardSummaryDto summary = new DashboardSummaryDto();
        summary.setService("Template service");
        summary.setMetrics(List.of(
                newMetric("companies", "Companies", totalCompanies, "blue"),
                newMetric("products", "Products", totalProducts, "green"),
                newMetric("reports", "Reports", totalReports, "purple"),
                newMetric("invites", "Invitations", totalInvites, "orange")
        ));
        return summary;
    }

    @Override
    public DashboardSummaryDto getDashboardSummaryByCompany(Long companyId, String username) {
        CompanyEntity company = requireCompany(companyId);
        long totalProducts = productRepository.countByCompanyId(companyId);
        long totalReports = reportRepository.countByCompanyId(companyId);
        long totalAudits = auditLogRepository.countByCompanyId(companyId);
        long totalInvites = inviteTokenRepository.countByCompanyId(companyId);

        DashboardSummaryDto summary = new DashboardSummaryDto();
        summary.setService(company.getName());
        summary.setMetrics(List.of(
                newMetric("companies", "Companies", 1, "blue"),
                newMetric("products", "Products", totalProducts, "green"),
                newMetric("reports", "Reports", totalReports, "purple"),
                newMetric("audits", "Audits", totalAudits, "orange"),
                newMetric("invites", "Invitations", totalInvites, "teal")
        ));
        return summary;
    }

    private DashboardMetricDto newMetric(String code, String label, long value, String color) {
        DashboardMetricDto metric = new DashboardMetricDto();
        metric.setCode(code);
        metric.setLabel(label);
        metric.setValue(value);
        metric.setColor(color);
        metric.setUpdatedAt(Instant.now().toString());
        return metric;
    }

    private CompanyEntity requireCompany(Long companyId) {
        if (companyId == null) {
            throw new IllegalArgumentException("Company id is required");
        }
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
    }

    private void saveAudit(CompanyEntity company, String action, String username, String details) {
        AuditLogEntity audit = new AuditLogEntity();
        audit.setCompany(company);
        audit.setAction(action);
        audit.setUsername(username);
        audit.setDetails(details);
        audit.setCreatedAt(Instant.now());
        auditLogRepository.save(audit);
    }
}
