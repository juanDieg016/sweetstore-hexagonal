package usta.sweetstore.infrastructure.adapter.in.web;

import io.quarkus.qute.Location;
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

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import usta.sweetstore.application.port.in.ManageInventoryUseCase;
import usta.sweetstore.application.port.in.ManageProductsUseCase;

import java.net.URI;


@Path("/reports")
@Tag(
        name = "Reportes",
        description = "Reportes del inventario SweetStore"
)
public class ReportsResource {


    @Inject
    @Location("reports.html")
    Template reports;


    @Inject
    ManageProductsUseCase
            manageProductsUseCase;


    @Inject
    ManageInventoryUseCase
            manageInventoryUseCase;


    @Inject
    InventoryPdfService
            inventoryPdfService;


    @Inject
    AuthSessionService
            authSessionService;


    @GET
    @Produces(MediaType.TEXT_HTML)
    @Operation(
            summary = "Muestra la pantalla de reportes"
    )
    public Response reportsPage(

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
                reports
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


    @GET
    @Path("inventory.pdf")
    @Produces("application/pdf")
    @Operation(
            summary = "Descarga el reporte general de inventario en PDF"
    )
    public Response inventoryPdf(

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


        var products =
                manageProductsUseCase
                        .findAll();


        byte[] pdf =
                inventoryPdfService
                        .generateInventoryReport(
                                products
                        );


        return Response
                .ok(pdf)
                .type("application/pdf")
                .header(
                        "Content-Disposition",
                        "attachment; filename=\"inventario-sweetstore.pdf\""
                )
                .build();
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