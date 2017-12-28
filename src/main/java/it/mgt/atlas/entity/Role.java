package it.mgt.atlas.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.mgt.util.spring.auth.AuthRole;
import java.io.Serializable;

@Entity
public class Role implements AuthRole, Serializable {

    public static final String ROOT_ROLE_NAME = "ROOT";

    // Fields

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int level = 0;
    @ElementCollection
    private Set<String> operations = new LinkedHashSet<>();
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new LinkedHashSet<>();


    // Constructors

    public Role() {
    }

    public Role(String name, Set<String> operations, int level) {
        this.operations = operations;
        this.name = name;
        this.level = level;
    }


    // Methods

    @Override
    public Set<String> authOperations() {
        return getOperations();
    }
    

    // Accessors

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getOperations() {
        return operations;
    }

    public void setOperations(Set<String> operations) {
        this.operations = operations;
    }

    @JsonIgnore
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}