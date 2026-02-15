package com.tech.featureFlag.repository;

import com.tech.featureFlag.entity.FeaturePincode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeaturePincodeRepository extends JpaRepository<FeaturePincode, Long> {
}