package com.tech.featureFlag.service;


import com.tech.featureFlag.FeatureProto;
import com.tech.featureFlag.FeatureServiceGrpc;
import com.tech.featureFlag.entity.*;
import com.tech.featureFlag.enums.VersionOperator;
import com.tech.featureFlag.repository.FeatureRepository;
import com.tech.featureFlag.repository.UserRepository;
import com.tech.featureFlag.utils.CodeUtils;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

@GrpcService
public class FeatureService extends FeatureServiceGrpc.FeatureServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(FeatureService.class);
    private final UserRepository userRepository;
    private final FeatureCacheService featureCacheService;

    public FeatureService(UserRepository userRepository, FeatureCacheService featureCacheService) {
        this.userRepository = userRepository;
        this.featureCacheService = featureCacheService;
    }

        @Override
        public void checkFeature (FeatureProto.User request,
                StreamObserver < FeatureProto.FeatureResponse > responseObserver){

            boolean enabled = false;

            try {
                log.info("Feature check | userId={} feature={}",
                        request.getId(), request.getFeatureName());

                Optional<User> userOptional = userRepository.findById(request.getId());
                if (userOptional.isEmpty()) {
                    respond(responseObserver, false);
                    return;
                }

                User user = userOptional.get();

                Feature feature = featureCacheService.getFeature(request.getFeatureName());

                if (feature == null) {
                    respond(responseObserver, false);
                    return;
                }

                enabled = evaluateFeature(feature, user);

            } catch (Exception e) {
                log.error("Feature evaluation failed", e);
                enabled = false; // fail safe
            }

            respond(responseObserver, enabled);
        }

    private boolean evaluateFeature(Feature feature, User user) {

        if (!feature.isActive()) return false;


        boolean pinMatch = feature.getPinCodes() == null || feature.getPinCodes().isEmpty() ||
                feature.getPinCodes().stream()
                        .filter(fp -> Boolean.TRUE.equals(fp.getActive()))
                        .anyMatch(fp -> fp.getPinCodeMaster() != null &&
                                        fp.getPinCodeMaster().getPinCode()
                                                .equals(user.getPinCode()));

        if (!pinMatch){
            log.info("Feature : {} not Available at pin code : {} ", feature.getFeatureName(), user.getPinCode());
            return false;
        }

        // Role match
        boolean roleMatch = feature.getRoles() != null || feature.getRoles().isEmpty() ||
                feature.getRoles().stream()
                        .filter(FeatureRole::getActive)
                        .anyMatch(role ->
                                role.getRoleName()
                                        .equals(user.getAuthority()));

        if (!roleMatch) {
            log.info("Feature : {} not Available for Role : {} ", feature.getFeatureName(), user.getAuthority());
            return false;
        }

        // Plan match
        boolean planMatch = feature.getPlans() != null || feature.getPlans().isEmpty() ||
                feature.getPlans().stream()
                        .filter(FeaturePlan::getActive)
                        .anyMatch(plan ->
                                plan.getPlanType()
                                        .equals(user.getPlanType()));

        if (!planMatch) {
            log.info("Feature : {} not Available for Plan : {} ", feature.getFeatureName(), user.getPlanType());
            return false;
        }

        if(!evaluateVersionRules(feature, user)){
            log.info("Feature : {} not Available for Version : {} ", feature.getFeatureName(), user.getAppVersion());
            return false;
        }

        int hash = Math.abs(
                (user.getId() + feature.getFeatureName())
                        .hashCode()
        ) % 100;

        return hash < feature.getRolloutPercentage();
    }

    private boolean evaluateVersionRules(Feature feature, User user) {

        FeatureVersionRule rule = feature.getFeatureVersionRule();

        // No rule → no restriction
        if (rule == null || Boolean.FALSE.equals(rule.getActive())) {
            return true;
        }

        String userVersion = user.getAppVersion();

        if (userVersion == null || userVersion.isBlank()) {
            return false; // user has no version → deny
        }

        int comparison = CodeUtils.compare(userVersion, rule.getVersion());

        VersionOperator operator =
                VersionOperator.fromSymbol(rule.getOperator());

        return operator.evaluate(comparison);
    }

    private void respond(StreamObserver<FeatureProto.FeatureResponse> observer,
                         boolean enabled) {

        observer.onNext(
                FeatureProto.FeatureResponse.newBuilder()
                        .setEnabled(enabled)
                        .build()
        );
        observer.onCompleted();
    }





}



