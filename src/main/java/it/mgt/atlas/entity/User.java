package it.mgt.atlas.entity;

import it.mgt.util.spring.auth.AuthRole;
import it.mgt.util.spring.auth.AuthUser;
import java.io.Serializable;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "\"User\"",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User implements AuthUser, Serializable {

    // Fields

    @Id
    @GeneratedValue
    protected Long id;
    @Column(nullable = false, unique = true)
    protected String username;
    @Column(nullable = false)
    protected String password;
    @Column(nullable = false, unique = true)
    protected String email;
    protected String firstName;
    protected String lastName;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date registrationDate;
    @ManyToMany
    private Set<Role> roles = new LinkedHashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    protected Set<Session> sessions = new LinkedHashSet<>();

    // Constructors

    public User() { }

    public User(String username, String password, String email, String firstName, String lastName, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
        this.registrationDate = new Date();
    }

    // Methods

    @Override
    public Set<AuthRole> authRoles() {
        return getRoles().stream()
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> authOperations() {
        return getRoles().stream()
                .map(Role::getOperations)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
    
    
    // Accessors

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }

    // Repo methods

}