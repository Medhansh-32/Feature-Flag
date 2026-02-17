package com.tech.featureFlag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "feature")
public class Feature extends BaseEntity{


    @Column(name = "name")
    private String featureName;

    @Column(name = "description")
    private String discription;

    @Column(name = "isActive")
    private boolean isActive;

    @Column(name = "rollout_percentage")
    private double rolloutPercentage;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeaturePinCode> pinCodes;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeaturePlan> plans;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeatureRole> roles;
}
