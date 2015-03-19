package com.teachtown.activity;
import java.util.Properties;

import com.teachtown.utils.DatabaseUtil;

import android.app.Application;
import android.os.Handler;

public class TeachTownApp extends Application {
	public static TeachTownApp mApp;
	@Override
    public void onCreate()
    {
		mApp = this;
		super.onCreate();
       
    }
	
	public void initApp(){
		
		 new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					// 1、加载数据库文件
					 DatabaseUtil.createDatabase(TeachTownApp.this);
					 
				}catch(Exception e){
					System.out.println("initApp Error:"+e.getMessage());
				}
			}
			 
		 }.start();
	    
	}
}
