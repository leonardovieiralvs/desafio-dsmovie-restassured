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

public class ScoreControllerRA {

	private Long existingMovieId;
	private Long nonExistingMovieId;
	private String clientToken;
	private Map<String, Object> putScoreInstance;

	@BeforeEach
	public void setUp() throws JSONException {
		baseURI = "http://localhost:8080";

		existingMovieId = 1L;
		nonExistingMovieId = 1000L;

		clientToken = TokenUtil.obtainAccessToken("alex@gmail.com", "123456");

		putScoreInstance = new HashMap<>();
		putScoreInstance.put("movieId", existingMovieId);
		putScoreInstance.put("score", 4.0);
	}

	@Test
	public void saveScoreShouldReturnOkWhenClientLoggedAndValidData() {
		JSONObject newObject = new JSONObject(putScoreInstance);

		given()
				.header("Authorization", "Bearer " + clientToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(newObject.toJSONString())
				.when()
				.put("/scores")
				.then()
				.statusCode(200)
				.body("id", is(existingMovieId.intValue()))
				.body("score", notNullValue())
				.body("count", greaterThanOrEqualTo(1));
	}

	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() {
		putScoreInstance.put("movieId", nonExistingMovieId);
		JSONObject newObject = new JSONObject(putScoreInstance);

		given()
				.header("Authorization", "Bearer " + clientToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(newObject.toJSONString())
				.when()
				.put("/scores")
				.then()
				.statusCode(404)
				.body("status", is(404))
				.body("error", equalTo("Recurso não encontrado"))
				.body("path", equalTo("/scores"));
	}

	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() {
		putScoreInstance.remove("movieId");
		JSONObject newObject = new JSONObject(putScoreInstance);

		given()
				.header("Authorization", "Bearer " + clientToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(newObject.toJSONString())
				.when()
				.put("/scores")
				.then()
				.statusCode(422)
				.body("status", is(422))
				.body("error", equalTo("Dados inválidos"))
				.body("path", equalTo("/scores"))
				.body("errors.fieldName", hasItem("movieId"))
				.body("errors.message", hasItem("Campo requerido"));
	}

	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() {
		putScoreInstance.put("score", -1.0);
		JSONObject newObject = new JSONObject(putScoreInstance);

		given()
				.header("Authorization", "Bearer " + clientToken)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(newObject.toJSONString())
				.when()
				.put("/scores")
				.then()
				.statusCode(422)
				.body("status", is(422))
				.body("error", equalTo("Dados inválidos"))
				.body("path", equalTo("/scores"))
				.body("errors.fieldName", hasItem("score"))
				.body("errors.message", hasItem("Valor mínimo 0"));
	}
}