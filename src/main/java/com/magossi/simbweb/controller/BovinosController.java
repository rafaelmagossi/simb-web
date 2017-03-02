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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.magossi.simbweb.client.BovinoClient;
import com.magossi.simbweb.domain.bovino.Bovino;
import com.magossi.simbweb.domain.bovino.Fazenda;
import com.magossi.simbweb.domain.bovino.Pelagem;
import com.magossi.simbweb.domain.bovino.Proprietario;
import com.magossi.simbweb.domain.bovino.Raca;


@Controller
@RequestMapping("/bovinos")
public class BovinosController {
	
	private static final String ALTERAR_BOVINO_VIEW = "bovino/AlterarBovino";
	private static final String PESQUISA_BOVINO_VIEW = "/bovino/PesquisaBovinos";
	
	
	
	@Autowired
	private BovinoClient bovinoClient;


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
	public String alterar(@Validated Bovino bovino, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return ALTERAR_BOVINO_VIEW;
		}
		
		
		
		try {
			bovinoClient.alterar(bovino);
			attributes.addFlashAttribute("mensagem", "Bovino alterado com sucesso!");
			return "redirect:/bovinos";
		} catch (IllegalArgumentException e) {
			errors.rejectValue("dataVencimento", null, e.getMessage());
			return ALTERAR_BOVINO_VIEW;
		}
	}
	
	
	
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Long codigo) {
		Bovino bovino = bovinoClient.listarUm(codigo);
		ModelAndView mv = new ModelAndView(ALTERAR_BOVINO_VIEW); 
		mv.addObject(bovino);
		return mv;
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
	
	
}
