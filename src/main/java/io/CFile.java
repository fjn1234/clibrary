package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class CFile extends File {

    /**
     *
     */
    private static final long serialVersionUID = 1095853825545646825L;

    public CFile(File dir, String name) {
        super(dir, name);
    }

    public CFile(String dirPath, String name) {
        super(dirPath, name);
    }

    public CFile(String path) {
        super(path);
    }

    public CFile(URI uri) {
        super(uri);
    }

    public void createNewFileAndDirectory() {
        try {
            boolean b;
            File dir = new File(this.getAbsolutePath().substring(0,
                    this.getAbsolutePath().lastIndexOf("/")));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!this.exists()) {
                try {
                    this.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public boolean copy(String path) {
        try {
            CFile file = new CFile(path);
            if (file.exists())
                file.delete();
            file.createNewFileAndDirectory();
            int byteread = 0;
            if (this.exists()) {
                InputStream inStream = new FileInputStream(this);
                FileOutputStream fs = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean move(String destPath) {
        CFile file = new CFile(destPath, this.getName());
        return this.renameTo(file);
    }


    @Override
    public boolean mkdirs() {
        return this.exists() || super.mkdirs();
    }

}
