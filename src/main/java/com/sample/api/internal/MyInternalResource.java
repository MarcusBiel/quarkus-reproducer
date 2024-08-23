package com.sample.api.internal;

import com.sample.IdentityService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("api/internal/my-internal-resource")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public final class MyInternalResource {

    private final IdentityService identityService;

    @Inject
    public MyInternalResource(IdentityService identityService) {
        this.identityService = identityService;
    }

    @GET
    public Response getMyInternalResources() {
        String customerIdFromOAuthToken = identityService.customerId();
        return Response.ok().build();
    }

    @DELETE
    @Transactional
    public Response deleteStuff(List<UUID> idsToDelete) {
        String customerIdFromOAuthToken = identityService.customerId();
        return Response.noContent().build();
    }
}
