package com.yota.tariffsrvc.repository.api;

import com.yota.tariffsrvc.model.entity.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Chesnokov Sergei
 */
public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByPhoneNumber(String phoneNumber);
}
