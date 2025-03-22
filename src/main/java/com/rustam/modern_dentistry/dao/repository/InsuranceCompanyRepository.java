package com.rustam.modern_dentistry.dao.repository;

import com.rustam.modern_dentistry.dao.entity.settings.InsuranceCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InsuranceCompanyRepository extends JpaRepository<InsuranceCompany, Long>, JpaSpecificationExecutor<InsuranceCompany> {
}
