import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.io.IOException;

 
import javax.imageio.ImageIO;
import java.util.*;
 
public class ImageResizerScript{
    public String outputImagePath = "imagesResize/";
    public String imageDirectory ="";
    public int scaledWidth = 0;
    public int scaledHeight = 0;
    public static int index = 0;
    public int indexTwo = 0;
    public String fileName = "img";
    public int numFiles =0;
    public File newfile =new File("newfile.txt");
   
    public ArrayList<File> files;
    ImageResizerScript(){};
    ImageResizerScript(String directory, int width, int height){
        files = new ArrayList<File>();
        this.imageDirectory = directory;
        //this.imageLocation = location;
        this.scaledHeight = height;
        this.scaledWidth = width;
    }

    public void resize(String inputImagePath,
            String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        //System.out.println("OUTPUT IMAGE: " + outputImage);
        // extracts extension of output file
        System.out.println("OUTPUT IMAGE PATH: " + outputImagePath);
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);
 
        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
        //++ index;
        //ImageIO.write(finalImg, "jpeg", new File("finalImg3.jpg"));
        //imgFiles[i] = new File("imagesResize/img" + i + ".jpg");
    }

    public void listAllFiles(File folder){
         System.out.println("In listAllfiles(File) method");
         File[] fileNames = folder.listFiles();
         for(File file : fileNames){
             // if directory call the same method again
             if(file.isDirectory()){
                 listAllFiles(file);
             }else{
                 try {
                     ++ numFiles;
                     files.add(file);
                     readContent(file);
                 } catch (IOException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                 }
        
             }
         }
     }

       public void readContent(File file) throws IOException{
        outputImagePath += "img";
        outputImagePath += indexTwo;
        outputImagePath += ".jpg";

        //System.out.println("image name: " + outputImagePath);
        try {
            // resize to a fixed width (not proportional)
            //int scaledWidth = 768;
            //int scaledHeight = 768;
            resize(file.getCanonicalPath(), outputImagePath, scaledWidth, scaledHeight);
            outputImagePath = "imagesResize/";
            ++ indexTwo;
 
        } catch (IOException ex) {
            System.out.println("Error resizing the image.");
            ex.printStackTrace();
        }

     }

     public int getNumFiles(){
        return numFiles;
     }
 
    public static void main(String[] args) {
        String inputImagePath = "portrait.jpg";
        File folder = new File("images");
        ImageResizerScript listFiles = new ImageResizerScript();
        listFiles.listAllFiles(folder);


    }
 
}