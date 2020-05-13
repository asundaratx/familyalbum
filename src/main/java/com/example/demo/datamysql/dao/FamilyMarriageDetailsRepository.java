package com.example.demo.datamysql.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface FamilyMarriageDetailsRepository extends CrudRepository<MarriedCouple, Integer> {
    MarriedCouple findBySpouse1IdOrSpouse2Id(Integer spouse1_id, Integer spouse2_id);

    @Modifying
    @Query("delete from MarriedCouple d where d.spouse1Id=:spouse1Id or d.spouse2Id=:spouse2Id")
    void deleteCouple(@Param("spouse1Id") Integer spouse1Id, @Param("spouse2Id") Integer spouse2Id);

    @Modifying
    @Query("update MarriedCouple d set d.marriageDate=:marriageDate where d.id=:id")
    void updateCoupleEventDate(@Param("id") Integer id, @Param("marriageDate") Date marriageDate);

    @Modifying
    @Query("update MarriedCouple d set d.marriageLocation=:location where d.id=:id")
    void updateCoupleEventLocation(@Param("id") Integer id, @Param("location") String location);

}
