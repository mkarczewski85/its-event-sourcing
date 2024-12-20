package com.karczewski.its.user.repository;

import com.karczewski.its.user.entity.UserAccount;
import com.karczewski.its.user.entity.UserAccountSettings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountSettingsRepository extends CrudRepository<UserAccountSettings, Long>  {

    UserAccountSettings findByUserAccount(UserAccount userAccount);

}
