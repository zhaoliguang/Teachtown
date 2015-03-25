package com.teachtown.activity;
import java.util.Properties;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.teachtown.utils.DatabaseUtil;

import android.app.Application;


public class TeachTownApp extends Application {
	public static TeachTownApp mApp;
	@Override
    public void onCreate()
    {
		mApp = this;
		super.onCreate();
       
    }
	
	public void initApp(){
		SpeechUtility.createUtility(TeachTownApp.this, SpeechConstant.APPID +"=550d3ee3"); 
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
