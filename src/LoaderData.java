import processing.core.PApplet;
import processing.core.PImage;

public class LoaderData {
	public PImage[] images;
	public int currentFile = 0;
	public String dataPath;
	public PApplet app;
	public LoaderData(PImage[] images, String dataPath, PApplet app){
		this.images = images;
		this.dataPath = dataPath;
		this.app = app;
	}
}
