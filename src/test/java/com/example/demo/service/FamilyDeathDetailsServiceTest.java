package com.example.demo.service;

import com.example.demo.datamysql.dao.DeathDetails;
import com.example.demo.datamysql.dao.FamilyDeathDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.InvalidParameterException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FamilyDeathDetailsServiceTest {
    private FamilyDeathDetailsRepository familyDeathDetailsRepository = Mockito.mock(FamilyDeathDetailsRepository.class);
    private FamilyDeathDetailsService familyDeathDetailsService;
    @BeforeEach
    void setup(){
        familyDeathDetailsService = new FamilyDeathDetailsService(familyDeathDetailsRepository);
    }

    @Test
    public void testfindByMemberIdReturnsMemberOnValidCall(){
        Integer memberId = 1;
        when(familyDeathDetailsRepository.findByMemberId(memberId)).thenReturn(memberDeath());
        DeathDetails deathDetails = familyDeathDetailsService.findByMemberId(memberId);
        assertThat(deathDetails).isNotNull();
    }

    @Test
    public void testfindByMemberIdNullMemberOnInvalidCall(){
        DeathDetails deathDetails = familyDeathDetailsService.findByMemberId(null);
        assertThat(deathDetails).isNull();
    }

    @Test
    public void testdeleteByMemberIdCallsRepoOnValidCall(){
        Integer memberId = 1;
        familyDeathDetailsService.deleteByMemberId(memberId);
        verify(familyDeathDetailsRepository, times(1)).deleteByMemberId(memberId);
    }

    @Test
    public void testdeleteByMemberIdDoesntCallRepoOnInvalidCall(){
        assertThrows(InvalidParameterException.class,() -> { familyDeathDetailsService.deleteByMemberId(null);} );
    }

    private DeathDetails memberDeath(){
        DeathDetails deathDetails = new DeathDetails();
        deathDetails.setMemberId(1);
        return deathDetails;
    }
}
