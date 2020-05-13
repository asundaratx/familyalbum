package com.example.demo.service;

import com.example.demo.datamysql.dao.FamilyMember;
import com.example.demo.datamysql.dao.FamilyMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

import static org.mockito.Mockito.when;

public class FamilyMemberServiceTest {
    private FamilyMemberRepository familyMemberRepository = Mockito.mock(FamilyMemberRepository.class);
    private FamilyMemberService familyMemberService;

    @BeforeEach
    void setup(){
        familyMemberService = new FamilyMemberService(familyMemberRepository);
    }

    @Test
    void findAllFamilyMembersReturnsMembers(){
        when(familyMemberRepository.findAll()).thenReturn(member());
        Iterable<FamilyMember> familyMembers = familyMemberService.findAllFamilyMembers();
        assertThat(familyMembers).isNotNull();
    }

    @Test
    void getMemberByFirstNameReturnsEmptyListOnInValidInput(){
        String firstname = "";
        List<FamilyMember> members = familyMemberService.getMemberByFirstName(firstname);
        assertThat(members).isNotNull();
        assertThat(members.size()).isEqualTo(0);
    }

    @Test
    void getMemberByFirstNameReturnsMemberOnValidInput(){
        String firstname = "Aparna";
        String lastname = "Sundar";
        String fullname = "Aparna Sundar";
        when(familyMemberRepository.findByFirstname(firstname)).thenReturn(member());
        List<FamilyMember> members = familyMemberService.getMemberByFirstName(fullname);
        assertThat(members).isNotNull();
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    void getMemberByFullNameReturnsEmptyListOnInValidInput(){
        String fullname = "";
        List<FamilyMember> members = familyMemberService.getMemberByFullName(fullname);
        assertThat(members).isNotNull();
        assertThat(members.size()).isEqualTo(0);
    }

    @Test
    void getMemberByFullNameReturnsMemberOnValidInput(){
        String firstname = "Aparna";
        String lastname = "Sundar";
        String fullname = "Aparna Sundar";
        when(familyMemberRepository.findByFirstnameAndLastname(firstname, lastname)).thenReturn(member());
        List<FamilyMember> members = familyMemberService.getMemberByFullName(fullname);
        assertThat(members).isNotNull();
        assertThat(members.size()).isEqualTo(1);
    }

    private List<FamilyMember> member(){
        FamilyMember familyMember = new FamilyMember();
        familyMember.setFirstname("Aparna");
        familyMember.setLastname("Sundar");
        List<FamilyMember> familyMemberList = new ArrayList<>();
        familyMemberList.add(familyMember);
        return familyMemberList;
    }
}
