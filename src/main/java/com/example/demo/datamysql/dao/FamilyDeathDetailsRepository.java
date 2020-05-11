package com.example.demo.datamysql.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface FamilyDeathDetailsRepository extends CrudRepository<DeathDetails, Integer> {
    DeathDetails findByMemberId(Integer member_Id);

    @Modifying
    @Query("update DeathDetails d set d.deathDate=:deathDate where d.id=:id")
    void updateDeathEventDate(@Param("id") Integer id, @Param("deathDate") Date deathDate);

    @Modifying
    @Query("update DeathDetails d set d.causeDeath=:causeDeath where d.id=:id")
    void updateDeathEventCause(@Param("id") Integer id, @Param("causeDeath") String causeDeath);
}
