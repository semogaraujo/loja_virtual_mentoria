package br.mentoria.lojavirtual.security.dto;

public class AcessoDTO {
	
	@jakarta.validation.constraints.NotBlank(message = "descricao é obrigatória")
    private String descricao;
	
    public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}        
  

}
