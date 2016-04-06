package io;

import java.util.ArrayList;
import java.util.Hashtable;

public class  CacheFactory<T> {
	
	private float clearRatio = 0.2f;
	private int maxCache = 0;
	private int cacheSize = 0;
	private Hashtable<String, T> bitmapCache;
	private ArrayList<String> keyOrderArray;
	
	
//
//	private T getCacheBitmap2(String pathMd5) {
//		T obj = null;
//		if (bitmapCache.containsKey(pathMd5))
//			return bitmapCache.get(pathMd5);
//		return obj;
//	}
//
//	private void recacheBitmap2(String pathMd5, T bitmap) {
//		if (maxCache == 0 || clearRatio < 0 || clearRatio > 1)
//			return;
//		clearRef2();
//		T oldBitmap = bitmapCache.get(pathMd5);
//		if (oldBitmap == null) {
//			bitmapCache.put(pathMd5, bitmap.get);
//			cacheSize += BitmapRevise.getBitmapSize(bitmap) / 1024;
//		} else {
//			cacheSize -= BitmapRevise.getBitmapSize(oldBitmap) / 1024;
//			bitmapCache.remove(pathMd5);
//			bitmapCache.put(pathMd5, bitmap);
//			cacheSize += BitmapRevise.getBitmapSize(bitmap) / 1024;
//		}
//		printLog("cacheSize:" + cacheSize);
//	}
//
//	private void cacheBitmap2(String pathMd5, Bitmap bitmap) {
//		if (getCacheBitmap(pathMd5) != null || maxCache == 0 || clearRatio < 0
//				|| clearRatio > 1)
//			return;
//		clearRef2();
//		bitmapCache.put(pathMd5, bitmap);
//		keyOrderArray.add(pathMd5);
//		cacheSize += BitmapRevise.getBitmapSize(bitmap) / 1024;
//		printLog("cacheSize:" + cacheSize);
//	}
//
//	private void clearRef2() {
//		if (cacheSize < maxCache)
//			return;
//		int clearSize = 0;
//		ArrayList<String> clearArray = new ArrayList<String>();
//		for (String key : keyOrderArray) {
//			clearSize += BitmapRevise.getBitmapSize(bitmapCache.get(key)) / 1024;
//			clearArray.add(key);
//			if (clearSize > maxCache * clearRatio)
//				break;
//		}
//		for (String key : clearArray) {
//			bitmapCache.remove(key);
//			keyOrderArray.remove(key);
//			printLog("clear:" + key);
//		}
//		cacheSize -= clearSize;
//		System.gc();
//		System.runFinalization();
//	}
//
//	public void clearAllCache() {
//		bitmapCache.clear();
//		System.gc();
//		System.runFinalization();
//	}
//
//	public static void setClearRatio(float clearRatio) {
//		CImageView.clearRatio = clearRatio;
//	}
//
//	public static void setMaxCache(int maxCache) {
//		CImageView.maxCache = maxCache;
//	}


}
