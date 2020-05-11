package com.example.demo.datamysql.dao;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "fam_marriage_details")
@SequenceGenerator(name="seq", initialValue=11, allocationSize=1)
public class MarriedCouple {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Integer id;

    @Column(name = "spouse1_id")
    private Integer spouse1Id;

    @Column(name = "spouse2_id")
    private Integer spouse2Id;

    @Basic
    @Temporal(TemporalType.DATE)
    @Column(name = "marriage_date")
    private java.util.Date marriageDate;

    @Column(name = "marriage_location")
    private String marriageLocation;
}
