package usta.sweetstore.infrastructure.adapter.in.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import jakarta.inject.Inject;

import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import usta.sweetstore.application.port.in.ManageCategoriesUseCase;
import usta.sweetstore.domain.exception.BusinessRuleException;

import java.net.URI;


@Path("/categories")
public class CategoriesResource {


    @Inject
    Template categories;


    @Inject
    ManageCategoriesUseCase
            manageCategoriesUseCase;


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
                categories
                        .data(
                                "categories",
                                manageCategoriesUseCase
                                        .findAll()
                        )
                        .data(
                                "error",
                                null
                        );


        return Response
                .ok(page)
                .build();
    }


    @POST
    @Consumes(
            MediaType.APPLICATION_FORM_URLENCODED
    )
    @Produces(MediaType.TEXT_HTML)
    public Response create(

            @CookieParam("sweetstore_session")
            Cookie cookie,

            @FormParam("name")
            String name,

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


            manageCategoriesUseCase.create(
                    name,
                    description
            );


            return Response
                    .seeOther(
                            URI.create("/categories")
                    )
                    .build();


        } catch (BusinessRuleException e) {


            TemplateInstance page =
                    categories
                            .data(
                                    "categories",
                                    manageCategoriesUseCase
                                            .findAll()
                            )
                            .data(
                                    "error",
                                    e.getMessage()
                            );


            return Response
                    .status(
                            Response.Status.BAD_REQUEST
                    )
                    .entity(page)
                    .type(MediaType.TEXT_HTML)
                    .build();
        }
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