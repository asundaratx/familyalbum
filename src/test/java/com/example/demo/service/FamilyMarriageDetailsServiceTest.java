package com.example.demo.service;

import com.example.demo.datamysql.dao.FamilyMarriageDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FamilyMarriageDetailsServiceTest {
    private FamilyMarriageDetailsRepository familyMarriageDetailsRepository = Mockito.mock(FamilyMarriageDetailsRepository.class);
    private FamilyMarriageDetailsService familyMarriageDetailsService;

    @BeforeEach
    void setup(){
        familyMarriageDetailsService = new FamilyMarriageDetailsService(familyMarriageDetailsRepository);
    }

    @Test
    public void testdeleteCoupleBySpouseIdsCallsRepoOnValidCall(){
        Integer spouse1Id = 1;
        Integer spouse2Id = 2;
        familyMarriageDetailsService.deleteCouple(spouse1Id, spouse2Id);
        verify(familyMarriageDetailsRepository, times(1)).deleteCouple(spouse1Id, spouse2Id);
    }

    @Test
    public void testdeleteCoupleBySpouseIdsDoesntCallRepoOnInvalidCall(){
        assertThrows(InvalidParameterException.class,() -> { familyMarriageDetailsService.deleteCouple(null, null);} );
    }
}
