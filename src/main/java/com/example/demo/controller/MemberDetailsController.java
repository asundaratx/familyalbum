package com.example.demo.controller;

import com.example.demo.datamysql.dao.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Slf4j
public class MemberDetailsController {
    @Autowired
    private FamilyMemberRepository familyMemberRepository;
    @Autowired
    private FamilyMarriageDetailsRepository familyMarriageDetailsRepository;
    @Autowired
    private FamilyDeathDetailsRepository familyDeathDetailsRepository;

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(       Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true, 10));
    }

    @GetMapping("/memberdetails/delete/{id}")
    @Transactional
    public String updateUser(@PathVariable("id") long id, FamilyMemberDetails memberDetails, Model model) {
        log.info("Got family member: ", memberDetails.getFamilyMember());
        FamilyMember familyMember = memberDetails.getFamilyMember();
        log.info("Got path id: ", id);
        familyMarriageDetailsRepository.deleteCouple(Integer.valueOf((int) id),Integer.valueOf((int) id));
        familyMemberRepository.deleteById(Integer.valueOf((int) id));
        return "index.html";
    }

    @PostMapping( path ="memberdetails")
    @Transactional
    public String createOrUpdateMemberDetails(FamilyMemberDetails memberDetails, Model model){
        log.info("Got family member: ", memberDetails.getFamilyMember());
        FamilyMember familyMember = memberDetails.getFamilyMember();
        FamilyMember mother = memberDetails.getMother();
        FamilyMember father = memberDetails.getFather();
        FamilyMember spouse = memberDetails.getSpouse();
        Event marriageEvent = memberDetails.getMarriageEvent();
        Event deathEvent = memberDetails.getDeathEvent();

        //TODO need to match by firstname, lastname, birthdate
        List<FamilyMember> existingMemberList = familyMemberRepository.findByFirstname(familyMember.getFirstname());
        if (existingMemberList!=null && existingMemberList.size() == 1) {
            FamilyMember existingMember = existingMemberList.get(0);
            log.info("Do we need to add relations");
            if(existingMember.getFatherId()== null&& father!=null && father.getId()>0 || existingMember.getFatherId()!=father.getId())
                familyMemberRepository.updateFather(father.getId(), existingMember.getId());
            if(existingMember.getMotherId()== null&&mother!=null&&mother.getId()>0 || existingMember.getMotherId()!=mother.getId())
                familyMemberRepository.updateMother(mother.getId(), existingMember.getId());
            log.info("Updating existing member");
            familyMemberRepository.updateGivenFamilyMember(familyMember.getFirstname(),
                    familyMember.getLastname(), familyMember.getOccupation(),
                    familyMember.getDomicile(),familyMember.getBirthDate(), existingMemberList.get(0).getId());
            DeathDetails existingDeathDetails = familyDeathDetailsRepository.findByMemberId(existingMember.getId());
            if(deathEvent!=null){
                if (existingDeathDetails!=null) {
                    if (deathEvent.getEventDate() != null && existingDeathDetails.getDeathDate()!=null) {
                        familyDeathDetailsRepository.updateDeathEventDate(existingDeathDetails.getId(),deathEvent.getEventDate());
                    }
                    if(deathEvent.getEventDetails().get("Cause_death")!=null){
                        familyDeathDetailsRepository.updateDeathEventCause(existingDeathDetails.getId(), deathEvent.getEventDetails().get("Cause_death"));
                    }
                }else {
                    familyMemberRepository.updateDeathStatus(Boolean.FALSE, existingMember.getId());
                    DeathDetails deathDetails = new DeathDetails();
                    deathDetails.setCauseDeath(deathEvent.getEventDetails().get("Cause_death"));
                    deathDetails.setDeathDate(deathEvent.getEventDate());
                    deathDetails.setMemberId(familyMember.getId());
                }
            }
            List<MarriedCouple> couples = familyMarriageDetailsRepository.findBySpouse1IdOrSpouse2Id(existingMember.getId(),existingMember.getId());
            MarriedCouple marriedCouple;
            if (couples!= null && couples.size() == 1 ) {
                // TODO Handle adding marriage details
                marriedCouple = couples.get(0);
                if (marriageEvent!=null) {
                    if (marriageEvent.getEventDate()!=null) {
                        familyMarriageDetailsRepository.updateCoupleEventDate(marriedCouple.getId(),marriageEvent.getEventDate() );
                    }
                    if (marriageEvent.getEventDetails().get("Location")!=null) {
                        familyMarriageDetailsRepository.updateCoupleEventLocation(marriedCouple.getId(),marriageEvent.getEventDetails().get("Location") );
                    }
                }

            } else if(couples == null || couples.size() == 0){
                MarriedCouple details = new MarriedCouple();
                details.setSpouse1Id(existingMember.getId());
                details.setSpouse2Id(spouse.getId());
                familyMemberRepository.updateMarriageStatus(Boolean.TRUE,familyMember.getId());
                familyMarriageDetailsRepository.save(details);
            }
        } else {
            log.info("creating member");
            familyMember.setIsAlive(true);
            familyMemberRepository.save(familyMember);
        }
        return "index.html";
    }

    @GetMapping(path = "memberdetails")
    public String getMemberDetailsByName(@RequestParam(name="fullname") String fullname, Model model){
        log.info("Getting family member details: ", fullname);
        FamilyMemberDetails familyMemberDetails = new FamilyMemberDetails();
        FamilyMember familyMember = null;
        if (fullname!= null && !fullname.isEmpty()) {
            String[] names = fullname.split(" ");
            familyMember = familyMemberRepository.findByFirstname(names[0]).get(0);
        }
        List<FamilyMemberShort> allPossibleRelations = familyMemberRepository.findAllFamilyMemberShortProjectedBy();
        if (familyMember==null) {
            familyMemberDetails.setPotentialRelations(allPossibleRelations);
            model.addAttribute("memberdetails", familyMemberDetails);
            return "familymember/memberdetails.html";
        }
        familyMemberDetails.setFamilyMember(familyMember);

        if(familyMember.getFatherId()!=null) {
            Optional<FamilyMember> father = familyMemberRepository.findById(familyMember.getFatherId());
            if(father.isPresent()) familyMemberDetails.setFather(father.get());
        }
        if(familyMember.getMotherId()!=null) {
            Optional<FamilyMember> mother = familyMemberRepository.findById(familyMember.getMotherId());
            if (mother.isPresent()) familyMemberDetails.setMother(mother.get());
        }
        if (familyMember.getIsMarried()!=null && familyMember.getIsMarried().equals(Boolean.TRUE)) {
            List<MarriedCouple> couples = familyMarriageDetailsRepository.findBySpouse1IdOrSpouse2Id(familyMember.getId(),familyMember.getId());
            MarriedCouple marriedCouple;
            if (couples!= null && couples.size() == 1) {
                marriedCouple = couples.get(0);
                FamilyMember spouse = null;
                if(familyMember.getId().equals(marriedCouple.getSpouse1Id())) {
                    spouse=familyMemberRepository.findById(marriedCouple.getSpouse2Id()).get();
                } else if(familyMember.getId().equals(marriedCouple.getSpouse2Id())) {
                    spouse=familyMemberRepository.findById(marriedCouple.getSpouse1Id()).get();
                }
                familyMemberDetails.setSpouse(spouse);
                Event marriageEvent = new Event();
                marriageEvent.setEventName("Marriage");
                if(marriedCouple.getMarriageDate()!=null)
                    marriageEvent.setEventDate(marriedCouple.getMarriageDate());
                if(marriedCouple.getMarriageLocation()!=null){
                    Map<String, String> eMap = new HashMap<>();
                    eMap.put("Location",marriedCouple.getMarriageLocation());
                    marriageEvent.setEventDetails(eMap);
                }
                familyMemberDetails.setMarriageEvent(marriageEvent);
            }
        }

        if (familyMember.getIsAlive()!=null && familyMember.getIsAlive().equals(Boolean.FALSE) ){
            DeathDetails deathDetails = familyDeathDetailsRepository.findByMemberId(familyMember.getId());
            if (deathDetails!=null){
                Event deathEvent = new Event();
                deathEvent.setEventName("Death");
                deathEvent.setEventDate(deathDetails.getDeathDate());
                if (deathDetails.getCauseDeath()!=null){
                    Map<String, String> eMap = new HashMap<>();
                    eMap.put("Cause_death",deathDetails.getCauseDeath());
                    deathEvent.setEventDetails(eMap);
                }
                familyMemberDetails.setDeathEvent(deathEvent);
            }
        }

        familyMemberDetails.setPotentialRelations(allPossibleRelations);
        model.addAttribute("memberdetails", familyMemberDetails);
        return "familymember/memberdetails.html";
    }
}
