package br.mentoria.lojavirtual.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "usuario")
@SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", initialValue = 1, allocationSize = 1)
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
	private Long id;

	private String login;

	private String senha;

	private LocalDate dataAtualSenha;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "usuarios_acesso", 
			uniqueConstraints = @UniqueConstraint(columnNames = { "usuario_id",	"acesso_id" }, 
			name = "unique_acesso_user"), 
			
			joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id", table = "usuario", 
			unique = false, foreignKey = @ForeignKey(name = "usuario_fk", value = ConstraintMode.CONSTRAINT)), 
	
	inverseJoinColumns = @JoinColumn(name = "acesso_id", 
			unique = false, referencedColumnName = "id", table = "acesso", 
			foreignKey = @ForeignKey(name = "acesso_fk", value = ConstraintMode.CONSTRAINT)))
	private List<Acesso> acessos;

	/*
	 * Autoridades = são os acessos ou seja ROLE_ADMIN, ROLE,_SECRETARIO,
	 * ROLE_FINANCEIRIO
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return this.acessos;
	}

	@Override
	public @Nullable String getPassword() {

		return this.senha;
	}

	@Override
	public String getUsername() {

		return this.login;
	}

}
