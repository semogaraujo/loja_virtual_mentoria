package br.mentoria.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import br.mentoria.lojavirtual.model.Acesso;
import br.mentoria.lojavirtual.service.AcessoService;

@Controller
public class AcessoController {

	@Autowired
	private AcessoService acessoService;
	
	@PostMapping("/salvarAcesso")
	public Acesso salvarAcesso(Acesso acesso) {
		
		return acessoService.save(acesso);
	}
}
