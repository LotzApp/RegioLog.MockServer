package org.lotzapp;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jspecify.annotations.NonNull;
import org.lotzapp.adminapi.model.Application;
import org.lotzapp.adminapi.model.UserBase;
import org.lotzapp.adminapi.model.UserStatus;
import org.lotzapp.component.ClientComponent;
import org.lotzapp.component.OrderComponent;
import org.lotzapp.component.ProductComponent;
import org.lotzapp.component.RequestComponent;
import org.lotzapp.regiologapi.model.*;
import org.lotzapp.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.lotzapp.component.CategoryComponent.getCategoriesResponse;
import static org.lotzapp.component.PermissionComponent.getPermissions;
import static org.lotzapp.component.RequestComponent.*;
import static org.lotzapp.component.UserComponent.getUsersResponse;

@Aspect
@Component
@Slf4j
public class RequestAspect {

    private final ProductComponent productComponent;
    private final OrderComponent orderComponent;

    @Autowired
    public RequestAspect(ProductComponent productComponent, OrderComponent orderComponent) {
        this.productComponent = productComponent;
        this.orderComponent = orderComponent;
    }

    private ResponseEntity<?> getAccountData(String userName) {
        // return ResponseEntity.status(500).body(new ErrorResponse(new Error(ErrorCode.NUMBER_0, "test")));

        // if (userName.startsWith("badRequest")) {
        //     return ResponseEntity.badRequest().body(new ErrorResponse(new Error(ErrorCode.NUMBER_0, "bad request")));
        // } else if (userName.startsWith("unauthorized")) {
        //     return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(new ErrorResponse(new Error(ErrorCode.NUMBER_0, "invalid credentials")));
        // } else if (userName.startsWith("error")) {
        //     return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(new ErrorResponse(new Error(ErrorCode.NUMBER_0, "server error")));
        // }
        
        return ResponseEntity.ok(
                new UserBase(
                        UUID.randomUUID(),
                        "asdf",
                        null, //OffsetDateTime.now(),
                        "yx",
                        "4561",
                        "reset",
                        UserStatus.ACTIVE,
                        null, //OffsetDateTime.now(),
                        OffsetDateTime.now(),
                        new ArrayList<>()
                )
        );
    }

    private ResponseEntity<List<String>> getBrokers() {
        return ResponseEntity.ok(List.of("Broker001", "Broker001"));
    }

    private ResponseEntity<List<Application>> getApplications() {
        var applications = List.of(
                new Application(UUID.randomUUID(), "regiolog", OffsetDateTime.now(), getPermissions())
        );
        return ResponseEntity.ok(applications);
    }

    ResponseEntity<List<Country>> getCountries() {
        return ResponseEntity.ok(getCountryList());
    }

    private static @NonNull List<Country> getCountryList() {
        return List.of(
                new Country(1, "AT", OffsetDateTime.now()),
                new Country(2, "DE", OffsetDateTime.now()),
                new Country(3, "ES", OffsetDateTime.now())
        );
    }

    ResponseEntity<List<Currency>> getCurrencies() {
        return ResponseEntity.ok(List.of(
             new Currency(1, "Euro", OffsetDateTime.now()),
             new Currency(2, "Krone", OffsetDateTime.now())
        ));
    }

    ResponseEntity<List<Unit>> getUnits() {
        return ResponseEntity.ok(List.of(
          new Unit(1, "kg", OffsetDateTime.now()),
          new Unit(2, "g", OffsetDateTime.now()),
          new Unit(3, "t", OffsetDateTime.now())
        ));
    }

    ResponseEntity<List<Label>> getLabels() {
        return ResponseEntity.ok(List.of(
          new Label(1, "Lbl1", OffsetDateTime.now()),
          new Label(2, "Lbl2", OffsetDateTime.now())
        ));
    }
    
    ResponseEntity<List<BioControlService>> getBioControlServices() {
        return ResponseEntity.ok(List.of(
            new BioControlService(1, "BCS (AT)", getCountryList().getFirst(), OffsetDateTime.now()),
            new BioControlService(2, "BCS (DE)", getCountryList().get(2), OffsetDateTime.now())
        ));
    }

    ResponseEntity<List<Level>> getLevels() {
        return ResponseEntity.ok(List.of(
                new Level(1, "Level 1", OffsetDateTime.now()),
                new Level(2, "Level 2", OffsetDateTime.now())
        ));
    }

    private HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes sra) {
            return sra.getRequest();
        }
        return null;
    }

    @Around("execution(org.springframework.http.ResponseEntity * (..)) && !within(org.lotzapp.component..*)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = getCurrentHttpRequest();

        var userName = "";
        if (request != null) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Basic ")) {
                String base64Credentials = authHeader.substring("Basic ".length());
                byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
                String credentials = new String(credDecoded, java.nio.charset.StandardCharsets.UTF_8);
                String[] values = credentials.split(":", 2);
                userName = values[0];
                // String password = values.length > 1 ? values[1] : "";
            }
        }

        var method = joinPoint.getSignature().getName();
        log.info("Calling: {} with {}", method, joinPoint.getArgs());


        TimeUtils.sleepForSeconds(1);
        return switch (method) {
            case "getAccountData" -> getAccountData(userName);
            case "getBrokers" -> getBrokers();
            case "getApplications" -> getApplications();
            case "getRequests" -> getRequestResponse();
            case "cancelRequest" -> cancelRequest(joinPoint.getArgs());
            case "acceptRequest" -> acceptRequest(joinPoint.getArgs());
            case "getCountries" -> getCountries();
            case "getCurrencies" -> getCurrencies();
            case "getUnits" -> getUnits();
            case "getUsers" -> getUsersResponse(joinPoint.getArgs());
            case "getLabels" -> getLabels();
            case "getBioControlServices" -> getBioControlServices();
            case "getCategories" -> getCategoriesResponse();
            case "getLevels" -> getLevels();
            case "updatePartnerProduct" -> productComponent.updateProduct(joinPoint.getArgs());
            case "addRequest" -> RequestComponent.addRequest(joinPoint.getArgs());
            case "getProducts" -> productComponent.getProducts(joinPoint.getArgs());
            case "getProductById" -> {
                var product = productComponent.getProduct(joinPoint.getArgs());
                if (product == null) yield ResponseEntity.notFound().build();
                TimeUtils.handleSpecialNames(product.getData().getName());
                yield ResponseEntity.ok(product);
            }
            case "getOrders" -> orderComponent.getOrders(joinPoint.getArgs());
            case "getOrdersById" -> {
                var orders = orderComponent.getOrder(joinPoint.getArgs());
                if(orders.isEmpty()) yield ResponseEntity.notFound().build();
                yield ResponseEntity.ok(orders);
            }
            case "addOrder" -> orderComponent.addOrder(joinPoint.getArgs());
            default -> joinPoint.proceed();
        };
    }
}
