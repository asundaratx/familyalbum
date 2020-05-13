package com.example.demo.service;

import com.example.demo.datamysql.dao.FamilyMember;
import com.example.demo.datamysql.dao.FamilyMemberRepository;
import com.example.demo.datamysql.dao.FamilyMemberShort;
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
        if(fullname == null || fullname.split(" ").length!=2) {
            log.info("Did not receive a full name with space separated first and last names.");
            return Collections.emptyList();
        }
        String[] names = fullname.split(" ");
        return familyMemberRepository.findByFirstnameAndLastname(names[0], names[1]);
    }

    public List<FamilyMember> getMemberByFirstnameAndLastname(String firstname, String lastname){
        if(firstname == null || lastname==null) {
            log.info("Did not receive a valid first and last name.");
            return Collections.emptyList();
        }
        return familyMemberRepository.findByFirstnameAndLastname(firstname, lastname);
    }

    public List<FamilyMember> getMemberByFirstName(String fullname){
        if(fullname == null || fullname.split(" ").length<1) {
            log.info("Did not receive a first name.");
            return Collections.emptyList();
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

    public void save(FamilyMember familyMember){
        if(familyMember == null){
            log.info("Invalid family member to create.");
            return;
        }
        familyMember.setIsAlive(Boolean.TRUE);
        familyMemberRepository.save(familyMember);
        return;
    }

    public FamilyMember findById(Integer memberId){
        if(memberId == null){
            log.info("Invalid parameters to find famly member.");
            return null;
        }
        Optional<FamilyMember> familyMember = familyMemberRepository.findById(memberId);
        if( familyMember. isPresent()) return familyMember.get();
        else return null;
    }

    public void updateFather(Integer fatherId, Integer memberId){
        if (fatherId == null || memberId==null){
            log.info("Invalid parameters to update father on member.");
            return;
        }
        familyMemberRepository.updateFather(fatherId, memberId);
        return;
    }

    public void updateMother(Integer motherId, Integer memberId){
        if (motherId == null || memberId==null){
            log.info("Invalid parameters to update mother on member.");
            return;
        }
        familyMemberRepository.updateMother(motherId, memberId);
        return;
    }

    public void updateIsAliveStatus(Boolean isAlive, Integer memberId){
        if (isAlive == null || memberId==null){
            log.info("Invalid parameters to update death status on member.");
            return;
        }
        familyMemberRepository.updateDeathStatus(isAlive, memberId);
        return;
    }

    public void updateMarriageStatus(Boolean isMarried, Integer memberId){
        if (isMarried == null || memberId==null){
            log.info("Invalid parameters to update death status on member.");
            return;
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
            return;
        }
        familyMemberRepository.updateGivenFamilyMember(firstname, lastname,occupation, domicile, date, id);
        return;
    }

}
