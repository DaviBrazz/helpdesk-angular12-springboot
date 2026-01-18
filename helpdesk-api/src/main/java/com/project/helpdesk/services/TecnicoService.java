package com.project.helpdesk.services;

import com.project.helpdesk.domain.Tecnico;
import com.project.helpdesk.repositories.TecnicoRepository;
import com.project.helpdesk.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    public Tecnico findById(Integer id) {
        Optional<Tecnico> tecnico = tecnicoRepository.findById(id);

        return tecnico.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID:" + id));
    }
}
