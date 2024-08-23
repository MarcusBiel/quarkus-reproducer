package com.sample;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class IdentityService {

    private final SecurityIdentity identity;

    @Inject
    public IdentityService(SecurityIdentity identity) {
        this.identity = identity;
    }

    public String customerId() {
        /*  this works for oauth tokens.. but I guess I still need to map the API key to the actual customerid somehow..*/
        return identity.getPrincipal().getName();
    }
}
