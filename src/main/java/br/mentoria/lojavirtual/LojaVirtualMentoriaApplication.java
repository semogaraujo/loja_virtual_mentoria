package br.mentoria.lojavirtual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "br.mentoria.lojavirtual.model")
@ComponentScan(basePackages = "br.*")
@EnableJpaRepositories(basePackages = "br.mentoria.lojavirtual.repository")
@EnableTransactionManagement
public class LojaVirtualMentoriaApplication {

	public static void main(String[] args) {
		 
		SpringApplication.run(LojaVirtualMentoriaApplication.class, args);		
		//System.out.println("Senha: " + new BCryptPasswordEncoder().encode("123456"));
	}
}
