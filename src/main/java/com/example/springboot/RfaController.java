package com.example.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rfa")
public class RfaController {

    private final Logger logger = LoggerFactory.getLogger(RfaController.class);

    private final RfaRepository rfaRepository;
    private final RabbitMqMessageSender rabbitMqMessageSender;


    public RfaController(RfaRepository rfaRepository, RabbitMqMessageSender rabbitMqMessageSender) {
        this.rfaRepository = rfaRepository;
        this.rabbitMqMessageSender = rabbitMqMessageSender;
    }

    @PostMapping(consumes = "text/plain")
    @ResponseStatus(HttpStatus.OK)
    void uploadRfa(@RequestBody String rfa) {
        RfaEntity rfaEntity = new RfaEntity();
        rfaEntity.setContent(rfa);
        RfaEntity rfaEntity1 = rfaRepository.save(rfaEntity);
        rabbitMqMessageSender.publishRfaUploadedEvent(rfaEntity1.getId());
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<String> downloadRfa(@PathVariable("id") Long id) {
        return rfaRepository.findById(id)
                .map(rfaEntity -> ResponseEntity.ok(rfaEntity.getContent()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping()
    ResponseEntity<List<RfaEntity>> downloadAllRfas() {
        List<RfaEntity> result = new ArrayList<>();
        Iterable<RfaEntity> rfaEntities = rfaRepository.findAll();
        rfaEntities.forEach(result::add);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(path = "/{id}")
    void deleteRfa(@PathVariable("id") Long id) {
        try {
            rfaRepository.deleteById(id);
        } catch (Exception e) {
            logger.info("Attempt to delete non-existing RFA with id {}", id, e);
        }

    }

    @DeleteMapping()
    void deleteAllRfas() {
        rfaRepository.deleteAll();
    }
}
