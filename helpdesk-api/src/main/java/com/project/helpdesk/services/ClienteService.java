package com.project.helpdesk.services;

import com.project.helpdesk.domain.Cliente;
import com.project.helpdesk.domain.dtos.ClienteDTO;
import com.project.helpdesk.repositories.PessoaRepository;
import com.project.helpdesk.repositories.ClienteRepository;
import com.project.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.project.helpdesk.services.exceptions.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Cliente findById(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Objeto não encontrado! ID: " + id));
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Cliente create(@Valid ClienteDTO dto) {
        dto.setId(null);
        dto.setSenha(bCryptPasswordEncoder.encode(dto.getSenha()));

        validaCpf(dto);
        validaEmail(dto);

        Cliente cliente = new Cliente(dto);
        return clienteRepository.save(cliente);
    }

    public Cliente update(Integer id, @Valid ClienteDTO dto) {
        Cliente cliente = findById(id);

        if (!dto.getCpf().equals(cliente.getCpf())) {
            validaCpf(dto);
            cliente.setCpf(dto.getCpf());
        }

        if (!dto.getEmail().equals(cliente.getEmail())) {
            validaEmail(dto);
            cliente.setEmail(dto.getEmail());
        }

        cliente.setNome(dto.getNome());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            cliente.setSenha(bCryptPasswordEncoder.encode(dto.getSenha()));
        }

        return clienteRepository.save(cliente);
    }

    public void delete(Integer id) {
        Cliente cliente = findById(id);

        if (!cliente.getChamados().isEmpty()) {
            throw new DataIntegrityViolationException(
                    "O Cliente possui ordens de serviços e não pode ser deletado!"
            );
        }

        clienteRepository.deleteById(id);
    }

    private void validaCpf(ClienteDTO dto) {
        pessoaRepository.findByCpf(dto.getCpf())
                .ifPresent(pessoa -> {
                    if (!pessoa.getId().equals(dto.getId())) {
                        throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
                    }
                });
    }

    private void validaEmail(ClienteDTO dto) {
        pessoaRepository.findByEmail(dto.getEmail())
                .ifPresent(pessoa -> {
                    if (!pessoa.getId().equals(dto.getId())) {
                        throw new DataIntegrityViolationException("Email já cadastrado no sistema!");
                    }
                });
    }
}
