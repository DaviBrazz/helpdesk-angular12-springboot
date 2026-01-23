package com.project.helpdesk.resources;

import com.project.helpdesk.domain.Tecnico;
import com.project.helpdesk.domain.dtos.TecnicoDTO;
import com.project.helpdesk.services.TecnicoService;
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
@RequestMapping("/tecnicos")
public class TecnicoResource {

    @Autowired
    private TecnicoService tecnicoService;

    @GetMapping("/{id}")
    public ResponseEntity<TecnicoDTO> findById(@PathVariable Integer id) {
        Tecnico tecnico = tecnicoService.findById(id);
        return ResponseEntity.ok(new TecnicoDTO(tecnico));
    }

    @GetMapping
    public ResponseEntity<List<TecnicoDTO>> findAll() {
        List<TecnicoDTO> listDTO = tecnicoService.findAll()
                .stream()
                .map(TecnicoDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(listDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody TecnicoDTO dto) {
        Tecnico tecnico = tecnicoService.create(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tecnico.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TecnicoDTO> update(@PathVariable Integer id, @Valid @RequestBody TecnicoDTO dto) {
        Tecnico tecnico = tecnicoService.update(id, dto);
        return ResponseEntity.ok(new TecnicoDTO(tecnico));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tecnicoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
