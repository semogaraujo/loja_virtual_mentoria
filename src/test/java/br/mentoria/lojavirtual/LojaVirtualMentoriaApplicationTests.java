package br.mentoria.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.mentoria.lojavirtual.controller.AcessoController;
import br.mentoria.lojavirtual.model.Acesso;
import br.mentoria.lojavirtual.repository.AcessoRepository;
import br.mentoria.lojavirtual.service.AcessoService;

@SpringBootTest(classes = LojaVirtualMentoriaApplication.class)
public class LojaVirtualMentoriaApplicationTests {

	//@Autowired
	//private AcessoService acessoService;
	
	@Autowired
	private AcessoController acessoController;	
		
	@Test
	public void testCadastroAcesso() {
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_ADMIN");
		
		acessoController.salvarAcesso(acesso);
		
	}

}
