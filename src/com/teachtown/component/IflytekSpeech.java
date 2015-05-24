package com.teachtown.component;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

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
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaofeng");
		//设置语速  
	     mTts.setParameter(SpeechConstant.SPEED, "50");
	   //设置音量，范围 0~100 
	     mTts.setParameter(SpeechConstant.VOLUME, "80");
	     
	}
	public static void init(Context ctx){
		// TODO Auto-generated constructor stub
		
		mTts = SpeechSynthesizer.createSynthesizer(ctx, mTtsInitListener);
		//设置发音人 
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaofeng");
		//mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaofeng");
		
		//设置语速  
	     mTts.setParameter(SpeechConstant.SPEED, "50");
	   //设置音量，范围 0~100 
	     mTts.setParameter(SpeechConstant.VOLUME, "80");
	}
	
	private static SpeechSynthesizer geIflyMts(){
		if(mTts==null){
			mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
			//设置发音人 
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
			mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaofeng");
			//设置语速  
		     mTts.setParameter(SpeechConstant.SPEED, "50");
		   //设置音量，范围 0~100 
		     mTts.setParameter(SpeechConstant.VOLUME, "80");
		}
		return mTts;
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
	public static  void startSpeech(String speech){
		if(mTts==null){
			mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
			//设置发音人 
			mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaofeng");
			//设置语速  
		     mTts.setParameter(SpeechConstant.SPEED, "50");
		   //设置音量，范围 0~100 
		     mTts.setParameter(SpeechConstant.VOLUME, "100");
		}
		int code = mTts.startSpeaking(speech, mSynListener);
		if (code != ErrorCode.SUCCESS) {
			
			System.out.println("语音合成失败,错误码: " + code);
		}
		
	}
	private static InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
        		System.out.println("初始化失败,错误码："+code);
        	} else {
				// 初始化成功，之后可以调用startSpeaking方法
        		// 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
        		// 正确的做法是将onCreate中的startSpeaking调用移至这里
			}		
		}
	};
	public static void stopSpeech(){
		if(mTts !=null){
			mTts.stopSpeaking();
			// 退出时释放连接
			//mTts.destroy();
		}
			
	
	}

	
	
	
		
	
		
}
