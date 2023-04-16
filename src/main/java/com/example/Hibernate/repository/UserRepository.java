package com.example.Hibernate.repository;

import com.example.Hibernate.config.CrudRepository;
import com.example.Hibernate.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends CrudRepository<User, Long>{


}
