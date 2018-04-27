import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Scanner;
import java.io.*;
import java.awt.*;

public class imageChunks{

	public File file = null;
   	public FileInputStream fis = null;
    public BufferedImage image = null;
    public String imageDirectory ="";
    public String imageLocation = "";
    public int cols =0;
    public int rows =0;
    public int chunks =0;
    public int chunkWidth =0;
    public int chunkHeight =0;

    imageChunks(String dir,String loc,String col,String row){
    	this.imageDirectory = dir;
  		this.imageLocation = loc;
  		this.cols = Integer.parseInt(col);
  		this.rows = Integer.parseInt(row);
    }
    public void GenImages(){
	    try{
	    	file = new File(imageLocation);
	   	  	fis = new FileInputStream(file);
	      	image = ImageIO.read(fis);

	    }catch(Exception e){
	    	e.printStackTrace();
	    }

	    chunks = rows * cols;

    	chunkWidth = image.getWidth() / cols;
        chunkHeight = image.getHeight() / rows;
        int count = 0;
        BufferedImage imgs[] = new BufferedImage[chunks];
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
               
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

              
                Graphics2D gr = imgs[count++].createGraphics();
                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
                gr.dispose();
            }
        }
        System.out.println("Splitting done");

    
        try{
        for (int i = 0; i < imgs.length; i++) {
            ImageIO.write(imgs[i], "jpg", new File("images/img" + i + ".jpg"));
        }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        System.out.println("Mini images created");
	}

	public void StitchImage(){
        int type;
        //fetching image files
        File[] imgFiles = new File[chunks];
        for (int i = 0; i < chunks; i++) {
            imgFiles[i] = new File("images/img" + i + ".jpg");
        }

      
        BufferedImage[] buffImages = new BufferedImage[chunks];
        try{
        for (int i = 0; i < chunks; i++) {
            buffImages[i] = ImageIO.read(imgFiles[i]);
        }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        type = buffImages[0].getType();
        chunkWidth = buffImages[0].getWidth();
        chunkHeight = buffImages[0].getHeight();

        //Initializing the final image
        BufferedImage finalImg = new BufferedImage(chunkWidth*cols, chunkHeight*rows, type);

        int num = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                finalImg.createGraphics().drawImage(buffImages[num], chunkWidth * j, chunkHeight * i, null);
                num++;
            }
        }
        System.out.println("Image concatenated.....");
        try{
        ImageIO.write(finalImg, "jpeg", new File("finalImg.jpg"));
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	


	}





	public static void main(String[] args){
		imageChunks newGen = new imageChunks(args[0], args[1], args[2], args[3]);
		newGen.GenImages();
		newGen.StitchImage();
	}
}