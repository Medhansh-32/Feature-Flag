package com.tech.featureFlag.service;

import com.tech.featureFlag.entity.Feature;
import com.tech.featureFlag.repository.FeatureRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class FeatureCacheService {

    private final FeatureRepository featureRepository;

    public FeatureCacheService(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    @Cacheable(
            value = "features",
            key = "#featureName",
            unless = "#result == null"
    )
    public Feature getFeature(String featureName) {
        return featureRepository
                .findByFeatureNameAndAndIsActive(featureName, true);
    }
}
