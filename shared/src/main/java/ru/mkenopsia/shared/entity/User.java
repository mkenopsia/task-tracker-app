package ru.mkenopsia.shared.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(schema = "user_management", name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "c_username")
    private String username;

    @Column(name = "c_email")
    private String email;

    @Column(name = "c_password")
    private String password;
}
