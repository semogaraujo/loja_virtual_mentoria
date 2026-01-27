package br.mentoria.lojavirtual.controller;

import java.net.URI;
import java.util.List;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.mentoria.lojavirtual.model.Acesso;
import br.mentoria.lojavirtual.security.dto.AcessoDTO;
import br.mentoria.lojavirtual.service.AcessoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/acessos")
public class AcessoController {

	private final AcessoService acessoService;

	public AcessoController(AcessoService acessoService) {
		this.acessoService = acessoService;
	}

	@ResponseBody /* Poder dar um retorno da API */
	@PostMapping(value = "/salvarAcesso", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Acesso> salvarAcesso(@Valid @RequestBody AcessoDTO dto){
						
		Acesso salvo = acessoService.saveFromDto(dto);
		URI location = URI.create("/loja_virtual_mentoria/acessos" + salvo.getId());

		return ResponseEntity.created(location).body(salvo);

	}

	@DeleteMapping("/deleteAcessoPorId/{id}")
	public ResponseEntity<String> deleteAcessoPorId(@PathVariable Long id) {
		acessoService.deleteById(id);
		return ResponseEntity.ok("Acesso removido com sucesso");
	}

	@GetMapping("/pesquisaAcessoPorId/{id}")
	public ResponseEntity<Acesso> pesquisaAcessoPorId(@PathVariable Long id) {
	    return acessoService.findById(id)
	        .map(ResponseEntity::ok)
	        .orElseThrow(() -> new ResourceNotFoundException("Registro não encontrado com o ID: " + id));
	}

	
	@GetMapping("/pesquisaAcessoPorDescricao/{desc}")
	public ResponseEntity<List<Acesso>> pesquisaAcessoPorDescricao(@PathVariable String desc) {
		return ResponseEntity.ok(acessoService.findByDescricao(desc));
	}	
}