package com.sample;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;

@Priority(Priorities.AUTHORIZATION)
@ApplicationScoped
public class CustomerFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        SecurityContext securityContext = requestContext.getSecurityContext();

        Principal userPrincipal = securityContext.getUserPrincipal();

        if (userPrincipal == null) {
            return;
        }
    }
}
