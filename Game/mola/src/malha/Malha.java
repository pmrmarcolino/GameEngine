package malha;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import models.RawModel;
import java.io.FileReader;
import java.io.IOException;

public class Malha {
	public static void main(String[] args) {
		
		 DisplayManager.createDisplay();
		 LoaderMalha loader = new LoaderMalha();
		 RendererMalha renderer = new RendererMalha();
	        MasterRenderer renderer2 = new MasterRenderer();
		 float[] vertices;
		 int[] indices;
		 
		 try{
			 ProcessaArquivo arq =  new ProcessaArquivo(new FileReader("src/arquivos/vertices.txt"));
		 	 vertices = arq.processaVertice();
		 	 
		 	 arq = new ProcessaArquivo(new FileReader("src/arquivos/indices.txt"));
		 	 indices = arq.processaIndice();
			 
		 	RawModel model = loader.loadToVAO(vertices, indices);
		 	Camera camera = new Camera();
		 	Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
		 	
		     while(!Display.isCloseRequested()){
		    	 	camera.move();
			        renderer.prepare();
			        renderer.render(model);
			    	 DisplayManager.updateDisplay();
			    	 renderer2.render(light, camera);
			     }
			     
			     loader.cleanUp();
			     DisplayManager.closeDisplay();
			     
		 }catch (IOException e){
			 System.err.printf("Erro na abertura do arquivo: %s.\n",
			          e.getMessage());
		 } 

	}
	 
}
