package com.example.demo.service;

import com.example.demo.datamysql.dao.FamilyMember;
import com.example.demo.datamysql.dao.FamilyMemberRepository;
import com.example.demo.datamysql.dao.FamilyMemberShort;
import com.example.demo.datamysql.dao.MarriedCouple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyMemberService {
    //TODO Make service throw error on invalid parameters instead of just logging info
    private final FamilyMemberRepository familyMemberRepository;

    public Iterable<FamilyMember> findAllFamilyMembers(){
        return familyMemberRepository.findAll();
    }

    public List<FamilyMember> getMemberByFullName(String fullname){
        if(fullname == null || fullname.isEmpty() || fullname.split(" ").length!=2) {
            log.info("Did not receive a full name with space separated first and last names.");
            throw new InvalidParameterException("Did not receive a full name with space separated first and last names.");
        }
        String[] names = fullname.split(" ");
        return familyMemberRepository.findByFirstnameAndLastname(names[0], names[1]);
    }

    public List<FamilyMember> getMemberByFirstnameAndLastname(String firstname, String lastname){
        if(firstname == null || lastname==null || firstname.isEmpty() || lastname.isEmpty()) {
            log.info("Did not receive a valid first and last name.");
            throw new InvalidParameterException("Did not receive a valid first and last name.");
        }
        return familyMemberRepository.findByFirstnameAndLastname(firstname, lastname);
    }

    public List<FamilyMember> getMemberByFirstName(String fullname){
        if(fullname == null || fullname.isEmpty() || fullname.split(" ").length<1) {
            log.info("Did not receive a first name.");
            throw new InvalidParameterException("Did not receive a valid first name.");
        }
        String[] names = fullname.split(" ");
        return familyMemberRepository.findByFirstname(names[0]);
    }

    public void deleteByMemberId(Integer memberId){
        if(memberId == null){
            log.error("Invalid parameters to delete famly member.");
            throw new InvalidParameterException("Invalid parameters to delete famly member.");
        }
        familyMemberRepository.deleteById(memberId);
        return;
    }

    public void createLivingFamilyMember(FamilyMember familyMember){
        if(familyMember == null || familyMember.getFirstname().equals(null) || familyMember.getLastname().equals(null)
                || familyMember.getFirstname().isEmpty() || familyMember.getLastname().isEmpty()){
            log.info("Invalid family member to create.");
            throw new InvalidParameterException("Did not receive a valid familymember with first and last name.");
        }
        familyMember.setIsAlive(Boolean.TRUE);
        familyMemberRepository.save(familyMember);
        return;
    }

    public FamilyMember findById(Integer memberId){
        if(memberId == null){
            log.info("Invalid parameters to find famly member.");
            throw new InvalidParameterException("Invalid parameters to find famly member.");
        }
        Optional<FamilyMember> familyMember = familyMemberRepository.findById(memberId);
        if( familyMember. isPresent()) return familyMember.get();
        else return null;
    }

    public void updateFather(Integer fatherId, Integer memberId){
        if (fatherId == null || memberId==null){
            log.info("Invalid parameters to update father on member.");
            throw new InvalidParameterException("Invalid parameters to update father on member.");
        }
        familyMemberRepository.updateFather(fatherId, memberId);
        return;
    }

    public void updateMother(Integer motherId, Integer memberId){
        if (motherId == null || memberId==null){
            log.info("Invalid parameters to update mother on member.");
            throw new InvalidParameterException("Invalid parameters to update mother on member.");
        }
        familyMemberRepository.updateMother(motherId, memberId);
        return;
    }

    public void updateIsAliveStatus(Boolean isAlive, Integer memberId){
        if (isAlive == null || memberId==null){
            log.info("Invalid parameters to update death status on member.");
            throw new InvalidParameterException("Invalid parameters to update death status on member.");
        }
        familyMemberRepository.updateDeathStatus(isAlive, memberId);
        return;
    }

    public void updateMarriageStatus(Boolean isMarried, Integer memberId){
        if (isMarried == null || memberId==null){
            log.info("Invalid parameters to update marriage status on member.");
            throw new InvalidParameterException("Invalid parameters to update marriage status on member.");
        }
        familyMemberRepository.updateMarriageStatus(isMarried, memberId);
        return;
    }

    public List<FamilyMemberShort> findAllFamilyMemberShortProjectedBy(){
        return familyMemberRepository.findAllFamilyMemberShortProjectedBy();
    }

    public void updateGivenFamilyMember(String firstname, String lastname, String occupation, String domicile, Date date, Integer id){
        if(id==null){
            log.info("Invalid parameter to update family member");
            throw new InvalidParameterException("Invalid parameters to update family member.");
        }
        familyMemberRepository.updateGivenFamilyMember(firstname, lastname,occupation, domicile, date, id);
        return;
    }

}
