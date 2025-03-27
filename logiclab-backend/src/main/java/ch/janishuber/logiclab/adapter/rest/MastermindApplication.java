package ch.janishuber.logiclab.adapter.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class MastermindApplication extends Application {
/*
    jpa --> Database
    mpConfig --> Microprofile config --> anstelle von db.properties
    OpenAPI --> API Documentation and Generation
    CDI / Injection --> Dependency Injection
*/
}