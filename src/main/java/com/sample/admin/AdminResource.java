package com.sample.admin;

import com.sample.IdentityService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@RolesAllowed("Admin")
@Path("api/admin/my-admin-resource")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResource {

    private final IdentityService identityService;

    @Inject
    public AdminResource(IdentityService identityService) {
        this.identityService = identityService;
    }

    @POST
    @Transactional
    @Path("/{someId}")
    public Response doSecretSuff(@PathParam("someId") UUID someId) {
        String adminId = identityService.customerId();
        return Response.ok().build();
    }
}


