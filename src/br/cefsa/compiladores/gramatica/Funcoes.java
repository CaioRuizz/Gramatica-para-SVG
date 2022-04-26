package br.cefsa.compiladores.gramatica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;


public class Funcoes 
{
	public static String lerArquivoTxt(String endereco)
	{
		String conteudo = "";
		
		try {
		      FileReader arq = new FileReader(endereco);
		      BufferedReader lerArq = new BufferedReader(arq);

		      String linha = lerArq.readLine();
		      conteudo += linha + "\n";

		      while (linha != null) {

		        linha = lerArq.readLine();
		        
		        if(linha != null)
		        	conteudo += linha + "\n";
		      }
		      arq.close();
		    } 
		catch (IOException e) 
		{
		        System.err.printf("Erro na abertura do arquivo: %s.\n",
		          e.getMessage());
		}
		return conteudo;
	}
	
	public static void gravarArquivoTxt(String endereco, String conteudo) throws IOException
	{
		 FileWriter arq = new FileWriter(endereco);
		 PrintWriter gravarArq = new PrintWriter(arq);
		 
		 gravarArq.printf(conteudo);
		 
		 arq.close();
		 
	}
	
	public static Gramatica obtemGramatica(String gramaticaTxt)
	{
		Gramatica gramatica = new Gramatica();
		ArrayList<String> alfabeto = new ArrayList<String>();
		
		String[] gramaticaVetor = gramaticaTxt.split("\n");
		String[] regrasDeProducao = new String[1];
		alfabeto.add(gramaticaVetor[0]);
		gramatica.setAlfabeto(alfabeto);
		gramatica.setPassos(Integer.parseInt(gramaticaVetor[1]));
		gramatica.setAxioma(gramaticaVetor[2]);
		gramatica.setAngulo(Double.parseDouble(gramaticaVetor[3]));
		regrasDeProducao = gramaticaVetor[4].split(">");
		gramatica.setRegrasProducao(regrasDeProducao);

		return gramatica;
	}

	
	public static String executaRegrasProducao(Gramatica gramatica)
	{
		String axiomaAtual = gramatica.getAxioma();
		ArrayList<String> axiomas = new ArrayList<String>();
		axiomas.add(axiomaAtual);
		String proximoAxioma = "";
		String charQueSeraSubstituido = gramatica.getRegrasProducao()[0];
		String charParaSubstituir = gramatica.getRegrasProducao()[1];
		
		for(int i = 0; i < gramatica.getPassos(); i++)
		{
			proximoAxioma = axiomaAtual.replace(charQueSeraSubstituido, charParaSubstituir);
			axiomas.add(proximoAxioma);
			axiomaAtual = proximoAxioma;
		}
		
		return axiomaAtual;
	}
	
	public static double[] atualizaPosicoes(double[] posicoes, double angulo)
	{
		int passo = 20;
		double radiano = angulo * (Math.PI/180);
		double[] novaPosicaoXY = new double[3];
		
		novaPosicaoXY[0] += Math.round(posicoes[0] + (Math.sin(radiano) * passo)); //co = sen * hip
		novaPosicaoXY[1] += Math.round(posicoes[1] - (Math.cos(radiano) * passo)); //ca = cos * hip
		
		System.out.println("X: " + novaPosicaoXY[0] + "\n"
				+ "Y: " + novaPosicaoXY[1] + "\n");

		return novaPosicaoXY;
	}
	
	public static String tartarugaSVG(String regraProcessada, Gramatica gramatica)
	{
		String corpoTagProcessado = "";
		double[] posicaoXY = new double[2];
		posicaoXY[0] = 800; //Referente a X
		posicaoXY[1] = 1500; //Referente a Y
		double[] proximaPosicaoXY = new double[2];
		proximaPosicaoXY[0] = 800; //Referente a X
		proximaPosicaoXY[1] = 1500; //Referente a Y
		Stack<double[]> posicaoGravadaXY = new Stack<double[]>();
		posicaoGravadaXY.add(proximaPosicaoXY);
		
		double anguloAtual = 0; //Para cima = 0 -- Sentido horario positivo
		

		for(int i = 0; i < regraProcessada.length(); i++)
		{
			if(regraProcessada.charAt(i) == 'F')
			{
				proximaPosicaoXY = atualizaPosicoes(posicaoXY, anguloAtual);
				
				corpoTagProcessado += "<line x1=\"" + posicaoXY[0] + "\" "
								+"y1=\"" + posicaoXY[1] + "\" "
								+"x2=\"" + proximaPosicaoXY[0] + "\" "
								+"y2=\"" + proximaPosicaoXY[1] + "\" "
								+"style=\"stroke:rgb(0,0,0);stroke-width:1\" />\r\n";
				
				posicaoXY = proximaPosicaoXY;

			}
			else if(regraProcessada.charAt(i) == '+')
			{
				anguloAtual += gramatica.getAngulo();
			}
			else if(regraProcessada.charAt(i) == '-')
			{
				anguloAtual -= gramatica.getAngulo();
			}
			else if(regraProcessada.charAt(i) == '[')
			{
				posicaoGravadaXY.add(posicaoXY);
				//anguloGravado = anguloAtual;
				//anguloAtual = 0;
			}
			else if(regraProcessada.charAt(i) == ']')
			{
				posicaoXY = posicaoGravadaXY.pop();
				//anguloAtual = anguloGravado;
			}

			
		}
		
		System.out.println("Caminhos executados");
		return corpoTagProcessado;
	}

	
	public static String geraHtmlSVG (Gramatica gramatica, String corpoTagProcessadoSVG)
	{
		String corpoHtml = "<html>"
				+ "<head>"
				+ "<title>Imagem Gerada</title>"
				+ "</head>"
				+ "<body>"
				+ "<svg height=\"3000\" width=\"3000\">"
				+ corpoTagProcessadoSVG
				+ "</svg>"
				+ "</body>"
				+ "</html>";
		
		return corpoHtml;
	}

}
