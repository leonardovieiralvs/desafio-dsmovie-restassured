package com.devsuperior.dsmovie.controllers;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.devsuperior.dsmovie.tests.TokenUtil;

public class MovieControllerRA {

	private Long existingMovieId;
	private Long nonExistingMovieId;
	private String movieTitle;
	private String adminToken;
	private String clientToken;
	private String invalidToken;
	private JSONObject movieWithValidTitle;
	private JSONObject movieWithBlankTitle;

	@BeforeEach
	public void setUp() throws JSONException {
		baseURI = "http://localhost:8080";

		existingMovieId = 1L;
		nonExistingMovieId = 1000L;
		movieTitle = "The Witcher";

		adminToken = TokenUtil.obtainAccessToken("maria@gmail.com", "123456");
		clientToken = TokenUtil.obtainAccessToken("alex@gmail.com", "123456");
		invalidToken = "invalid_token";

		movieWithValidTitle = new JSONObject();
		movieWithValidTitle.put("title", "Test Movie");
		movieWithValidTitle.put("score", 0.0);
		movieWithValidTitle.put("count", 0);
		movieWithValidTitle.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");

		movieWithBlankTitle = new JSONObject();
		movieWithBlankTitle.put("title", "");
		movieWithBlankTitle.put("score", 0.0);
		movieWithBlankTitle.put("count", 0);
		movieWithBlankTitle.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");
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
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {	
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {		
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
	}
}
