package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MovieControllerRA {

	private Long existingMovieId;
	private Long nonExistingMovieId;
	private String movieTitle;
	private String adminToken;
	private String clientToken;
	private String invalidToken;
	private Map<String, Object> postMovieInstance;

	@BeforeEach
	public void setUp() throws JSONException {
		baseURI = "http://localhost:8080";

		existingMovieId = 1L;
		nonExistingMovieId = 1000L;
		movieTitle = "The Witcher";

		adminToken = TokenUtil.obtainAccessToken("maria@gmail.com", "123456");
		clientToken = TokenUtil.obtainAccessToken("alex@gmail.com", "123456");
		invalidToken = "invalid_token";

		postMovieInstance = new HashMap<>();
		postMovieInstance.put("title", "Test Movie");
		postMovieInstance.put("score", 0.0);
		postMovieInstance.put("count", 0);
		postMovieInstance.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");
	}

	@Test
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
		given()
				.get("/movies")
				.then()
				.statusCode(200)
				.body("content", not(empty()))
				.body("content.title", hasItems("The Witcher", "Venom: Tempo de Carnificina"));
	}

	@Test
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {
		given()
				.queryParam("title", movieTitle)
				.queryParam("page", 0)
				.queryParam("size", 12)
				.get("/movies")
				.then()
				.statusCode(200)
				.body("content.size()", is(1))
				.body("content[0].id", is(existingMovieId.intValue()))
				.body("content[0].title", equalTo(movieTitle));
	}

	@Test
	public void findByIdShouldReturnMovieWhenIdExists() {
		given()
				.get("/movies/{id}", existingMovieId)
				.then()
				.statusCode(200)
				.body("id", is(existingMovieId.intValue()))
				.body("title", equalTo(movieTitle));
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
		given()
				.get("/movies/{id}", nonExistingMovieId)
				.then()
				.statusCode(404)
				.body("status", is(404))
				.body("error", equalTo("Recurso não encontrado"))
				.body("path", equalTo("/movies/" + nonExistingMovieId));
	}

	@Test
	public void insertShouldReturnCreatedWhenAdminLoggedAndValidData() {
		JSONObject newObject = new JSONObject(postMovieInstance);

		given()
				.header("Authorization", "Bearer " + adminToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(newObject.toJSONString())
				.when()
				.post("/movies")
				.then()
				.statusCode(201)
				.body("id", notNullValue())
				.body("title", equalTo(postMovieInstance.get("title")))
				.body("image", equalTo(postMovieInstance.get("image")));
	}

	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() {
		postMovieInstance.put("title", "");
		JSONObject newObject = new JSONObject(postMovieInstance);

		given()
				.header("Authorization", "Bearer " + adminToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(newObject.toJSONString())
				.when()
				.post("/movies")
				.then()
				.statusCode(422)
				.body("status", is(422))
				.body("error", equalTo("Dados inválidos"))
				.body("path", equalTo("/movies"))
				.body("errors.fieldName", hasItem("title"))
				.body("errors.message", hasItem("Campo requerido"));
	}

	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() {
		JSONObject newObject = new JSONObject(postMovieInstance);

		given()
				.header("Authorization", "Bearer " + clientToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(newObject.toJSONString())
				.when()
				.post("/movies")
				.then()
				.statusCode(403);
	}

	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() {
		JSONObject newObject = new JSONObject(postMovieInstance);

		given()
				.header("Authorization", "Bearer " + invalidToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(newObject.toJSONString())
				.when()
				.post("/movies")
				.then()
				.statusCode(401);
	}
}