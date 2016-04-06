package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import io.CFile;


public class FileUtil {

    public static boolean inputstreamToFile(InputStream ins, String filePath) {
        try {
            CFile file = new CFile(filePath);
            file.createNewFileAndDirectory();
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static StringBuilder readTxt(String filePath) {
        InputStreamReader read = null;
        StringBuilder builder = new StringBuilder();
        try {
            String encoding = "UTF-8";
            CFile file = new CFile(filePath);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                System.out.println(bufferedReader.toString());
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    builder.append(lineTxt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            builder = new StringBuilder();
        } finally {
            if (read != null) {
                try {
                    read.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return builder;
        }
    }

    public static int getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                int dirSize = 0;
                for (File f : children)
                    dirSize += getDirSize(f);
                return dirSize;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                int size = (int) file.length() / 1024 / 1024;
                return size;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0;
        }
    }

    public static int getDirSize(String dir) {
        return getDirSize(new CFile(dir));
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static boolean deleteDir(String dir) {
        return deleteDir(new CFile(dir));
    }
}
