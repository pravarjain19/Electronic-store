package com.lcwd.electronic.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@Configuration
public class swaggerConfig {

    @Bean
    public Docket docket(){


      Docket docket = new Docket(DocumentationType.SWAGGER_2);
      docket.apiInfo(getApiInfo());

        docket.securityContexts(Arrays.asList(getSecurityContext()));
        docket.securitySchemes(Arrays.asList(getSecuritySchemes()));

        ApiSelectorBuilder selectorBuilder = docket.select();
        selectorBuilder.apis(RequestHandlerSelectors.any());
        selectorBuilder.paths(PathSelectors.any());
        Docket build = selectorBuilder.build();
        return  build;
    }

    private ApiKey getSecuritySchemes() {
        return  new ApiKey("JWT", "Authorization" , "header");
    }

    private SecurityContext getSecurityContext() {

        SecurityContext build = SecurityContext.builder()
                .securityReferences(getSecurityReferences())
                .build();

        return  build;
    }

    private List<SecurityReference> getSecurityReferences() {

        AuthorizationScope [] scopes = {
                new AuthorizationScope("Gobal" ,"Access Everthing")
        };
        return  Arrays.asList(new SecurityReference("JWT" ,scopes ));
    }

    private ApiInfo getApiInfo() {

        ApiInfo apiInfo = new ApiInfo("Electronic Store project" , "All the api used by electronic store are documented" , "1.0.0" , "" ,
                "Pravar" , "Lincense of api" , "");

        return  apiInfo;
    }


}
