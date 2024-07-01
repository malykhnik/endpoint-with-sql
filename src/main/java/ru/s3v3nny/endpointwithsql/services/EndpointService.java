package ru.s3v3nny.endpointwithsql.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.s3v3nny.endpointwithsql.dto.*;
import ru.s3v3nny.endpointwithsql.dto.Error;
import ru.s3v3nny.endpointwithsql.entities.Token;
import ru.s3v3nny.endpointwithsql.repositories.TokenRepository;
import ru.s3v3nny.endpointwithsql.util.TokenUtil;


import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class EndpointService {

    private final TokenRepository repository;
    private final TokenUtil util;

    @Value("${auth.login}")
    String login;
    @Value("${auth.password_hash}")
    String passwordHash;

    public Response<Message, Error> checkServiceStatus(String token) {
        if (!validateToken(token)) {
            var err = new Error("Wrong token");
            return new Response<>(null, err);
        }
        ArrayList<ServiceDto> serviceDtos = new ArrayList<ServiceDto>();
        ServiceDto serviceDto = new ServiceDto("endpoint", "active");
        serviceDtos.add(serviceDto);
        try {
            repository.findAll();
            serviceDto = new ServiceDto("sql_database", "active");
            serviceDtos.add(serviceDto);
        } catch (Exception e) {
            serviceDto = new ServiceDto("sql_database", "inactive");
            serviceDtos.add(serviceDto);
        }
        String newToken = util.generateToken();
        try {
            Token tokenObj = repository.findAll().get(0);
            tokenObj.setValue(newToken);
            repository.save(tokenObj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        var msg = new Message(newToken, serviceDtos);
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
        try {
            Token token = repository.findAll().get(0);
            token.setValue(newToken);
            repository.save(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        var tokenDto = new TokenData(newToken);
        return new Response<>(tokenDto, null);
    }

    public boolean validateAuth(AuthData auth) {
        return auth.login().equals(this.login) && auth.password_hash().equals(this.passwordHash);
    }
}
