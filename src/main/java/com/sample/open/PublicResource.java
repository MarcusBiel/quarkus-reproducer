package com.sample.open;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@PermitAll
@Path("api/public/my-public-resource")
public final class PublicResource {

    @GET
    public Response doStuff() {
        return Response.ok().build();
    }
}
