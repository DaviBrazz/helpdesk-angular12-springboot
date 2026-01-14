package com.project.helpdesk;

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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class HelpdeskApplication implements CommandLineRunner {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private TecnicoRepository tecnicoRepository;

	@Autowired
	private ChamadoRepository chamadoRepository;

	public static void main(String[] args) {
		SpringApplication.run(HelpdeskApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Tecnico tecnico1 = new Tecnico(null, "Valdir Cezar", "12398734576", "valdircezar@yahoo.com", "thebigvaldir");
		tecnico1.addPerfil(Perfil.ADMIN);

		Cliente cliente1 = new Cliente(null, "Ernesto Freitas Barros","88733466578","ernestob@gmail.com", "432");
		cliente1.addPerfil(Perfil.CLIENTE);

		Chamado chamado1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO,"Chamado 01", "Primeiro chamado", tecnico1, cliente1);

		tecnicoRepository.saveAll(Arrays.asList(tecnico1));
		clienteRepository.saveAll(Arrays.asList(cliente1));
		chamadoRepository.saveAll(Arrays.asList(chamado1));
	}
}
