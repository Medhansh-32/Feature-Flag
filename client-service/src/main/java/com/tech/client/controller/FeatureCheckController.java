package com.tech.client.controller;


import com.tech.client.service.FeatureCheckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/feature")
@RestController
public class FeatureCheckController {

    private final FeatureCheckService featureCheckService;

    public FeatureCheckController(FeatureCheckService featureCheckService) {
        this.featureCheckService = featureCheckService;
    }

    @GetMapping("/value")
    public String value(@RequestParam("feature") String feature, @RequestParam("userId") Long userId) {
        if(featureCheckService.isFeatureAvailable(feature,userId)){
            return "Feature available";
        }else{
            return "Feature not available";
        }

    }


}
