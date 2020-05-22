package com.example.demo.service;

import com.example.demo.datamysql.dao.DeathDetails;
import com.example.demo.datamysql.dao.Event;
import com.example.demo.datamysql.dao.FamilyMember;
import com.example.demo.datamysql.dao.MarriedCouple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberDetailsService {
    private final FamilyMarriageDetailsService marriageDetailsService;
    private final FamilyMemberService familyMemberService;
    private final FamilyDeathDetailsService familyDeathDetailsService;

    public void createMarriedCouple(Integer spouse1Id, Integer spouse2Id, Event marriageEvent){
        MarriedCouple details = new MarriedCouple();
        details.setSpouse1Id(spouse1Id);
        details.setSpouse2Id(spouse2Id);
        if (marriageEvent!=null) {
            if (marriageEvent.getEventDate()!=null) {
                details.setMarriageDate(marriageEvent.getEventDate());
            }
            if (marriageEvent.getEventDetails().get("Location")!=null) {
                details.setMarriageLocation(marriageEvent.getEventDetails().get("Location"));
            }
        }
        familyMemberService.updateMarriageStatus(Boolean.TRUE, spouse1Id);
        familyMemberService.updateMarriageStatus(Boolean.TRUE, spouse2Id);
        marriageDetailsService.save(details);
    }

    public void createDeathDetails(Integer memberId, Event deathEvent){
        familyMemberService.updateIsAliveStatus(Boolean.FALSE, memberId);
        DeathDetails deathDetails = new DeathDetails();
        deathDetails.setCauseDeath(deathEvent.getEventDetails().get("Cause_death"));
        deathDetails.setDeathDate(deathEvent.getEventDate());
        deathDetails.setMemberId(memberId);
        familyDeathDetailsService.save(deathDetails);
    }

    private Integer getSpouseId(Integer memberId,MarriedCouple marriedCouple){
        if(marriedCouple==null || memberId == null){
            log.info("Invalid parameters to get spouseId");
            throw new InvalidParameterException("Invalid parameters to get spouseId");
        }
        if (memberId.equals(marriedCouple.getSpouse1Id())) {
            return (marriedCouple.getSpouse2Id());
        } else if (memberId.equals(marriedCouple.getSpouse2Id())) {
            return (marriedCouple.getSpouse1Id());
        }
        return null;
    }

    public FamilyMember getSpouseOfMember(Integer memberId, MarriedCouple marriedCouple){
        FamilyMember familyMemberSpouse = null;
        if (memberId == null || marriedCouple==null) {
            log.info("Invalid parameter to get member spouse details");
            throw new InvalidParameterException("Invalid parameters to get member spouse details");
        }
        Integer spouseId = getSpouseId(memberId, marriedCouple);
        if (spouseId!=null) {
            familyMemberSpouse = familyMemberService.findById(spouseId);
        }
        return familyMemberSpouse;
    }

    public void deleteMember(Integer memberId){
        if(memberId == null){
            log.info("Invalid parameter to delete member ");
            throw new InvalidParameterException("Invalid parameter to delete member");
        }
        MarriedCouple marriedCouple = marriageDetailsService.findBySpouse1IdOrSpouse2Id(memberId,memberId);
        if (marriedCouple!=null) {
            Integer spouseId = getSpouseId(memberId, marriedCouple);
            marriageDetailsService.deleteCouple(memberId, memberId);
            familyMemberService.updateMarriageStatus(Boolean.FALSE, spouseId);
        }
        familyDeathDetailsService.deleteByMemberId(memberId);
        familyMemberService.deleteByMemberId(memberId);
    }
}
