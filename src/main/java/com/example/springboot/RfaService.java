package com.example.springboot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class RfaService {

    private final RfaRepository rfaRepository;
    private final RabbitMqMessageSender rabbitMqMessageSender;

    public void saveRfaContent(String rfa) {
        RfaEntity rfaEntity = new RfaEntity();
        rfaEntity.setContent(rfa);
        RfaEntity rfaEntity1 = rfaRepository.save(rfaEntity);
        rabbitMqMessageSender.publishRfaUploadedEvent(rfaEntity1.getId());
    }

    public Optional<RfaDto> getRfaById(Long id) {
        return rfaRepository.findById(id)
                .map(this::convertToRfaDto);
    }

    public List<RfaDto> getAllRfas() {
        List<RfaDto> result = new ArrayList<>();
        Iterable<RfaEntity> rfaEntities = rfaRepository.findAll();
        rfaEntities.forEach(rfaEntity -> result.add(convertToRfaDto(rfaEntity)));
        return result;
    }

    public void deleteRfa(@PathVariable("id") Long id) {
        try {
            rfaRepository.deleteById(id);
        } catch (Exception e) {
            log.info("Attempt to delete non-existing RFA with id {}", id, e);
        }
    }

    public void deleteAllRfas() {
        rfaRepository.deleteAll();
    }

    private RfaDto convertToRfaDto(RfaEntity rfaEntity) {
        RfaDto rfaDto = new RfaDto();
        rfaDto.setId(rfaEntity.getId());
        rfaDto.setContent(rfaEntity.getContent());
        return rfaDto;
    }

}
