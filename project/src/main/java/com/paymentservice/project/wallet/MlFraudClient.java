package com.paymentservice.project.wallet;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MlFraudClient {
	
	private final WebClient webClient;

    public MlFraudClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public double getFraudScore(FraudFeatureRequest request) {

        FraudScoreResponse response = webClient.post()
                .uri("http://localhost:8000/predict")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(FraudScoreResponse.class)
                .block(); // blocking for simplicity

        return response.getFraudScore();
    }
}
