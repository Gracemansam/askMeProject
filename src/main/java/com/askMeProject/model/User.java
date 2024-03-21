package com.askMeProject.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "App_users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {

    private String firstName;
    private String lastName;
    @NaturalId(mutable = true)
    @Column(name = "EMAIL", nullable = false)
    private String email;
    private String phoneNumber;
    private String password;
    private String username;
    private String speciality;
    private String qualification;
//    @ManyToMany(fetch=FetchType.EAGER)
//    @JoinTable(
//            name="user_role_junction",
//            joinColumns = {@JoinColumn(name="user_id")},
//            inverseJoinColumns = {@JoinColumn(name="role_id")}
//    )
    private String authorities;


    private boolean isEnabled = false;
    private boolean mfaEnabled;
    private String secret;





//    public User() {
//            super();
//            authorities = new HashSet<>();
//    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return this.authorities;
//    }


}
