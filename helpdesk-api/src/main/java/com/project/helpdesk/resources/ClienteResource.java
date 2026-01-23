package com.project.helpdesk.resources;

import com.project.helpdesk.domain.Cliente;
import com.project.helpdesk.domain.dtos.ClienteDTO;
import com.project.helpdesk.services.ClienteService;
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
@RequestMapping("/clientes")
public class ClienteResource {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> findById(@PathVariable Integer id) {
        Cliente cliente = clienteService.findById(id);
        return ResponseEntity.ok(new ClienteDTO(cliente));
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> findAll() {
        List<ClienteDTO> listDTO = clienteService.findAll()
                .stream()
                .map(ClienteDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(listDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ClienteDTO dto) {
        Cliente cliente = clienteService.create(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cliente.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody ClienteDTO dto) {

        Cliente cliente = clienteService.update(id, dto);
        return ResponseEntity.ok(new ClienteDTO(cliente));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
