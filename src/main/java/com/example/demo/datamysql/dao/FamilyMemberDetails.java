package com.example.demo.datamysql.dao;

import lombok.Data;

import java.util.List;

@Data
public class FamilyMemberDetails {
    FamilyMember familyMember;
    FamilyMember father;
    FamilyMember mother;
    FamilyMember spouse;
    Event marriageEvent;
    Event deathEvent;
    List<FamilyMemberShort> potentialRelations;
}
