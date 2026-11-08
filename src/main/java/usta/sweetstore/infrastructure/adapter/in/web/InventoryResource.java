package usta.sweetstore.infrastructure.adapter.in.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import jakarta.inject.Inject;

import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import usta.sweetstore.application.port.in.ManageInventoryUseCase;
import usta.sweetstore.application.port.in.ManageProductsUseCase;

import usta.sweetstore.domain.exception.BusinessRuleException;

import java.net.URI;


@Path("/movements")
public class InventoryResource {


    @Inject
    Template movements;


    @Inject
    ManageInventoryUseCase
            manageInventoryUseCase;


    @Inject
    ManageProductsUseCase
            manageProductsUseCase;


    @Inject
    AuthSessionService
            authSessionService;


    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response list(

            @CookieParam("sweetstore_session")
            Cookie cookie
    ) {


        if (!isAuthenticated(cookie)) {

            return Response
                    .seeOther(
                            URI.create("/login")
                    )
                    .build();
        }


        TemplateInstance page =
                buildPage(null);


        return Response
                .ok(page)
                .build();
    }


    @POST
    @Path("entry")
    @Consumes(
            MediaType.APPLICATION_FORM_URLENCODED
    )
    @Produces(MediaType.TEXT_HTML)
    public Response registerEntry(

            @CookieParam("sweetstore_session")
            Cookie cookie,

            @FormParam("productId")
            Long productId,

            @FormParam("quantity")
            int quantity,

            @FormParam("description")
            String description
    ) {


        if (!isAuthenticated(cookie)) {

            return Response
                    .seeOther(
                            URI.create("/login")
                    )
                    .build();
        }


        try {


            manageInventoryUseCase
                    .registerEntry(
                            productId,
                            quantity,
                            description
                    );


            return Response
                    .seeOther(
                            URI.create("/movements")
                    )
                    .build();


        } catch (BusinessRuleException e) {


            return Response
                    .status(
                            Response.Status.BAD_REQUEST
                    )
                    .entity(
                            buildPage(
                                    e.getMessage()
                            )
                    )
                    .type(
                            MediaType.TEXT_HTML
                    )
                    .build();
        }
    }


    @POST
    @Path("exit")
    @Consumes(
            MediaType.APPLICATION_FORM_URLENCODED
    )
    @Produces(MediaType.TEXT_HTML)
    public Response registerExit(

            @CookieParam("sweetstore_session")
            Cookie cookie,

            @FormParam("productId")
            Long productId,

            @FormParam("quantity")
            int quantity,

            @FormParam("description")
            String description
    ) {


        if (!isAuthenticated(cookie)) {

            return Response
                    .seeOther(
                            URI.create("/login")
                    )
                    .build();
        }


        try {


            manageInventoryUseCase
                    .registerExit(
                            productId,
                            quantity,
                            description
                    );


            return Response
                    .seeOther(
                            URI.create("/movements")
                    )
                    .build();


        } catch (BusinessRuleException e) {


            return Response
                    .status(
                            Response.Status.BAD_REQUEST
                    )
                    .entity(
                            buildPage(
                                    e.getMessage()
                            )
                    )
                    .type(
                            MediaType.TEXT_HTML
                    )
                    .build();
        }
    }


    private TemplateInstance buildPage(
            String error
    ) {


        return movements
                .data(
                        "products",
                        manageProductsUseCase
                                .findAll()
                )
                .data(
                        "movements",
                        manageInventoryUseCase
                                .findAllMovements()
                )
                .data(
                        "error",
                        error
                );
    }


    private boolean isAuthenticated(
            Cookie cookie
    ) {


        return cookie != null
                && authSessionService
                .isValid(
                        cookie.getValue()
                );
    }
}