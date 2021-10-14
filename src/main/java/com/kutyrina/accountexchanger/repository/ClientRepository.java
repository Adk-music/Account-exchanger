package com.kutyrina.accountexchanger.repository;

import com.kutyrina.accountexchanger.entity.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {

    Optional<Client> findByLogin(String login);
}
