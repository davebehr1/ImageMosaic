import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Scanner;
import java.io.*;
import java.awt.*;

public class JavaImageIOTest
{

  

  	 File file = null;
   	 FileInputStream fis = null;
    BufferedImage image = null;
     String imageDirectory ="";
     String imageLocation = "";
     int cols = 0;
     int rows = 0; 

  	JavaImageIOTest(String dir,String loc,int col,int row){
  		 this.imageDirectory = dir;
  		 this.imageLocation = loc;
  		 this.cols = col;
  		 this.rows = row;

  	}

  	 
    try
    {
      // the line that reads the image file
   	  file = new File(imageLocation);
   	  fis = new FileInputStream(file);
      image = ImageIO.read(fis);

      // work with the image here ...
    } 
    catch (IOException e)
    {
      e.printStackTrace();
    }
    int chunks = rows * cols;

    	int chunkWidth = image.getWidth() / cols;
        int chunkHeight = image.getHeight() / rows;
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

  

  public static void main(String[] args)
  {
    JavaImageIOTest newImage = new JavaImageIOTest(args[0], args[1], args[2], arg[3]);

    
  }

}