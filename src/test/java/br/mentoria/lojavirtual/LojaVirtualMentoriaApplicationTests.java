package br.mentoria.lojavirtual;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.mentoria.lojavirtual.controller.AcessoController;
import br.mentoria.lojavirtual.model.Acesso;
import br.mentoria.lojavirtual.repository.AcessoRepository;
import junit.framework.TestCase;

@SpringBootTest(classes = LojaVirtualMentoriaApplication.class)
public class LojaVirtualMentoriaApplicationTests extends TestCase{
	
	@Autowired
	private AcessoController acessoController;	
	
	@Autowired
	private AcessoRepository acessoRepository;	
		
	@Autowired
	private WebApplicationContext wac;
	
	
	
	@Test
	public void testRestApiCadastroAcesso() throws JsonProcessingException, Exception {
		
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_COMPRADOR");
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc.perform(MockMvcRequestBuilders.post("/salvarAcesso")
									.content(objectMapper.writeValueAsString(acesso))
									.contentType(MediaType.APPLICATION_JSON) //define o tipo
									.accept(MediaType.APPLICATION_JSON)); //depois define o que espera na resposta
				
		System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
		
		/*Converter o retorno da API para um objeto de acesso*/
		
		Acesso objRetorno = objectMapper
							.readValue(retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);
		
		assertEquals(acesso.getDescricao(), objRetorno.getDescricao());
		
	}

	@Test
	public void testRestApiDeletePorIdAcesso() throws JsonProcessingException, Exception {
		
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_TESTE_DELETE_ID");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc.perform(MockMvcRequestBuilders.delete("/deleteAcessoPorId/" + acesso.getId())
									.content(objectMapper.writeValueAsString(acesso))
									.contentType(MediaType.APPLICATION_JSON) //define o tipo
									.accept(MediaType.APPLICATION_JSON)); //depois define o que espera na resposta
				
		System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
		System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getStatus());
		
		assertEquals("Acesso removido com sucesso", retornoApi.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
				
	}

	@Test
	public void testRestApipesquisaAcessoPorId() throws JsonProcessingException, Exception {
		
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_TESTE_PESQUISA_ID");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc.perform(MockMvcRequestBuilders.get("/pesquisaAcessoPorId/" + acesso.getId())
									.content(objectMapper.writeValueAsString(acesso))
									.contentType(MediaType.APPLICATION_JSON) //define o tipo
									.accept(MediaType.APPLICATION_JSON)); //depois define o que espera na resposta	
		
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
		Acesso acessoRetorno = objectMapper.readValue(retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);
			
		assertEquals(acesso.getDescricao(), acessoRetorno.getDescricao());
		assertEquals(acesso.getId(), acessoRetorno.getId());
	}
	
	@Test
	public void testRestApipesquisaAcessoPorDescricao() throws JsonProcessingException, Exception {
		
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_TESTE_PESQUISA_LIST_DESC");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc.perform(MockMvcRequestBuilders.get("/pesquisaAcessoPorDescricao/PESQUISA_LIST")
									.content(objectMapper.writeValueAsString(acesso))
									.contentType(MediaType.APPLICATION_JSON) //define o tipo
									.accept(MediaType.APPLICATION_JSON)); //depois define o que espera na resposta	
		
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
		
		
		List<Acesso> retornoApiList = objectMapper.readValue(retornoApi.andReturn().getResponse().getContentAsString(), new TypeReference<List<Acesso>>(){});
		
		assertEquals(1, retornoApiList.size());
		
		assertEquals(acesso.getDescricao(), retornoApiList.get(0).getDescricao());
		
		assertEquals(acesso.getDescricao(), retornoApiList.getFirst().getDescricao());
		
		acessoRepository.deleteById(acesso.getId());
				
	}
	
	@Test
	public void testRestApiDeleteAcesso() throws JsonProcessingException, Exception {
		
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_TESTE_DELETE");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc.perform(MockMvcRequestBuilders.post("/deleteAcesso")
									.content(objectMapper.writeValueAsString(acesso))
									.contentType(MediaType.APPLICATION_JSON) //define o tipo
									.accept(MediaType.APPLICATION_JSON)); //depois define o que espera na resposta
				
		System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
		System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getStatus());
		
		assertEquals("Acesso removido com sucesso", retornoApi.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
	}
		
	
	@Test
	public void testCadastroAcesso() {
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_ADMIN");
		
		assertEquals(true, acesso.getId() == null);
		
		acesso = acessoController.salvarAcesso(acesso).getBody();
		
		assertEquals(true, acesso.getId() > 0);
		
		/*Validar dados salvos da forma correta*/
		assertEquals("ROLE_ADMIN", acesso.getDescricao());
		
		/*Teste de carregamento*/
	
		Acesso acesso2 = acessoRepository.findById(acesso.getId()).get();
		
		assertEquals(acesso.getId(), acesso2.getId());
		
		
		/*Teste de delete*/
		acessoRepository.deleteById(acesso2.getId());
		
		acessoRepository.flush(); /*Roda esse SQL e delete no banco de dados*/
		
		Acesso acesso3 = acessoRepository.findById(acesso2.getId()).orElse(null);
		
		assertEquals(true, acesso3 == null);
		
		/*Teste de query*/
		
		acesso = new Acesso();
		
		acesso.setDescricao("ROLE_ALUNO");
		
		acesso = acessoController.salvarAcesso(acesso).getBody();
		List<Acesso> acessos = acessoRepository.buscaAcessoDesc("ALUNO".trim().toUpperCase());
		
		assertEquals(1, acessos.size());	
		
		acessoRepository.deleteById(acesso.getId());
	}

}
