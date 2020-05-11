package com.example.demo.datamysql.dao;

import com.example.demo.datamysql.dao.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
}
