package cs410.raytracer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Image {
    
    RGB[][] pixels;
    
    public Image(int height, int width){
        pixels = new RGB[height][width];
    }
    
    public int getHeight(){
        return pixels.length;
    }
    
    public int getWidth(){
        return pixels[0].length;
    }
       
    
    public void writeToFile(String outputfile) throws IOException{
        FileWriter fw = new FileWriter(new File(outputfile));
        
        //first line: "magic number"
        fw.write("P3\n");
        
        //second line: width, height, 255 (max pixel value)
        fw.write(getWidth() + " " + getHeight() + " " + 255 + "\n" );
        
        // write the rgb value lines
        for(int i = 0; i < pixels.length; i++){
            StringBuilder line = new StringBuilder();
            for(int j = 0; j < pixels[i].length; j++){
                line.append(pixels[i][j].toString() + "    ");
                
            }
            line.append("\n");
            fw.write(line.toString());
        }
        
        fw.flush();
        fw.close();
    }

}
