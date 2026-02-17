package com.tech.featureFlag.service;


import com.tech.featureFlag.FeatureProto;
import com.tech.featureFlag.FeatureServiceGrpc;
import com.tech.featureFlag.entity.Feature;
import com.tech.featureFlag.repository.FeatureRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Slf4j
@RequiredArgsConstructor
public class FeatureService extends FeatureServiceGrpc.FeatureServiceImplBase {

    private final FeatureRepository featureRepository;

    @Override
    public void checkFeature(FeatureProto.User request, StreamObserver<FeatureProto.FeatureResponse> responseObserver) {

        log.info("Request received for userId {} {}",request.getId(), request.getFeatureName());

        String featureName = request.getFeatureName();

        Feature feature = featureRepository.findByFeatureNameAndAndIsActive(featureName,true);

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
                                            .equals(request.getPinCode())
                    );
        }

        responseObserver.onNext(
                FeatureProto.FeatureResponse.newBuilder()
                        .setEnabled(isEnabled)
                        .build()
        );
        responseObserver.onCompleted();



    }
}
