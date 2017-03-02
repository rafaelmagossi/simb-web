package com.magossi.simbweb.client;

import java.net.URI;
import java.util.Arrays;
import java.util.List;


import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.magossi.simbweb.domain.bovino.Bovino;
import com.magossi.simbweb.domain.bovino.Fazenda;
import com.magossi.simbweb.domain.bovino.Pelagem;
import com.magossi.simbweb.domain.bovino.Proprietario;
import com.magossi.simbweb.domain.bovino.Raca;



@Component
public class BovinoClient {
	
	private RestTemplate restTemplate = new RestTemplate();
	private String URI_BASE = "http://192.168.0.100:8080";
	private String URN_BASE = "/bovino/ativos";
	private String FINAL_URI_BASE = URI_BASE + URN_BASE;
	
	public BovinoClient(){
	}
	
	public void teste(){
		System.out.println(URI_BASE);
	}
	
	
	public String alterar(Bovino bovino){
		
		RequestEntity<Bovino> request = RequestEntity
				.put(URI.create("http://192.168.0.100:8080/bovino"))
				.body(bovino);
		
		ResponseEntity<Void> response = restTemplate.exchange(request, Void.class);
		
		return response.getHeaders().getLocation().toString();
	}
	
	public List<Bovino> listar(){
		
		RequestEntity<Void> request = RequestEntity
				.get(URI.create(FINAL_URI_BASE))
				.build();
		ResponseEntity<Bovino[]> response = restTemplate.exchange(request, Bovino[].class);
		return Arrays.asList(response.getBody());
			
	}
	
public List<Bovino> listarPorNome(String nome){
		
		RequestEntity<Void> request = RequestEntity
				.get(URI.create("http://192.168.0.100:8080/bovino/nome/"+nome))
				.build();
		ResponseEntity<Bovino[]> response = restTemplate.exchange(request, Bovino[].class);
		return Arrays.asList(response.getBody());
			
	}
	
	
	
	public Bovino listarUm(Long codigo){
		
		RequestEntity<Void> request = RequestEntity
				.get(URI.create("http://192.168.0.100:8080/bovino/"+codigo))
				.build();
		ResponseEntity<Bovino> response = restTemplate.exchange(request, Bovino.class);
		return response.getBody();
			
	}
	
	public List<Raca> listarRacas(){
		
		RequestEntity<Void> request = RequestEntity
				.get(URI.create("http://192.168.0.100:8080/raca"))
				.build();
		ResponseEntity<Raca[]> response = restTemplate.exchange(request, Raca[].class);
		return Arrays.asList(response.getBody());
			
	}
	
	public List<Pelagem> listarPelagems(){
		
		RequestEntity<Void> request = RequestEntity
				.get(URI.create("http://192.168.0.100:8080/pelagem"))
				.build();
		ResponseEntity<Pelagem[]> response = restTemplate.exchange(request, Pelagem[].class);
		return Arrays.asList(response.getBody());
			
	}
	
	public List<Fazenda> listarFazendas(){
		
		RequestEntity<Void> request = RequestEntity
				.get(URI.create("http://192.168.0.100:8080/fazenda"))
				.build();
		ResponseEntity<Fazenda[]> response = restTemplate.exchange(request, Fazenda[].class);
		return Arrays.asList(response.getBody());
			
	}
	
	public List<Proprietario> listarProprietarios(){
		
		RequestEntity<Void> request = RequestEntity
				.get(URI.create("http://192.168.0.100:8080/proprietario"))
				.build();
		ResponseEntity<Proprietario[]> response = restTemplate.exchange(request, Proprietario[].class);
		return Arrays.asList(response.getBody());
			
	}
	
	

}
