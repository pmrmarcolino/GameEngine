/**
 * Classe RendererImagem, junto com a classe ExemploImagem, mostra um exemplo de 
 * como trabalhar com imagens em OpenGL utilizando a API JOGL.
 * 
 * @author Marcelo Cohen, Isabel H. Manssour 
 * @version 1.0
 */

import java.awt.event.*; 
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import com.sun.opengl.util.*;
import java.nio.FloatBuffer;

public class RendererImagem extends MouseAdapter implements GLEventListener, KeyListener
{
	// Atributos
	private GL gl;
	private GLU glu;
	private GLUT glut;
	private GLAutoDrawable glDrawable;
	private double fAspect;
	private Imagem imgs[], nova;
	private int sel;
	
	/**
	 * Construtor da classe RendererImagem que recebe um array com as imagens
	 */
	public RendererImagem(Imagem imgs[])
	{
		// Inicializa o valor para correï¿½Ã£o de aspecto   
		fAspect = 1;

		// Imagem carregada do arquivo
		this.imgs = imgs;
		nova = null;
		sel = 0;	// selecionada = primeira imagem
	}

	/**
	 * Mï¿½todo definido na interface GLEventListener e chamado pelo objeto no qual serï¿½ feito o desenho
	 * logo apï¿½s a inicializaï¿½ï¿½o do contexto OpenGL. 
	 */    
	public void init(GLAutoDrawable drawable)
	{
		glDrawable = drawable;
		gl = drawable.getGL();
		// glu = drawable.getGLU();       
		glu = new GLU();
		glut = new GLUT();

		drawable.setGL(new DebugGL(gl));        

		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		// Define a janela de visualizaï¿½Ã£o 2D
		gl.glMatrixMode(GL.GL_PROJECTION);
		glu.gluOrtho2D(0,1,0,1);
		gl.glMatrixMode(GL.GL_MODELVIEW);
	}

	/**
	 * Método definido na interface GLEventListener e chamado pelo objeto no qual serï¿½ feito o desenho
	 * para comeï¿½ar a fazer o desenho OpenGL pelo cliente.
	 */  
	public void display(GLAutoDrawable drawable)
	{
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		gl.glLoadIdentity();    

		gl.glColor3f(0.0f, 0.0f, 1.0f);

		// Desenha a imagem original
		gl.glRasterPos2f(0,0);
		gl.glDrawPixels(imgs[sel].getWidth(), imgs[sel].getHeight(),
			       GL.GL_BGR, GL.GL_UNSIGNED_BYTE, imgs[sel].getData());

		// Desenha a imagem resultante
		if(nova!=null) {
			gl.glRasterPos2f(0.5f,0);
			gl.glDrawPixels(nova.getWidth(), nova.getHeight(),
			       GL.GL_BGR, GL.GL_UNSIGNED_BYTE, nova.getData());
		}
	}

	/**
	 * Método definido na interface GLEventListener e chamado pelo objeto no qual serÃ¡ feito o desenho
	 * depois que a janela foi redimensionada.
	 */  
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		gl.glViewport(0, 0, width, height);
		fAspect = (float)width/(float)height;      
	}

	/**
	 * Método definido na interface GLEventListener e chamado pelo objeto no qual serÃ¡ feito o desenho
	 * quando o modo de exibiï¿½Ã£o ou o dispositivo de exibiï¿½Ã£o associado foi alterado.
	 */  
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) { }

	/**
	 * Método da classe MouseAdapter que estÃ¡ sendo sobrescrito para gerenciar os 
	 * eventos de clique de mouse, de maneira que seja feito zoom in e zoom out.
	 */  
	public void mouseClicked(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1) // botÃ£o 1
		{ }
		if (e.getButton() == MouseEvent.BUTTON3) // botÃ£o 2
		{ }
		glDrawable.display();
	}

	/**
	 * Método definido na interface KeyListener que estÃ¡ sendo implementado para, 
	 * de acordo com as teclas pressionadas, permitir mover a posiï¿½Ã£o do observador
	 * virtual.
	 */        
	public void keyPressed(KeyEvent e)
	{
		// F1 para prÃ³xima imagem
		if(e.getKeyCode()==KeyEvent.VK_F1)
		{
			if(++sel>imgs.length-1) sel=imgs.length-1;
		}
		// F2 para imagem anterior
		else if(e.getKeyCode()==KeyEvent.VK_F2)
		{
			if(--sel<0) sel = 0;
		}

		// Cria a imagem resultante
		nova = (Imagem) imgs[sel].clone();

		switch (e.getKeyCode())
		{
			case KeyEvent.VK_1:		// Para exibir a imagem "original": nÃ£o faz nada
										System.out.println("Original");
										break;
			case KeyEvent.VK_2:		// Para converter a imagem para tons de cinza
										System.out.println("Grayscale");
										convertToGrayScale();
										break;     
			case KeyEvent.VK_3:		// Para converter a imagem para preto e branco  
										System.out.println("B&W");
										convertToGrayScale();
										convertToBlackAndWhite();
										break;
			case KeyEvent.VK_4:		// Para aplicar um filtro passa-alta (realce de bordas)
										System.out.println("High pass");
										convertToGrayScale();
										highPass();
										break;
			case KeyEvent.VK_5:		// Para aplicar um filtro passa-baixa (suavizaÃ§Ã£o)
										System.out.println("Low pass");
										convertToGrayScale();
										lowPass();
										break;
			case KeyEvent.VK_6:		// Para gerar a imagem atravï¿½s da tï¿½cnica de posterizaï¿½Ã£o
										System.out.println("Posterize");
										posterize();
										break; 	
			case KeyEvent.VK_7:		// Para converter a imagem para tons de sï¿½pia  
										System.out.println("Sepia");
										convertToGrayScale();
										sepia();
										break; 
			case KeyEvent.VK_ESCAPE:	System.exit(0);
										break;
		}  
		glDrawable.display();
	}

	/**
	 * Método definido na interface KeyListener.
	 */      
	public void keyTyped(KeyEvent e) { }

	/**
	 * Método definido na interface KeyListener.
	 */       
	public void keyReleased(KeyEvent e) { }
    
	/**
	 * Método que converte a imagem para tons de cinza.
	 */       
	public void convertToGrayScale() 
	{ 
		// Tarefa 1:
		//		Gerar uma imagem em tons de cinza 
		//		Use os métodos 
		//			getPixel/getR/getG/getB e setPixel da classe Imagem
		// 		Altere apenas o atributo nova.
		//     Experimente executar e testar nas imagens disponibilizadas.		


	}    

	/**
	 * Método que converte a imagem de tons de cinza para preto e branco.
	 */       
	public void convertToBlackAndWhite() 
	{ 
		// Tarefa 2:
		//		Uma forma de segmentar/transformar as imagens para P&B é a
		//		limiarizaïção (thresholding). Se o valor do pixel for menor que o valor do
		//		limiar a cor deve ser preta, caso contrário deve ser branca.
		//		Implemente essa técnica e varie o valor do limiar para 
		//		números pequenos como 10 ou 15 e números grandes como 100 e 150.
		//		Altere apenas o atributo nova.
		//      Experimente executar e testar nas imagens disponibilizadas.

		// Tarefa 3:
		//		Agora apenas mude o valor do limiar da tarefa 2 para um número 
		//		que represente a média entre os valores de densidade encontrados 
		//		na imagem. Altere apenas o atributo nova.
		//      Experimente executar e testar nas imagens disponibilizadas.		
		
		// Obs: neste ponto, a imagem já está em tons de cinza (r=g=b)
		// Passos:
		// - Varre a imagem para achar o menor e maior tom de cinza
		// - Calcula o valor médio, e usado este valor como limiar 
		// - Varre novamente a imagem, substituindo os pixels
	}	

	/**
	 * Método para aplicar um kernel (filtro) qualquer a uma imagem
	 * Deve receber uma matrix 3x3, e aplica este Ã  imagem original,
	 * alterando a imagem "nova"
	 */
	public void applyKernel(float [][]kernel)
	{
		for(int x=0; x<imgs[sel].getWidth()-2; x++)
		{
			for(int y=0; y<imgs[sel].getHeight()-2; y++)
			{
				float soma = 0;
				for(int i=0; i<3; i++)
				{
					for(int j=0; j<3; j++)
					{
						soma += imgs[sel].getR(x+i,y+j) * kernel[i][j];
					}
				}
				if(soma>255) soma = 255;
				if(soma<0) soma = 0;
				nova.setPixel(x+1,y+1,(int)soma,(int)soma,(int)soma);
			}
		}
	}

	/**
	 * Método para aplicar um filtro passa-alta
	 */
	public void highPass()
	{
		// Tarefa 4:
		//		Implemente um filtro passa-alta (realce de bordas). 
		//		Defina uma matriz 3x3 com os valores corretos e chame o método applyKernel.
		//		ex: float[][] matriz = { 0, 0, 0 } , { 0 , 0, 0 } , { 0, 0 ,0 }};
		//		    applyKernel(matriz);
	}

	/**
	 * Método para aplicar um filtro passa-baixa
	 */
	public void lowPass()
	{
		// Tarefa 5:
		//		Implemente um filtro passa-baixa (suavização). 
		//		Defina uma matriz 3x3 com os valores corretos e chame o método applyKernel.
		//		ex: float[][] matriz = { 0, 0, 0 } , { 0 , 0, 0 } , { 0, 0 ,0 }};
		//		    applyKernel(matriz);		
	}

	/**
	 * Método que altera a imagem através da técnica de posterização.
	 */       
	public void posterize() 
	{ 
		// Tarefa 6:
		//		Gere uma imagem atravï¿½s da tï¿½cnica de posterizaï¿½ï¿½o, que simplesmente 
		//		reduz a quantidade de tons diferentes de cada componente de cor. Por 
		//		exemplo, se quisermos posterizar para 10 intensidades, 25 tons de cada 
		//		componente se transformam no tom mï¿½dio de cada intervalo 
		//		(0..25 = 12, 26..50 = 38, ...). 
		//   Experimente executar e testar nas imagens disponibilizadas.
		
	}	
	
	/**
	 * Método que converte a imagem para tons de sépia.
	 */       
	public void sepia() 
	{ 
		// Tarefa 7:
		//		Gere uma imagem em tons de sï¿½pia, atravï¿½s do seguinte algoritmo:
		//		- A imagem deve estar ema tons de cinza; 
		//		- Faï¿½a as sombras (cinzas mais escuros) ficarem ainda mais escuras (0 <= cinza < 60); 
		//		- Faï¿½a os cinzas mï¿½dios ficarem meio amarronzados (60 <= cinza < 190), reduzindo o azul; 
		//		- Faï¿½a as partes claras (cinzas claros) ficarem meio amarelados (190 <= cinza):
		//			aumente vermelho e verde
		//			ou diminua o azul. 
		//      Experimente executar e testar nas imagens disponibilizadas.		


	}		
}

