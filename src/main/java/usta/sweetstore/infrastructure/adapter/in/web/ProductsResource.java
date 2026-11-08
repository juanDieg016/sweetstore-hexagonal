package usta.sweetstore.infrastructure.adapter.in.web;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import jakarta.inject.Inject;

import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import usta.sweetstore.application.port.in.ManageCategoriesUseCase;
import usta.sweetstore.application.port.in.ManageProductsUseCase;

import usta.sweetstore.domain.exception.BusinessRuleException;
import usta.sweetstore.domain.model.Product;

import java.math.BigDecimal;
import java.net.URI;


@Path("/products")
public class ProductsResource {


    @Inject
    @Location("products.html")
    Template products;


    @Inject
    Template productForm;


    @Inject
    Template productEdit;


    @Inject
    ManageProductsUseCase
            manageProductsUseCase;


    @Inject
    ManageCategoriesUseCase
            manageCategoriesUseCase;


    @Inject
    AuthSessionService
            authSessionService;


    // =========================================
    // LISTADO
    // =========================================

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response list(

            @CookieParam("sweetstore_session")
            Cookie cookie
    ) {


        if (!isAuthenticated(cookie)) {

            return redirectLogin();
        }


        TemplateInstance page =
                products.data(
                        "products",
                        manageProductsUseCase.findAll()
                );


        return Response
                .ok(page)
                .build();
    }


    // =========================================
    // FORMULARIO NUEVO
    // =========================================

    @GET
    @Path("new")
    @Produces(MediaType.TEXT_HTML)
    public Response newProduct(

            @CookieParam("sweetstore_session")
            Cookie cookie
    ) {


        if (!isAuthenticated(cookie)) {

            return redirectLogin();
        }


        TemplateInstance page =
                productForm
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


    // =========================================
    // CREAR
    // =========================================

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
            String description,

            @FormParam("price")
            BigDecimal price,

            @FormParam("stock")
            int stock,

            @FormParam("minimumStock")
            int minimumStock,

            @FormParam("categoryId")
            Long categoryId
    ) {


        if (!isAuthenticated(cookie)) {

            return redirectLogin();
        }


        try {


            manageProductsUseCase.create(
                    name,
                    description,
                    price,
                    stock,
                    minimumStock,
                    categoryId
            );


            return Response
                    .seeOther(
                            URI.create("/products")
                    )
                    .build();


        } catch (BusinessRuleException e) {


            TemplateInstance page =
                    productForm
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


    // =========================================
    // FORMULARIO EDITAR
    // =========================================

    @GET
    @Path("{id}/edit")
    @Produces(MediaType.TEXT_HTML)
    public Response edit(

            @CookieParam("sweetstore_session")
            Cookie cookie,

            @PathParam("id")
            Long id
    ) {


        if (!isAuthenticated(cookie)) {

            return redirectLogin();
        }


        try {


            Product product =
                    manageProductsUseCase
                            .findById(id);


            TemplateInstance page =
                    productEdit
                            .data(
                                    "product",
                                    product
                            )
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


        } catch (BusinessRuleException e) {


            return Response
                    .seeOther(
                            URI.create("/products")
                    )
                    .build();
        }
    }


    // =========================================
    // ACTUALIZAR
    // =========================================

    @POST
    @Path("{id}")
    @Consumes(
            MediaType.APPLICATION_FORM_URLENCODED
    )
    @Produces(MediaType.TEXT_HTML)
    public Response update(

            @CookieParam("sweetstore_session")
            Cookie cookie,

            @PathParam("id")
            Long id,

            @FormParam("name")
            String name,

            @FormParam("description")
            String description,

            @FormParam("price")
            BigDecimal price,

            @FormParam("minimumStock")
            int minimumStock,

            @FormParam("categoryId")
            Long categoryId
    ) {


        if (!isAuthenticated(cookie)) {

            return redirectLogin();
        }


        try {


            manageProductsUseCase.update(
                    id,
                    name,
                    description,
                    price,
                    minimumStock,
                    categoryId
            );


            return Response
                    .seeOther(
                            URI.create("/products")
                    )
                    .build();


        } catch (BusinessRuleException e) {


            Product product =
                    manageProductsUseCase
                            .findById(id);


            TemplateInstance page =
                    productEdit
                            .data(
                                    "product",
                                    product
                            )
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


    private Response redirectLogin() {

        return Response
                .seeOther(
                        URI.create("/login")
                )
                .build();
    }
}