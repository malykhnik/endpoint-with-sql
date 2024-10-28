package ru.malykhnik.endpointwithsql.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.malykhnik.endpointwithsql.dto.AuthData;
import ru.malykhnik.endpointwithsql.services.EndpointService;
import ru.malykhnik.endpointwithsql.dto.Response;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EndpointController {
    private final Logger LOGGER = LoggerFactory.getLogger(EndpointController.class);
    private final EndpointService service;

    @GetMapping("/check-status")
    public ResponseEntity checkServiceStatus (@RequestHeader("token") String token) {
        LOGGER.info("ВЫЗВАН контроллен /check-status");
        Response response = service.checkServiceStatus(token);
        LOGGER.info("RESPONSE:"  + response.toString());
        return response.getError() == null ? ResponseEntity.ok().body(response.getValue())
                : ResponseEntity.badRequest().body(response.getError());
    }

    @PostMapping("/get-token")
    public ResponseEntity getToken (@RequestBody AuthData authData) {
        LOGGER.info("ВЫЗВАН контроллен /get-token");
        Response response = service.getToken(authData);
        LOGGER.info("RESPONSE:"  + response.toString());
        return response.getError() == null ? ResponseEntity.ok().body(response.getValue())
                : ResponseEntity.badRequest().body(response.getError());
    }
}
