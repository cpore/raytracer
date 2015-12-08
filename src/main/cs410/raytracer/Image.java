package cs410.raytracer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Image {
    
    RGB[][] pixels;
    int minu, minv;
    Progress progress;
    
    final double MIN = 0.0;
    final double MAX = 255.0;
    double minVal = 1.0;
    double maxVal = 0.0;
    
    public Image(int height, int width, int minu, int minv){
        pixels = new RGB[height][width];
        this.minu = minu;
        this.minv = minv;
        progress = new Progress(height * width);
    }
    
    public int getHeight(){
        return pixels.length;
    }
    
    public int getWidth(){
        return pixels[0].length;
    }
    
    public synchronized void fillPixel(int u, int v, RGB color){
        
        int x = u - minu;
        int y = v - minv; 
        
        //System.out.println("filling pixel: (" + u + ", " + v + ") at index: " + y + ", " + x +")");
        double minColor = color.getMinVal();
        if(minColor < minVal) minVal = minColor;
        double maxColor = color.getMaxVal();
        if(maxColor > maxVal) maxVal = maxColor;
        
        pixels[y][x] = color;
        progress.update();
    }
    
    private RGB[][] getScaledPixels(){
        RGB[][] scaledPixels = new RGB[pixels.length][pixels[0].length];
        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                scaledPixels[i][j] = getScaledPixel(pixels[i][j]);
            }
        }
        
        return scaledPixels;
    }
    
    private RGB getScaledPixel(RGB pixel){
        RGB scaledPixel = new RGB();
        for(int i = 0; i < 3; i++){
            scaledPixel.rgb[i] = 255.0 * ((pixel.rgb[i] - minVal) / (maxVal - minVal));//(((MAX-MIN)*(pixel.rgb[i] - minVal)) / (maxVal - minVal)) + MIN;
        }
        
        return scaledPixel; 
    }
    
    public void writeToFile(String outputfile) throws IOException{
        FileWriter fw = new FileWriter(new File(outputfile));
        
        System.out.println("minVal = " + minVal + ", maxVal = " + maxVal);
        
        RGB[][] pixels = getScaledPixels();
        
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
