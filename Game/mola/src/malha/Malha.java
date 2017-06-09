package malha;

import org.lwjgl.opengl.Display;
import renderEngine.DisplayManager;
import models.RawModel;
import java.io.FileReader;
import java.io.IOException;

public class Malha {
	public static void main(String[] args) {
		
		 DisplayManager.createDisplay();
		 LoaderMalha loader = new LoaderMalha();
		 RendererMalha renderer = new RendererMalha();
		 float[] vertices;
		 int[] indices;
		 
		 try{
			 ProcessaArquivo arq =  new ProcessaArquivo(new FileReader("src/arquivos/vertices.txt"));
		 	 vertices = arq.processaVertice();
		 	 
		 	 arq = new ProcessaArquivo(new FileReader("src/arquivos/indices.txt"));
		 	 indices = arq.processaIndice();
			 
		 	RawModel model = loader.loadToVAO(vertices, indices);
		 	
		     while(!Display.isCloseRequested()){
			        renderer.prepare();
			        renderer.render(model);
			    	 DisplayManager.updateDisplay();
			     }
			     
			     loader.cleanUp();
			     DisplayManager.closeDisplay();
			     
		 }catch (IOException e){
			 System.err.printf("Erro na abertura do arquivo: %s.\n",
			          e.getMessage());
		 } 

	}
	 
}
