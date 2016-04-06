package entities;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ParcelCreator")
public class PhotoDirectory {
	public String directory;
	public List<String> photoes =new ArrayList<String>();

    public List<String> getPhotoes() {
        return photoes;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
