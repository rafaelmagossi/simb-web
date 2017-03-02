package com.magossi.simbweb.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;


import com.magossi.simbweb.domain.funcionario.Funcionario;

@Component
public class FuncionarioClient {
	
	public FuncionarioClient(){
		
	}
	
	public List<Funcionario> listarFuncionarios(){
		
		List<Funcionario> funcionarios = new ArrayList<Funcionario>();
		Funcionario funcionario = new Funcionario();
		funcionario.setIdFuncionario((long) 1);
		funcionario.setNome("Sebastiao");
		funcionario.setImei("354983059791193");
		
		funcionarios.add(funcionario);
		
		return funcionarios;
		
	}
	

}
