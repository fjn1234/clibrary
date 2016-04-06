package utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;


import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import entities.PhotoDirectory;


public class PhotoUtil {

    private PhotoUtil() {
    }

    public static ArrayList<String> loadDirectoryFromDB(Context context) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Uri uri = intent.getData();
        ArrayList<String> list = new ArrayList<String>();
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri,
                new String[]{"_data"}, null, null, "date_modified desc");
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex("_data"));
            list.add(new File(path).getAbsolutePath());
        }
        return list;
    }

    public static List<PhotoDirectory> getAllPhotoDirectory(Context context) {
        List<PhotoDirectory> data = new ArrayList<PhotoDirectory>();
        Hashtable<String, PhotoDirectory> table = new Hashtable<String, PhotoDirectory>();
        PhotoDirectory photoDirectory;
        List<String> directoryList = loadDirectoryFromDB(context);
        if (directoryList != null) {
            for (int i = 0; i < directoryList.size(); i++) {
                String dirtory = getDirectoryName(directoryList.get(i));
                if (table.containsKey(dirtory))
                    photoDirectory = table.get(dirtory);
                else {
                    photoDirectory = new PhotoDirectory();
                    photoDirectory.setDirectory(dirtory);
                    table.put(dirtory, photoDirectory);
                }
                photoDirectory.getPhotoes().add(directoryList.get(i));
            }
        }
        for (Iterator it = table.keySet().iterator(); it.hasNext(); ) {
            String k = (String) it.next();
            PhotoDirectory v = table.get(k);
            data.add(v);
        }
        return data;
    }

    private static String getDirectoryName(String filePath) {
        String file[] = filePath.split("/");
        if (file != null) {
            return file[file.length - 2];
        }
        return null;
    }

}
