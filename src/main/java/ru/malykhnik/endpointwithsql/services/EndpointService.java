package ru.malykhnik.endpointwithsql.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.malykhnik.endpointwithsql.dto.*;
import ru.malykhnik.endpointwithsql.dto.Error;
import ru.malykhnik.endpointwithsql.entities.Token;
import ru.malykhnik.endpointwithsql.repositories.TokenRepository;
import ru.malykhnik.endpointwithsql.util.TokenUtil;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EndpointService {
    private final Logger LOGGER = LoggerFactory.getLogger(EndpointService.class);

    private final TokenRepository repository;
    private final TokenUtil util;
    private final DatabaseService databaseService;

    @Value("${auth.login}")
    String login;
    @Value("${auth.password_hash}")
    String passwordHash;

    private boolean database_online;
    private String cachedToken;


    public Response<Message, Error> checkServiceStatus(String token) {
        if(!database_online && token.equals(cachedToken)) {
            ArrayList<ServiceDto> serviceDtos = new ArrayList<ServiceDto>();
            ServiceDto serviceDto = new ServiceDto("endpoint", "active", null);
            serviceDtos.add(serviceDto);
            serviceDto = new ServiceDto("sql_database", "inactive", null);
            String newToken = util.generateToken();
            cachedToken = newToken;
            serviceDtos.add(serviceDto);
            var msg = new Message(newToken, serviceDtos);
            LOGGER.info("ЗАШЛО В if" + msg);
            return new Response(msg, null);
        } else if (!token.equals(cachedToken) && cachedToken != null) {
            var err = new Error("Wrong token");
            LOGGER.error("Wrong token_1");
            return new Response<>(null, err);
        }
        if (!validateToken(token)) {
            var err = new Error("Wrong token");
            LOGGER.error("Wrong token_2");
            return new Response<>(null, err);
        }

        ArrayList<ServiceDto> serviceDtos = new ArrayList<ServiceDto>();
        ServiceDto serviceDto = new ServiceDto("endpoint", "active", null);
        serviceDtos.add(serviceDto);
        serviceDto = new ServiceDto("sql_database", "active", databaseService.checkCRUDAvailability());
        serviceDtos.add(serviceDto);

        String newToken = util.generateToken();
        Token tokenObj = repository.findAll().get(0);
        tokenObj.setValue(newToken);
        repository.save(tokenObj);

        var msg = new Message(newToken, serviceDtos);

        LOGGER.info(String.valueOf(msg));
        return new Response(msg, null);
    }

    public boolean validateToken(String token) {
        return repository.findAll().get(0).getValue().equals(token);
    }

    public Response getToken(AuthData auth) {
        if (!validateAuth(auth)) {
            var err = new Error("Wrong auth data");
            return new Response<>(null, err);
        }
        String newToken = util.generateToken();
        LOGGER.info("New Token: " + newToken);
        try {
            List<Token> tokenList = repository.findAll();
            if (tokenList.isEmpty()) {
                Token token = Token.builder().value(newToken).build();
                repository.save(token);
                LOGGER.info("List is Empty. Token saved.");
            } else {
                Token token = tokenList.get(0);
                repository.delete(token);
                token.setValue(newToken);
                repository.save(token);
                LOGGER.info("List is not Empty. Token saved." + token);
            }
            database_online = true;
        } catch (Exception e) {
            LOGGER.error("Зашло в catch");
            database_online = false;
            cachedToken = newToken;
        }
        var tokenDto = new TokenData(newToken);
        LOGGER.info(tokenDto.toString());
        return new Response<>(tokenDto, null);
    }

    public boolean validateAuth(AuthData auth) {
        return auth.login().equals(this.login) && auth.password_hash().equals(this.passwordHash);
    }
}
