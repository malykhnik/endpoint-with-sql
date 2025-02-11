package ru.malykhnik.endpointwithsql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.malykhnik.endpointwithsql.entities.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByValue(String value);
}
