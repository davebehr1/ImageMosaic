import java.awt.image.BufferedImage;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Scanner;
import java.io.*;
import java.awt.*;
import java.util.*;

public class imageChunks{

	public File file = null;
   	public FileInputStream fis = null;
    public BufferedImage image = null;
    public String imageDirectory ="";
    public String imageLocation = "";
    public int cols =0;
    public int pixelAverage =0;
    public ArrayList<Integer> imageAverage; 
    public int rows =0;
    public int chunks =0;
    public int chunkWidth =0;
    public int chunkHeight =0;
    public BufferedImage imgs[] = null;
    public BufferedImage resizedImgs[] = null;
    public Map<Boolean, Integer> map = new HashMap<Boolean, Integer>();
    public String members[][] = null;

    imageChunks(String dir,String loc,String col,String row){
    	this.imageDirectory = dir;
  		this.imageLocation = loc;
  		this.cols = Integer.parseInt(col);
  		this.rows = Integer.parseInt(row);
        imageAverage = new ArrayList<Integer>();
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
        imgs = new BufferedImage[chunks];
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
            ImageIO.write(imgs[i], "jpg", new File("images3/image" + i + ".jpg"));
        }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        System.out.println("Mini images created");
	}

	public void StitchImage(){ //stitch mosaic
        int type;
        //fetching image files
        File[] imgFiles = new File[chunks];
        for (int i = 0; i < chunks; i++) {
            imgFiles[i] = new File("imagesResize/img" + i + ".jpg");
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
        ImageIO.write(finalImg, "jpeg", new File("finalImg3.jpg"));
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	


	}

    public void popInit(int upperLimit, int popSize){
        //initialize population randomly
        // 5 members
       
        Random rand = new Random();
        int value = 0;
        String dest ="imagesResize/img";
        members = new String[popSize][chunks];
        
        for(int i =0; i < popSize;++i){
            for(int j = 0; j < chunks;++ j){
                value = rand.nextInt(upperLimit);
                dest += value + ".jpg";
                members[i][j] = dest;
                dest = "imagesResize/img";
            }
        }

        for(int i =0; i < popSize;++i){
            for(int j = 0; j < chunks;++ j){
                System.out.println(members[i][j]);
            }
            System.out.println("");
        }




    }

    public int getScoreOriginal(){
        int targetPixel =0;
        int originalImageAverage =0;
        Random newRand = new Random();
        int randX = 0;
        int randY = 0;
        int randNum = newRand.nextInt(imgs.length);
        System.out.println("RANDOM NUMBER: " + randNum); 
        for(int j = 0; j < imgs.length;++j){
            for(int k = 0; k < 20;++k){
                randX = newRand.nextInt(chunkWidth);
                randY = newRand.nextInt(chunkHeight);
                targetPixel = imgs[j].getRGB(randX,randY);
                //System.out.println("TARGETPIXEL: " + targetPixel);
                pixelAverage += targetPixel;
            }
            pixelAverage = pixelAverage / 20;
            imageAverage.add(pixelAverage);
        }
        System.out.println(imageAverage);

        for(int i =0; i < imageAverage.size();++i){
                originalImageAverage += imageAverage.get(i);
        }
        originalImageAverage = originalImageAverage / imageAverage.size();
        System.out.println("FULL IMAGE AVERAGE: " + originalImageAverage);
        
    
        return 0;
    }

    public int getScoreChromosone(){


        return 0;

    }

    public void StitchImageTwo(){ //stitch original
        int type;
        //fetching image files
        File[] imgFiles = new File[chunks];
        for (int i = 0; i < chunks; i++) {
            imgFiles[i] = new File("images3/image" + i + ".jpg");
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
        ImageIO.write(finalImg, "jpeg", new File("finalImgFace.jpg"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        


    }

    public void stitchMember(int memberNumber){
        int type;
        //how the member of a population currently looks

        File[] imgFiles = new File[chunks];
        for (int i = 0; i < chunks; i++) {
            imgFiles[i] = new File(members[memberNumber][i]);
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
        ImageIO.write(finalImg, "jpeg", new File("members/Member" + memberNumber+ ".jpg" ));
        }
        catch(Exception e){
            e.printStackTrace();
        }



    }

    public int getWidth(){
        return chunkWidth;
    }
    public int getHeight(){
        return chunkHeight;
    }





	public static void main(String[] args){

		imageChunks newGen = new imageChunks(args[0], args[1], args[2], args[3]); //image directory / image.jpg / x / y
        int numberOfImages = 0;
		newGen.GenImages();
        System.out.println(newGen.getWidth());
        System.out.println(newGen.getHeight());
        //System.out.println(newGen.getScore());
        newGen.getScoreOriginal(); 
        int popSize = 4;
        newGen.popInit(9145, popSize);
        for(int i =0 ; i < popSize;++i){
        newGen.stitchMember(i);
        }

        // number of file is 9145

        /*ImageResizerScript newResizer = new ImageResizerScript(args[0],newGen.getWidth(),newGen.getHeight());
        File folder = new File(args[0]);
        newResizer.listAllFiles(folder);
        System.out.println("Number of files: " +newResizer.getNumFiles());
        System.out.println("Number of images in Directory: " + numberOfImages);
        newGen.popInit(newResizer.getNumFiles());*/


		newGen.StitchImage();
        newGen.StitchImageTwo();

	}
}