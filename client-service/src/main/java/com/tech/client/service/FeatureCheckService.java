package com.tech.client.service;

import com.google.protobuf.Timestamp;
import com.tech.featureFlag.FeatureProto;
import com.tech.featureFlag.FeatureServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class FeatureCheckService {


    @GrpcClient("feature-service")
    private FeatureServiceGrpc.FeatureServiceBlockingStub featureServiceStub;

    public Boolean isFeatureAvailable(String feature,Long userId) {
        return featureServiceStub.checkFeature(FeatureProto.User.newBuilder()
                .setFeatureName(feature)
                .setId(userId)
                .setRaisedAt(Timestamp.getDefaultInstance())
                .build())
                .getEnabled();
    }

}
