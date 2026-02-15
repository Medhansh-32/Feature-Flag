package com.tech.featureFlag.repository;

import com.tech.featureFlag.entity.FeaturePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeaturePlanRepository extends JpaRepository<FeaturePlan, Long> {
}