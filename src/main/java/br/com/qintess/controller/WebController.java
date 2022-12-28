package br.com.qintess.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.AuthenticationFailedException;
import javax.transaction.Transactional;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.MongoClientException;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

import br.com.qintess.DTO.Login;
import br.com.qintess.DTO.PagamentoDTO;
import br.com.qintess.DTO.TransacaoDTO;
import br.com.qintess.repository.TransacaoRepository;
import br.com.qintess.repository.UserRepository;
import br.com.qintess.service.MyUserDetailsService;
import br.com.qintess.service.TransacaoServiceImpl;

@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@Controller
@RequestMapping(value = "/")
@EnableCaching(mode = AdviceMode.ASPECTJ)
@EnableMongoRepositories(basePackageClasses = br.com.qintess.repository.UserRepository.class)
public class WebController {

	private static Gson gson = new Gson();
	private static MongoClient mongo = MongoClients.create("mongodb://localhost:27017/api");
	private static MongoDatabase DB = mongo.getDatabase("api");
	private static MongoCollection<Document> collection = mongo.getDatabase("api").getCollection("Transacao");
	private static MongoCollection<Document> userCollection = mongo.getDatabase("api").getCollection("user");
	private static DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private static Session httpSession;

	public static boolean isValid = false;

	public static String estabelecimento = "";
	public static double valor = 0;
	public static boolean estorno = false;

	private static final String ROOTPATH = "http://localhost:8080/";
	@Autowired

	private UserRepository userRepo;

	@Autowired
	private TransacaoRepository tranRepo;

	private MyUserDetailsService user = new MyUserDetailsService();

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@Transactional
	@SessionScope
	public ModelAndView rootPath() {
		return new ModelAndView("/login");
	}

	@PostMapping("/")
	public String redirectHome() {
		return "home";

	}

	/*
	 * @RequestMapping(value = "/error") public ModelAndView loginError(Model model)
	 * { model.addAttribute("message", model.getAttribute("message")); return new
	 * ModelAndView("redirect:/error").addObject("message", model); }
	 */

	public static void closeMongoConnection(MongoClient client) throws MongoException, Exception {
		try {
			if (!client.startSession().hasActiveTransaction() == true) {
				client.close();
			}
		} catch (MongoClientException ex) {
			throw new MongoClientException("Client NOT FOUND", ex.getCause());
		}
	}

	@PostMapping(value = "/login")
	public ModelAndView Authenticate(@RequestParam(required = true) String username,
			@RequestParam(required = true) String password, Model login, RedirectAttributes attrs)
			throws AuthenticationFailedException, Exception {
		if (username != null && !username.isBlank() == true && password != null && !password.isBlank() == true) {
			login.addAttribute("password", password);
			login.addAttribute("username", username);

			if (validLogin(username, password) == true) {
				// File sessionID = new File("c:/logs");
				// httpSession.setStoreDir(sessionID);
				// httpSession.setTimeout(Duration.ofMinutes(10));
				FindIterable<Document> documents = collection.find().filter(Filters.exists("pagamento"));

				List<Document> lista = generateListDocuments(login, documents);

				return new ModelAndView("home").addObject(lista).addObject(login);
			} else {
				String message = "Usuário ou senha inválidos.";
				return new ModelAndView("/error").addObject("message", message);
			}
		}
		return new ModelAndView("/login").addObject("login", null);

	}

	@PostMapping(value = "/register")
	@org.springframework.transaction.annotation.Transactional
	@CacheEvict(allEntries = true)
	public ModelAndView registerUser(Model model, /* BindingResult result, */
			@RequestParam(required = true, name = "username") String username,
			@RequestParam(required = true) String email, @RequestParam(required = true) String password1,
			@RequestParam(required = true) String password2) {

		try {
			// if (result.hasFieldErrors()) {
			// return new ModelAndView("/error");
			// }
			br.com.qintess.DTO.Login loginUser = new br.com.qintess.DTO.Login(UUID.randomUUID().toString(), username,
					password2);
			userCollection.insertOne(new Document().append("_id", UUID.randomUUID().toString())
					.append("username", loginUser.getUsername()).append("password", loginUser.getPassword()));
			return new ModelAndView("/");
		} catch (MongoException e) {
			e.printStackTrace();
		}
		return null;

	}

	@PostMapping(value = "/deleteDoc")
	@org.springframework.transaction.annotation.Transactional
	public ModelAndView deleteDoc(@RequestParam(value = "tipo", required = true) String tipo,
			@RequestParam(required = true, name = "estabelecimento") String estabelecimento, Model model)
			throws MongoException, MongoClientException, Exception {
		try {
			if (tipo != null && estabelecimento != null && !tipo.isBlank() == true
					&& !estabelecimento.isBlank() == true) {
				try {
					FindIterable<Document> document = collection.find(Filters.eq("tipo", tipo))
							.filter(Filters.eq("estabelecimento", estabelecimento));
					Document doc = new Document();
					// doc.append("tipo", tipo).append("estabelecimento", estabelecimento);
					// DeleteResult result = collection
					// .deleteOne(Filters.eq("estabelecimento",
					// estabelecimento.toString()).toBsonDocument());
					DeleteResult result = collection.deleteOne(Filters.eq("tipo", tipo.toString()));
					// = collection
					// .deleteOne(Filters.all("estabelecimento", estabelecimento, "tipo", tipo));
					if (result.getDeletedCount() >= 1) {
						Bson query = Filters.eq("estabelecimento", estabelecimento.toString());
						collection.deleteOne(query);
						FindIterable<Document> lista = collection.find().filter(Filters.exists("tipo"));
						List<Document> list_result = generateListDocuments(model, lista);
						model.addAttribute("lista", list_result);
						return new ModelAndView("home").addObject("lista", list_result);
					}
				} catch (MongoException ex) {
					throw new RuntimeException(ex.getCause());
				}
				// collection.deleteMany(Filters.exists(estabelecimento,
				// true),Filters.eq(tipo));
			}
		} catch (Exception ex) {

		}
		return new ModelAndView("/error");
	}

	private List<Document> generateListDocuments(Model login, FindIterable<Document> documents) {
		Collection<Document> list = new ArrayList<>();

		for (Document doc : documents) {
			String tipo = doc.getString("tipo");
			Document pagamento = (Document) doc.get("pagamento");

			if (pagamento != null) {
				if (pagamento.containsKey("valor")) {
					valor = Double.parseDouble(pagamento.get("valor").toString());
				}
				if (pagamento.containsKey("estabelecimento")) {
					estabelecimento = (String) pagamento.get("estabelecimento");
				}
				if (pagamento.containsKey("estorno")) {
					estorno = Boolean.parseBoolean(pagamento.get("estorno").toString());
				}
				list.add(new Document("tipo", tipo).append("pagamento", pagamento)
						.append("estabelecimento", estabelecimento).append("valor", valor).append("estorno", estorno));
			}

		}

		List<Document> lista = new ArrayList<>();
		for (Document doc : list) {
			login.addAttribute("document", doc);
			lista.add(doc);

		}
		login.addAttribute("lista", lista);
		return lista;
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public ModelAndView registerUser() {
		return new ModelAndView("/register");

	}

	@PostMapping(value = "/save")
	@org.springframework.transaction.annotation.Transactional(transactionManager = "saveTransacao")
	@ResponseStatus(code = HttpStatus.CREATED)

	public ModelAndView saveTransacao(
			/* @Valid BindingResult result, */ @RequestParam(required = true) String tipo_transacao,
			@RequestParam(required = true) String estabelecimento, @RequestParam(required = true) String valor,
			@RequestParam(required = true) String estorno, Model login) throws Exception {
		// if (!result.hasErrors() == true) {
		PagamentoDTO pagamento = new PagamentoDTO(estabelecimento, Double.parseDouble(valor), Boolean.valueOf(estorno));
		String tipo = String.valueOf(tipo_transacao);
		TransacaoDTO transacaoDTO = new TransacaoDTO(tipo_transacao.toString(), pagamento);
		try {
			Document pag = new Document();
			pag.append("estabelecimento", pagamento.getEstabelecimento()).append("valor", pagamento.getValor())
					.append("estorno", false);
			Document tran = new Document();
			tran.append("tipo", transacaoDTO.getTipo().toString()).append("pagamento", pag);
			try {

				collection.insertOne(tran);
				FindIterable<Document> documents = collection.find().filter(Filters.exists("pagamento"));
				login.addAttribute("username", login.getAttribute("username"));
				List<Document> lista = generateListDocuments(login, documents);
				return new ModelAndView("/home").addObject(lista);
			} catch (MongoException e) {
				throw new MongoException("error", e.getCause());
			}
		} catch (Exception ex) {

		}
		return new ModelAndView("/error");

	}

	private boolean validLogin(String username, String password) {
		if (!username.isBlank() && !password.isBlank() == true) {
			Login login = new Login();
			// login.setId(UUID.randomUUID().toString());
			login.setId("ObjectId(\"6380cc73d99e078eb1069a79\")");
			login.setPassword(password);
			login.setUsername(username);

			try {
				Document document = new Document();
				document.append("username", username.toString()).append("password", password.toString()).append("_id",
						login.getId());
				FindIterable<?> list = collection.find(document);
				Document result = new Document(list.explain());

				JsonElement element = JsonParser.parseString(result.toJson());
				JsonObject obj = element.getAsJsonObject();
				JsonElement elementDocument = JsonParser.parseString(obj.toString());
				JsonObject filter = elementDocument.getAsJsonObject().get("command").getAsJsonObject().get("filter")
						.getAsJsonObject();
				String _username = filter.get("username").getAsString();
				String _password = filter.get("password").getAsString();

				try {
					java.util.List<Document> resp = Stream.of(result).collect(Collectors.toList());
					System.out.println(resp);
					List<Document> loginResult = (List<Document>) resp.stream()
							.filter(l -> resp.toString().contains(username) && resp.toString().contains(password))
							.collect(Collectors.toList());
					Document docUsername = (Document) userCollection.find(Filters.eq("username", username)).first();
					Document docPassword = (Document) userCollection.find(Filters.eq("password", password)).first();

					if (docUsername != null && docPassword != null) {
						isValid = true;
						return isValid;
					}

				} catch (Exception e) {
					throw new UsernameNotFoundException(username);
				}

			} catch (

			Exception e) {
				e.printStackTrace();
			}

		}
		isValid = false;
		return isValid;
	}

	@SessionScope(proxyMode = ScopedProxyMode.NO)
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Model model) {
		FindIterable<Document> documents = collection.find().filter(Filters.exists("pagamento"));
		List<Document> lista = generateListDocuments(model, documents);
		return "home";
	}

	@RequestMapping(value = "/pagamento", method = RequestMethod.GET)
	@org.springframework.transaction.annotation.Transactional
	public String pagamento() {
		return "pagamento";
	}

	@Autowired
	private TransacaoServiceImpl service;

	@PostMapping(value = "add", produces = MediaType.TEXT_HTML_VALUE)
	@Transactional
	@CacheEvict(allEntries = true)
	@ResponseStatus(code = HttpStatus.CREATED)
	@ResponseBody
	public ResponseEntity<TransacaoDTO> addTransacaoDTO(
			@RequestParam(required = true, value = "tipo_transacao") String tipo_transacao,
			@RequestParam(required = true, value = "estabelecimento") String estabelecimento,
			@RequestParam(required = false, value = "valor") String valor) throws Exception {
		if (tipo_transacao != null && !tipo_transacao.isEmpty() == true) {
			// var pagamento =
			// var tipo = tipo_transacao;
			PagamentoDTO pagamento = new PagamentoDTO(estabelecimento, Double.parseDouble(valor), false);
			TransacaoDTO tran = new TransacaoDTO();
			tran.setId(UUID.randomUUID());
			tran.setTipo(tipo_transacao);
			tran.setPagamento(pagamento);

			service.createTransacao(new TransacaoDTO());
			return ResponseEntity.status(HttpStatus.CREATED).build();

		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

	}

}
