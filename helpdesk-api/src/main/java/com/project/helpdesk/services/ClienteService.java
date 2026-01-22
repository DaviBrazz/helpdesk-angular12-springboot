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
    private ClienteRepository ClienteRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Cliente findById(Integer id) {
        return ClienteRepository.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Objeto não encontrado! ID: " + id));
    }

    public List<Cliente> findAll() {
        return ClienteRepository.findAll();
    }

    public Cliente create(ClienteDTO clienteDTO) {
        clienteDTO.setId(null);
        clienteDTO.setSenha(bCryptPasswordEncoder.encode(clienteDTO.getSenha()));
        validaCpf(clienteDTO);
        validaEmail(clienteDTO);
        Cliente novoCliente = new Cliente(clienteDTO);
        return ClienteRepository.save(novoCliente);
    }


    public Cliente update(Integer id, @Valid ClienteDTO clienteDTO) {
        clienteDTO.setId(id);
        Cliente cliente = findById(id);
        validaCpf(clienteDTO);
        validaEmail(clienteDTO);
        cliente = new Cliente(clienteDTO);
        return ClienteRepository.save(cliente);
    }

    public void delete(Integer id) {
        Cliente cliente = findById(id);
        if (cliente.getChamados().size() > 0) {
            throw new DataIntegrityViolationException("O Cliente possui ordens de serviços e não pode ser deletado!");
        }
        ClienteRepository.deleteById(id);
    }
    private void validaCpf(ClienteDTO clienteDTO) {
        pessoaRepository.findByCpf(clienteDTO.getCpf())
                .filter(pessoa -> !pessoa.getId().equals(clienteDTO.getId()))
                .ifPresent(pessoa -> {
                    throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
                });
    }

    private void validaEmail(ClienteDTO clienteDTO) {
        pessoaRepository.findByEmail(clienteDTO.getEmail())
                .filter(pessoa -> !pessoa.getId().equals(clienteDTO.getId()))
                .ifPresent(pessoa -> {
                    throw new DataIntegrityViolationException("Email já cadastrado no sistema!");
                });
    }

}
