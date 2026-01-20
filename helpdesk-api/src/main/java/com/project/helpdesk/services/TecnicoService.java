package com.project.helpdesk.services;

import com.project.helpdesk.domain.Pessoa;
import com.project.helpdesk.domain.Tecnico;
import com.project.helpdesk.domain.dtos.TecnicoDTO;
import com.project.helpdesk.repositories.PessoaRepository;
import com.project.helpdesk.repositories.TecnicoRepository;
import com.project.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.project.helpdesk.services.exceptions.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    public Tecnico findById(Integer id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Objeto não encontrado! ID: " + id));
    }

    public List<Tecnico> findAll() {
        return tecnicoRepository.findAll();
    }

    public Tecnico create(TecnicoDTO tecnicoDTO) {
        tecnicoDTO.setId(null);
        validaCpf(tecnicoDTO);
        validaEmail(tecnicoDTO);
        Tecnico novoTecnico = new Tecnico(tecnicoDTO);
        return tecnicoRepository.save(novoTecnico);
    }

    private void validaCpf(TecnicoDTO tecnicoDTO) {
        pessoaRepository.findByCpf(tecnicoDTO.getCpf())
                .filter(pessoa -> !pessoa.getId().equals(tecnicoDTO.getId()))
                .ifPresent(pessoa -> {
                    throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
                });
    }

    private void validaEmail(TecnicoDTO dto) {
        pessoaRepository.findByEmail(dto.getEmail())
                .filter(pessoa -> !pessoa.getId().equals(dto.getId()))
                .ifPresent(pessoa -> {
                    throw new DataIntegrityViolationException("Email já cadastrado no sistema!");
                });
    }

    public Tecnico update(Integer id, @Valid TecnicoDTO tecnicoDTO) {
        tecnicoDTO.setId(id);
        Tecnico tecnico = findById(id);
        validaCpf(tecnicoDTO);
        validaEmail(tecnicoDTO);
        tecnico = new Tecnico(tecnicoDTO);
        return tecnicoRepository.save(tecnico);
    }
}
