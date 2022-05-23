package com.example.springboot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @BeforeEach
    void setup() {
        rfaRepository.deleteAll();
    }

    @Test
    public void postSavesToRfaRepository() throws Exception {
        String rfaContent = "Some String";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/rfa")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(rfaContent))
                .andDo(print())
                .andExpect(status().isOk());
        Mockito.verify(rabbitMqMessageSender).publishRfaUploadedEvent(captor.capture());

        Long id = captor.getValue();

        RfaEntity rfaEntity = rfaRepository.findById(id).orElseThrow(() -> new Exception());
        assertEquals(rfaEntity.getContent(), rfaContent);
    }

    @Test
    public void shouldFailDueToUnsuppportedMediaType() throws Exception {
        // Given
        String rfaContent = "Some String";

        // When
        this.mockMvc.perform(MockMvcRequestBuilders.post("/rfa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rfaContent))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnRfa() throws Exception {
        // Given
        RfaEntity rfaEntity = new RfaEntity();
        rfaEntity.setContent("Hello World");
        RfaEntity rfaEntitySaved = rfaRepository.save(rfaEntity);

        // When
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/rfa/" + rfaEntitySaved.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        RfaEntity rfaEntityResponse = objectMapper.readValue(contentAsString, RfaEntity.class);

        //Then
        assertEquals(rfaEntity.getId(), rfaEntityResponse.getId());
        assertEquals(rfaEntity.getContent(), rfaEntityResponse.getContent());
    }

    @Test
    void shouldReturnAllRfas() throws Exception {
        // Given
        List<RfaEntity> rfaEntities = new ArrayList<>();
        for (long id = 1L; id < 5L; id++) {
            RfaEntity rfaEntity = new RfaEntity();
            rfaEntity.setContent("Hello World " + id);
            rfaEntities.add(rfaRepository.save(rfaEntity));
        }
        rfaRepository.saveAll(rfaEntities);

        // When
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/rfa")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<RfaEntity> rfaEntitiesResponse = objectMapper.readValue(contentAsString,
                new TypeReference<List<RfaEntity>>() {});

        // Then
        for (int index = 0; index < 4; index++) {
            RfaEntity rfaEntity = rfaEntities.get(index);
            RfaEntity rfaEntityResponse = rfaEntitiesResponse.get(index);
            assertEquals(rfaEntity.getId(), rfaEntityResponse.getId());
            assertEquals(rfaEntityResponse.getContent(), rfaEntityResponse.getContent());
        }
    }

    @Test
    void shouldDeleteRfa() throws Exception {
        // Given
        RfaEntity rfaEntity1 = new RfaEntity();
        rfaEntity1.setContent("Hello World 1");
        RfaEntity rfaEntity1Saved = rfaRepository.save(rfaEntity1);
        RfaEntity rfaEntity2 = new RfaEntity();
        rfaEntity2.setContent("Hello World 2");
        RfaEntity rfaEntity2Saved = rfaRepository.save(rfaEntity2);

        // When
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/rfa/" + rfaEntity1Saved.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        Optional<RfaEntity> deletedRfaEntity = rfaRepository.findById(rfaEntity1Saved.getId());
        assertTrue(deletedRfaEntity.isEmpty());
        Optional<RfaEntity> existingRfaEntity = rfaRepository.findById(rfaEntity2Saved.getId());
        assertTrue(existingRfaEntity.isPresent());
    }

    @Test
    void shouldDeleteAllRfas() throws Exception {
        // Given
        RfaEntity rfaEntity1 = new RfaEntity();
        rfaEntity1.setContent("Hello World 1");
        RfaEntity rfaEntity1Saved = rfaRepository.save(rfaEntity1);
        RfaEntity rfaEntity2 = new RfaEntity();
        rfaEntity2.setContent("Hello World 2");
        RfaEntity rfaEntity2Saved = rfaRepository.save(rfaEntity2);

        // When
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/rfa")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        long count = rfaRepository.count();
        assertEquals(0, count);
    }

    @Test
    void shouldNotDeleteNotExistentRfa() throws Exception {
        // Given
        RfaEntity rfaEntity1 = new RfaEntity();
        rfaEntity1.setContent("Hello World 1");
        RfaEntity rfaEntity1Saved = rfaRepository.save(rfaEntity1);
        RfaEntity rfaEntity2 = new RfaEntity();
        rfaEntity2.setContent("Hello World 2");
        RfaEntity rfaEntity2Saved = rfaRepository.save(rfaEntity2);

        // When
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/rfa/" + 107184)
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        assertEquals(2, rfaRepository.count());
    }
}