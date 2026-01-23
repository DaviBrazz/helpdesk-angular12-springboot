package com.project.helpdesk.services;

import com.project.helpdesk.domain.Tecnico;
import com.project.helpdesk.domain.dtos.TecnicoDTO;
import com.project.helpdesk.repositories.PessoaRepository;
import com.project.helpdesk.repositories.TecnicoRepository;
import com.project.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.project.helpdesk.services.exceptions.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Tecnico findById(Integer id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Objeto não encontrado! ID: " + id));
    }

    public List<Tecnico> findAll() {
        return tecnicoRepository.findAll();
    }

    public Tecnico create(@Valid TecnicoDTO dto) {
        dto.setId(null);
        dto.setSenha(bCryptPasswordEncoder.encode(dto.getSenha()));

        validaCpf(dto);
        validaEmail(dto);

        Tecnico tecnico = new Tecnico(dto);
        return tecnicoRepository.save(tecnico);
    }

    public Tecnico update(Integer id, @Valid TecnicoDTO dto) {
        Tecnico tecnico = findById(id);

        if (!dto.getCpf().equals(tecnico.getCpf())) {
            validaCpf(dto);
            tecnico.setCpf(dto.getCpf());
        }

        if (!dto.getEmail().equals(tecnico.getEmail())) {
            validaEmail(dto);
            tecnico.setEmail(dto.getEmail());
        }

        tecnico.setNome(dto.getNome());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            tecnico.setSenha(bCryptPasswordEncoder.encode(dto.getSenha()));
        }
        return tecnicoRepository.save(tecnico);
    }

    public void delete(Integer id) {
        Tecnico tecnico = findById(id);

        if (!tecnico.getChamados().isEmpty()) {
            throw new DataIntegrityViolationException(
                    "O Técnico possui ordens de serviço e não pode ser deletado!"
            );
        }
        tecnicoRepository.deleteById(id);
    }

    private void validaCpf(TecnicoDTO dto) {
        pessoaRepository.findByCpf(dto.getCpf())
                .ifPresent(pessoa -> {
                    if (!pessoa.getId().equals(dto.getId())) {
                        throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
                    }
                });
    }

    private void validaEmail(TecnicoDTO dto) {
        pessoaRepository.findByEmail(dto.getEmail())
                .ifPresent(pessoa -> {
                    if (!pessoa.getId().equals(dto.getId())) {
                        throw new DataIntegrityViolationException("Email já cadastrado no sistema!");
                    }
                });
    }
}
