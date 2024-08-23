package com.sample.api;

import com.sample.IdentityService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@RolesAllowed({"User", "ApiUser"})
@Path("api/my-shared-resource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MySharedResource {

    private final IdentityService identityService;

    @Inject
    public MySharedResource(IdentityService identityService) {
        this.identityService = identityService;
    }

    @POST
    @Transactional
    @Path("/{someId}")
    public Response doStuff(@PathParam("someId") UUID someId) {
        String customerIdFromTokenOrApi = identityService.customerId();
        return Response.ok().build();
    }
}