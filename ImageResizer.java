import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.io.IOException;

 
import javax.imageio.ImageIO;
 
public class ImageResizer {
    public String outputImagePath = "imagesResize/";
    public String imageDirectory ="";
    public String imageLocation ="";
    public int index =0;
    public BufferedImage resizedImgs[] = null;
    ImageResizer(){};
    ImageResizer(String directory, String location){
        this.imageDirectory = directory;
        this.imageLocation = location;
    }

    public static void resize(String inputImagePath,
            String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());

        resizedImgs[index] = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 
        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);
 
        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }

    public void listAllFiles(File folder){
        // System.out.println("In listAllfiles(File) method");
         File[] fileNames = folder.listFiles();
         for(File file : fileNames){
             // if directory call the same method again
             if(file.isDirectory()){
                 listAllFiles(file);
             }else{
                 try {
                     readContent(file);
                 } catch (IOException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                 }
        
             }
         }
     }

       public void readContent(File file) throws IOException{
         /*System.out.println("read file " + file.getCanonicalPath() );
         try(BufferedReader br  = new BufferedReader(new FileReader(file))){
               String strLine;
               // Read lines from the file, returns null when end of stream 
               // is reached
               while((strLine = br.readLine()) != null){
                System.out.println("Line is - " + strLine);
               }
              }*/
              outputImagePath += file.getName();
        try {
            // resize to a fixed width (not proportional)
            int scaledWidth = 768;
            int scaledHeight = 768;
            index ++;
            ImageResizer.resize(file.getCanonicalPath(), outputImagePath, scaledWidth, scaledHeight);
            outputImagePath = "imagesResize/";
 
        } catch (IOException ex) {
            System.out.println("Error resizing the image.");
            ex.printStackTrace();
        }

     }
     public BufferedImage getImageArray(){
        return resizedImgs;
     }
 
    public static void main(String[] args) {
        String inputImagePath = "portrait.jpg";
        File folder = new File("properties");
        ImageResizer listFiles = new ImageResizer();
        listFiles.listAllFiles(folder);


    }
 
}