package com.mhupfauer.votej.persistence.Entity;

import com.mhupfauer.votej.security.ROLES;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "User")
@Table(name = "USERS")
@NoArgsConstructor
@Getter
@Setter
public class UserEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Long dateofbirthtimestamp;
    private String phonenumber;
    private String pass;
    private ROLES role;
}
