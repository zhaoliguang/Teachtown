package com.teachtown.activity;
import java.util.Properties;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.teachtown.component.IflytekSpeech;
import com.teachtown.utils.DatabaseUtil;

import android.app.Application;


public class TeachTownApp extends Application {
	public static TeachTownApp mApp;
	public static int studentId;
	@Override
    public void onCreate()
    {
		mApp = this;
		super.onCreate();
       
    }
	
	public static void initApp(){
		try{
			// 1、加载数据库文件
			
			 DatabaseUtil.createDatabase(mApp);
			 
		}catch(Exception e){
			System.out.println("initApp Error:"+e.getMessage());
		}
		SpeechUtility.createUtility(mApp, SpeechConstant.APPID +"=550d3ee3,"+SpeechConstant.FORCE_LOGIN +"=true");
		IflytekSpeech.init(mApp);
		
				// TODO Auto-generated method stub
				
			}
		
	    
	

	public static int getStudentId() {
		return studentId;
	}

	public static void setStudentId(int studentId) {
		TeachTownApp.studentId = studentId;
	}
}
