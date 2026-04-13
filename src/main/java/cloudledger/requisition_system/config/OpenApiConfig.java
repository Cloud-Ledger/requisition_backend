package cloudledger.requisition_system.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@SecurityScheme
        (
                name =
                        "SECURED GATEWAY API"
                ,
                type =
                        SecuritySchemeType
                                .
                                HTTP
                ,
                bearerFormat =
                        "JWT"
                ,
                scheme =
                        "bearer"
                ,
                in =
                        SecuritySchemeIn
                                .
                                HEADER
        )
@OpenAPIDefinition

@Configuration

public class
OpenApiConfig
{

    @Bean

    public OpenAPI
    customOpenAPI()
            throws
            IOException {

        return new
                OpenAPI()
                .info(
                        new
                                Info()
                                .title(
                                        "Requisition System V1"
                                )
                                .description(
                                        "A streamlined accounting system that enhances efficiency in payment\n" +
                                                "processing, data management, and reporting, while integrating seamlessly with SAGE for\n" +
                                                "minimal manual intervention"
                                )
                                .termsOfService(
                                        "terms"
                                )
                                .contact(
                                        new
                                                Contact().email(
                                                "ngonidzashenyachoto@gmail.com"
                                        ))
                                .license(
                                        new
                                                License().name(
                                                "Cloudledger Zimbabwe"
                                        ))
                                .version(
                                        "v1.0"
                                )
                )
                ;
    }
}