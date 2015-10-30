package cs410.raytracer;

public class Progress {
    
    int totalPixels, pixelCounter, percent;

    public Progress(int totalPixels) {
       this.totalPixels = totalPixels;
       
    }
    
    public synchronized void update(){
      //update progress
        if(pixelCounter == 0){
            System.out.println("Reticulating Splines... " + (percent) + "% Complete.");
        }
        int newPct = (int) (((float)++pixelCounter / (float)totalPixels) * 100f);
        if(newPct == 100){
            System.out.println("Reticulating Splines... " + (newPct) + "% Complete.");

        }else if(newPct > percent+5){
            percent = newPct-1;
            System.out.println("Reticulating Splines... " + (percent) + "% Complete.");

        }
    }

}
