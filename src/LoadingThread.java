import processing.core.PImage;

public class LoadingThread implements Runnable{
	
	LoaderData data;
	
	public LoadingThread(LoaderData data){
		super();
		this.data = data;
	}
	
	@Override
	public void run() {
		while(data.currentFile < 90){
			data.images[data.currentFile] = data.app.loadImage(data.dataPath + "/cell_tex/" + String.format("%04d", data.currentFile) + ".png");
			if(data.currentFile < 89){
				data.currentFile++;
			}
		}
		
	}

}
