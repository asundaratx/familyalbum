package com.example.demo.datamysql.dao;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_details")
@Data
public class User {
    @Id
    private Integer id;

    @Column(name = "email_id")
    private String email;
}
