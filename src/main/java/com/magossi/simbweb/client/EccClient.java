package com.magossi.simbweb.client;

import java.net.URI;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.magossi.simbweb.domain.bovino.Ecc;


@Component
public class EccClient {
	
	private RestTemplate restTemplate = new RestTemplate();
	private String URI_BASE = "http://192.168.0.100:8080/ecc";
	
	
	public String deletar(Long codigo){
		
		RequestEntity<Void> request = RequestEntity
				.delete(URI.create(URI_BASE + "/" + codigo))
				.build();
		
		ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
		
		return response.getStatusCode().toString();
	}


	public String alterar(Ecc ecc) {
		
		RequestEntity<Ecc> request = RequestEntity
				.put(URI.create(URI_BASE))
				.body(ecc);
		
		ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
		
		return response.getHeaders().getLocation().toString();
		
	}

}
