package ru.s3v3nny.endpointwithsql.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.s3v3nny.endpointwithsql.dto.Message;
import ru.s3v3nny.endpointwithsql.dto.Response;
import ru.s3v3nny.endpointwithsql.dto.ServiceDto;
import ru.s3v3nny.endpointwithsql.repositories.TokenRepository;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class EndpointService {

    private final TokenRepository repository;

    public Response<Message, Error> checkServiceStatus() {
        Message msg = new Message(new ArrayList<ServiceDto>());
        ServiceDto serviceDto = new ServiceDto("endpoint", "active");
        msg.services().add(serviceDto);
        try {
            repository.findAll();
            serviceDto = new ServiceDto("sql_database", "active");
            msg.services().add(serviceDto);
        } catch (Exception e) {
            serviceDto = new ServiceDto("sql_database", "inactive");
            msg.services().add(serviceDto);
        }

        return new Response(msg, null);
    }
}
