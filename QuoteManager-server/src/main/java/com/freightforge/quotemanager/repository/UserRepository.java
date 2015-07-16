package com.freightforge.quotemanager.repository;

import com.freightforge.quotemanager.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findOneByUsername(String username);
}
