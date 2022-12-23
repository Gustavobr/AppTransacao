package br.com.qintess.pojo;

import java.util.Collection;
import java.util.UUID;

import org.bson.Document;

import com.mongodb.MongoClientException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.com.qintess.DTO.PagamentoDTO;
import br.com.qintess.DTO.Tipo_Transacao;
import br.com.qintess.DTO.TransacaoDTO;

public class Transacao {

	public static void main(String[] args) {
		String uri = "mongodb://localhost:27017/api";
		MongoClient client = MongoClients.create(uri);
		MongoDatabase db = client.getDatabase("api");
		MongoCollection<Document> transacao = db.getCollection("Transacao");

		TransacaoDTO t = new TransacaoDTO();
		try {
			FindIterable<Document> count = db.getCollection("Transacao").find();
			count.forEach(doc -> {
				System.out.println(doc);
			});
			/*
			 * Document doc = new Document(); doc.append("id",
			 * UUID.randomUUID().toString()).append("tipo_transacao",
			 * Tipo_Transacao.BOLETO); transacao.insertOne(doc); System.out.println("Doc" +
			 * "\n" + " inserido.");
			 */
		} catch (MongoClientException e) {

		}

	}

}
