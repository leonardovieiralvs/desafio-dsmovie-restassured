package com.devsuperior.dsmovie.controllers;

import static io.restassured.RestAssured.baseURI;

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
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {		
	}
}
