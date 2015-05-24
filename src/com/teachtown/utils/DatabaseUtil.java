package com.teachtown.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.hfut.teachtown.R;


import net.tsz.afinal.FinalDb;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class DatabaseUtil {
	 private static FinalDb db;
	 private static final int BUFFER_SIZE =400000;
	 public static final String DB_NAME ="clientdb.db";//保存的数据库文件名
	 public static final String PACKAGE_NAME ="com.hfut.teachtown";
	 public static final String DB_PATH ="/data"
	            + Environment.getDataDirectory().getAbsolutePath() +"/"
	            + PACKAGE_NAME; //在手机里存放数据库的位置
	 public static final String dbfile = DB_PATH+"/"+DB_NAME;
	 public static void createDatabase(Context context) {
	        try{
	            if(!(new File(dbfile).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
	                InputStream is =context.getResources().openRawResource(
	                        R.raw.clientdb);//欲导入的数据库
	                FileOutputStream fos =new FileOutputStream(dbfile);
	                byte[] buffer =new byte[BUFFER_SIZE];
	                int count =0;
	                while((count = is.read(buffer)) >0) {
	                    fos.write(buffer,0, count);
	                }
	                fos.close();
	                is.close();
	            }
	        
	        }catch(FileNotFoundException e) {
	            Log.e("Database","File not found");
	            e.printStackTrace();
	        }catch(IOException e) {
	            Log.e("Database","IO exception");
	            e.printStackTrace();
	        }
	       
	    }

	 public static FinalDb getDatabase(Context context){
		 if(db==null)
			  return FinalDb.create(context, dbfile, true, 300800, null);
		 else 
			 return db;
	 }
	 
}
