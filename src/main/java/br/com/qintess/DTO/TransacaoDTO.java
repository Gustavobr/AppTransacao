package br.com.qintess.DTO;

import java.util.UUID;

import javax.persistence.Entity;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@Document(collection = "transacao")
@Entity
public class TransacaoDTO {

	public TransacaoDTO(String tipo_transacao, PagamentoDTO pagamentoDTO) {

	}

	@Field(name = "ID")
	private UUID id;
	@Field(name = "TIPO_TRANSACAO")
	private String tipo;
	// @Field(name = "PAGAMENTOS")
	@DBRef
	private PagamentoDTO pagamento;

	public TransacaoDTO() {
		// TODO Auto-generated constructor stub
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo_Transacao) {
		this.tipo = tipo_Transacao;
	}

	public PagamentoDTO getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoDTO pagamento) {
		this.pagamento = pagamento;
	}

}
