package com.teachtown.activity;
import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hfut.teachtown.R;
import com.nineoldandroids.animation.ObjectAnimator;
import com.teachtown.component.AnimationTrial;
import com.teachtown.component.AssetType;
import com.teachtown.component.DialogUtil;
import com.teachtown.component.IflytekSpeech;
import com.teachtown.model.Asset;
import com.teachtown.model.TestResultSync;
import com.teachtown.model.Trial;
import com.teachtown.model.TrialAssetMap;
import com.teachtown.utils.DatabaseUtil;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

public class TrialReceptiveLabeActivity extends FinalActivity implements OnClickListener,OnPreparedListener,OnCompletionListener,OnTouchListener,OnErrorListener{
	//private IflytekSpeech iflytekSpeech;
	private FinalDb database;
	private List<Trial> trialList;
	private HashMap<Integer, HashMap<Integer,List<TrialAssetMap>>> trialHashmap;
	private ArrayList<List<TrialAssetMap>> allTrialAssetList;
	private String lessonName;
	private HashMap<Integer,TestResultSync> testResultSyncMap;
	private List<Map.Entry<Integer, TestResultSync>> testResultSyncMapSort,testResultPromptSyncMapSort;
	private int numDistractors ;
	private int selectAssetId;//记录每次应该选中的资源id
	private int lessonHandle;
	private int selectTrialid;
	private  int MapTypeSize = 8;
	private View view;
	private JsonObjectRequest jsonObjRequest;
	private RequestQueue mVolleyQueue;
	//private int duringTime = 3000;测试修改
	private int duringTime = 1000;
	//private int animationFadeTime = 2000;测试修改
	private int animationFadeTime = 1000;
	private int fadeTime = 1000;
	
	//private int waiteTime =4000;测试修改
	private int waiteTime =2000;
	private int allCount = 30;
	private double noPromptTestAllCount=0,correctCount=0,wrongCount=0,promptAllTestCount=0,allTestCount=0,selectTestCount;
	
	private String sdRootFolder ="/storage/sdcard0" ;
	private Random random;
	private ImageView selectView,selectView_square,distractorView1,distractorView1_square,distractorView2,distractorView2_square,distractorView3,distractorView3_square;
	private VideoView selectVideoView,distractorVideoView1,distractorVideoView2,distractorVideoView3;
	private List<Map<String,View>> imageViewList;
	private  Timer timer = new Timer();
	private MediaPlayer mediaPlayerWin;
	private MediaPlayer mediaPlayerLose;
	private String tiralSpeech;
	private int speechType=1;//读音类型
	private View emiter,emiter_top_left,emiter_top_right;
	private View viewBack0,viewBack1,viewBack2,viewBack3; 
	private int animationIndex=0;
	private int mp3AssetId;
	private boolean isPrompt=false;
	private final String TAG_REQUEST = "MY_TAG";
	HashMap<String, View> mapSelectView;
	private HashMap<String ,View> mapDistractorView1,mapDistractorView2,mapDistractorView3;
	private double selectCorrectPercent=0,selectPromptCorrectPercent=0;
	private ImageView button_prompt_receptive_label0,button_prompt_receptive_label1,button_prompt_receptive_label2,button_prompt_receptive_label3;
	@ViewInject(id = R.id.teacher_standby_iv) ImageView im_teacher_standby_iv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Bundle bundle = this.getIntent().getExtras();
		IflytekSpeech.init(TrialReceptiveLabeActivity.this);
		numDistractors = bundle.getInt("numDistractors");
		lessonHandle = bundle.getInt("lessonHandle");
		lessonName= bundle.getString("lessonName");
		random = new Random();
		testResultSyncMap = new HashMap<Integer, TestResultSync>();
		mediaPlayerWin = MediaPlayer.create(this,R.raw.win);  
		mediaPlayerLose = MediaPlayer.create(this,R.raw.lose); 
		mVolleyQueue = Volley.newRequestQueue(this);
		// iflytekSpeech = new IflytekSpeech(TrialReceptiveLabeActivity.this);
		 imageViewList = new ArrayList<Map<String,View>>();
		switch(numDistractors){
		case 0:
			view = inflater.inflate(R.layout.receptive_label_0, null,true);
			setContentView(view);
			viewBack0 = view.findViewById(R.id.rr_receptive_label_0);
			button_prompt_receptive_label0=(ImageView) view.findViewById(R.id.button_prompt_receptive_label0);
			emiter = view.findViewById(R.id.receptive_label0_emiter_center);
			emiter_top_left=view.findViewById(R.id.reveptive_label0_emiter_top_left);
			emiter_top_right=view.findViewById(R.id.reveptive_label0_emiter_top_right);
					
			selectView = (ImageView) view.findViewById(R.id.im_select0);
			selectView_square = (ImageView) view.findViewById(R.id.im_select0_square);
			selectVideoView = (VideoView) view.findViewById(R.id.webView_select0);
			setVideoView(selectVideoView);
			selectView.setOnClickListener(this);
			mapSelectView = new HashMap<String, View>();
			mapSelectView.put("im_distractor", selectView);
			mapSelectView.put("im_distractor_square", selectView_square);
			mapSelectView.put("webView", selectVideoView);
			imageViewList.add(mapSelectView);
			break;
		case 1:
			view = inflater.inflate(R.layout.receptive_label_1, null,true);
			setContentView(view);
			viewBack1 = view.findViewById(R.id.rr_receptive_label_1);
			button_prompt_receptive_label1=(ImageView) view.findViewById(R.id.button_prompt_receptive_label1);
			emiter = view.findViewById(R.id.receptive_label1_emiter_center);
			emiter_top_left=view.findViewById(R.id.reveptive_label1_emiter_top_left);
			emiter_top_right=view.findViewById(R.id.reveptive_label1_emiter_top_right);
			
			
			selectView = (ImageView) view.findViewById(R.id.im_select1);
			selectVideoView = (VideoView) view.findViewById(R.id.webView_select1);
			setVideoView(selectVideoView);
			selectView_square = (ImageView) view.findViewById(R.id.im_select1_square);
			distractorView1 = (ImageView) view.findViewById(R.id.im_distractor1_distractor1);
			distractorView1_square = (ImageView) view.findViewById(R.id.im_distractor1_distractor1_square);
			distractorVideoView1 = (VideoView) view.findViewById(R.id.webView_distractor1_distractor1);
			setVideoView(distractorVideoView1);
			selectView.setOnClickListener(this);
			distractorView1.setOnClickListener(this);
			mapSelectView = new HashMap<String, View>();
			mapSelectView.put("im_distractor", selectView);
			mapSelectView.put("im_distractor_square", selectView_square);	
			mapSelectView.put("webView", selectVideoView);
			imageViewList.add(mapSelectView);
			 mapDistractorView1 = new HashMap<String, View>();
			mapDistractorView1.put("im_distractor", distractorView1);
			mapDistractorView1.put("im_distractor_square", distractorView1_square);
			mapDistractorView1.put("webView", distractorVideoView1);
			imageViewList.add(mapDistractorView1);
			break;
		case 2:
			view = inflater.inflate(R.layout.receptive_label_2, null,true);
			setContentView(view);
			viewBack2 = view.findViewById(R.id.rr_receptive_label_2);
			button_prompt_receptive_label2=(ImageView) view.findViewById(R.id.button_prompt_receptive_label2);
			emiter = view.findViewById(R.id.receptive_label2_emiter_center);
			emiter_top_left=view.findViewById(R.id.reveptive_label2_emiter_top_left);
			emiter_top_right=view.findViewById(R.id.reveptive_label2_emiter_top_right);
			selectView = (ImageView) view.findViewById(R.id.im_select2);
			selectView_square = (ImageView) view.findViewById(R.id.im_select2_square);
			selectVideoView = (VideoView) view.findViewById(R.id.webView_select2);
			setVideoView(selectVideoView);
			distractorView1 = (ImageView) view.findViewById(R.id.im_distractor2_distractor1);
			distractorView1_square = (ImageView) view.findViewById(R.id.im_distractor2_distractor1_square);
			distractorVideoView1 = (VideoView) view.findViewById(R.id.webView_distractor2_distractor1);
			setVideoView(distractorVideoView1);
			distractorView2 = (ImageView) view.findViewById(R.id.im_distractor2_distractor2);
			distractorView2_square = (ImageView) view.findViewById(R.id.im_distractor2_distractor2_square);
			distractorVideoView2 = (VideoView) view.findViewById(R.id.webView_distractor2_distractor2);
			setVideoView(distractorVideoView2);
			selectView.setOnClickListener(this);
			distractorView1.setOnClickListener(this);
			distractorView2.setOnClickListener(this);
			mapSelectView = new HashMap<String, View>();
			mapSelectView.put("im_distractor", selectView);
			mapSelectView.put("im_distractor_square", selectView_square);
			mapSelectView.put("webView", selectVideoView);
			imageViewList.add(mapSelectView);
			 mapDistractorView1 = new HashMap<String, View>();
				mapDistractorView1.put("im_distractor", distractorView1);
				mapDistractorView1.put("im_distractor_square", distractorView1_square);
				mapDistractorView1.put("webView", distractorVideoView1);
				imageViewList.add(mapDistractorView1);
				
				mapDistractorView2 = new HashMap<String, View>();
				mapDistractorView2.put("im_distractor", distractorView2);
				mapDistractorView2.put("im_distractor_square", distractorView2_square);
				mapDistractorView2.put("webView", distractorVideoView2);
				imageViewList.add(mapDistractorView2);


			break;
		case 3:
			view = inflater.inflate(R.layout.receptive_lable_3, null,true);
			setContentView(view);
			viewBack3 = view.findViewById(R.id.rr_receptive_label_3);
			button_prompt_receptive_label3=(ImageView) view.findViewById(R.id.button_prompt_receptive_label3);
			emiter = view.findViewById(R.id.receptive_label3_emiter_center);
			emiter_top_left=view.findViewById(R.id.reveptive_label3_emiter_top_left);
			emiter_top_right=view.findViewById(R.id.reveptive_label3_emiter_top_right);
			
			selectView = (ImageView) view.findViewById(R.id.im_select3);
			selectView_square = (ImageView) view.findViewById(R.id.im_select3_square);
			selectVideoView = (VideoView) view.findViewById(R.id.webView_select3);
			setVideoView(selectVideoView);
			distractorView1 = (ImageView) view.findViewById(R.id.im_distractor3_distractor1);
			distractorView1_square = (ImageView) view.findViewById(R.id.im_distractor3_distractor1_square);
			distractorVideoView1 = (VideoView) view.findViewById(R.id.webView_distractor3_distractor1);
			setVideoView(distractorVideoView1);
			distractorView2 = (ImageView) view.findViewById(R.id.im_distractor3_distractor2);
			distractorView2_square = (ImageView) view.findViewById(R.id.im_distractor3_distractor2_square);
			distractorVideoView2 = (VideoView) view.findViewById(R.id.webView_distractor3_distractor2);
			setVideoView(distractorVideoView2);
			distractorView3 = (ImageView) view.findViewById(R.id.im_distractor3_distractor3);
			distractorView3_square = (ImageView) view.findViewById(R.id.im_distractor3_distractor3_square);
			distractorVideoView3 = (VideoView) view.findViewById(R.id.webView_distractor3_distractor3);
			setVideoView(distractorVideoView3);
			selectView.setOnClickListener(this);
			distractorView1.setOnClickListener(this);
			distractorView2.setOnClickListener(this);
			distractorView3.setOnClickListener(this);
			
			mapSelectView = new HashMap<String, View>();
			mapSelectView.put("im_distractor", selectView);
			mapSelectView.put("im_distractor_square", selectView_square);
			mapSelectView.put("webView", selectVideoView);
			imageViewList.add(mapSelectView);
			 mapDistractorView1 = new HashMap<String, View>();
				mapDistractorView1.put("im_distractor", distractorView1);
				mapDistractorView1.put("im_distractor_square", distractorView1_square);
				mapDistractorView1.put("webView", distractorVideoView1);
				imageViewList.add(mapDistractorView1);
				
				mapDistractorView2 = new HashMap<String, View>();
				mapDistractorView2.put("im_distractor", distractorView2);
				mapDistractorView2.put("im_distractor_square", distractorView2_square);
				mapDistractorView2.put("webView", distractorVideoView2);
				imageViewList.add(mapDistractorView2);
				
				mapDistractorView3 = new HashMap<String, View>();
				mapDistractorView3.put("im_distractor", distractorView3);
				mapDistractorView3.put("im_distractor_square", distractorView3_square);
				mapDistractorView3.put("webView", distractorVideoView3);
				imageViewList.add(mapDistractorView3);
			break;
		}
		trialHashmap = new HashMap<Integer, HashMap<Integer,List<TrialAssetMap>>>();
		//初始化allTrialAssetList 
		allTrialAssetList = new ArrayList<List<TrialAssetMap>>();
		for(int i=0;i<=MapTypeSize;i++){   
			List<TrialAssetMap> trialAssetMapList = new ArrayList<TrialAssetMap>();
			allTrialAssetList.add(trialAssetMapList);   
	    }   
		
		database = DatabaseUtil.getDatabase(TrialReceptiveLabeActivity.this);
		//对TrialAssetMap按照mapType进行分类
		trialList = database.findAllByWhere(Trial.class, "lessonHandle="+lessonHandle, "trialId");
		constructorTrialMap(trialList,trialHashmap);
		new UpdateImageViewTask(trialList, trialHashmap, imageViewList).execute("");
	
		
	}
	private void constructorTrialMap(List<Trial> trialList,Map trialHashmap){
		for(Trial trial:trialList){
			TestResultSync testResultSync = new TestResultSync();
			testResultSync.setTrialId(trial.getTrialId());
			testResultSync.setLessonHandle(lessonHandle);
			testResultSync.setExerciseName(trial.getChineseName());

			testResultSyncMap.put(trial.getTrialId(), testResultSync);
			List<TrialAssetMap> trialAssetMapList= (List<TrialAssetMap>) database.findAllByWhere(TrialAssetMap.class, "trialId="+trial.getTrialId());
			Map trialAssetHashMap = new HashMap<Integer, List<TrialAssetMap>>();
			for(int i=1;i<=8;i++)
				trialAssetHashMap.put(i, new ArrayList<TrialAssetMap>());
			for(TrialAssetMap trialAssetMap:trialAssetMapList)
				((ArrayList<TrialAssetMap>)(trialAssetHashMap.get(trialAssetMap.getMapType()))).add(trialAssetMap);
			trialHashmap.put(trial.getTrialId(), trialAssetHashMap);
	
		}
		
		testResultSyncMapSort = new ArrayList<Map.Entry<Integer, TestResultSync>>( testResultSyncMap.entrySet());  
		testResultPromptSyncMapSort = new ArrayList<Map.Entry<Integer, TestResultSync>>( testResultSyncMap.entrySet());
	}
	
	public void updateImageView(List<Map<String ,View>> imageViewIvList,HashMap<Integer, Integer> map){
		
		Collections.shuffle(imageViewIvList);//随机排序ImageView，使每次匹配项出现位置不同
		
		int selectAssetId,distractorAssetId1,distractorAssetId2,distractorAssetId3;
		Asset asset;
		switch(imageViewIvList.size()){
		case 1:
			
			
			mapSelectView = (HashMap<String, View>) imageViewIvList.get(0);
			selectView=(ImageView) mapSelectView.get("im_distractor");
			selectView_square=(ImageView) mapSelectView.get("im_distractor_square");
			selectVideoView= (VideoView) mapSelectView.get("webView");
			selectView.setEnabled(true);
			selectVideoView.setEnabled(true);
			selectAssetId = map.get(AssetType.select);
			
			animate(selectView).setDuration(0);
			animate(selectView_square).setDuration(0);
			animate(selectVideoView).setDuration(0);
			
			
			animate(selectView).alpha(1);
			animate(selectView_square).alpha(1);
			animate(selectVideoView).alpha(1);
			
			
			asset = getAsset(selectAssetId);
			if(asset.getExtension().equals("mp4")){
				selectVideoView.setId(selectAssetId);
				selectView.setImageResource(R.drawable.white);
				animate(selectView).alpha(0);
			
				selectVideoView.setVideoPath(getFileName(asset));
				selectVideoView.setVisibility(View.VISIBLE);
				animate(selectVideoView).alpha(1);
				selectVideoView.start();
				
			}else
			{
				selectView.setId(selectAssetId);
				selectVideoView.setVisibility(View.INVISIBLE);
				animate(selectView).alpha(1);
				selectView.setImageBitmap(getImageFromFile(getFileName(asset)));
			}
		
			break;
		case 2:
			mapSelectView = (HashMap<String, View>) imageViewIvList.get(0);
			selectView=(ImageView) mapSelectView.get("im_distractor");
			selectView_square=(ImageView) mapSelectView.get("im_distractor_square");
			selectVideoView= (VideoView) mapSelectView.get("webView");
		
			selectVideoView.setEnabled(true);
			mapDistractorView1 = (HashMap<String, View>) imageViewIvList.get(1);
			distractorView1=(ImageView) mapDistractorView1.get("im_distractor");
			distractorView1_square=(ImageView) mapDistractorView1.get("im_distractor_square");
			distractorVideoView1= (VideoView) mapDistractorView1.get("webView");
	
			selectView.setEnabled(true);
			selectVideoView.setEnabled(true);
			distractorView1.setEnabled(true);
			distractorVideoView1.setEnabled(true);
			animate(selectView).setDuration(0);
			animate(selectView_square).setDuration(0);
			animate(selectVideoView).setDuration(0);
			animate(selectView).alpha(1);
			animate(selectVideoView).alpha(1);
			animate(selectView_square).alpha(1);
			
			
			animate(distractorView1).setDuration(0);
			animate(distractorView1_square).setDuration(0);
			animate(distractorVideoView1).setDuration(0);
			
			
			
			selectAssetId = map.get(AssetType.select);
			distractorAssetId1 = map.get(AssetType.distractor1);
			
		
			asset = getAsset(selectAssetId);
			if(asset.getExtension().equals("mp4")){
				selectVideoView.setId(selectAssetId);
				selectView.setImageResource(R.drawable.white);
				animate(selectView).alpha(0);
				selectVideoView.setVisibility(View.VISIBLE);
				selectVideoView.setVideoPath(getFileName(asset));
				selectVideoView.setVisibility(View.VISIBLE);
				animate(selectVideoView).alpha(1);
				selectVideoView.start();
				
			}else
			{
				selectView.setId(selectAssetId);
				selectVideoView.setVisibility(View.INVISIBLE);
				animate(selectView).alpha(1);
				selectView.setImageBitmap(getImageFromFile(getFileName(asset)));
			}
			
			
			
			asset = getAsset(distractorAssetId1);
			if(asset.getExtension().equals("mp4")){
				distractorVideoView1.setId(distractorAssetId1);
				distractorView1.setImageResource(R.drawable.white);
				animate(distractorView1).alpha(0);
				
				
				
				distractorVideoView1.setVideoPath(getFileName(asset));
				distractorVideoView1.setVisibility(View.VISIBLE);
				animate(distractorVideoView1).alpha(1);
				distractorVideoView1.start();
			}else
			{
				distractorView1.setId(distractorAssetId1);
				distractorVideoView1.setVisibility(View.INVISIBLE);
				animate(distractorView1).alpha(1);
				distractorView1.setImageBitmap(getImageFromFile(getFileName(asset)));
			}
			if(selectTestCount>2){
				if(selectCorrectPercent<=0.25&&selectTestCount>4){
					animate(distractorView1).alpha((float) 0);
					animate(distractorView1_square).alpha((float) 0);
					distractorView1.setEnabled(false);
					distractorView1_square.setEnabled(false);
					distractorVideoView1.setEnabled(false);
					animate(distractorVideoView1).alpha((float) 0);
				}
				else if(selectCorrectPercent<=0.5){
					animate(distractorView1).alpha((float) 0.5);
					animate(distractorView1_square).alpha((float) 0.5);
					animate(distractorVideoView1).alpha((float) 1);
				}else{
					animate(distractorView1_square).alpha((float) 1);
				}
					
			}else{
				
				animate(distractorView1_square).alpha((float) 1);
			}
			
			
			break;
		case 3:
			mapSelectView = (HashMap<String, View>) imageViewIvList.get(0);
			selectView=(ImageView) mapSelectView.get("im_distractor");
			selectView_square=(ImageView) mapSelectView.get("im_distractor_square");
			selectVideoView= (VideoView) mapSelectView.get("webView");
			
			mapDistractorView1 = (HashMap<String, View>) imageViewIvList.get(1);
			distractorView1=(ImageView) mapDistractorView1.get("im_distractor");
			distractorView1_square=(ImageView) mapDistractorView1.get("im_distractor_square");
			distractorVideoView1= (VideoView) mapDistractorView1.get("webView");
			
			mapDistractorView2 = (HashMap<String, View>) imageViewIvList.get(2);
			distractorView2=(ImageView) mapDistractorView2.get("im_distractor");
			distractorView2_square=(ImageView) mapDistractorView2.get("im_distractor_square");
			distractorVideoView2= (VideoView) mapDistractorView2.get("webView");
			
			selectView.setEnabled(true);
			selectVideoView.setEnabled(true);
			distractorView1.setEnabled(true);
			distractorView2.setEnabled(true);
			distractorVideoView1.setEnabled(true);
			distractorVideoView2.setEnabled(true);
			
			animate(selectView).setDuration(0);
			animate(selectView_square).setDuration(0);
			animate(selectVideoView).setDuration(0);
			
			animate(selectView).alpha(1);
			animate(selectView_square).alpha(1);
			animate(selectVideoView).alpha(1);
			
			animate(distractorView1).setDuration(0);
			animate(distractorView1_square).setDuration(0);
			animate(distractorVideoView1).setDuration(0);
			
			animate(distractorView2).setDuration(0);
			animate(distractorView2_square).setDuration(0);
			animate(distractorVideoView2).setDuration(0);
			
			
			
			
			
			selectAssetId = map.get(AssetType.select);
			distractorAssetId1 = map.get(AssetType.distractor1);
			distractorAssetId2 = map.get(AssetType.distractor2);
			
			asset = getAsset(selectAssetId);
			if(asset.getExtension().equals("mp4")){
				selectVideoView.setId(selectAssetId);
				
				selectView.setImageResource(R.drawable.white);
				animate(selectView).alpha(0);
				selectVideoView.setVisibility(View.VISIBLE);
				animate(selectVideoView).alpha(1);
				selectVideoView.setVideoPath(getFileName(asset));
				selectVideoView.start();
			}else
			{
				selectView.setId(selectAssetId);
				selectVideoView.setVisibility(View.INVISIBLE);
				animate(selectView).alpha(1);
				selectView.setImageBitmap(getImageFromFile(getFileName(asset)));
			}
			asset = getAsset(distractorAssetId1);
			if(asset.getExtension().equals("mp4")){
				
				distractorVideoView1.setId(distractorAssetId1);
				
				distractorView1.setImageResource(R.drawable.white);
				animate(distractorView1).alpha(0);
				distractorVideoView1.setVideoPath(getFileName(asset));
				distractorVideoView1.setVisibility(View.VISIBLE);
				animate(distractorVideoView1).alpha(1);
				distractorVideoView1.start();
			}else
			{
				distractorView1.setId(distractorAssetId1);
				distractorVideoView1.setVisibility(View.INVISIBLE);
				animate(distractorView1).alpha(1);
				distractorView1.setImageBitmap(getImageFromFile(getFileName(asset)));
			}
			asset = getAsset(distractorAssetId2);
			if(asset.getExtension().equals("mp4")){
				
				distractorVideoView2.setId(distractorAssetId2);
				
				distractorView2.setImageResource(R.drawable.white);
				animate(distractorView2).alpha(0);
				distractorVideoView2.setVideoPath(getFileName(asset));
				distractorVideoView2.setVisibility(View.VISIBLE);
				animate(distractorVideoView2).alpha(1);
				distractorVideoView2.start();
			}else
			{
				distractorView2.setId(distractorAssetId2);
				distractorVideoView2.setVisibility(View.INVISIBLE);
				animate(distractorView2).alpha(1);
				distractorView2.setImageBitmap(getImageFromFile(getFileName(asset)));
			}
			
			if(selectTestCount>2){
				if(selectCorrectPercent<=0.25&&selectTestCount>4){
					animate(distractorView1).alpha((float) 0);
					animate(distractorView1_square).alpha((float) 0);
					animate(distractorVideoView1).alpha((float) 0);
					distractorView1.setEnabled(false);
					distractorView1_square.setEnabled(false);
					distractorVideoView1.setEnabled(false);
					animate(distractorView2).alpha((float) 0);	
					animate(distractorView2_square).alpha((float) 0);	
					distractorView2.setEnabled(false);
					distractorView2_square.setEnabled(false);
					distractorVideoView2.setEnabled(false);
					animate(distractorVideoView2).alpha((float) 0);	
				}
				else if(selectCorrectPercent<=0.5){
					animate(distractorView1).alpha((float) 0.5);
					animate(distractorView1_square).alpha((float) 0.5);
					animate(distractorVideoView1).alpha((float) 1);
					animate(distractorView2).alpha((float) 0.5);
					animate(distractorView2_square).alpha((float) 0.5);
					animate(distractorVideoView2).alpha((float) 1);
				}else {
					animate(distractorView1_square).alpha((float) 1);
					animate(distractorView2_square).alpha((float) 1);
				}
					
			}else{
				
				animate(distractorView1_square).alpha((float) 1);
				animate(distractorView2_square).alpha((float) 1);	
			}
			break;
			
		case 4:
			mapSelectView = (HashMap<String, View>) imageViewIvList.get(0);
			selectView=(ImageView) mapSelectView.get("im_distractor");
			selectView_square=(ImageView) mapSelectView.get("im_distractor_square");
			selectVideoView= (VideoView) mapSelectView.get("webView");
			
			mapDistractorView1 = (HashMap<String, View>) imageViewIvList.get(1);
			distractorView1=(ImageView) mapDistractorView1.get("im_distractor");
			distractorView1_square=(ImageView) mapDistractorView1.get("im_distractor_square");
			distractorVideoView1= (VideoView) mapDistractorView1.get("webView");
			
			mapDistractorView2 = (HashMap<String, View>) imageViewIvList.get(2);
			distractorView2=(ImageView) mapDistractorView2.get("im_distractor");
			distractorView2_square=(ImageView) mapDistractorView2.get("im_distractor_square");
			distractorVideoView2= (VideoView) mapDistractorView2.get("webView");
			
			mapDistractorView3 = (HashMap<String, View>) imageViewIvList.get(3);
			distractorView3=(ImageView) mapDistractorView3.get("im_distractor");
			distractorView3_square=(ImageView) mapDistractorView3.get("im_distractor_square");
			distractorVideoView3= (VideoView) mapDistractorView3.get("webView");
		
			selectView.setEnabled(true);
			selectVideoView.setEnabled(true);
			distractorView1.setEnabled(true);
			distractorVideoView1.setEnabled(true);
			distractorView2.setEnabled(true);
			distractorVideoView2.setEnabled(true);
			distractorView3.setEnabled(true);
			distractorVideoView3.setEnabled(true);
			animate(selectView).setDuration(0);
			animate(selectView_square).setDuration(0);
			animate(selectVideoView).setDuration(0);
			
			animate(selectView).alpha(1);
			animate(selectView_square).alpha(1);
			animate(selectVideoView).alpha(1);
			
			animate(distractorView1).setDuration(0);
			animate(distractorView1_square).setDuration(0);
			animate(distractorVideoView1).setDuration(0);
			
			animate(distractorView2).setDuration(0);
			animate(distractorView2_square).setDuration(0);
			animate(distractorVideoView2).setDuration(0);
			
			
			animate(distractorView3).setDuration(0);
			animate(distractorView3_square).setDuration(0);
			animate(distractorVideoView3).setDuration(0);
			
			
			selectAssetId = map.get(AssetType.select);
			distractorAssetId1 = map.get(AssetType.distractor1);
			distractorAssetId2 = map.get(AssetType.distractor2);
			distractorAssetId3 = map.get(AssetType.distractor3);
			
			//selectView.setImageBitmap(getImageFromFile(getFileName(selectAssetId)));
			
			//distractorView1.setImageBitmap(getImageFromFile(getFileName(distractorAssetId1)));
			
			
			
			asset = getAsset(selectAssetId);
			if(asset.getExtension().equals("mp4")){
				selectVideoView.setId(selectAssetId);
				
				selectView.setImageResource(R.drawable.white);
				animate(selectView).alpha(0);
				
				selectVideoView.setVideoPath(getFileName(asset));
				selectVideoView.setVisibility(View.VISIBLE);
				animate(selectVideoView).alpha(1);
				selectVideoView.start();
			}else
			{
				selectView.setId(selectAssetId);
				selectVideoView.setVisibility(View.INVISIBLE);
				animate(selectView).alpha(1);
				selectView.setImageBitmap(getImageFromFile(getFileName(asset)));
			}
			asset = getAsset(distractorAssetId1);
			if(asset.getExtension().equals("mp4")){
				
				distractorVideoView1.setId(distractorAssetId1);
		
				distractorView1.setImageResource(R.drawable.white);
				animate(distractorView1).alpha(0);
				distractorVideoView1.setVideoPath(getFileName(asset));
				distractorVideoView1.setVisibility(View.VISIBLE);
				animate(distractorVideoView1).alpha(1);
				distractorVideoView1.start();
			}else
			{
				distractorView1.setId(distractorAssetId1);
				distractorVideoView1.setVisibility(View.INVISIBLE);
				animate(distractorView1).alpha(1);
				distractorView1.setImageBitmap(getImageFromFile(getFileName(asset)));
			}
			asset = getAsset(distractorAssetId2);
			if(asset.getExtension().equals("mp4")){
				
				distractorVideoView2.setId(distractorAssetId2);
				
				distractorView2.setImageResource(R.drawable.white);
				animate(distractorView2).alpha(0);
				distractorVideoView2.setVideoPath(getFileName(asset));
				distractorVideoView2.setVisibility(View.VISIBLE);
				animate(distractorVideoView2).alpha(1);
				distractorVideoView2.start();
			}else
			{
				distractorView2.setId(distractorAssetId2);
				distractorVideoView2.setVisibility(View.INVISIBLE);
				animate(distractorView2).alpha(1);
				distractorView2.setImageBitmap(getImageFromFile(getFileName(asset)));
			}
			asset = getAsset(distractorAssetId3);
			if(asset.getExtension().equals("mp4")){
				
				distractorVideoView3.setId(distractorAssetId3);
				
				distractorView3.setImageResource(R.drawable.white);
				animate(distractorView3).alpha(0);
				distractorVideoView3.setVideoPath(getFileName(asset));
				distractorVideoView3.setVisibility(View.VISIBLE);
				animate(distractorVideoView3).alpha(1);
				distractorVideoView3.start();
				
			}else
			{
				distractorView3.setId(distractorAssetId3);
				distractorVideoView3.setVisibility(View.INVISIBLE);
				animate(distractorView3).alpha(1);
				distractorView3.setImageBitmap(getImageFromFile(getFileName(asset)));
			}
			if(selectTestCount>2){
				if(selectCorrectPercent<=0.25&&selectTestCount>4){
					animate(distractorView1).alpha((float) 0);
					animate(distractorView1_square).alpha((float) 0);
					distractorView1.setEnabled(false);
					distractorView1_square.setEnabled(false);
					distractorVideoView1.setEnabled(false);
					animate(distractorView1).alpha((float) 0);
					animate(distractorVideoView1).alpha((float) 0);
					
					animate(distractorView2).alpha((float) 0);	
					animate(distractorView2_square).alpha((float) 0);
					distractorView2.setEnabled(false);
					distractorView2_square.setEnabled(false);
					distractorVideoView2.setEnabled(false);
					animate(distractorVideoView2).alpha((float) 0);
					
					animate(distractorView3).alpha((float) 0);
					animate(distractorView3_square).alpha((float) 0);
					distractorView3.setEnabled(false);
					distractorView3_square.setEnabled(false);
					distractorVideoView3.setEnabled(false);
					animate(distractorVideoView3).alpha((float) 0);
				}
				else if(selectCorrectPercent<=0.5){
					animate(distractorView1).alpha((float) 0.5);
					animate(distractorView1_square).alpha((float) 0.5);
					animate(distractorVideoView1).alpha((float) 1);
					animate(distractorView2).alpha((float) 0.5);
					animate(distractorView2_square).alpha((float) 0.5);
					animate(distractorVideoView2).alpha((float) 1);
					animate(distractorView3).alpha((float) 0.5);
					animate(distractorView3_square).alpha((float) 0.5);
					animate(distractorVideoView3).alpha((float) 1);
				}else {
					animate(distractorView1_square).alpha((float) 1);
					animate(distractorView2_square).alpha((float) 1);
					animate(distractorView3_square).alpha((float) 1);
				}
					
			}else{
				animate(distractorView1_square).alpha((float) 1);
				animate(distractorView2_square).alpha((float) 1);
				animate(distractorView3_square).alpha((float) 1);
			}
			
			break;
		}
		
        
		
	}
	private boolean isFlash(Asset asset){
		if(asset.getExtension().equals("png"))
			return true;
		return false;
	}

	private Asset getAsset(int assetId){
		String fileDirectory = String.valueOf(((int)assetId/100)*100);
		Asset asset = database.findById(assetId, Asset.class);
//		if(!asset.getExtension().equals("png")&&!asset.getExtension().equals("jpg"))
//		{
//			String ss = asset.getExtension() ;
//			System.out.println(ss);
//		}
		return asset;
	}
	private String getFileName(Asset asset){
		String fileDirectory = String.valueOf(((int)asset.getAssetId()/100)*100);
		
		String fileName=sdRootFolder+"/teachTown/Assets/"+fileDirectory+"/"+asset.getAssetId()+"."+asset.getExtension();;
		System.out.println("fileName:"+fileName);
		Log.i("fileName", fileName);
		
		return fileName;
	}
	
	private Bitmap getImageFromFile(String fileName)  
	  {  
		    
            Bitmap bitmap=BitmapFactory.decodeFile(fileName);
            return bitmap;
	
	  }
	private Map getRandomImage(List<Trial> trialList,Map trialHashMap){
		TrialAssetMap trialSelectAssetMap;//选中的资源
		TrialAssetMap trialMatchSelectAssetMap;//匹配选中的资源
		int distractorTrialId1=0,distractorTrialId2=0,distractorTrialId3=0;
		TrialAssetMap trialDistractorAssetMap1;//干扰资源1
		TrialAssetMap trialDistractorAssetMap2;//干扰资源2
		TrialAssetMap trialDistractorAssetMap3;//干扰资源3
		Collections.shuffle(trialList);//trialList重新随机排序
		//得到选中的trial，因为已经随机排序所以取第一个即为随机值
		Trial trial =trialList.get(0);
		

		//对测试结果正确率由低到高排序
		if(isPrompt){
			testResultPromptCorrectPercentsort(testResultPromptSyncMapSort);
			selectCorrectPercent = testResultPromptSyncMapSort.get(0).getValue().getPercentPrompted();
			if(selectCorrectPercent<0.5){
				trial = database.findById(selectTrialid,Trial.class);
				selectTrialid = testResultPromptSyncMapSort.get(0).getValue().getTrialId();
				distractorTrialId1 = testResultPromptSyncMapSort.get(1).getValue().getTrialId();
				
				if(testResultPromptSyncMapSort.size()>3){
					
					distractorTrialId2 = testResultPromptSyncMapSort.get(2).getValue().getTrialId();
					distractorTrialId3 = testResultPromptSyncMapSort.get(3).getValue().getTrialId();
				}
				else if(testResultPromptSyncMapSort.size()>2){
					distractorTrialId2 = testResultPromptSyncMapSort.get(2).getValue().getTrialId();
					distractorTrialId3 = distractorTrialId2;
				}
				
			}else{
				trial =trialList.get(0);
				selectTrialid=trial.getTrialId();
				distractorTrialId1 = trialList.get(1).getTrialId();
				if(trialList.size()>3){
					
					distractorTrialId2 = trialList.get(2).getTrialId();
					distractorTrialId3 = trialList.get(3).getTrialId();
				}
				else if(trialList.size()>2){
					distractorTrialId2 = trialList.get(2).getTrialId();
					distractorTrialId3 = distractorTrialId2;
				}
				
			}
			
			
			
//			for(Entry<Integer, TestResultSync> testresult:testResultPromptSyncMapSort){
//				Log.d("testResultPromptCorrectPercentsort", String.valueOf(testresult.getValue().getPercentPrompted()));
//			}
			selectTestCount = testResultSyncMap.get(selectTrialid).getPromptTestCount();	
			
		}else{
			testResultCorrectPercentsort(testResultSyncMapSort);
			selectTrialid = testResultSyncMapSort.get(0).getValue().getTrialId();
			trial = database.findById(selectTrialid,Trial.class);
			selectCorrectPercent = testResultSyncMapSort.get(0).getValue().getPercentCorrect();
			
			if(selectCorrectPercent<0.5){
				distractorTrialId1 = testResultSyncMapSort.get(1).getValue().getTrialId();
				
				if(testResultSyncMapSort.size()>3){
					
					distractorTrialId2 = testResultSyncMapSort.get(2).getValue().getTrialId();
					distractorTrialId3 = testResultSyncMapSort.get(3).getValue().getTrialId();
				}
				else if(testResultSyncMapSort.size()>2){
					distractorTrialId2 = testResultSyncMapSort.get(2).getValue().getTrialId();
					distractorTrialId3 = distractorTrialId2;
				}
			}else{
				trial =trialList.get(0);
				selectTrialid=trial.getTrialId();
				distractorTrialId1 = trialList.get(1).getTrialId();
				if(trialList.size()>3){
					
					distractorTrialId2 = trialList.get(2).getTrialId();
					distractorTrialId3 = trialList.get(3).getTrialId();
				}
				else if(trialList.size()>2){
					distractorTrialId2 = trialList.get(2).getTrialId();
					distractorTrialId3 = distractorTrialId2;
				}
				
			}
		
			
//			for(Entry<Integer, TestResultSync> testresult:testResultSyncMapSort){
//				Log.d("testResultSyncMapSort", String.valueOf(testresult.getValue().getPercentCorrect()));
//			}
			selectTestCount = testResultSyncMap.get(selectTrialid).getTestCount();	
			
		}
		
		HashMap<Integer,List<TrialAssetMap>> selectTriaAssetMap = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(selectTrialid);
		
		//得到选中trial中的mapType为7的mp3资源文件
		//List<TrialAssetMap> selectMp3TrialAssetMapList = selectTriaAssetMap.get(7);
		//mp3AssetId = selectMp3TrialAssetMapList.get(random.nextInt(selectMp3TrialAssetMapList.size())).getAssetId();
		
		tiralSpeech = trial.getChineseName();
		Log.d("tiralSpeech", tiralSpeech);
		if(trial.getSpeechFlag()==0){
			switch(speechType){
			case 1:
				IflytekSpeech.startSpeech( "哪个是"+tiralSpeech);
				break;
			case 2:
				IflytekSpeech.startSpeech( "有没有看到"+tiralSpeech);
				break;
			case 3:
				IflytekSpeech.startSpeech( "找找"+tiralSpeech+"在哪里");
				break;
			
			}
			if(speechType%3==0)
				speechType=0;
			speechType++;
		}
		else{
			IflytekSpeech.startSpeech(tiralSpeech);
		}
		
	
	
		
		//iflytekSpeech.startSpeech( "请找出"+tiralSpeech);
		int rand = random.nextInt(2)+3;//随机产生3,4
		//得到选中trial中的mapType为3或4的资源文件
		List<TrialAssetMap> selectTrialAssetMapList = selectTriaAssetMap.get(rand);
		//该trial中mapType为3或4的资源文件列表随机排序
		Collections.shuffle(selectTrialAssetMapList);
		//得到选中项资源，随机排序后得到的第一项即为随机值
	//	trialSelectAssetMap = getAssetMap(selectTrialAssetMapList);
		trialSelectAssetMap=selectTrialAssetMapList.get(0);
		
		
		//如果选中trial的rl_ShareDistractors属性为1，则干扰项从另外相同lessonhandle中选中，若为0，则从自己assetMap中选取mapType为5,6的作为干扰项
		int numDistractors = trial.getNumDistractors();
		Map assetMap=new HashMap<Integer,Integer>();;
		int selectAssetId,distractorAssetId1,distractorAssetId2,distractorAssetId3;
		switch(numDistractors){
		case 0:
			selectAssetId = trialSelectAssetMap.getAssetId();
			assetMap = new HashMap<Integer,Integer>();
			assetMap.put(AssetType.select, selectAssetId);
			break;
			
		case 1:
			if(trial.getRl_ShareDistractors()==1){
				
				//抽取除选中的trial另外选三个干扰项
				
				
				HashMap<Integer,List<TrialAssetMap>> distractortTriaAssetHashMap1 = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(distractorTrialId1);
				
				
				
				rand = random.nextInt(2)+3;//随机产生3,4
				List<TrialAssetMap> distractorTrialAssetList1 = distractortTriaAssetHashMap1.get(rand);
				Collections.shuffle(distractorTrialAssetList1);
				//trialDistractorAssetMap1 =getAssetMap(distractorTrialAssetList1); //得到随机干扰项资源
				trialDistractorAssetMap1=distractorTrialAssetList1.get(0);
				
			}
			else {
				int randDistra = random.nextInt(2)+5;//随机产生5,6
				List<TrialAssetMap> distractorTrialAssetList1 = selectTriaAssetMap.get(randDistra);
				Collections.shuffle(distractorTrialAssetList1);
				//trialDistractorAssetMap1 = getAssetMap(distractorTrialAssetList1);
				trialDistractorAssetMap1=distractorTrialAssetList1.get(0);	
			}
			selectAssetId = trialSelectAssetMap.getAssetId();
			
			distractorAssetId1 = trialDistractorAssetMap1.getAssetId();
			assetMap = new HashMap<Integer,Integer>();
			assetMap.put(AssetType.select, selectAssetId);
			assetMap.put(AssetType.distractor1, distractorAssetId1);
		break;
			
		case 2:
			if(trial.getRl_ShareDistractors()==1){
				
				//抽取除选中的trial另外选三个干扰项
				
				
				HashMap<Integer,List<TrialAssetMap>> distractortTriaAssetHashMap1 = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(distractorTrialId1);
				HashMap<Integer,List<TrialAssetMap>> distractortTriaAssetHashMap2 = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(distractorTrialId2);
				HashMap<Integer,List<TrialAssetMap>> distractortTriaAssetHashMap3 = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(distractorTrialId3);
				
				
				rand = random.nextInt(2)+3;//随机产生3,4
				List<TrialAssetMap> distractorTrialAssetList1 = distractortTriaAssetHashMap1.get(rand);
				Collections.shuffle(distractorTrialAssetList1);
				//trialDistractorAssetMap1 =getAssetMap(distractorTrialAssetList1); //得到随机干扰项资源
				trialDistractorAssetMap1=distractorTrialAssetList1.get(0);
				rand = random.nextInt(2)+3;//随机产生3,4
				List<TrialAssetMap> distractorTrialAssetList2 = distractortTriaAssetHashMap2.get(rand);
				Collections.shuffle(distractorTrialAssetList2);
				//trialDistractorAssetMap2 = getAssetMap(distractorTrialAssetList2);
				trialDistractorAssetMap2=distractorTrialAssetList2.get(0);
				
			}
			else {
				int randDistra = random.nextInt(2)+5;//随机产生5,6
				List<TrialAssetMap> distractorTrialAssetList1 = selectTriaAssetMap.get(randDistra);
				Collections.shuffle(distractorTrialAssetList1);
				//trialDistractorAssetMap1 = getAssetMap(distractorTrialAssetList1);
				trialDistractorAssetMap1=distractorTrialAssetList1.get(0);
				randDistra = random.nextInt(2)+5;//随机产生5,6
				List<TrialAssetMap> distractorTrialAssetList2 = selectTriaAssetMap.get(randDistra);
				Collections.shuffle(distractorTrialAssetList2);
				//trialDistractorAssetMap2 =  getAssetMap(distractorTrialAssetList2);
				trialDistractorAssetMap2=distractorTrialAssetList2.get(0);
				
			}
			selectAssetId = trialSelectAssetMap.getAssetId();
			
			distractorAssetId1 = trialDistractorAssetMap1.getAssetId();
			distractorAssetId2 = trialDistractorAssetMap2.getAssetId();
			assetMap.put(AssetType.select, selectAssetId);
			assetMap.put(AssetType.distractor1, distractorAssetId1);
			assetMap.put(AssetType.distractor2, distractorAssetId2);
			break;
		case 3:
			if(trial.getRl_ShareDistractors()==1){
				
				//抽取除选中的trial另外选三个干扰项
				
				
				HashMap<Integer,List<TrialAssetMap>> distractortTriaAssetHashMap1 = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(distractorTrialId1);
				HashMap<Integer,List<TrialAssetMap>> distractortTriaAssetHashMap2 = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(distractorTrialId2);
				HashMap<Integer,List<TrialAssetMap>> distractortTriaAssetHashMap3 = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(distractorTrialId3);
				
				
				rand = random.nextInt(2)+3;//随机产生3,4
				List<TrialAssetMap> distractorTrialAssetList1 = distractortTriaAssetHashMap1.get(rand);
				Collections.shuffle(distractorTrialAssetList1);
				//trialDistractorAssetMap1 =getAssetMap(distractorTrialAssetList1); //得到随机干扰项资源
				trialDistractorAssetMap1=distractorTrialAssetList1.get(0);
				rand = random.nextInt(2)+3;//随机产生3,4
				List<TrialAssetMap> distractorTrialAssetList2 = distractortTriaAssetHashMap2.get(rand);
				Collections.shuffle(distractorTrialAssetList2);
				//trialDistractorAssetMap2 = getAssetMap(distractorTrialAssetList2);
				trialDistractorAssetMap2=distractorTrialAssetList2.get(0);
				rand = random.nextInt(2)+3;//随机产生3,4
				List<TrialAssetMap> distractorTrialAssetList3 = distractortTriaAssetHashMap3.get(rand);
				Collections.shuffle(distractorTrialAssetList3);
				//trialDistractorAssetMap3 = getAssetMap(distractorTrialAssetList3);
				trialDistractorAssetMap3=distractorTrialAssetList3.get(0);
				
			}
			else {
				int randDistra = random.nextInt(2)+5;//随机产生5,6
				List<TrialAssetMap> distractorTrialAssetList1 = selectTriaAssetMap.get(randDistra);
				Collections.shuffle(distractorTrialAssetList1);
				//trialDistractorAssetMap1 = getAssetMap(distractorTrialAssetList1);
				trialDistractorAssetMap1=distractorTrialAssetList1.get(0);
				randDistra = random.nextInt(2)+5;//随机产生5,6
				List<TrialAssetMap> distractorTrialAssetList2 = selectTriaAssetMap.get(randDistra);
				Collections.shuffle(distractorTrialAssetList2);
				//trialDistractorAssetMap2 =  getAssetMap(distractorTrialAssetList2);
				trialDistractorAssetMap2=distractorTrialAssetList2.get(0);
				randDistra = random.nextInt(2)+5;//随机产生5,6
				List<TrialAssetMap> distractorTrialAssetList3 = selectTriaAssetMap.get(randDistra);
				Collections.shuffle(distractorTrialAssetList3);
				trialDistractorAssetMap3 = distractorTrialAssetList3.get(0);
				
			}
			
			selectAssetId = trialSelectAssetMap.getAssetId();
			 distractorAssetId1 = trialDistractorAssetMap1.getAssetId();
			 distractorAssetId2 = trialDistractorAssetMap2.getAssetId();
			distractorAssetId3 = trialDistractorAssetMap3.getAssetId();

		
			assetMap.put(AssetType.select, selectAssetId);
			assetMap.put(AssetType.distractor1, distractorAssetId1);
			assetMap.put(AssetType.distractor2, distractorAssetId2);
			assetMap.put(AssetType.distractor3, distractorAssetId3);
			break;
		
		}
		return assetMap;
		
	}
	public void buttonClick(View view){
		int clickId = view.getId();
		switch(clickId){
		case R.id.button_prompt_receptive_label0:
			if(!isPrompt){
				isPrompt=true;
				button_prompt_receptive_label0.setImageResource(R.drawable.prompt_open);
				
			}else{
				isPrompt=false;
				button_prompt_receptive_label0.setImageResource(R.drawable.prompt_close);
			}
			break;
		case R.id.button_prompt_receptive_label1:
			if(!isPrompt){
				isPrompt=true;
				button_prompt_receptive_label1.setImageResource(R.drawable.prompt_open);
			}else{
				isPrompt=false;
				button_prompt_receptive_label1.setImageResource(R.drawable.prompt_close);
			}
			break;
		case R.id.button_prompt_receptive_label2:
			if(!isPrompt){
				isPrompt=true;
				button_prompt_receptive_label2.setImageResource(R.drawable.prompt_open);
			}else{
				isPrompt=false;
				button_prompt_receptive_label2.setImageResource(R.drawable.prompt_close);
			}
			break;
		case R.id.button_prompt_receptive_label3:
			if(!isPrompt){
				isPrompt=true;
				button_prompt_receptive_label3.setImageResource(R.drawable.prompt_open);
			}else{
				isPrompt=false;
				button_prompt_receptive_label3.setImageResource(R.drawable.prompt_close);
			}
			break;
			
		}
	}
	public void setVideoView(VideoView videoView){
	
		videoView.setOnPreparedListener(this);
		videoView.setOnCompletionListener(this);
		videoView.setOnClickListener(this);
		videoView.setOnTouchListener(this);
		videoView.setOnErrorListener(this);
		
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		System.out.println("onClick");
		int clickId = view.getId();
		double promptTestCount = testResultSyncMap.get(selectTrialid).getPromptTestCount();	
		double promptCorrect = testResultSyncMap.get(selectTrialid).getPromptCorrectCount();
		double noPromptTestCount = testResultSyncMap.get(selectTrialid).getTestCount();
		double correct = testResultSyncMap.get(selectTrialid).getCorrectCount();
		if(isPrompt){
			promptTestCount++;
			testResultSyncMap.get(selectTrialid).setPromptTestCount(promptTestCount);
			promptAllTestCount++;
		}
		else{
			noPromptTestCount++;
			testResultSyncMap.get(selectTrialid).setTestCount(noPromptTestCount);
			noPromptTestAllCount++;
		}
		allTestCount = promptAllTestCount+noPromptTestAllCount;
		if(clickId == selectView.getId()||clickId == selectVideoView.getId()){
			//正确 
			//正确
			if(isPrompt){
				
				promptCorrect++;
				testResultSyncMap.get(selectTrialid).setPromptCorrectCount(promptCorrect);
				testResultSyncMap.get(selectTrialid).setPromptTestCount(promptTestCount);
				if(promptTestCount!=0)
					testResultSyncMap.get(selectTrialid).setPercentPrompted(promptCorrect/promptTestCount);
			}
			else{
			
				correct++;
				testResultSyncMap.get(selectTrialid).setCorrectCount(correct);
				testResultSyncMap.get(selectTrialid).setTestCount(noPromptTestCount);
				if(noPromptTestCount!=0)
					testResultSyncMap.get(selectTrialid).setPercentCorrect(correct/noPromptTestCount);
			}
			
			
			if(mediaPlayerWin != null)  
		     {  
				mediaPlayerWin.stop();  
		     }      
			try {
				mediaPlayerWin.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			mediaPlayerWin.start();  
			switch(animationIndex)
			{
			case 0:
				AnimationTrial.animatedParticle(emiter, TrialReceptiveLabeActivity.this,animationFadeTime);
				break;
			case 1:
				AnimationTrial.fireWorks(emiter, TrialReceptiveLabeActivity.this,animationFadeTime);
				break;
			case 2:
				AnimationTrial.OneShot(emiter, TrialReceptiveLabeActivity.this,animationFadeTime);
				break;
			case 3:
				AnimationTrial.stars(emiter, TrialReceptiveLabeActivity.this,animationFadeTime);
				break;
			}
			
		}
		else
		{
			//错误
			if(promptTestCount!=0)
				testResultSyncMap.get(selectTrialid).setPercentPrompted(promptCorrect/promptTestCount);
			if(noPromptTestCount!=0)
				testResultSyncMap.get(selectTrialid).setPercentCorrect(correct/noPromptTestCount);
			
			
			
			
			if(mediaPlayerLose != null)  
		     {  
				mediaPlayerLose.stop();  
		     }      
			try {
				mediaPlayerLose.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			mediaPlayerLose.start(); 
			
		}
		
		
		
		switch(imageViewList.size()){
		case 1:
			//ObjectAnimator.ofFloat(selectView, "rotationY", 0, 180, 0).setDuration(duringTime).start();
			selectView.setEnabled(false);
			selectVideoView.setEnabled(false);
			break;
		case 2:
			//ObjectAnimator.ofFloat(selectView, "rotationY", 0, 180, 0).setDuration(duringTime).start();
			animate(distractorView1).setDuration(fadeTime);
			animate(distractorView1_square).setDuration(fadeTime);
			animate(distractorVideoView1).setDuration(fadeTime);
			animate(distractorView1).alpha(0);
			animate(distractorView1_square).alpha(0);
			animate(distractorVideoView1).alpha(0);
			selectView.setEnabled(false);
			selectVideoView.setEnabled(false);
			distractorView1.setEnabled(false);
			distractorVideoView1.setEnabled(false);
			break;
		case 3:
			//ObjectAnimator.ofFloat(selectView, "rotationY", 0, 180, 0).setDuration(duringTime).start();
			animate(distractorView1).setDuration(fadeTime);
			animate(distractorView1_square).setDuration(fadeTime);
			animate(distractorVideoView1).setDuration(fadeTime);
			animate(distractorView2).setDuration(fadeTime);
			animate(distractorView2_square).setDuration(fadeTime);
			animate(distractorVideoView2).setDuration(fadeTime);
			animate(distractorView1).alpha(0);
			animate(distractorView1_square).alpha(0);
			animate(distractorVideoView1).alpha(0);
			animate(distractorView2).alpha(0);
			animate(distractorView2_square).alpha(0);
			animate(distractorVideoView2).alpha(0);
			
			selectView.setEnabled(false);
			selectVideoView.setEnabled(false);
			distractorView1.setEnabled(false);
			distractorVideoView1.setEnabled(false);
			distractorView2.setEnabled(false);
			distractorVideoView2.setEnabled(false);
			break;
		case 4:
			//ObjectAnimator.ofFloat(selectView, "rotationY", 0, 180, 0).setDuration(duringTime).start();
			animate(distractorView1).setDuration(fadeTime);
			animate(distractorView1_square).setDuration(fadeTime);
			animate(distractorVideoView1).setDuration(fadeTime);
			animate(distractorView2).setDuration(fadeTime);
			animate(distractorView2_square).setDuration(fadeTime);
			animate(distractorVideoView2).setDuration(fadeTime);
			animate(distractorView3).setDuration(fadeTime);
			animate(distractorView3_square).setDuration(fadeTime);
			animate(distractorVideoView3).setDuration(fadeTime);
			animate(distractorView1).alpha(0);
			animate(distractorView1_square).alpha(0);
			animate(distractorVideoView1).alpha(0);
			animate(distractorView2).alpha(0);
			animate(distractorView2_square).alpha(0);
			animate(distractorView2).alpha(0);
			animate(distractorView3).alpha(0);
			animate(distractorView3_square).alpha(0);
			animate(distractorVideoView3).alpha(0);
			selectView.setEnabled(false);
			selectVideoView.setEnabled(false);
			distractorVideoView1.setEnabled(false);
			distractorVideoView2.setEnabled(false);
			distractorVideoView3.setEnabled(false);
			break;
		}
		
		if(++animationIndex==4)
			animationIndex=0;
		
		
		
		
		if(allTestCount<allCount)
			allTestCount++;
		else
		{
			DialogUtil.showGameDialog(TrialReceptiveLabeActivity.this);
		}
		
		timer.schedule(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					new UpdateImageViewTask(trialList, trialHashmap,  imageViewList).execute("");
				}
			}, waiteTime);
	}
	class UpdateImageViewTask extends AsyncTask<String , Integer, Map>{
		List<Trial> trialList;
		Map trialHashMap;
		List<Map<String ,View>> imageIvList;
		public UpdateImageViewTask(List<Trial> trialList, Map trialHashMap,
				List<Map<String ,View>> imageIvList) {
			super();
			this.trialList = trialList;
			this.trialHashMap = trialHashMap;
			this.imageIvList = imageIvList;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}
		@Override
		protected Map doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			return getRandomImage(trialList, trialHashMap);
		}

		@Override
		protected void onPostExecute(Map map) {
			// TODO Auto-generated method stub
			super.onPostExecute(map);
			updateImageView(imageIvList,(HashMap<Integer, Integer>) map);
		}
		
	}
	//对正确率排序

	private static void testResultCorrectPercentsort(List<Map.Entry<Integer, TestResultSync>>data) {
		 
        Collections.sort(data,  new Comparator<Map.Entry<Integer, TestResultSync>>() {  
            public int compare(Map.Entry<Integer, TestResultSync> o1,  
            		Map.Entry<Integer, TestResultSync> o2) {
            	BigDecimal data1 = new BigDecimal(o1.getValue().getPercentCorrect()); 
            	BigDecimal data2 = new BigDecimal(o2.getValue().getPercentCorrect()); 
                return data1.compareTo(data2);  
            }  
        });
    }
	//对提示状态下的正确率排序
	private static void testResultPromptCorrectPercentsort(List<Map.Entry<Integer, TestResultSync>>data) {
		 
        Collections.sort(data,  new Comparator<Map.Entry<Integer, TestResultSync>>() {  
            public int compare(Map.Entry<Integer, TestResultSync> o1,  
            		Map.Entry<Integer, TestResultSync> o2) {
            	BigDecimal data1 = new BigDecimal(o1.getValue().getPercentPrompted()); 
            	BigDecimal data2 = new BigDecimal(o2.getValue().getPercentPrompted()); 
                return data1.compareTo(data2);  
            }  
        });
    }
	
	public void syncTestResult(TestResultSync testResult,String action){
		String url="";
		if(action.equals("add"))
			url= "http://101.200.177.122:8080/childProject/SaveCourseInfo";
		else
			url= "http://101.200.177.122:8080/childProject/UpdateCourseInfo";
		 Uri.Builder builder = Uri.parse(url).buildUpon();
		
		 builder.appendQueryParameter("sessionGuid", testResult.getSessionGuid());
		 builder.appendQueryParameter("dateTaken", testResult.getDateTaken());
	//	 builder.appendQueryParameter("testResultGuid",testResult.getTestResultGuid());
		 builder.appendQueryParameter("lessonHandle", String.valueOf(testResult.getLessonHandle()));
		 builder.appendQueryParameter("trialId", String.valueOf(testResult.getTrialId()));
		 builder.appendQueryParameter("promptCorrectCount", String.valueOf(testResult.getPromptCorrectCount()));
		 builder.appendQueryParameter("percentCorrect", String.valueOf(testResult.getPercentCorrect()));
		 builder.appendQueryParameter("percentPrompted", String.valueOf(testResult.getPercentPrompted()));
		 builder.appendQueryParameter("percentTimeout", String.valueOf(testResult.getPercentTimeout()));
		 builder.appendQueryParameter("finalPromptLevel", String.valueOf(testResult.getFinalPromptLevel()));
		 builder.appendQueryParameter("promptTestCount", String.valueOf(testResult.getPromptTestCount()));
		 builder.appendQueryParameter("studentId", String.valueOf(testResult.getStudentId()));
		 builder.appendQueryParameter("exerciseName", testResult.getExerciseName());
		 builder.appendQueryParameter("correctCount", String.valueOf(testResult.getCorrectCount()));
		 builder.appendQueryParameter("testCount", String.valueOf(testResult.getTestCount()));
		 builder.appendQueryParameter("lessonName", testResult.getLessonName());
		 
		
			 jsonObjRequest = new JsonObjectRequest(Request.Method.GET, builder.toString(), null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							//成功
							//parseFlickrImageResponse(response);
							//mAdapter.notifyDataSetChanged();
							System.out.println("上传成绩成功");
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// For ServerError 5xx, you can do retry or handle accordingly.
						if( error instanceof NetworkError) {
						} else if( error instanceof ClientError) { 
						} else if( error instanceof ServerError) {
						} else if( error instanceof AuthFailureError) {
						} else if( error instanceof ParseError) {
						} else if( error instanceof NoConnectionError) {
						} else if( error instanceof TimeoutError) {
						}
					}
				});
		 
		 
		//Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
			jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			jsonObjRequest.setTag(TAG_REQUEST);	
			
			mVolleyQueue.add(jsonObjRequest);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		List<TestResultSync> studentResultList = database.findAllByWhere(TestResultSync.class, "studentId="+TeachTownApp.getStudentId()+" and lessonHandle="+lessonHandle);
		Iterator iter = testResultSyncMap.entrySet().iterator();
		double correctPercent=0;
		double correctPromptPercent=0;
		if(studentResultList.size()>0){
			
			while (iter.hasNext()) {
				Entry  obj = (Entry)iter.next();
				TestResultSync studentResult = (TestResultSync) obj.getValue();
				if(studentResult.getTestCount()!=0)
					correctPercent = studentResult.getCorrectCount()/studentResult.getTestCount();
				else
					correctPercent=0;
				if(studentResult.getPromptTestCount()!=0)
					correctPromptPercent = studentResult.getPromptCorrectCount()/studentResult.getPromptTestCount();
				else
					correctPromptPercent=0;
				studentResult.setPercentCorrect(correctPercent);
				studentResult.setPercentPrompted(correctPromptPercent);
				studentResult.setStudentId(TeachTownApp.getStudentId());
				studentResult.setLessonName(lessonName);
				Date date=new Date();
				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				studentResult.setDateTaken(df.format(date));
				System.out.println("database.update(studentResult)");
				System.out.println("TeachTownApp.getStudentId()"+TeachTownApp.getStudentId());
				database.update(studentResult, "trialId="+studentResult.getTrialId()+" and studentId=" + TeachTownApp.getStudentId());
				syncTestResult(studentResult,"update");
			}
		}else{
			while (iter.hasNext()) {
				Entry  obj = (Entry)iter.next();
				TestResultSync studentResult = (TestResultSync) obj.getValue();
				if(studentResult.getTestCount()!=0)
					correctPercent = studentResult.getCorrectCount()/studentResult.getTestCount();
				else
					correctPercent=0;
				if(studentResult.getPromptTestCount()!=0)
					correctPromptPercent = studentResult.getPromptCorrectCount()/studentResult.getPromptTestCount();
				else
					correctPromptPercent=0;
				studentResult.setPercentCorrect(correctPercent);
				studentResult.setPercentPrompted(correctPromptPercent);
				studentResult.setStudentId(TeachTownApp.getStudentId());
				studentResult.setLessonName(lessonName);
				Date date=new Date();
				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				studentResult.setDateTaken(df.format(date));
				System.out.println("database.save(studentResult)");
				System.out.println("TeachTownApp.getStudentId()"+TeachTownApp.getStudentId());
				database.save(studentResult);
				
				syncTestResult(studentResult,"add");
			}
		}	
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		IflytekSpeech.stopSpeech();
		// mediaPlayer.release();
	    // mediaPlayer = null;
		 if (mediaPlayerWin != null && mediaPlayerWin.isPlaying()) {
			
			 mediaPlayerWin.stop();
			
			 mediaPlayerWin.release();
			
			 mediaPlayerWin = null;
			
		       }
		 if (mediaPlayerLose != null && mediaPlayerLose.isPlaying()) {
				
			 mediaPlayerLose.stop();
			
			 mediaPlayerLose.release();
			
			 mediaPlayerLose = null;
			
		       }
		 if(selectVideoView!=null){
			 selectVideoView.stopPlayback();
			 selectVideoView=null;
		 }
		 if(distractorVideoView1!=null){
			 distractorVideoView1.stopPlayback();
			 distractorVideoView1=null;
		 }
		 if(distractorVideoView2!=null){
			 distractorVideoView2.stopPlayback();
			 distractorVideoView2=null;
		 }
		 if(distractorVideoView3!=null){
			 distractorVideoView3.stopPlayback();
			 distractorVideoView3=null;
		 }
		 
		 
		 
		if(viewBack0!=null)
			viewBack0.setBackgroundResource(0);
		if(viewBack1!=null)
			viewBack1.setBackgroundResource(0);
		if(viewBack2!=null)
			viewBack2.setBackgroundResource(0);
		if(viewBack3!=null)
			viewBack3.setBackgroundResource(0);
		
		System.out.println("释放内存：System.gc();");
		System.gc();
			
	}
	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		

         mp.setLooping(true);  
		
	}
	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.start();
	}
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		onClick(arg0);
		return true;
	}
	
	@Override
	public boolean onError(MediaPlayer videoView, int arg1, int arg2) {
		// TODO Auto-generated method stub
		final MediaPlayer video=videoView;
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
			// TODO Auto-generated method stub
				video.start();
			}
			}, 0);

		return true;
	}

}
