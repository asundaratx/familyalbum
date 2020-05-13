package com.example.demo.datamysql.dao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface FamilyMemberRepository extends CrudRepository<FamilyMember, Integer>{
    List<FamilyMember> findByFirstname(String firstname);
    List<FamilyMember> findByLastname(String lastname);
    List<FamilyMember> findByFirstnameAndLastname(String firstname, String lastname);

    @Modifying
    @Query("UPDATE FamilyMember f set f.firstname= :firstname, f.lastname = :lastname, f.occupation= :occupation, f.domicile= :domicile,f.birthDate=:date where f.id=:id")
    void updateGivenFamilyMember(@Param("firstname") String firstname, @Param("lastname") String lastname,
                                 @Param("occupation") String occupation, @Param("domicile") String domicile,
                                 @Param("date") Date date, @Param("id") Integer id);

    @Modifying
    @Query("UPDATE FamilyMember f set f.fatherId= :fatherid where f.id=:id")
    void updateFather(@Param("fatherid") Integer fatherid, @Param("id") Integer id);

    @Modifying
    @Query("UPDATE FamilyMember f set f.motherId= :motherid where f.id=:id")
    void updateMother(@Param("motherid") Integer motherid, @Param("id") Integer id);

    @Modifying
    @Query("UPDATE FamilyMember f set f.isMarried= :isMarried where f.id=:id")
    void updateMarriageStatus(@Param("isMarried") Boolean isMarried, @Param("id") Integer id);

    @Modifying
    @Query("UPDATE FamilyMember f set f.isAlive= :isAlive where f.id=:id")
    void updateDeathStatus(@Param("isAlive") Boolean isAlive, @Param("id") Integer id);

    List<FamilyMemberShort> findAllFamilyMemberShortProjectedBy();
}
