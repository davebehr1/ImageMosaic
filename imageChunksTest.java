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
import java.lang.Math;
import java.io.*;
import java.awt.*;
import java.util.*;

public class imageChunksTest{
	public File file = null;
   	public FileInputStream fis = null;
    public BufferedImage image = null;
    public String imageDirectory ="";
    public String imageLocation = "";

    //ints:

    public int cols =0;
    public int pixelAverage =0;
    public int rows =0;
    public int chunks =25;
    public int chunkWidth =0;
    public int chunkHeight =0;
    public int value =0;
    public int keyValueOne =0;
    public int keyValueTwo = 0;
    public int originalImageAverage =0; 
    public int red, green, blue;
    public int populationSize = 0;


    // ARRAYLISTS

    public ArrayList<ArrayList<String>> members;
    public ArrayList<Integer> averageCell;
    public ArrayList<Integer> origImageAverage;
    public ArrayList<String> childOne;
    public ArrayList<String> childTwo;
    public ArrayList<ArrayList<Integer>> imageAverage; 

    //MAPS:
    public Map<Integer,Double> map = new HashMap<Integer,Double>();
    public Map<Integer,Double> tournamentMapOne;
    public Map<Integer, Double> tournamentMapTwo;

    //arrays:

    public int []  wholeImageAverage;
    public int []  candidateImageAverage;
    public BufferedImage imgs[] = null;
    public BufferedImage resizedImgs[] = null;


	imageChunksTest(String dir,String loc,String col,String row){
    	this.imageDirectory = dir;
  		this.imageLocation = loc;
  		this.cols = Integer.parseInt(col);
  		this.rows = Integer.parseInt(row);
        this.averageCell = new ArrayList<Integer>();
        this.origImageAverage = new ArrayList<Integer>();
        imageAverage = new ArrayList<ArrayList<Integer>>();
        this.chunks = rows * cols;   
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
        //System.out.println("Splitting done");
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
    }

    public void printPixelARGB(int pixel) {
        //int alpha = (pixel >> 24) & 0xff;
        red = (pixel >> 16) & 0xff;
        green = (pixel >> 8) & 0xff;
        blue = (pixel) & 0xff;
        System.out.println("argb: " +  ", " + red + ", " + green + ", " + blue);
    }

    public double euclideanDistance(int[] colorOne, int[] colorTwo){
      double distance = Math.sqrt(Math.pow((colorOne[0] - colorTwo[0]), 2) +  Math.pow((colorOne[1] - colorTwo[1]), 2) + Math.pow((colorOne[2] - colorTwo[2]),2));
      System.out.println("DISTANCE: " + distance);

      return distance;
    }

    public int[] getScoreOriginal(){
        int targetPixel =0;
        wholeImageAverage = new int[3];
        
        averageCell.add(0);
        averageCell.add(0);
        averageCell.add(0);

        origImageAverage = new ArrayList<Integer>();
        origImageAverage.add(0);
        origImageAverage.add(0);
        origImageAverage.add(0);

        Random newRand = new Random();
        int randX = 0;
        int randY = 0;
        int randNum = newRand.nextInt(imgs.length);


        for(int j = 0; j < imgs.length;++j){
            for(int k = 0; k < 20;++k){
                randX = newRand.nextInt(chunkWidth);
                randY = newRand.nextInt(chunkHeight);
                targetPixel = imgs[j].getRGB(randX,randY);
    
                red = (targetPixel >> 16) &0xff;
                green = (targetPixel >> 8) & 0xff;
                blue = (targetPixel) & 0xff;

                averageCell.set(0, averageCell.get(0) + red);
                averageCell.set(1, averageCell.get(1) + red);
                averageCell.set(2, averageCell.get(2) + red);
               
            }
            averageCell.set(0, averageCell.get(0) /20);
            averageCell.set(1, averageCell.get(1) /20);
            averageCell.set(2, averageCell.get(2)/20);
        
            imageAverage.add(averageCell);
        }


        for(int i =0; i < imageAverage.size();++i){

                origImageAverage.set(0,origImageAverage.get(0)+ imageAverage.get(i).get(0));
                origImageAverage.set(1,origImageAverage.get(1)+ imageAverage.get(i).get(1));
                origImageAverage.set(2,origImageAverage.get(2)+ imageAverage.get(i).get(2));

        }

        origImageAverage.set(0,origImageAverage.get(0)/20);
        origImageAverage.set(1,origImageAverage.get(1)/20);
        origImageAverage.set(2,origImageAverage.get(2)/20);
        wholeImageAverage[0] = origImageAverage.get(0);
        wholeImageAverage[1] = origImageAverage.get(1);
        wholeImageAverage[2] = origImageAverage.get(2);
     
        
        return wholeImageAverage;

    }

    public int[] getScore(BufferedImage[] buffImages, int memberNumber){
        int targetPixel =0;
        candidateImageAverage = new int[3];

        averageCell.clear();
        averageCell.add(0);
        averageCell.add(0);
        averageCell.add(0);

        origImageAverage.clear();
        origImageAverage.add(0);
        origImageAverage.add(0);
        origImageAverage.add(0);

        Random newRand = new Random();
        int randX = 0;
        int randY = 0;
        int randNum = newRand.nextInt(imgs.length);


        for(int j = 0; j < imgs.length;++j){
            for(int k = 0; k < 20;++k){
                randX = newRand.nextInt(chunkWidth);
                randY = newRand.nextInt(chunkHeight);
                targetPixel = buffImages[j].getRGB(randX,randY);

                red = (targetPixel >> 16) &0xff;
                green = (targetPixel >> 8) & 0xff;
                blue = (targetPixel) & 0xff;

                averageCell.set(0, averageCell.get(0) + red);
                averageCell.set(1, averageCell.get(1) + red);
                averageCell.set(2, averageCell.get(2) + red);

            }

            averageCell.set(0, averageCell.get(0) /20);
            averageCell.set(1, averageCell.get(1) /20);
            averageCell.set(2, averageCell.get(2)/20);

            imageAverage.add(averageCell);
        }


        for(int i =0; i < imageAverage.size();++i){

                origImageAverage.set(0,origImageAverage.get(0)+ imageAverage.get(i).get(0));
                origImageAverage.set(1,origImageAverage.get(1)+ imageAverage.get(i).get(1));
                origImageAverage.set(2,origImageAverage.get(2)+ imageAverage.get(i).get(2));

        }

        origImageAverage.set(0,origImageAverage.get(0)/20);
        origImageAverage.set(1,origImageAverage.get(1)/20);
        origImageAverage.set(2,origImageAverage.get(2)/20);
        candidateImageAverage[0] = origImageAverage.get(0);
        candidateImageAverage[1] = origImageAverage.get(1);
        candidateImageAverage[2] = origImageAverage.get(2);

        map.put(memberNumber, euclideanDistance(wholeImageAverage,candidateImageAverage));
        
        return candidateImageAverage;
    }

    public void StitchMember(int memberNumber){
        int type;
        File[] imgFiles = new File[chunks];
        for(int i =0; i < chunks;++i){
            imgFiles[i] = new File(members.get(memberNumber).get(i));
        }

        BufferedImage[] buffImages = new BufferedImage[chunks];
        try{
        for (int i = 0; i < chunks; i++) {
            buffImages[i] = ImageIO.read(imgFiles[i]);
        }
        getScore(buffImages, memberNumber);
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

    public void loadImages(int memberNumber){
        File[] imgFiles = new File[chunks];
        for (int i = 0; i < chunks; i++) {
            imgFiles[i] = new File(members.get(memberNumber).get(i));
        }

        BufferedImage[] buffImages = new BufferedImage[chunks];
        try{
        for (int i = 0; i < chunks; i++) {
            buffImages[i] = ImageIO.read(imgFiles[i]);
        }
        getScore(buffImages,memberNumber);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void mutate(int memberNumber){
        String randImage ="imagesResize/img";
        int randIndex =0;
        int randCell =0;

        Random muteRand = new Random();
        randIndex = muteRand.nextInt(9145);
        randCell = muteRand.nextInt(chunks);

        randImage = randImage + randIndex + ".jpg";

        members.get(memberNumber).set(randCell, randImage);

    } 

    public void genImage(){
        System.out.println("SITCHED");
        map = sortByValue(map);
        StitchMember(Integer.parseInt(map.keySet().toArray()[0].toString()));

    }

     public int getWidth(){
        return chunkWidth;
    }
    public int getHeight(){
        return chunkHeight;
    } 
    public ArrayList getMembers(){
        return members;
    }
    public void tournamentSelection(int k){
        int value =0;
        int keyValueOne =0;
        int keyValueTwo = 0;
        tournamentMapOne = new HashMap<Integer, Double>();
        tournamentMapTwo = new HashMap<Integer, Double>();
        Random randSelect = new Random();

        //PARENT 1s
        for(int i =0; i < k;++i){
        value = randSelect.nextInt(populationSize);
        tournamentMapOne.put(value, map.get(value));
        }
        sortByValue(tournamentMapOne);
        System.out.println("tournmanent ONE: ");
        printMap(tournamentMapOne);
        System.out.println("");


        //PARENT 2
        for(int i =0; i < k;++i){
        value = randSelect.nextInt(populationSize);
        tournamentMapTwo.put(value, map.get(value));
        }
        System.out.println("tournmanent TWO: ");
        sortByValue(tournamentMapTwo);
        printMap(tournamentMapTwo);
        
        System.out.println("MAP:    ");
        printMap(map);
        keyValueOne = Integer.parseInt(tournamentMapOne.keySet().toArray()[0].toString());
        keyValueTwo = Integer.parseInt(tournamentMapTwo.keySet().toArray()[0].toString());
        System.out.println("key value 1: " + keyValueOne + "key value 2:" + keyValueTwo);
        crossOver(keyValueOne, keyValueTwo);


    }

    public void crossOver(int dad, int mom){
        childOne = new ArrayList<String>();
        childTwo = new ArrayList<String>();
        childOne = members.get(dad);
        childTwo =members.get(mom);
        for(int i =chunks/2; i < chunks;++i ){
            childOne.set(i, members.get(mom).get(i));
            childTwo.set(i, members.get(dad).get(i));

        }
        //tournamentSelectionReplacements(3);

        System.out.println("tournmanent ONE 1: ");
        tournamentMapOne = sortByValue(tournamentMapOne);
        printMap(tournamentMapOne);
        System.out.println("");

        tournamentMapTwo = sortByValue(tournamentMapTwo);
        System.out.println("tournmanent TWO 2: ");
        printMap(tournamentMapTwo);

         keyValueOne = Integer.parseInt(tournamentMapOne.keySet().toArray()[0].toString());
        keyValueTwo = Integer.parseInt(tournamentMapTwo.keySet().toArray()[0].toString());
        System.out.println("key value 1: " + keyValueOne + "key value 2:" + keyValueTwo);
        //crossOver(keyValueOne, keyValueTwo);
        populationPropagation(keyValueOne, keyValueTwo);




      
    }

    public void populationPropagation(int keyValOne, int keyValTwo){

        members.set(keyValOne, childOne);
        members.set(keyValTwo, childTwo);
        map.clear();
        for(int i =0; i < populationSize;++i){
            loadImages(i);
        }
        //genImage();

    }

    private Map<Integer, Double> sortByValue(Map<Integer, Double> unsortMap) {

        // 1. Convert Map to List of Map
        List<Map.Entry<Integer, Double>> list =
                new LinkedList<Map.Entry<Integer, Double>>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1,
                               Map.Entry<Integer, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
        for (Map.Entry<Integer, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        /*
        //classic iterator example
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }*/


        return sortedMap;
    }



    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());
        }
    }


	public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int mutationChance =0;
        File f = new File("imagesResize");
        int numberOfFiles = 0;
        int popSize = 5;
		imageChunksTest newGen = new imageChunksTest(args[0], args[1], args[2], args[3]); 
        newGen.GenImages();  
        newGen.getScoreOriginal();    
        //int number = 5;  
        if(f.exists() && f.isDirectory()){
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("images have already been resized");
             System.out.println("--------------------------------------------------------------------------------");
            numberOfFiles = f.listFiles().length;
             newGen.popInit(numberOfFiles,popSize);

             System.out.println("--------------------------------------------------------------------------------");
            System.out.println("population initialized");
             System.out.println("--------------------------------------------------------------------------------");
             System.out.println("input the probability for mutation to occur");
             mutationChance = sc.nextInt();





            for(int i =0 ; i < popSize;++i){
                newGen.loadImages(i);
            }
            for(int i =0; i < 3;++i){
            newGen.tournamentSelection(3);
            }


        }else{
            ImageResizerScript newResizer = new ImageResizerScript(args[0],newGen.getWidth(),newGen.getHeight());
            File folder = new File(args[0]);
            newResizer.listAllFiles(folder);
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("images have been resized");
             System.out.println("--------------------------------------------------------------------------------");


            newGen.popInit(newResizer.getNumFiles(),popSize);

            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("population initialized");
             System.out.println("--------------------------------------------------------------------------------");
            for(int i =0 ; i < popSize;++i){
                newGen.loadImages(i);
            }

        }
	}
}