package usta.sweetstore.infrastructure.adapter.in.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/")
@Tag(name = "Autenticación")
public class LoginResource {

    @Inject
    Template login;

    @Inject
    AuthSessionService authSessionService;

    @ConfigProperty(name = "sweetstore.admin.username")
    String adminUsername;

    @ConfigProperty(name = "sweetstore.admin.password")
    String adminPassword;


    @GET
    @Path("login")
    @Produces(MediaType.TEXT_HTML)
    @Operation(summary = "Muestra el formulario de inicio de sesión")
    public TemplateInstance loginPage() {

        return login.data("error", null);
    }


    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @Operation(summary = "Inicia sesión como administrador")
    public Response login(
            @FormParam("username") String username,
            @FormParam("password") String password
    ) {

        if (!adminUsername.equals(username)
                || !adminPassword.equals(password)) {

            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(
                            login.data(
                                    "error",
                                    "Usuario o contraseña incorrectos."
                            )
                    )
                    .build();
        }

        String token =
                authSessionService.createSession(username);

        NewCookie cookie = new NewCookie.Builder(
                "sweetstore_session"
        )
                .value(token)
                .path("/")
                .httpOnly(true)
                .build();

        return Response
                .seeOther(java.net.URI.create("/"))
                .cookie(cookie)
                .build();
    }


    @GET
    @Path("logout")
    public Response logout(
            @CookieParam("sweetstore_session")
            Cookie cookie
    ) {

        if (cookie != null) {
            authSessionService.closeSession(cookie.getValue());
        }

        NewCookie deleteCookie =
                new NewCookie.Builder(
                        "sweetstore_session"
                )
                        .value("")
                        .path("/")
                        .maxAge(0)
                        .build();

        return Response
                .seeOther(java.net.URI.create("/login"))
                .cookie(deleteCookie)
                .build();
    }
}