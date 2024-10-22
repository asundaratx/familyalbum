package com.example.demo.service;

import com.example.demo.datamysql.dao.Event;
import com.example.demo.datamysql.dao.FamilyMarriageDetailsRepository;
import com.example.demo.datamysql.dao.FamilyMember;
import com.example.demo.datamysql.dao.MarriedCouple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyMarriageDetailsService {
    private final FamilyMarriageDetailsRepository familyMarriageDetailsRepository;
    public MarriedCouple findBySpouse1IdOrSpouse2Id(Integer spouse1Id, Integer spouse2Id){
        if ((spouse1Id == null) || (spouse2Id==null)) {
            log.info("Invalid parameters to lookup marriage details");
            throw new InvalidParameterException("Invalid parameters to lookup marriage details");
        }
        return familyMarriageDetailsRepository.findBySpouse1IdOrSpouse2Id(spouse1Id, spouse2Id);
    }

    public void deleteCouple(Integer spouse1Id, Integer spouse2Id) {
        if ((spouse1Id == null) || (spouse2Id==null)) {
            log.error("Invalid parameters to delete marriage details");
            throw new InvalidParameterException("Invalid parameters to delete marriage details");
        }
        familyMarriageDetailsRepository.deleteCouple(spouse1Id, spouse2Id);
        return;
    }

    public void updateCoupleEventDate(Integer id, Date marriageDate) {
        if ((id == null) || (marriageDate==null)) {
            log.info("Invalid parameters to update marriage details");
            throw new InvalidParameterException("Invalid parameters to update marriage details");
        }
        familyMarriageDetailsRepository.updateCoupleEventDate(id, marriageDate);
        return;
    }

    public void updateCoupleEventLocation(Integer id, String marriageLocation) {
        if ((id == null) || (marriageLocation==null)) {
            log.info("Invalid parameters to update marriage details");
            throw new InvalidParameterException("Invalid parameters to delete marriage details");
        }
        familyMarriageDetailsRepository.updateCoupleEventLocation(id, marriageLocation);
        return;
    }

    public void save(MarriedCouple marriedCouple){
        if(marriedCouple==null || marriedCouple.getSpouse1Id()==null || marriedCouple.getSpouse2Id()==null) {
            log.info("Invalid parameters to create marriage details");
            throw new InvalidParameterException("Invalid parameters to create marriage details");
        }
        familyMarriageDetailsRepository.save(marriedCouple);
        return;
    }

    public void updateMarriage(Integer coupleId, Event marriageEvent){
        if( coupleId == null || marriageEvent == null){
            log.info("Invalid parameters to update marriage details");
            throw new InvalidParameterException("Invalid parameters to update marriage details");
        }
        if (marriageEvent.getEventDate()!=null) {
            updateCoupleEventDate(coupleId,marriageEvent.getEventDate() );
        }
        if (marriageEvent.getEventDetails().get("Location")!=null) {
            updateCoupleEventLocation(coupleId,marriageEvent.getEventDetails().get("Location") );
        }
    }


}
