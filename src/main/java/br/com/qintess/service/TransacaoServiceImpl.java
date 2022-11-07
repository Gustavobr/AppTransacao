package br.com.qintess.service;

import java.util.Collection;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.com.qintess.DTO.PagamentoDTO;
import br.com.qintess.DTO.TransacaoDTO;
import br.com.qintess.repository.TransacaoRepository;

@Service
@Component
public class TransacaoServiceImpl implements TransacaoService {
	public TransacaoServiceImpl() {

	}

	private Gson gson;
	private JsonParser parser;
	@Autowired

	private TransacaoRepository tranRepo;

	private static MongoClient mongo = MongoClients.create("mongodb://localhost:27017/api");
	private static MongoDatabase DB = mongo.getDatabase("api");
	private static MongoCollection<Document> collection = mongo.getDatabase("api").getCollection("Transacao");

	@Override
	public void createTransacao(TransacaoDTO transacao) {
		if (transacao != null) {
			String jsonResp = gson.toJson(transacao);
			JsonElement element = parser.parse(jsonResp);
			JsonObject obj = element.getAsJsonObject();
			String pagamento = obj.get("pagamento").getAsString();
			if(pagamento != null && !pagamento.isEmpty() == true) {
				JsonObject objPagamento = obj.get("pagamento").getAsJsonObject();
				String estabelecimento = objPagamento.get("estabelecimento").getAsString();
				String id = objPagamento.get("id").getAsString();
				double valor = Double.parseDouble(objPagamento.get("valor").getAsString());
				boolean estorno = Boolean.valueOf(objPagamento.get("estorno").getAsString());
				
				Document doc = new Document();
				doc.append("id", transacao.getId())
							.append("tipo_transacao", transacao.getTipo())
							.append("pagamento:{", null)
							.append(id, id)
							.append("estabelecimento", estabelecimento)
							.append("valor", valor)
							.append("estorno", estorno);
				try {
					collection.insertOne(doc);
				}catch(Exception ex) {
					
				}
							
			}
			
		}

	}

	@Override
	public void updateTransacao(UUID id, PagamentoDTO pagamento) {
		if (id != null && pagamento != null) {
			TransacaoDTO oldTransacao;
			// TransacaoDTO newTransacao;
			try {
				oldTransacao = tranRepo.findById(id).get();
				oldTransacao
						.setPagamento(new PagamentoDTO(pagamento.getEstabelecimento(), pagamento.getValor(), false));
				oldTransacao.setId(pagamento.getId());
				tranRepo.insert(oldTransacao);
			} catch (Exception e) {
				throw new MessagingException((Message<?>) e.getCause());
			}
		}

	}

	@Override
	public void deleteTransacao(UUID id) {
		TransacaoDTO oldTransacao;
		oldTransacao = tranRepo.findById(id).get();
		if (oldTransacao != null) {
			tranRepo.delete(oldTransacao);
		}

	}

	@Override
	public Collection<TransacaoDTO> findAll() {

		return null;
	}

	@Override
	public void deleteTransacao(TransacaoDTO transacao) {

	}

}
