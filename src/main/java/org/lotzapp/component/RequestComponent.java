package org.lotzapp.component;

import org.lotzapp.adminapi.model.*;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.lotzapp.component.InstanceComponent.createInstance;
import static org.lotzapp.component.PermissionComponent.getPermissions;

public class RequestComponent {
    private static final List<RequestBase> RECEIVED_REQUESTS = new ArrayList<>();
    private static final List<Request> CREATED_REQUESTS = new ArrayList<>();

    static {
        RECEIVED_REQUESTS.add(new RequestBase(
                UUID.randomUUID(),
                createInstance("ReceivedInstance01"),
                getPermissions().getFirst(),
                null,
                null,
                OffsetDateTime.now()
        ));

        CREATED_REQUESTS.add(new Request(
                UUID.randomUUID(),
                createInstance("Instance01"),
                getPermissions().getFirst(),
                null,
                null,
                OffsetDateTime.now(),
                "Token",
                "ApiKey"
        ));
    }

    public static ResponseEntity<GetRequestsResponse> getRequestResponse() {
        var response = new GetRequestsResponse();
        response.setRequestsCreated(CREATED_REQUESTS);
        response.setRequestsReceived(RECEIVED_REQUESTS);
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<RequestBase> cancelRequest(Object[] args) {
        var uuid = (UUID) args[0];
        var requestToCancel = CREATED_REQUESTS.stream().filter(
                r -> r.getId().equals(uuid))
                .findFirst();
        if(requestToCancel.isPresent()) {
            var req = requestToCancel.get();
            //CREATED_REQUESTS.remove(req);
            return ResponseEntity.ok(new RequestBase(
                    req.getId(),
                    req.getPartnerInstance(),
                    req.getPermission(),
                    req.getAcceptedAt().orElse(null),
                    req.getCancelledAt().orElse(null),
                    req.getCreatedAt()
            ));
        }

        var receivedRequestToCancel = RECEIVED_REQUESTS.stream().filter(
                        r -> r.getId().equals(uuid))
                .findFirst();
        if(receivedRequestToCancel.isPresent()) {
            var req = receivedRequestToCancel.get();
            //RECEIVED_REQUESTS.remove(req);
            return ResponseEntity.ok(req);
        }

        return ResponseEntity.notFound().build();
    }

    public static ResponseEntity<Request> acceptRequest(Object[] args) {
        var uuid = (UUID) args[0];
        var broker = (String) args[1];
        var acceptBody = (BodyAcceptRequest) args[2];

        return ResponseEntity.ok(new Request(
                UUID.randomUUID(),
                createInstance(broker),
                getPermissions().getFirst(),
                OffsetDateTime.now(),
                null,
                OffsetDateTime.now(),
                acceptBody.getToken(),
                "ApiKey"
        ));
    }

    public static ResponseEntity<Request> addRequest(Object[] args) {
        var broker = (String) args[0];
        var body = (BodyAddRequest) args[1];

        var request = new Request(
          UUID.randomUUID(),
          createInstance(body.getDomain()),
          getPermissions().getFirst(),
          null,
          null,
          OffsetDateTime.now(),
          "Token",
          "ApiKey"
        );
        CREATED_REQUESTS.add(request);

        return ResponseEntity.created(URI.create("xx")).body(request);
    }
}
