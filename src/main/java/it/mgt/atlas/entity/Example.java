package it.mgt.atlas.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;
import java.util.Date;
import it.mgt.atlas.repository.ExampleRepo;
import java.io.Serializable;

@Entity
@Table(indexes = {
        @Index(columnList = "code", unique = true)
})
@Configurable
@NamedQueries({
    @NamedQuery(name = "Example.findAll", query = "SELECT e FROM Example"),
    @NamedQuery(name = "Example.findByCode", query = "SELECT e FROM Example WHERE e.code = :code"),
    @NamedQuery(name = "Example.countAll", query = "SELECT COUNT(e) FROM Example"),
})
public class Example implements Serializable {

    // Fields

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date entryDate;

    // Constructors

    public Example() { }

    public Example(String code, Date entryDate) {
        this.code = code;
        this.entryDate = entryDate;
    }

    
    // Methods


    // Accessors

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

}
