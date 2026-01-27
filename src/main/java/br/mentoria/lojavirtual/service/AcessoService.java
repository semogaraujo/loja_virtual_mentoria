package br.mentoria.lojavirtual.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.mentoria.lojavirtual.exception.LojaVirtualException;
import br.mentoria.lojavirtual.model.Acesso;
import br.mentoria.lojavirtual.repository.AcessoRepository;
import br.mentoria.lojavirtual.repository.UsuarioRepository;
import br.mentoria.lojavirtual.security.dto.AcessoDTO;

@Service
public class AcessoService {

	private final UsuarioRepository usuarioRepository;

	private final AcessoRepository acessoRepository;

	public AcessoService(AcessoRepository acessoRepository, UsuarioRepository usuarioRepository) {
		this.acessoRepository = acessoRepository;
		this.usuarioRepository = usuarioRepository;
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

		List<Acesso> acessos = acessoRepository.buscaAcessoDesc(dto.getDescricao());

		for (Acesso acesso : acessos) {

			if (acesso.getDescricao().toUpperCase().equals(dto.getDescricao().toUpperCase())) {
				throw new LojaVirtualException("Já existe Acesso com a descrição: " + dto.getDescricao());
			}
		}

		Acesso acesso = new Acesso();
		acesso.setDescricao(dto.getDescricao().toUpperCase());
		return acessoRepository.save(acesso);
	}

}
