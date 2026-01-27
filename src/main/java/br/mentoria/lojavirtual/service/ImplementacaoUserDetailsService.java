package br.mentoria.lojavirtual.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.mentoria.lojavirtual.model.Usuario;
import br.mentoria.lojavirtual.repository.UsuarioRepository;

@Service
public class ImplementacaoUserDetailsService implements UserDetailsService {

	private UsuarioRepository usuarioRepository;

	public ImplementacaoUserDetailsService(UsuarioRepository usuarioRepository) {

		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = usuarioRepository.findByUsername(username); /* Recebe o login para consulta */

		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário não encontrado" + username);
		}

		return usuario;
	}

}
