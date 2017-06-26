package malha;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ProcessaArquivo {

	FileReader arq;
	String linha;
	List<String> processos;

	public ProcessaArquivo(FileReader arq) {
		this.arq = arq;
		abrirArquivo();
	}

	public void abrirArquivo() {
		try {
			BufferedReader lerArq = new BufferedReader(arq);

			linha = lerArq.readLine(); 
			while (linha != null) {
				processos = Arrays.asList(linha.split(","));
				linha = lerArq.readLine(); 
			}

			arq.close();
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}

		System.out.println();
	}
	
	public float[] processaVertice(){
		 float[] numbers = new float[processos.size()];
		    for (int i = 0; i < processos.size(); ++i) {
		         numbers[i] = Float.parseFloat(processos.get(i));
		    }	
		return numbers;
	}
	
	public int[] processaIndice(){
		 int[] numbers = new int[processos.size()];
		    for (int i = 0; i < processos.size(); ++i) {
		         numbers[i] = Integer.parseInt(processos.get(i));
		    }	
		return numbers;
	}
}
