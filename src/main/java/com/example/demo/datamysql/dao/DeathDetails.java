package com.example.demo.datamysql.dao;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "fam_death_details")
@SequenceGenerator(name="dseq", initialValue=11, allocationSize=1)
public class DeathDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dseq")
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "cause_death")
    private String causeDeath;

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "death_date")
    private java.util.Date deathDate;
}
