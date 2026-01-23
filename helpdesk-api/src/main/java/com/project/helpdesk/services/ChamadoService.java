package com.project.helpdesk.services;

import com.project.helpdesk.domain.Chamado;
import com.project.helpdesk.domain.Cliente;
import com.project.helpdesk.domain.Tecnico;
import com.project.helpdesk.domain.dtos.ChamadoDTO;
import com.project.helpdesk.domain.enums.Prioridade;
import com.project.helpdesk.domain.enums.Status;
import com.project.helpdesk.repositories.ChamadoRepository;
import com.project.helpdesk.services.exceptions.ObjectNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private TecnicoService tecnicoService;

    @Autowired
    private ClienteService clienteService;

    public Chamado findById(Integer id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id));
    }

    public List<Chamado> findAll() {
        return chamadoRepository.findAll();
    }

    public Chamado create(@Valid ChamadoDTO dto) {
        Chamado chamado = buildChamadoFromDTO(dto, new Chamado());
        return chamadoRepository.save(chamado);
    }

    public Chamado update(Integer id, @Valid ChamadoDTO dto) {
        Chamado chamadoExistente = findById(id);
        Chamado chamadoAtualizado = buildChamadoFromDTO(dto, chamadoExistente);
        return chamadoRepository.save(chamadoAtualizado);
    }

    private Chamado buildChamadoFromDTO(ChamadoDTO dto, Chamado chamado) {
        Tecnico tecnico = tecnicoService.findById(dto.getTecnico());
        Cliente cliente = clienteService.findById(dto.getCliente());

        chamado.setTecnico(tecnico);
        chamado.setCliente(cliente);
        chamado.setPrioridade(Prioridade.toEnum(dto.getPrioridade()));
        chamado.setStatus(Status.toEnum(dto.getStatus()));
        chamado.setTitulo(dto.getTitulo());
        chamado.setObservacoes(dto.getObservacoes());

        if (chamado.getStatus() != null && chamado.getStatus().getCodigo() == Status.ENCERRADO.getCodigo()) {
            chamado.setDataFechamento(LocalDate.now());
        } else {
            chamado.setDataFechamento(null);
        }

        return chamado;
    }
}
