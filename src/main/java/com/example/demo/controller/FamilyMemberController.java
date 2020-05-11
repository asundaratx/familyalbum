package com.example.demo.controller;

import com.example.demo.datamysql.dao.FamilyMember;
import com.example.demo.datamysql.dao.FamilyMemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.collections4.IteratorUtils;

import java.util.List;

@Controller
@RequestMapping(path="/familymember")
@Slf4j
public class FamilyMemberController {
    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @GetMapping(path="/all")
    public @ResponseBody
    Iterable<FamilyMember> getAllFamilyMembers() {
        return familyMemberRepository.findAll();
    }

    @GetMapping(path = "find")
    public @ResponseBody
    List<FamilyMember> getMemberByName(@RequestParam(name="fullname") String fullname){
        log.info("Getting family member by name: ", fullname);
        String[] names = fullname.split(" ");
        return familyMemberRepository.findByFirstname(names[0]);
    }

    @GetMapping(path = "view-all")
    public String viewAllFamilyMembers(Model model){
        List<FamilyMember> familyMemberList = IteratorUtils.toList(getAllFamilyMembers().iterator());
        log.info("Getting all family members: ", familyMemberList);
        model.addAttribute("familymembers", familyMemberList);
        return "familymember/viewall.basictable.html";
    }
}