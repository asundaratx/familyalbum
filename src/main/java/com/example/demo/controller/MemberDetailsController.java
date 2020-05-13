package com.example.demo.controller;

import com.example.demo.datamysql.dao.*;
import com.example.demo.service.FamilyMarriageDetailsService;
import com.example.demo.service.FamilyMemberService;
import com.example.demo.service.FamilyDeathDetailsService;
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
    private FamilyMarriageDetailsService marriageDetailsService;
    @Autowired
    private FamilyDeathDetailsService familyDeathDetailsService;
    @Autowired
    private FamilyMemberService familyMemberService;

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(       Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true, 10));
    }

    @GetMapping("/memberdetails/delete/{id}")
    @Transactional
    public String deleteUser(@PathVariable("id") long id, FamilyMemberDetails memberDetails, Model model) {
        log.info("Deleting family member: ", memberDetails.getFamilyMember());
        Integer memberId = Integer.valueOf((int) id);
        FamilyMember familyMember = memberDetails.getFamilyMember();
        MarriedCouple marriedCouple = marriageDetailsService.findBySpouse1IdOrSpouse2Id(memberId,memberId);
        if (marriedCouple!=null) {
            Integer spouseId = null;
            if (marriedCouple.getSpouse2Id().equals(memberId)) spouseId=marriedCouple.getSpouse1Id();
            if (marriedCouple.getSpouse1Id().equals(memberId)) spouseId=marriedCouple.getSpouse2Id();
            marriageDetailsService.deleteCouple(memberId, memberId);
            familyMemberService.updateMarriageStatus(Boolean.FALSE, spouseId);
        }
        familyDeathDetailsService.deleteByMemberId(Integer.valueOf((int) id));
        familyMemberService.deleteByMemberId(Integer.valueOf((int) id));
        return "index.html";
    }

    @PostMapping( path ="memberdetails")
    @Transactional
    public String createOrUpdateMemberDetails(FamilyMemberDetails memberDetails, Model model){
        log.info("Create or update for family member: ", memberDetails.getFamilyMember());
        FamilyMember familyMember = memberDetails.getFamilyMember();
        FamilyMember mother = memberDetails.getMother();
        FamilyMember father = memberDetails.getFather();
        FamilyMember spouse = memberDetails.getSpouse();
        Event marriageEvent = memberDetails.getMarriageEvent();
        Event deathEvent = memberDetails.getDeathEvent();

        //TODO need to match by firstname, lastname, AND birthdate
        List<FamilyMember> existingMemberList = familyMemberService.getMemberByFirstnameAndLastname(familyMember.getFirstname(), familyMember.getLastname());
        if (existingMemberList!=null && existingMemberList.size() == 1) {
            FamilyMember existingMember = existingMemberList.get(0);
            if(existingMember.getFatherId()== null&& father!=null && father.getId()>0 || existingMember.getFatherId()!=father.getId())
                familyMemberService.updateFather(father.getId(), existingMember.getId());
            if(existingMember.getMotherId()== null&&mother!=null&&mother.getId()>0 || existingMember.getMotherId()!=mother.getId())
                familyMemberService.updateMother(mother.getId(), existingMember.getId());
            log.info("Updating existing member");
            familyMemberService.updateGivenFamilyMember(familyMember.getFirstname(),
                    familyMember.getLastname(), familyMember.getOccupation(),
                    familyMember.getDomicile(),familyMember.getBirthDate(), existingMemberList.get(0).getId());
            DeathDetails existingDeathDetails = familyDeathDetailsService.findByMemberId(existingMember.getId());
            if(deathEvent!=null){
                if (existingDeathDetails!=null) {
                    if (deathEvent.getEventDate() != null && !deathEvent.getEventDate().equals(existingDeathDetails.getDeathDate())) {
                        familyDeathDetailsService.updateDeathEventDate(existingDeathDetails.getId(),deathEvent.getEventDate());
                    }
                    if(deathEvent.getEventDetails().get("Cause_death")!=null && !deathEvent.getEventDetails().get("Cause_death").equals(existingDeathDetails.getCauseDeath())){
                        familyDeathDetailsService.updateDeathEventCause(existingDeathDetails.getId(), deathEvent.getEventDetails().get("Cause_death"));
                    }
                }else {
                    familyMemberService.updateIsAliveStatus(Boolean.FALSE, existingMember.getId());
                    DeathDetails deathDetails = new DeathDetails();
                    deathDetails.setCauseDeath(deathEvent.getEventDetails().get("Cause_death"));
                    deathDetails.setDeathDate(deathEvent.getEventDate());
                    deathDetails.setMemberId(existingMember.getId());
                    familyDeathDetailsService.save(deathDetails);
                }
            }
            MarriedCouple marriedCouple = marriageDetailsService.findBySpouse1IdOrSpouse2Id(existingMember.getId(),existingMember.getId());
            if (marriedCouple!= null  ) {
                if (marriageEvent!=null) {
                    if (marriageEvent.getEventDate()!=null) {
                        marriageDetailsService.updateCoupleEventDate(marriedCouple.getId(),marriageEvent.getEventDate() );
                    }
                    if (marriageEvent.getEventDetails().get("Location")!=null) {
                        marriageDetailsService.updateCoupleEventLocation(marriedCouple.getId(),marriageEvent.getEventDetails().get("Location") );
                    }
                }

            } else {
                if(spouse!=null && spouse.getId() > 0) {
                    MarriedCouple details = new MarriedCouple();
                    details.setSpouse1Id(existingMember.getId());
                    details.setSpouse2Id(spouse.getId());
                    if (marriageEvent!=null) {
                        if (marriageEvent.getEventDate()!=null) {
                            details.setMarriageDate(marriageEvent.getEventDate());
                        }
                        if (marriageEvent.getEventDetails().get("Location")!=null) {
                            details.setMarriageLocation(marriageEvent.getEventDetails().get("Location"));
                        }
                    }
                    familyMemberService.updateMarriageStatus(Boolean.TRUE, existingMember.getId());
                    familyMemberService.updateMarriageStatus(Boolean.TRUE, spouse.getId());
                    marriageDetailsService.save(details);
                }
            }
        } else {
            //TODO Handle case when multiple people have same first and last name. Alternately Add birthdate for uniqueness
            log.info("creating member who is alive");
            familyMemberService.save(familyMember);
        }
        return "index.html";
    }

    @GetMapping(path = "memberdetails")
    public String getMemberDetailsByName(@RequestParam(name="fullname") String fullname, Model model){
        log.info("Getting family member details: ", fullname);
        FamilyMemberDetails familyMemberDetails = new FamilyMemberDetails();
        FamilyMember familyMember = null;

        if (fullname!= null && !fullname.isEmpty() && fullname.split(" ").length==2) {
            String[] names = fullname.split(" ");
            familyMember = familyMemberService.getMemberByFirstnameAndLastname(names[0], names[1]).get(0);
        }
        List<FamilyMemberShort> allPossibleRelations = familyMemberService.findAllFamilyMemberShortProjectedBy();
        if (familyMember==null) {
            familyMemberDetails.setPotentialRelations(allPossibleRelations);
            model.addAttribute("memberdetails", familyMemberDetails);
            return "familymember/memberdetails.html";
        }
        familyMemberDetails.setFamilyMember(familyMember);

        if(familyMember.getFatherId()!=null) {
            FamilyMember father = familyMemberService.findById(familyMember.getFatherId());
            if(father!=null) familyMemberDetails.setFather(father);
        }
        if(familyMember.getMotherId()!=null) {
            FamilyMember mother = familyMemberService.findById(familyMember.getMotherId());
            if (mother!=null) familyMemberDetails.setMother(mother);
        }
        if (familyMember.getIsMarried()!=null && familyMember.getIsMarried().equals(Boolean.TRUE)) {
            MarriedCouple marriedCouple = marriageDetailsService.findBySpouse1IdOrSpouse2Id(familyMember.getId(),familyMember.getId());
            if (marriedCouple!= null ) {
                FamilyMember spouse = null;
                if(familyMember.getId().equals(marriedCouple.getSpouse1Id())) {
                    spouse=familyMemberService.findById(marriedCouple.getSpouse2Id());
                } else if(familyMember.getId().equals(marriedCouple.getSpouse2Id())) {
                    spouse=familyMemberService.findById(marriedCouple.getSpouse1Id());
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
            DeathDetails deathDetails = familyDeathDetailsService.findByMemberId(familyMember.getId());
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
