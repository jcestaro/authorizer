package com.github.jcestaro.authorizer.domain.repository;

import com.github.jcestaro.authorizer.domain.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
