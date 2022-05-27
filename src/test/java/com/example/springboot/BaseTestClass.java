package com.example.springboot;


import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public abstract class BaseTestClass {

    @Autowired
    RfaController rfaController;

    @MockBean
    RfaService rfaService;

   // @BeforeEach

//    public void setup() {
//        RestAssuredMockMvc.standaloneSetup(rfaController);
//        Mockito.when(rfaService.getRfaById(1L))
//                .thenReturn(Optional.of(new RfaDto(1L, "Test 1")));
//    }
}