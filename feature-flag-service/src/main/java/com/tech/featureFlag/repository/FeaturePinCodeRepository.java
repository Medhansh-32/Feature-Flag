package com.tech.featureFlag.repository;

import com.tech.featureFlag.entity.FeaturePinCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeaturePinCodeRepository extends JpaRepository<FeaturePinCode, Long> {
}