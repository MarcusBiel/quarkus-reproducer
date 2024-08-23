package com.sample;

import io.quarkus.logging.Log;
import io.vertx.core.http.HttpServerRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public final class LoggingFilter implements ContainerRequestFilter {

    @Context
    UriInfo info;

    @Context
    HttpServerRequest request;

    @Inject
    IdentityService identityService;


    @Override
    public void filter(ContainerRequestContext context) {
        String customerId = identityService.customerId();
        String method = context.getMethod();
        String path = info.getPath();

        for (Map.Entry<String, String> header : request.headers()) {
            Log.info(header.getKey() + ":" + header.getValue());
        }

        if (customerId != null) {
            Log.info(method + " " + path + " from customerId:" + customerId);
        }
    }
}
