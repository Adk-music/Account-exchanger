package com.kutyrina.accountexchanger.repository;

import com.kutyrina.accountexchanger.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {


}
