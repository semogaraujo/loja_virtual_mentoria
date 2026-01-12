package br.mentoria.lojavirtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.mentoria.lojavirtual.model.Acesso;
import br.mentoria.lojavirtual.repository.AcessoRepository;

@Service
public class AcessoService {

	@Autowired
	private AcessoRepository acessoRepository;
	
	public Acesso save(Acesso acesso) {
		
		/*Qualquer tipo de validacao*/
		return acessoRepository.save(acesso);
	}
}
