package com.magossi.simbweb.controller;


import java.util.Arrays;
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
import com.magossi.simbweb.client.FuncionarioClient;
import com.magossi.simbweb.client.TarefaClient;
import com.magossi.simbweb.domain.bovino.Bovino;
import com.magossi.simbweb.domain.funcionario.Funcionario;
import com.magossi.simbweb.domain.tarefa.Tarefa;
import com.magossi.simbweb.domain.tarefa.TipoTarefaEnum;

@Controller
@RequestMapping("/tarefas")
public class TarefasController {
	
	private static final String ADICIONAR_TAREFA_VIEW = "tarefa/AdicionarTarefa";
	private static final String ALTERAR_TAREFA_VIEW = "tarefa/AlterarTarefa";
	private static final String PESQUISA_TAREFAS_ATIVAS_VIEW = "tarefa/PesquisaTarefasAbertas";
	private static final String PESQUISA_TAREFAS_CONCLUIDAS_VIEW = "tarefa/PesquisaTarefasFinalizadas";
	
	
	@Autowired
	private FuncionarioClient funcionarioClient;
	
	@Autowired
	private BovinoClient bovinoClient;
	
	@Autowired
	private TarefaClient tarefaClient;

	
	/* ----------------------------------- GET ---------------------------------------*/
	
	@RequestMapping("ativas")
	public ModelAndView pesquisaAtivos(@RequestParam(defaultValue="todos") String descricao) {
		List<Tarefa> tarefas  = tarefaClient.listarTarefaAtivasPorData(descricao);
		//System.out.println(bovinos.get(1).toString());
		ModelAndView mv = new ModelAndView(PESQUISA_TAREFAS_ATIVAS_VIEW);
		mv.addObject("tarefas", tarefas);

		return mv;
	}
	
	@RequestMapping("concluidas")
	public ModelAndView pesquisaConcluidas(@RequestParam(defaultValue="todos") String descricao) {
		List<Tarefa> tarefas  = tarefaClient.listarTarefaConcluidasPorData(descricao);
		//System.out.println(bovinos.get(1).toString());
		ModelAndView mv = new ModelAndView(PESQUISA_TAREFAS_CONCLUIDAS_VIEW);
		mv.addObject("tarefas", tarefas);

		return mv;
	}
	
	@RequestMapping("/adicionar/{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Long codigo) {
		Bovino bovinoMatriz = bovinoClient.listarUm(codigo);
		
		Tarefa tarefa = new Tarefa();
		tarefa.setBovinoMatriz(bovinoMatriz);
		
		ModelAndView mv = new ModelAndView(ADICIONAR_TAREFA_VIEW); 
		mv.addObject(tarefa);
		return mv;
	}
	
	@RequestMapping("/alterar/{codigo}")
	public ModelAndView alterar(@PathVariable("codigo") Long codigo) {
		Tarefa tarefa = tarefaClient.listarUma(codigo);
		System.out.println("DATA INCLUSAO: " + tarefa.getDataInclusao());

		ModelAndView mv = new ModelAndView(ALTERAR_TAREFA_VIEW); 
		mv.addObject(tarefa);
		return mv;
	}
	
	/* ----------------------------------- POST ---------------------------------------*/
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Tarefa tarefa, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return ADICIONAR_TAREFA_VIEW;
		}
		
		tarefa.setStatus(true);
		
		try {
			tarefaClient.salvar(tarefa);
			attributes.addFlashAttribute("mensagem", "Tarefa salva com sucesso!");
			return "redirect:/bovinos";
		} catch (HttpServerErrorException e) {
			//errors.rejectValue("dataVencimento", null, e.getMessage());
			String erro = ""+e.getStatusCode();
			return "redirect:/"+ erro;
			
		} catch (IllegalArgumentException e) {
			errors.rejectValue("dataVencimento", null, e.getMessage());
			return ADICIONAR_TAREFA_VIEW;
		}
	}
	
	/* ----------------------------------- PUT ---------------------------------------*/
	
	@RequestMapping(method = RequestMethod.PUT)
	public String alterar(@Validated Tarefa tarefa, RedirectAttributes attributes) {
		
		
		Tarefa tarefaAux = tarefaClient.listarUma(tarefa.getIdTarefa());
		tarefaAux.setTipoTarefa(tarefa.getTipoTarefa());
		tarefaAux.setDataInclusao(tarefa.getDataInclusao());
		tarefaAux.setImei(tarefa.getImei());
		
		System.out.println("ID: " + tarefaAux.getIdTarefa());
		System.out.println("BOVINO: " + tarefaAux.getBovinoMatriz().getNomeBovino());
		System.out.println("IMEI: " + tarefaAux.getImei());
		System.out.println("TIPO: " + tarefaAux.getTipoTarefa());
		System.out.println("STATUS: " + tarefaAux.isStatus());
		System.out.println("STATUS TAREFA: " + tarefaAux.getStatusDaTarefa());
		System.out.println("DATA INCLUSAO: " + tarefaAux.getDataInclusao());
		System.out.println("DATA CONCLUSAO: " + tarefaAux.getDataConclusao());
		
		
		try {
			tarefaClient.alterar(tarefaAux);
			attributes.addFlashAttribute("mensagem", "Tarefa alterada com sucesso!");
			return "redirect:/tarefas/ativas";
		} catch (IllegalArgumentException e) {
			//errors.rejectValue("dataVencimento", null, e.getMessage());
			return ALTERAR_TAREFA_VIEW;
		}
	}
	
	
	/* ----------------------------------- DELETE ---------------------------------------*/
	
	@RequestMapping(value="/concluidas/{codigo}", method = RequestMethod.DELETE)
	public String excluirTarefasFinalizadas(@PathVariable Long codigo, RedirectAttributes attributes) {
	
		Tarefa tarefa = tarefaClient.listarUma(codigo);
		tarefa.setStatus(false);
	

		try {
			tarefaClient.alterar(tarefa);
			attributes.addFlashAttribute("mensagem", "Tarefa excluida com sucesso !");
			return "redirect:/tarefas/concluidas";
		}catch (HttpServerErrorException e) {
			//errors.rejectValue("dataVencimento", null, e.getMessage());
			String erro = ""+e.getStatusCode();
			return "redirect:/"+ erro;
			
		}catch (IllegalArgumentException e) {
			//errors.rejectValue("dataVencimento", null, e.getMessage());
			return PESQUISA_TAREFAS_CONCLUIDAS_VIEW;
		}

	}
	
	@RequestMapping(value="/ativas/{codigo}", method = RequestMethod.DELETE)
	public String excluirTarefasAtivas(@PathVariable Long codigo, RedirectAttributes attributes) {
		
		try {
			tarefaClient.deletar(codigo);
			attributes.addFlashAttribute("mensagem", "Tarefa excluida com sucesso !");
			return "redirect:/tarefas/ativas";
		} catch (HttpServerErrorException e) {
			//errors.rejectValue("dataVencimento", null, e.getMessage());
			String erro = ""+e.getStatusCode();
			return "redirect:/"+ erro;
		}catch (IllegalArgumentException e) {
			//errors.rejectValue("dataVencimento", null, e.getMessage());
			return PESQUISA_TAREFAS_ATIVAS_VIEW;
		}
	

	}
	
	
	
	@ModelAttribute("todosTiposTarefa")
	public List<TipoTarefaEnum> todosTiposTarefa() {
		return Arrays.asList(TipoTarefaEnum.values());
	}
	
	@ModelAttribute("todosFuncionarios")
	public List<Funcionario> todosFuncionarios() {
		List<Funcionario> funcionarios = funcionarioClient.listarFuncionarios();
		return funcionarios;
	}

}
