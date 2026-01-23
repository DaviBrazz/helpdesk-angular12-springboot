package com.project.helpdesk.services;

import com.project.helpdesk.domain.Chamado;
import com.project.helpdesk.domain.Cliente;
import com.project.helpdesk.domain.Tecnico;
import com.project.helpdesk.domain.enums.Perfil;
import com.project.helpdesk.domain.enums.Prioridade;
import com.project.helpdesk.domain.enums.Status;
import com.project.helpdesk.repositories.ChamadoRepository;
import com.project.helpdesk.repositories.ClienteRepository;
import com.project.helpdesk.repositories.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DBService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void instanciaDB() {

        Tecnico tecnico1 = new Tecnico(null, "Valdir Cezar", "39053344705", "valdircezar@yahoo.com", bCryptPasswordEncoder.encode("123456789"));
        tecnico1.addPerfil(Perfil.ADMIN);

        Tecnico tecnico2 = new Tecnico(null, "Romário Souza", "31782255010", "romariosouza@gmail.com", bCryptPasswordEncoder.encode("123456789"));
        tecnico1.addPerfil(Perfil.ADMIN);

        Tecnico tecnico3 = new Tecnico(null, "Anderson Hudson", "13083150075", "hudsonanderson@hotmail.com", bCryptPasswordEncoder.encode("123789456"));
        tecnico1.addPerfil(Perfil.ADMIN);

        Cliente cliente1 = new Cliente(null, "Ernesto Freitas Barros","52998224725","ernestob@gmail.com", bCryptPasswordEncoder.encode("987654321"));
        cliente1.addPerfil(Perfil.CLIENTE);

        Cliente cliente2 = new Cliente(null, "Marcos Dias","56328156090","marcos.eng.dias@gmail.com", bCryptPasswordEncoder.encode("marcosdias123"));
        cliente1.addPerfil(Perfil.CLIENTE);


        Chamado chamado1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO,"Chamado 01", "Primeiro chamado", tecnico1, cliente1);

        tecnicoRepository.saveAll(Arrays.asList(tecnico1,tecnico2,tecnico3));
        clienteRepository.saveAll(Arrays.asList(cliente1,cliente2));
        chamadoRepository.saveAll(Arrays.asList(chamado1));
    }
}
