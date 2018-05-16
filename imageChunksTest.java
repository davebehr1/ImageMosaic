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

public class imageChunksTest{
	public File file = null;
   	public FileInputStream fis = null;
    public BufferedImage image = null;
    public String imageDirectory ="";
    public String imageLocation = "";
    public int cols =0;
    public int pixelAverage =0;
    public ArrayList<Integer> imageAverage; 
    public int rows =0;
    public int chunks =25;
    public int chunkWidth =0;
    public int chunkHeight =0;
    public BufferedImage imgs[] = null;
    public BufferedImage resizedImgs[] = null;
    //public Map<Boolean, Integer> map = new HashMap<Boolean, Integer>();
    public Map<Integer,Integer> map = new HashMap<Integer,Integer>();
    public int originalImageAverage =0; 

	public int populationSize = 0;
	ArrayList<ArrayList<String>> members;

	imageChunksTest(String dir,String loc,String col,String row){
    	this.imageDirectory = dir;
  		this.imageLocation = loc;
  		this.cols = Integer.parseInt(col);
  		this.rows = Integer.parseInt(row);
        imageAverage = new ArrayList<Integer>();
    }

    public void popInit(int upperLimit, int popSize){
        //initialize population randomly
        // 5 members
        members = new ArrayList<ArrayList<String>>();
        populationSize = popSize;
        Random rand = new Random();
        int value = 0;
        String dest ="imagesResize/img";

         for(int i = 0; i < popSize; ++i){
        members.add(new ArrayList<String>());
        }

        for(int i = 0; i < popSize; ++i){
            for(int j = 0; j < chunks ;++j){
            	value = rand.nextInt(upperLimit);
                dest += value + ".jpg";
                members.get(i).add(dest);
                dest = "imagesResize/img";
            }
        }

        for(int i = 0 ; i < popSize; ++i){
            for(int j =0; j < chunks; ++j){
            System.out.println(members.get(i).get(j));
            }
            System.out.println("");
        }
        //members = new String[popSize][chunks]; //[rows][cols]
        
     

  




    }


	public static void main(String[] args){

		imageChunksTest newGen = new imageChunksTest(args[0], args[1], args[2], args[3]); 

		int popSize = 5;
		newGen.popInit(9145,popSize);



	}
}