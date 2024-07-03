package ru.s3v3nny.endpointwithsql.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.s3v3nny.endpointwithsql.dto.CrudStatus;
import ru.s3v3nny.endpointwithsql.entities.Token;
import ru.s3v3nny.endpointwithsql.repositories.TokenRepository;

@Service
@RequiredArgsConstructor
public class DatabaseService {

    private final TokenRepository repository;

    public CrudStatus checkCRUDAvailability () {
        CrudStatus status = new CrudStatus(true, true, true, true);
        Token token = new Token();
        token.setValue("test value");

        repository.save(token);
        Token foundToken = repository.findByValue("test value");
        if (foundToken == null) {
            status.setCreate(false);
            status.setRead(false);
            status.setUpdate(false);
            status.setDelete(false);
            return status;
        }
        foundToken.setValue("test value1");
        repository.save(foundToken);
        foundToken = repository.findByValue("test value1");
        if (foundToken == null) {
            status.setUpdate(false);
            foundToken.setValue("test value");
            repository.delete(foundToken);
            if (repository.findByValue("test value") != null) {
                status.setDelete(false);
            }
            return status;
        } else {
            repository.delete(foundToken);
            if (repository.findByValue("test value1") != null) {
                status.setDelete(false);
            }
            return status;
        }
    }
}
