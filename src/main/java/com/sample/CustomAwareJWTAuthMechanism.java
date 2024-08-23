package com.sample;


import io.quarkus.oidc.runtime.OidcAuthenticationMechanism;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.quarkus.vertx.http.runtime.security.ChallengeData;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
import io.quarkus.vertx.http.runtime.security.HttpCredentialTransport;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import org.apache.http.auth.BasicUserPrincipal;

import java.security.Principal;
import java.util.Set;

@Alternative
@Priority(1)
@ApplicationScoped
public class CustomAwareJWTAuthMechanism implements HttpAuthenticationMechanism {

    private static final String API_KEY_HEADER = "X-API-Key";

    private final ApiKeyService apiKeyService;
    private final OidcAuthenticationMechanism delegate;
    private final PostAuthenticationProcessor postProcessor;

    @Inject
    public CustomAwareJWTAuthMechanism(ApiKeyService apiKeyService, OidcAuthenticationMechanism delegate, PostAuthenticationProcessor postProcessor) {
        this.apiKeyService = apiKeyService;
        this.delegate = delegate;
        this.postProcessor = postProcessor;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(RoutingContext context, IdentityProviderManager identityProviderManager) {
        String apiKey = context.request().getHeader(API_KEY_HEADER);

        if (apiKey != null) {
            return processApiKeyAuthentication(apiKey);
        } else {
            return processTokenAuthentication(context, identityProviderManager);
        }
    }

    private Uni<SecurityIdentity> processTokenAuthentication(RoutingContext context, IdentityProviderManager identityProviderManager) {
        return delegate.authenticate(context, identityProviderManager)
                .onItem().transform(identity -> postProcessor.processIdentity(identity, "User"));
    }

    private Uni<SecurityIdentity> processApiKeyAuthentication(String apiKey) {
        String customerId = apiKeyService.findAuthenticationIdByApiKey(apiKey);
        if (customerId == null) {
            return Uni.createFrom().nullItem();
        }
        Principal principal = new BasicUserPrincipal(customerId);
        QuarkusSecurityIdentity initialIdentity = QuarkusSecurityIdentity.builder()
                .setPrincipal(principal)
                .build();
        /* Use PostAuthenticationProcessor to add ApiUser role */
        return Uni.createFrom().item(() -> postProcessor.processIdentity(initialIdentity, "ApiUser"));
    }

    @Override
    public Uni<ChallengeData> getChallenge(RoutingContext context) {
        return delegate.getChallenge(context);
    }

    @Override
    public Set<Class<? extends AuthenticationRequest>> getCredentialTypes() {
        return delegate.getCredentialTypes();
    }

    @Override
    public Uni<HttpCredentialTransport> getCredentialTransport(RoutingContext context) {
        return delegate.getCredentialTransport(context);
    }
}
