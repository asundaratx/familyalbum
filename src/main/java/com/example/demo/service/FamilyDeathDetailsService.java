package com.example.demo.service;

import com.example.demo.datamysql.dao.DeathDetails;
import com.example.demo.datamysql.dao.FamilyDeathDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyDeathDetailsService {
    private final FamilyDeathDetailsRepository familyDeathDetailsRepository;
    public DeathDetails findByMemberId(Integer memberId) {
        if(memberId == null){
            log.info("Invalid parameter to get death details of member.");
            return null;
        }
        return familyDeathDetailsRepository.findByMemberId(memberId);
    }

    public void updateDeathEventDate(Integer id, Date deathDate){
        if(id==null || deathDate==null) {
            log.info("Invalid parameters to update date of death.");
            return;
        }
        familyDeathDetailsRepository.updateDeathEventDate(id, deathDate);
        return;
    }

    public void updateDeathEventCause(Integer id, String cause){
        if(id==null || cause==null) {
            log.info("Invalid parameters to update cause of death.");
            return;
        }
        familyDeathDetailsRepository.updateDeathEventCause(id, cause);
        return;
    }

    public void save(DeathDetails deathDetails){
        if(deathDetails == null || deathDetails.getMemberId()==null){
            log.info("Invalid parameters to save death of member.");
            return;
        }
        familyDeathDetailsRepository.save(deathDetails);
        return;
    }

    public void deleteByMemberId(Integer memberId){
        if(memberId == null){
            log.error("Invalid parameter to get death details of member.");
            throw new InvalidParameterException("Invalid parameter to get death details of member.");
        }
        familyDeathDetailsRepository.deleteByMemberId(memberId);
        return;
    }
}
