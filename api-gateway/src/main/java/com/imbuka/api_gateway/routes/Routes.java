package com.imbuka.api_gateway.routes;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {


    @Bean
    public RouterFunction<ServerResponse> customerServiceRoutes() {
        return GatewayRouterFunctions.route("customer_service")
                .route(RequestPredicates.POST("/api/v1/accounts/createAccount"), HandlerFunctions.http("http://localhost:8080"))
                .route(RequestPredicates.GET("/api/v1/accounts/fetchAccountDetails"), HandlerFunctions.http("http://localhost:8080"))
                .route(RequestPredicates.PUT("/api/v1/accounts/updateAccountDetails"), HandlerFunctions.http("http://localhost:8080"))
                .route(RequestPredicates.DELETE("/api/v1/accounts/deleteAccountDetails"), HandlerFunctions.http("http://localhost:8080"))
                .route(RequestPredicates.POST("/api/v1/auth/register"), HandlerFunctions.http("http://localhost:8080"))
                .route(RequestPredicates.POST("/api/v1/auth/authenticate"), HandlerFunctions.http("http://localhost:8080"))
                .route(RequestPredicates.GET("/api/v1/auth/findAllUsers"), HandlerFunctions.http("http://localhost:8080"))
                .route(RequestPredicates.PUT("/api/v1/auth/updateProfile"), HandlerFunctions.http("http://localhost:8080"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> transactionServiceRoutes() {
        return GatewayRouterFunctions.route("transaction_service")
                .route(RequestPredicates.POST("/api/v1/transactions/deposit"), HandlerFunctions.http("http://localhost:8081"))
                .route(RequestPredicates.POST("/api/v1/transactions/withdraw"), HandlerFunctions.http("http://localhost:8081"))
                .route(RequestPredicates.POST("/api/v1/transactions/transfer"), HandlerFunctions.http("http://localhost:8081"))
                .build();
    }
}
