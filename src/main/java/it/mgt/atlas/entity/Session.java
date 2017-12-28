package it.mgt.atlas.entity;

import it.mgt.util.spring.auth.AuthSession;
import it.mgt.util.spring.auth.AuthUser;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "\"Session\"",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "token")
        })
@XmlRootElement
@Configurable
public class Session implements AuthSession, Serializable {

    // Fields

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    private User user;
    @Column(nullable = false)
    private String token;
    private String ip;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    // Constructors

    public Session() { }

    public Session(User user, String token) {
        this.user = user;
        this.token = token;
        this.startDate = new Date();
    }

    public Session(User user, String token, String ip) {
        this(user, token);
        this.ip = ip;
    }

    public Session(User user, String token, String ip, Date endDate) {
        this(user, token);
        this.ip = ip;
        this.endDate = endDate;
    }

    // Methods

    @Override
    public AuthUser authUser() {
        return user;
    }

    @Override
    public int getExpirySeconds() {
        return (int) Math.min((endDate.getTime() - new Date().getTime()) / 1000, Integer.MAX_VALUE);
    }

    
    // Accessors

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}