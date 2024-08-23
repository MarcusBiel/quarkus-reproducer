package com.sample;

import io.quarkus.logging.Log;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.configuration.ConfigUtils;

import java.util.List;

@QuarkusMain
public class Application {

    public static void main(String... args) {
        List<String> activeProfiles = ConfigUtils.getProfiles().stream()
                .filter(ConfigUtils::isProfileActive)
                .toList();
        Log.info("starting with profiles: "+ activeProfiles);
        Quarkus.run(args);
    }
}
