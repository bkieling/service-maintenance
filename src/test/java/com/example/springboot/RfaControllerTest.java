package com.example.springboot;

import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Ignore
@SpringBootTest
@AutoConfigureMockMvc
class RfaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RfaRepository rfaRepository;
    @MockBean
    private RabbitMqMessageSender rabbitMqMessageSender;
    @Captor
    ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

//    @Ignore
//    @Test
//    public void postSavesToRfaRepository() throws Exception {
//        String rfaContent = "Some String";
//        Mockito.verify(rabbitMqMessageSender).publishRfaUploadedEvent(captor.capture());
//
//        this.mockMvc.perform(MockMvcRequestBuilders.post("/rfa")
//                        .contentType(MediaType.TEXT_PLAIN)
//                        .content(rfaContent))
//                .andDo(print())
//                .andExpect(status().isOk());
//        //Long id = captor.getValue();
//        Long id = 1L;
//        RfaEntity rfaEntity = rfaRepository.findById(id).orElseThrow(() -> new Exception());
//        Assertions.assertEquals(rfaEntity.getContent(), rfaContent);
//    }

    @Ignore
    @Test
    public void shouldFailDueToUnsuppportMediaType() throws Exception {
        // Given
        String rfaContent = "Some String";

        // When
        this.mockMvc.perform(MockMvcRequestBuilders.post("/rfa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rfaContent))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

}