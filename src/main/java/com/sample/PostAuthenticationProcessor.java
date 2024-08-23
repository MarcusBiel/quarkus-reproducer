package com.sample;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostAuthenticationProcessor {

    public SecurityIdentity processIdentity(SecurityIdentity identity, String role) {
        if (identity != null) {
            QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder(identity);
            builder.addRole(role);  /* Dynamically add role based on the authentication type */
            return builder.build();
        }
        return identity;
    }
}
