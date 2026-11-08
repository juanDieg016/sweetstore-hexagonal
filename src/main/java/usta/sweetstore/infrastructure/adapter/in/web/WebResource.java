package usta.sweetstore.infrastructure.adapter.in.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import jakarta.inject.Inject;

import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import usta.sweetstore.application.port.in.ManageInventoryUseCase;
import usta.sweetstore.application.port.in.ManageProductsUseCase;

import java.net.URI;


@Path("/")
public class WebResource {


    @Inject
    Template index;


    @Inject
    ManageProductsUseCase
            manageProductsUseCase;


    @Inject
    ManageInventoryUseCase
            manageInventoryUseCase;


    @Inject
    AuthSessionService
            authSessionService;


    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response home(

            @CookieParam(
                    "sweetstore_session"
            )
            Cookie cookie
    ) {


        if (cookie == null
                || !authSessionService
                .isValid(cookie.getValue())) {


            return Response
                    .seeOther(
                            URI.create("/login")
                    )
                    .build();
        }


        var products =
                manageProductsUseCase
                        .findAll();


        var lowStockProducts =
                manageInventoryUseCase
                        .findLowStockProducts();


        int totalUnits =
                products
                        .stream()
                        .mapToInt(
                                product ->
                                        product.getStock()
                        )
                        .sum();


        TemplateInstance page =
                index
                        .data(
                                "products",
                                products
                        )
                        .data(
                                "totalProducts",
                                products.size()
                        )
                        .data(
                                "totalUnits",
                                totalUnits
                        )
                        .data(
                                "lowStockCount",
                                lowStockProducts.size()
                        );


        return Response
                .ok(page)
                .build();
    }
}