package com.tech.featureFlag.service;


import com.tech.featureFlag.FeatureProto;
import com.tech.featureFlag.FeatureServiceGrpc;
import com.tech.featureFlag.entity.Feature;
import com.tech.featureFlag.entity.FeaturePlan;
import com.tech.featureFlag.entity.FeatureRole;
import com.tech.featureFlag.entity.User;
import com.tech.featureFlag.repository.FeatureRepository;
import com.tech.featureFlag.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@GrpcService
public class FeatureService extends FeatureServiceGrpc.FeatureServiceImplBase {

    private final FeatureRepository featureRepository;

    private static final Logger log =
            LoggerFactory.getLogger(FeatureService.class);
    private final UserRepository userRepository;

    public FeatureService(FeatureRepository featureRepository, UserRepository userRepository) {
        this.featureRepository = featureRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void checkFeature(FeatureProto.User request, StreamObserver<FeatureProto.FeatureResponse> responseObserver) {

        log.info("Request received for userId {} {}",request.getId(), request.getFeatureName());

        String featureName = request.getFeatureName();

        Feature feature = featureRepository.findByFeatureNameAndAndIsActive(featureName,true);

        Optional<User> userOptional = userRepository.findById(request.getId());

        if(userOptional.isEmpty()){
            responseObserver.onNext(
                    FeatureProto.FeatureResponse.newBuilder()
                            .setEnabled(false)
                            .build()
            );
            responseObserver.onCompleted();
        }

        User user = userOptional.get();

        if (feature == null) {
            responseObserver.onNext(
                    FeatureProto.FeatureResponse.newBuilder()
                            .setEnabled(false)
                            .build()
            );
            responseObserver.onCompleted();
            return;
        }

        boolean isEnabled = false;

        if (feature.getPinCodes() != null) {
            isEnabled = feature.getPinCodes()
                    .stream()
                    .anyMatch(fp ->
                            fp.getPinCodeMaster() != null &&
                                    fp.getPinCodeMaster().getPinCode()
                                            .equals(user.getPinCode())
                    );
        }

        List<FeatureRole> authorityList = feature.getRoles();
        List<FeaturePlan> planList = feature.getPlans();


        isEnabled = authorityList.stream().anyMatch(featureRole -> featureRole.getRoleName().equals(user.getAuthority()));
        if (!isEnabled) {
            responseObserver.onNext(
                    FeatureProto.FeatureResponse.newBuilder()
                            .setEnabled(false)
                            .build()
            );
            responseObserver.onCompleted();
            return;
        }
        isEnabled = planList.stream().anyMatch(plan -> plan.getPlanType().equals(user.getPlanType()));
        if (!isEnabled) {
            responseObserver.onNext(
                    FeatureProto.FeatureResponse.newBuilder()
                            .setEnabled(false)
                            .build()
            );
            responseObserver.onCompleted();
            return;
        }
        int hash = Math.abs(Long.hashCode(request.getId()) % 100);
        if(hash < feature.getRolloutPercentage()){
            isEnabled = true;
        }

        responseObserver.onNext(
                FeatureProto.FeatureResponse.newBuilder()
                        .setEnabled(isEnabled)
                        .build()
        );
        responseObserver.onCompleted();



    }
}
