package com.example.demo.datamysql.dao;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

@Entity
@Table(name = "fam_member")
@Data
@SequenceGenerator(name="mseq", initialValue=11, allocationSize=1)
public class FamilyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mseq")
    private Integer id;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "domicile")
    private String domicile;

    @Basic
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private java.util.Date birthDate;

    @Column(name = "father_id")
    private Integer fatherId;

    @Column(name = "mother_id")
    private Integer motherId;

    @Column(name = "is_married", columnDefinition = "BOOLEAN")
    private Boolean isMarried;

    @Column(name = "is_alive", columnDefinition = "BOOLEAN")
    private Boolean isAlive;
}
