package com.tech.featureFlag.repository;

import com.tech.featureFlag.entity.FeatureRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRoleRepository extends JpaRepository<FeatureRole, Long> {
}