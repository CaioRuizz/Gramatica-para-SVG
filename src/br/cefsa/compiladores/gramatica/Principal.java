package br.cefsa.compiladores.gramatica;

import java.io.IOException;



public class Principal {
	
	public static void main(String[] args) throws IOException 
	{
		String caminhoArquivoGramatica = "arquivos\\gramatica.txt";
		String caminhoGravacaoHTML = "arquivos\\saida_SVG.html";

		
		String textoGramatica = Funcoes.lerArquivoTxt(caminhoArquivoGramatica);
		
		Gramatica gramatica = new Gramatica();
		
		gramatica = Funcoes.obtemGramatica(textoGramatica);
		
		String regraProcessada = Funcoes.executaRegrasProducao(gramatica);
		
		String corpoTagProcessadoCaminhoTartarugaSVG = Funcoes.tartarugaSVG(regraProcessada, gramatica);
		
		String arquivoHtmlSVG = Funcoes.geraHtmlSVG(gramatica, corpoTagProcessadoCaminhoTartarugaSVG);
		
		Funcoes.gravarArquivoTxt(caminhoGravacaoHTML, arquivoHtmlSVG);
	}

}
