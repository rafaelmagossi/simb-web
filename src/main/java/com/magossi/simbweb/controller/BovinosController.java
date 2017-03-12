package com.magossi.simbweb.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.magossi.simbweb.client.BovinoClient;
import com.magossi.simbweb.client.EccClient;
import com.magossi.simbweb.client.PesoClient;
import com.magossi.simbweb.client.TarefaClient;
import com.magossi.simbweb.domain.bovino.Bovino;
import com.magossi.simbweb.domain.bovino.Ecc;
import com.magossi.simbweb.domain.bovino.Fazenda;
import com.magossi.simbweb.domain.bovino.Pelagem;
import com.magossi.simbweb.domain.bovino.Peso;
import com.magossi.simbweb.domain.bovino.Proprietario;
import com.magossi.simbweb.domain.bovino.Raca;
import com.magossi.simbweb.domain.tarefa.Tarefa;


@Controller
@RequestMapping("/bovinos")
public class BovinosController {
	
	private static final String ALTERAR_BOVINO_VIEW = "bovino/AlterarBovino";
	private static final String PESQUISA_BOVINO_VIEW = "/bovino/PesquisaBovinos";

	Bovino auxBovino;
	
	@Autowired
	private BovinoClient bovinoClient;
	
	@Autowired
	private TarefaClient tarefaClient;
	
	@Autowired
	private EccClient eccClient;
	
	@Autowired
	private PesoClient pesoClient;



	/* ----------------------------------- GET ---------------------------------------*/
	
	@RequestMapping("/alterar")
	public String alterar() {
		return "/bovino/AlterarBovino";
	}
	
	
	@RequestMapping
	public ModelAndView pesquisa(@RequestParam(defaultValue="todos") String descricao) {
		List<Bovino> bovinos  = bovinoClient.listarPorNome(descricao);
		//System.out.println(bovinos.get(1).toString());
		ModelAndView mv = new ModelAndView(PESQUISA_BOVINO_VIEW);
		mv.addObject("bovinos", bovinos);

		return mv;
	}
	
	
	
	
	/* ----------------------------------- POST ---------------------------------------*/
	
	
	
	@RequestMapping(method = RequestMethod.POST)
	public String alterar(@Validated Bovino bovino, RedirectAttributes attributes) {
		
		
		Bovino bovinoAux = bovinoClient.listarUm(bovino.getIdBovino());
		bovino.setEcc(bovinoAux.getEcc());
		bovino.setPeso(bovinoAux.getPeso());
		
		
		try {
			bovinoClient.alterar(bovino);
			attributes.addFlashAttribute("mensagem", "Bovino alterado com sucesso!");
			return "redirect:/bovinos";
		} catch (IllegalArgumentException e) {
			//errors.rejectValue("dataVencimento", null, e.getMessage());
			return ALTERAR_BOVINO_VIEW;
		}
	}
	

	
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Long codigo) {
		auxBovino = bovinoClient.listarUm(codigo);
		Ecc ecc = new Ecc();
		Peso peso = new Peso();
		//ecc.setEscore(0);
		ModelAndView mv = new ModelAndView(ALTERAR_BOVINO_VIEW); 
		mv.addObject(auxBovino);
		mv.addObject(ecc);
		mv.addObject(peso);
		return mv;
	}
	
	
	/* ------------------------------------ PUT ----------------------------------------*/
	
	@RequestMapping(value="/editarEcc", method = RequestMethod.PUT)
	public String alterarEcc(@Validated Ecc ecc, Errors errors, RedirectAttributes attributes) {
	
			System.out.println(ecc.getEscore());
			attributes.addFlashAttribute("mensagem", "Ecc alterado com sucesso!");
			return "redirect:/bovinos/"+auxBovino.getIdBovino();
	}
	
	@RequestMapping(value="/editarPeso", method = RequestMethod.PUT)
	public String alterarEcc(@Validated Peso peso, Errors errors, RedirectAttributes attributes) {
	
			System.out.println(peso.getPeso());
			attributes.addFlashAttribute("mensagem", "Peso alterado com sucesso!");
			return "redirect:/bovinos/"+auxBovino.getIdBovino();
	}
	
	/* ----------------------------------- DELETE ---------------------------------------*/
	
	@RequestMapping(value="{codigo}", method = RequestMethod.DELETE)
	public String excluirBovino(@PathVariable Long codigo, RedirectAttributes attributes) {
	
		Bovino bovino = bovinoClient.listarUm(codigo);
		bovino.setStatus(false);
		
		List<Tarefa> tarefas = tarefaClient.listarTarefasBovinoMatriz(bovino.getIdBovino());

		try {
			bovinoClient.alterar(bovino);
			for(int i=0; i < tarefas.size() ; i++){
				Tarefa tarefa = tarefas.get(i);
				tarefa.setStatus(false);
				tarefaClient.alterar(tarefa);
				
			}
			attributes.addFlashAttribute("mensagem", "Bovino excluido com sucesso !");
			return "redirect:/bovinos";
		} catch (IllegalArgumentException e) {
			//errors.rejectValue("dataVencimento", null, e.getMessage());
			return PESQUISA_BOVINO_VIEW;
		}

	}
	
	
	@RequestMapping(value="/ecc/{idEcc}", method = RequestMethod.DELETE)
	public String excluireEccBovino(@PathVariable Long idEcc, RedirectAttributes attributes) {

		try {
			eccClient.deletar(idEcc);
			//eccClient.deletar(idEcc);
			attributes.addFlashAttribute("mensagem", "Ecc excluido com sucesso !");
			return "redirect:/bovinos/"+auxBovino.getIdBovino();
		} catch (HttpServerErrorException e) {
			//errors.rejectValue("dataVencimento", null, e.getMessage());
			String erro = ""+e.getStatusCode();
			return "redirect:/"+ erro;
			
		}
		
	}
	
	
	@RequestMapping(value="/peso/{idPeso}", method = RequestMethod.DELETE)
	public String excluirePesoBovino(@PathVariable Long idPeso, RedirectAttributes attributes) {

		try {
			pesoClient.deletar(idPeso);
			//eccClient.deletar(idEcc);
			attributes.addFlashAttribute("mensagem", "Peso excluido com sucesso !");
			return "redirect:/bovinos/"+auxBovino.getIdBovino();
		} catch (HttpServerErrorException e) {
			//errors.rejectValue("dataVencimento", null, e.getMessage());
			String erro = ""+e.getStatusCode();
			return "redirect:/"+ erro;
			
		}
		
	}
	
	
	
	/* ---------------------- Model Attributes ------------------------*/
	
	@ModelAttribute("todasRacasBovino")
	public List<Raca> todosRacasBovino() {
		List<Raca> racas = bovinoClient.listarRacas();
		return racas;
	}
	
	@ModelAttribute("todasPelagemsBovino")
	public List<Pelagem> todasPelagemsBovino() {
		List<Pelagem> pelagems = bovinoClient.listarPelagems();
		return pelagems;
	}
	
	@ModelAttribute("todosProprietariosBovino")
	public List<Proprietario> todosProprietariosBovino() {
		List<Proprietario> proprietarios = bovinoClient.listarProprietarios();
		return proprietarios;
	}
	
	@ModelAttribute("todasFazendasBovino")
	public List<Fazenda> todasFazendasBovino() {
		List<Fazenda> fazendas = bovinoClient.listarFazendas();
		return fazendas;
	}
	
	
	@ModelAttribute("todosGenerosBovino")
	public List<Boolean> todosGenerosBovino() {
		List<Boolean> generos = new ArrayList<Boolean>();
		generos.add(true);
		generos.add(false);
		return generos;
	}
	
	
	
	@ModelAttribute("todosEccs")
	public List<Ecc> todosEccsBovino() {
		List<Ecc> eccs = new ArrayList<Ecc>();

		for(int i=0; i<9 ; i++){
			Ecc ecc = new Ecc();
			ecc.setIdECC(0L);
			ecc.setEscore(i+1);
			eccs.add(ecc);
		}
		return eccs;
	}
	
	
}
