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

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Document(collection = "transacao")
@Entity
public class TransacaoDTO {

	public TransacaoDTO() {

	}

	@Field(name = "ID")
	private UUID id;
	@Field(name = "TIPO_TRANSACAO")
	private Tipo_Transacao tipo;
	// @Field(name = "PAGAMENTOS")
	@DBRef
	private PagamentoDTO pagamento;

	public TransacaoDTO(Tipo_Transacao tipo, PagamentoDTO pagamento) {
		super();
		this.tipo = tipo;
		this.pagamento = pagamento;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Tipo_Transacao getTipo() {
		return tipo;
	}

	public void setTipo(Tipo_Transacao tipo) {
		this.tipo = tipo;
	}

	public PagamentoDTO getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoDTO pagamento) {
		this.pagamento = pagamento;
	}

}
