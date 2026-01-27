package br.mentoria.lojavirtual.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.mentoria.lojavirtual.model.Acesso;
import br.mentoria.lojavirtual.repository.AcessoRepository;
import br.mentoria.lojavirtual.security.dto.AcessoDTO;

@Service
public class AcessoService {

	private final AcessoRepository acessoRepository;

	public AcessoService(AcessoRepository acessoRepository) {
		this.acessoRepository = acessoRepository;
	}

	public Acesso save(Acesso acesso) {
		return acessoRepository.save(acesso);
	}
	public void deleteById(Long id) {
		acessoRepository.deleteById(id);
	}

	public Optional<Acesso> findById(Long id) {
		return acessoRepository.findById(id);
	}

	public List<Acesso> findByDescricao(String descricao) {
		return acessoRepository.buscaAcessoDesc(descricao);
	}

	public Acesso saveFromDto(AcessoDTO dto) {
		Acesso acesso = new Acesso();
		acesso.setDescricao(dto.getDescricao());
		return acessoRepository.save(acesso);
	}

}
