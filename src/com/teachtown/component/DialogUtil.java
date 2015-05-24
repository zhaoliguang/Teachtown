package com.teachtown.component;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.cosina.game.kickkick.KickKick;
import com.hfut.teachtown.R;
import com.mololovecode.airdream.game.GameViewActivity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlus.Builder;
import com.orhanobut.dialogplus.DialogPlus.Gravity;
import com.orhanobut.dialogplus.DialogPlus.ScreenType;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.teachtown.adapter.GameGridViewAdapter;

import edu.upenn.cis350.simplegraphics.GameActivity;


public class DialogUtil {
	
	public static void showGameDialog(final Context context){
		final Activity activity = (Activity) context;
		Builder builder= new Builder(context);
		ArrayList<Integer> gameImageViewList = new ArrayList<Integer>();
		gameImageViewList.add(R.drawable.kick_hit);
		gameImageViewList.add(R.drawable.plane);
		gameImageViewList.add(R.drawable.game_unicorn);
		//gameImageViewList.add(R.drawable.kick_hit);
		GameGridViewAdapter adapter = new GameGridViewAdapter(context, gameImageViewList);
		builder.setAdapter(adapter);
		builder.setContentHolder(new GridHolder(gameImageViewList.size()));
		builder.setBackgroundColorResourceId(R.color.dialog_background);
		
		builder.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(DialogPlus dialog, Object item, View view, int positon) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();

				if(positon==0){
					intent.setClass(context, KickKick.class);
					context.startActivity(intent);
					
					Timer timer = new Timer();
					dialog.dismiss();
					activity.finish();
					timer.schedule(new TimerTask() {
				        public void run() {
				        	KickKick.instance.finish();
				        
				        }
				}, 60000);
				}else if(positon==1){
					intent.setClass(context, GameViewActivity.class);
					context.startActivity(intent);
					dialog.dismiss();
					activity.finish();
					Timer timer = new Timer();
					dialog.dismiss();
					timer.schedule(new TimerTask() {
				        public void run() {
				        	GameViewActivity.instance.finish();
				        	activity.finish();
				        }
				}, 60000);
					
				}else if(positon==2){
					intent.setClass(context, GameActivity.class);
					context.startActivity(intent);
					dialog.dismiss();
					activity.finish();
					Timer timer = new Timer();
					dialog.dismiss();
					timer.schedule(new TimerTask() {
				        public void run() {
				        	GameActivity.instance.finish();
				        	activity.finish();
				        }
				}, 60000);
					
				}else if(positon==3){
//					intent.setClass(context, MyGame.class);
//					context.startActivity(intent);
//					dialog.dismiss();
//					activity.finish();
				}
				
			}
		});
		
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogPlus arg0) {
				// TODO Auto-generated method stub
		
			}
		});
		
		builder.setMargins(300, 300, 300, 400);
		builder.setInAnimation(R.anim.fade_in_center);
		builder.setOutAnimation(R.anim.fade_out_center);
		builder.setGravity(Gravity.CENTER);
		builder.setScreenType(ScreenType.HALF);
		builder.setPadding(30, 30, 30, 30);
		builder.setHeader(R.layout.game_header);
		builder.setCancelable(true);
		DialogPlus dialog = builder.create();
		
		dialog.show();
		
		class Task extends TimerTask {
			public void run()
		{ 
		System.out.println("定时任务执行");
		 }
		}
		
	}

	 

}
