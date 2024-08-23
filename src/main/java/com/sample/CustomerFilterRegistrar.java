package com.sample;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.stream.Stream;

@Provider
public class CustomerFilterRegistrar implements DynamicFeature {

    private final Instance<String> usersProviderTenantPath;
    private final CustomerFilter subscriptionFilter;

    @Inject
    public CustomerFilterRegistrar(@ConfigProperty(name = "oidc.users.provider.tenant.path") Instance<String> usersProviderTenantPath, CustomerFilter subscriptionFilter) {
        this.usersProviderTenantPath = usersProviderTenantPath;
        this.subscriptionFilter = subscriptionFilter;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        String resourcePath = getPath(resourceInfo);

        if (!resourcePath.startsWith(usersProviderTenantPath.get())) {
            context.register(subscriptionFilter);
        }
    }

    private String getPath(ResourceInfo resourceInfo) {
        StringBuilder resourcePath = new StringBuilder();
        Optional<Path> classPathAnnotation = getPath(resourceInfo.getResourceMethod()
                .getDeclaringClass()
                .getDeclaredAnnotations());

        classPathAnnotation.ifPresent(a -> resourcePath.append(a.value()));

        Optional<Path> methodPathAnnotation =
                getPath(resourceInfo.getResourceMethod()
                        .getDeclaredAnnotations());
        methodPathAnnotation.ifPresent(a -> resourcePath.append(a.value()));
        return resourcePath.toString();
    }

    private Optional<Path> getPath(Annotation[] declaredAnnotations) {
        return Stream.of(declaredAnnotations)
                .filter(annotation -> Path.class.equals(annotation.annotationType()))
                .map(Path.class::cast)
                .findFirst();
    }
}
