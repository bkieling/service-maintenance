package com.example.springboot;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rfa")
public class RfaController {

    private final RfaService rfaService;

    @PostMapping(consumes = "text/plain")
    @ResponseStatus(HttpStatus.OK)
    public void uploadRfa(@RequestBody String rfa) {
        rfaService.saveRfaContent(rfa);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<RfaDto> getRfaById(@PathVariable("id") Long id) {
        return rfaService.getRfaById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping()
    public ResponseEntity<List<RfaDto>> getAllRfas() {
        return ResponseEntity.ok(rfaService.getAllRfas());
    }

    @DeleteMapping(path = "/{id}")
    public void deleteRfa(@PathVariable("id") Long id) {
        rfaService.deleteRfa(id);
    }

    @DeleteMapping()
    public void deleteAllRfas() {
        rfaService.deleteAllRfas();
    }


}
