package com.teachtown.component;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.iflytek.cloud.util.ResourceUtil.RESOURCE_TYPE;
import com.teachtown.activity.TrialMatchingActivity;

public class IflytekSpeech {
	private static Context context;
	private static String TAG = "IflytekSpeech"; 	
	// 默认本地发音人
	public static String voicerLocal="xiaoyan";
	// 语音合成对象
	private static SpeechSynthesizer mTts;
	// 引擎类型
	private String 	mEngineType =  SpeechConstant.TYPE_LOCAL;
	public IflytekSpeech(Context context) {
		this.context = context;
		// TODO Auto-generated constructor stub
		mTts = SpeechSynthesizer.createSynthesizer(context, null);
		//设置发音人 
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
		//设置语速  
	     mTts.setParameter(SpeechConstant.SPEED, "50");
	   //设置音量，范围 0~100 
	     mTts.setParameter(SpeechConstant.VOLUME, "80");
	     
	}
    final static SynthesizerListener mSynListener = new SynthesizerListener(){

			@Override
			public void onBufferProgress(int arg0, int arg1, int arg2,
					String arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onCompleted(SpeechError arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSpeakBegin() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSpeakPaused() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSpeakProgress(int arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSpeakResumed() {
				// TODO Auto-generated method stub
				
			}

		
        };
	public  void startSpeech(String speech){
	   
		int code = mTts.startSpeaking(speech, mSynListener);
		if (code != ErrorCode.SUCCESS) {
			
			System.out.println("语音合成失败,错误码: " + code);
		}
		
	}

	
	
	
		
	
		
}
