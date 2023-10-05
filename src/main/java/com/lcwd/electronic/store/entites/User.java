package com.lcwd.electronic.store.entites;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.net.PasswordAuthentication;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @Column(name = "user_id")
    private String userId;
    @Column(name = "user_Name")
    private String name;
    @Column(unique = true,nullable = false,length = 50)
    private String email;
    @Column(length = 500)
    private String password;
    @Column(length = 1000)
    private String about;

    @Column(name = "user_image_name")
    private String imageName;

    private String gender;

    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY  , cascade = CascadeType.REMOVE)
    private List<Order> orders = new ArrayList<>();

    @ManyToMany(fetch=FetchType.EAGER , cascade = CascadeType.ALL  )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.REMOVE  , mappedBy = "user")
    private  Cart cart;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> auth = this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
        return auth;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public  String getPassword(){
        return this.password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
