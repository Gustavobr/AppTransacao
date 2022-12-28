package br.com.qintess.DTO;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@Document(collection = "pagamento")
@Entity
public class PagamentoDTO {

	public PagamentoDTO() {

	}

	@Override
	public int hashCode() {
		return Objects.hash(estabelecimento, estorno, id, valor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PagamentoDTO other = (PagamentoDTO) obj;
		return Objects.equals(estabelecimento, other.estabelecimento) && estorno == other.estorno
				&& Objects.equals(id, other.id)
				&& Double.doubleToLongBits(valor) == Double.doubleToLongBits(other.valor);
	}

	@Override
	public String toString() {
		return "PagamentoDTO [id=" + id + ", valor=" + valor + ", estorno=" + estorno + "]";
	}

	public PagamentoDTO(@NotNull String estabelecimento, @NotNull double valor, @NotNull boolean estorno) {
		super();
		this.estabelecimento = estabelecimento;
		this.valor = valor;
		this.estorno = estorno;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// @Column(name = "ID", unique = true)
	@Field(name = "ID")
	private UUID id;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getEstabelecimento() {
		return estabelecimento;
	}

	public void setEstabelecimento(String estabelecimento) {
		this.estabelecimento = estabelecimento;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public boolean isEstorno() {
		return estorno;
	}

	public void setEstorno(boolean estorno) {
		this.estorno = estorno;
	}

	// @NotNull
	// @Column(name = "ESTABELECIMENTO", updatable = true)
	@Field(name = "ESTABELECIMENTO")
	private String estabelecimento;
	// @NotNull
	// @Column(name = "VALOR", updatable = true)
	@Field(name = "VALOR")
	private double valor;
	// @NotNull
	// @Column(name = "ESTORNO", updatable = true)
	@Field(name = "ESTORNO")
	private boolean estorno;

}
