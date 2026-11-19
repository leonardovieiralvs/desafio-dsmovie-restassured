package com.devsuperior.dsmovie.controllers;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.devsuperior.dsmovie.tests.TokenUtil;

public class ScoreControllerRA {

	private Long existingMovieId;
	private Long nonExistingMovieId;
	private String adminToken;
	private String clientToken;
	private JSONObject scoreWithNonexistentMovie;
	private JSONObject scoreWithMissingMovieId;
	private JSONObject scoreWithNegativeValue;

	@BeforeEach
	public void setUp() throws JSONException {
		baseURI = "http://localhost:8080";

		existingMovieId = 1L;
		nonExistingMovieId = 1000L;

		adminToken = TokenUtil.obtainAccessToken("maria@gmail.com", "123456");
		clientToken = TokenUtil.obtainAccessToken("alex@gmail.com", "123456");

		scoreWithNonexistentMovie = new JSONObject();
		scoreWithNonexistentMovie.put("movieId", nonExistingMovieId);
		scoreWithNonexistentMovie.put("score", 4.0);

		scoreWithMissingMovieId = new JSONObject();
		scoreWithMissingMovieId.put("score", 4.0);

		scoreWithNegativeValue = new JSONObject();
		scoreWithNegativeValue.put("movieId", existingMovieId);
		scoreWithNegativeValue.put("score", -1.0);
	}
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		given()
			.header("Authorization", "Bearer " + clientToken)
			.contentType("application/json")
			.body(scoreWithNonexistentMovie.toJSONString())
		.when()
			.put("/scores")
		.then()
			.statusCode(404)
			.body("status", is(404))
			.body("error", equalTo("Recurso não encontrado"))
			.body("path", equalTo("/scores"));
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		given()
			.header("Authorization", "Bearer " + clientToken)
			.contentType("application/json")
			.body(scoreWithMissingMovieId.toJSONString())
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
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		given()
			.header("Authorization", "Bearer " + clientToken)
			.contentType("application/json")
			.body(scoreWithNegativeValue.toJSONString())
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
