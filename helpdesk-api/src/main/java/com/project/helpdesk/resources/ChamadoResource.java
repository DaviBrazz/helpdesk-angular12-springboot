package com.project.helpdesk.resources;

import com.project.helpdesk.domain.Chamado;
import com.project.helpdesk.domain.dtos.ChamadoDTO;
import com.project.helpdesk.services.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chamados")
public class ChamadoResource {

    @Autowired
    private ChamadoService chamadoService;
    
    @GetMapping("/{id}")
    public ResponseEntity<ChamadoDTO> findById(@PathVariable Integer id) {
        Chamado chamado = chamadoService.findById(id);
        return ResponseEntity.ok(new ChamadoDTO(chamado));
    }

    @GetMapping
    public ResponseEntity<List<ChamadoDTO>> findAll() {
        List<ChamadoDTO> listDTO = chamadoService.findAll()
                .stream()
                .map(ChamadoDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(listDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ChamadoDTO dto) {
        Chamado chamado = chamadoService.create(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(chamado.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ChamadoDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody ChamadoDTO dto) {

        Chamado chamado = chamadoService.update(id, dto);
        return ResponseEntity.ok(new ChamadoDTO(chamado));
    }
}
