package com.teachtown.activity;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

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
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.teachtown.component.AnimationTrial;
import com.teachtown.component.AssetType;
import com.teachtown.component.DialogUtil;
import com.teachtown.component.IflytekSpeech;
import com.teachtown.model.Asset;
import com.teachtown.model.Student;
import com.teachtown.model.TestResultSync;
import com.teachtown.model.Trial;
import com.teachtown.model.TrialAssetMap;
import com.teachtown.utils.DatabaseUtil;
import com.teachtown.utils.Md5Utils;
import com.teachtown.utils.UnityUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

public class TrialMatchingActivity extends FinalActivity {
	
	//private IflytekSpeech iflytekSpeech;
	public static TrialMatchingActivity instance=null;
	private FinalDb database;
	private int lessonHandle;
	private List<Trial> trialList;
	private HashMap<Integer, HashMap<Integer,List<TrialAssetMap>>> trialHashmap;
	private ArrayList<List<TrialAssetMap>> allTrialAssetList;
	private HashMap<Integer,TestResultSync> testResultSyncMap;
	private List<Map.Entry<Integer, TestResultSync>> testResultSyncMapSort,testResultPromptSyncMapSort;
	private List<Map<String,ImageView>> imageViewList;
	private String sdRootFolder ="/storage/sdcard0" ;
	private int duringTime = 3000;//测试修改
	//private int duringTime = 1000;
	private int animationFadeTime = 3000;//测试修改
	//private int animationFadeTime = 1000;
	private int fadeTime = 1000;
	private int waiteTime =4000;//测试修改
//	private int waiteTime =2000;
	private  int MapTypeSize = 8;
	private String lessonName;
	private int matchId;
	private int selectTrialid;
	private int animationIndex=0;
	private int speechType=1;//读音类型
	private ImageView matchView,matchView_quare,distractorView1,distractorView1_square,distractorView2,distractorView2_suqare ;
	private UpdateImageViewTask updateImageViewTask;
	private Timer timer = new Timer();
	private MediaPlayer mediaPlayerWin;
	private MediaPlayer mediaPlayerLose;
	private int allCount = 30;
	private double noPromptTestAllCount=0,correctCount=0,wrongCount=0,promptAllTestCount=0,allTestCount,selectTestCount;
	private int mp3AssetId;
	private String tiralSpeech;
	private Random random;
	private View emiter,emiter_top_left,emiter_top_right;
	private int rand;
	private double selectCorrectPercent=0,selectPromptCorrectPercent=0;
	private RequestQueue mVolleyQueue;
	private JsonObjectRequest jsonObjRequest;
	private final String TAG_REQUEST = "MY_TAG";
	private FinalDb dataBase;
	private boolean isPrompt=false;
	RelativeLayout rl_gameinterface_bottom;
	@ViewInject(id = R.id.chooseborder_iv) ImageView iv_selected;
	@ViewInject(id = R.id.chooseborder2_1_iv, click = "imageViewlick") ImageView im_distractor1;
	@ViewInject(id = R.id.chooseborder2_2_iv, click = "imageViewlick") ImageView im_distractor2;
	@ViewInject(id = R.id.chooseborder2_3_iv, click = "imageViewlick") ImageView im_distractor3;
	@ViewInject(id = R.id.chooseborder2_1_iv_square) ImageView im_distractor1_square;
	@ViewInject(id = R.id.chooseborder2_2_iv_square) ImageView im_distractor2_square;
	@ViewInject(id = R.id.chooseborder2_3_iv_square) ImageView im_distractor3_square;
	@ViewInject(id = R.id.teacher_standby_iv) ImageView im_teacher_standby_iv;
	@ViewInject(id = R.id.button_prompt, click = "buttonClick") ImageView button_prompt;
	//private Map<Integer, List<TrialAssetMap>> assetMap;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trial_matching_activity);
		instance=this;
		IflytekSpeech.init(TrialMatchingActivity.this);
		rl_gameinterface_bottom = (RelativeLayout) findViewById(R.id.rl_gameinterface_bottom);
		dataBase =  DatabaseUtil.getDatabase(this);
		mVolleyQueue = Volley.newRequestQueue(this);
//		BitmapFactory.Options opts = new BitmapFactory.Options();
//		opts.inJustDecodeBounds = true;
//		try {
//			Bitmap gameinterface_bottom=BitmapFactory.decodeResource(getResources(), R.drawable.gameinterface_bottom, opts);
//			rl_gameinterface_bottom.setBackground(new BitmapDrawable(getResources(), gameinterface_bottom));
//			} catch (OutOfMemoryError err) {
//				
//			}
		emiter = findViewById(R.id.emiter_center);
		emiter_top_left = findViewById(R.id.emiter_top_left);
		mediaPlayerWin = MediaPlayer.create(this,R.raw.win);  
		mediaPlayerLose = MediaPlayer.create(this,R.raw.lose); 
		emiter_top_right = findViewById(R.id.emiter_top_right);
		trialHashmap = new HashMap<Integer, HashMap<Integer,List<TrialAssetMap>>>();
		testResultSyncMap = new HashMap<Integer, TestResultSync>();
		 //iflytekSpeech = new IflytekSpeech(TrialMatchingActivity.this);
		random = new Random();
		//初始化allTrialAssetList 
		allTrialAssetList = new ArrayList<List<TrialAssetMap>>();
		for(int i=0;i<=MapTypeSize;i++){   
			List<TrialAssetMap> trialAssetMapList = new ArrayList<TrialAssetMap>();
			allTrialAssetList.add(trialAssetMapList);   
	    }   
		Bundle bundle = this.getIntent().getExtras();
		lessonHandle = bundle.getInt("lessonHandle");
		lessonName= bundle.getString("lessonName");
		
		database = DatabaseUtil.getDatabase(TrialMatchingActivity.this);
		//对TrialAssetMap按照mapType进行分类
		trialList = database.findAllByWhere(Trial.class, "lessonHandle="+lessonHandle, "trialId");
		constructorTrialMap(trialList,trialHashmap);
		imageViewList = new ArrayList<Map<String,ImageView>>();
		Map map = new HashMap<String, ImageView>();
		map.put("im_distractor", im_distractor1);
		map.put("im_distractor_square", im_distractor1_square);
		imageViewList.add(map);
		map = new HashMap<String, ImageView>();
		map.put("im_distractor", im_distractor2);
		map.put("im_distractor_square", im_distractor2_square);
		imageViewList.add(map);
		map = new HashMap<String, ImageView>();
		map.put("im_distractor", im_distractor3);
		map.put("im_distractor_square", im_distractor3_square);
		imageViewList.add(map);
		
		new UpdateImageViewTask(trialList, trialHashmap, iv_selected, imageViewList).execute("");
	
		//setImageView(trialList,trialHashmap,iv_selected,imageViewList);
		
	}
	public void buttonClick(View view){
		
		if(!isPrompt){
			isPrompt=true;
			button_prompt.setImageResource(R.drawable.prompt_open);
		}else{
			isPrompt=false;
			button_prompt.setImageResource(R.drawable.prompt_close);
		}
	}
	public void imageViewlick(View view){

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
		if(clickId == matchId){
			//正确
			if(isPrompt){
				
				promptCorrect++;
				testResultSyncMap.get(selectTrialid).setPromptCorrectCount(promptCorrect);
				
				if(promptTestCount!=0)
					testResultSyncMap.get(selectTrialid).setPercentPrompted(promptCorrect/promptTestCount);
				
			}
			else{
				
				correct++;
				testResultSyncMap.get(selectTrialid).setCorrectCount(correct);
				
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
				AnimationTrial.animatedParticle(emiter, TrialMatchingActivity.this,animationFadeTime);
				break;
			
			case 1:
				AnimationTrial.fireWorks(emiter, TrialMatchingActivity.this,animationFadeTime);
				break;
			case 2:
				AnimationTrial.OneShot(emiter, TrialMatchingActivity.this,animationFadeTime);
				break;
			case 3:
				AnimationTrial.stars(emiter, TrialMatchingActivity.this,animationFadeTime);
				break;
			}
		}
		else
		{
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
		//bjectAnimator.ofFloat(matchView, "rotationY", 0, 180, 0).setDuration(duringTime).start();
		
			animate(distractorView1).setDuration(fadeTime);
			animate(distractorView1_square).setDuration(fadeTime);
			animate(distractorView2).setDuration(fadeTime);
			animate(distractorView2_suqare).setDuration(fadeTime);
			animate(distractorView1).alpha((float) 0);
			animate(distractorView1_square).alpha((float) 0);
			animate(distractorView2).alpha((float) 0);	
			animate(distractorView2_suqare).alpha((float) 0);		
	
		
		
		if(++animationIndex==4)
			animationIndex=0;
		
		im_distractor1.setEnabled(false);
		
		im_distractor2.setEnabled(false);
		im_distractor3.setEnabled(false);
		if(allTestCount<allCount)
			allTestCount++;
		else
		{
			
			
			DialogUtil.showGameDialog(TrialMatchingActivity.this);
		}
		timer.schedule(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					new UpdateImageViewTask(trialList, trialHashmap, iv_selected, imageViewList).execute("");
				}
			}, waiteTime);

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
	private String getFileName(int assetId){
		
		String fileDirectory = String.valueOf(((int)assetId/100)*100);
		Asset asset = database.findById(assetId, Asset.class);
		String fileName=sdRootFolder+"/teachTown/Assets/"+fileDirectory+"/"+assetId+"."+asset.getExtension();
		System.out.println("fileName:"+fileName);
		
		return fileName;
	}
	private Bitmap getImageFromFile(String fileName)  
	  {  
		//BitmapFactory.Options options = new BitmapFactory.Options();   
		//options.inSampleSize = 4;
		Bitmap bitmap=BitmapFactory.decodeFile(fileName);
        return bitmap;
	
	  }

	public void updateImageView(ImageView seletIv,List<Map<String,ImageView>> distractorIvList,HashMap<Integer, Integer> map){
		
		Collections.shuffle(distractorIvList);//随机排序ImageView，使每次匹配项出现位置不同
		Map mapMatchView = distractorIvList.get(0);
		matchView = (ImageView) mapMatchView.get("im_distractor");
		matchView_quare=(ImageView) mapMatchView.get("im_distractor_square");
		
		Map mapDistractorView1 = distractorIvList.get(1);
		distractorView1=(ImageView) mapDistractorView1.get("im_distractor");
		distractorView1_square=(ImageView) mapDistractorView1.get("im_distractor_square");
		
		Map mapDistractorView2 = distractorIvList.get(2);
		distractorView2 = (ImageView) mapDistractorView2.get("im_distractor");
		distractorView2_suqare = (ImageView) mapDistractorView2.get("im_distractor_square");
		
		matchView.setEnabled(true);
		distractorView1.setEnabled(true);
		distractorView2.setEnabled(true);
//		animate(matchView).setDuration(0);
//		animate(distractorView1).setDuration(0);
//		animate(distractorView2).setDuration(0);

		
		animate(distractorView1).setDuration(0);
		animate(distractorView1_square).setDuration(0);
		animate(distractorView2).setDuration(0);
		animate(distractorView2_suqare).setDuration(0);
		if(selectTestCount>2){
			if(selectCorrectPercent<=0.25&&selectTestCount>4){
				animate(distractorView1).alpha((float) 0);
				animate(distractorView1_square).alpha((float) 0);
				distractorView1.setEnabled(false);
				distractorView1_square.setEnabled(false);
				animate(distractorView2).alpha((float) 0);	
				animate(distractorView2_suqare).alpha((float) 0);
				distractorView2.setEnabled(false);
				distractorView2_suqare.setEnabled(false);
			}
			else if(selectCorrectPercent<=0.5){
				animate(distractorView1).alpha((float) 0.5);
				animate(distractorView1_square).alpha((float) 0.5);
				animate(distractorView2).alpha((float) 0.5);
				animate(distractorView2_suqare).alpha((float) 0.5);
			}else{
				animate(distractorView1).alpha((float) 1);
				animate(distractorView1_square).alpha((float) 1);
				
				animate(distractorView2).alpha((float) 1);		
				animate(distractorView2_suqare).alpha((float) 1);	
			}
				
		}else{
			animate(distractorView1).alpha((float) 1);
			animate(distractorView1_square).alpha((float) 1);
			animate(distractorView2).alpha((float) 1);		
			animate(distractorView2_suqare).alpha((float) 1);	
		}
		animate(matchView).setDuration(0);
		animate(matchView_quare).setDuration(0);
		animate(matchView).alpha((float) 1);
		animate(matchView_quare).alpha((float) 1);
		int selectAssetId = map.get(AssetType.select);
		int matchAssetId = map.get(AssetType.match);
		int distractorAssetId1 = map.get(AssetType.distractor1);
		int distractorAssetId2 = map.get(AssetType.distractor2);
		
		
		seletIv.setImageBitmap(getImageFromFile(getFileName(selectAssetId)));
		
		matchView.setId(matchAssetId);
		matchView.setImageBitmap(getImageFromFile(getFileName(matchAssetId)));
		matchId = matchAssetId;
		distractorView1.setId(distractorAssetId1);
		distractorView1.setImageBitmap(getImageFromFile(getFileName(distractorAssetId1)));
		
		distractorView2.setId(distractorAssetId2);
		distractorView2.setImageBitmap(getImageFromFile(getFileName(distractorAssetId2)));
		
	}
	
	private Map getRandomImage(List<Trial> trialList,Map trialHashMap){
		TrialAssetMap trialSelectAssetMap;//选中的资源
		TrialAssetMap trialMatchSelectAssetMap;//匹配选中的资源
		TrialAssetMap trialDistractorAssetMap1;//干扰资源1
		TrialAssetMap trialDistractorAssetMap2;//干扰资源2
		Collections.shuffle(trialList);//trialList重新随机排序
		//得到选中的trial，因为已经随机排序所以取第一个即为随机值
		Trial trial =trialList.get(0);
		int distractorTriaId1;
		int distractorTriaId2;
			//对测试结果正确率由低到高排序
				if(isPrompt){
					testResultPromptCorrectPercentsort(testResultPromptSyncMapSort);
					
					selectTrialid = testResultPromptSyncMapSort.get(0).getValue().getTrialId();
					selectCorrectPercent= testResultPromptSyncMapSort.get(0).getValue().getPercentPrompted();
					
					if(selectCorrectPercent<0.5){
						distractorTriaId1 = testResultPromptSyncMapSort.get(1).getValue().getTrialId();
						if(testResultPromptSyncMapSort.size()>2){
							
							distractorTriaId2 = testResultPromptSyncMapSort.get(2).getValue().getTrialId();
						}
						else{
							distractorTriaId2 = distractorTriaId1;
						}
						
						trial = database.findById(selectTrialid,Trial.class);
						
					}
					else{
						trial =trialList.get(0);
						selectTrialid=trial.getTrialId();
						
						distractorTriaId1 = trialList.get(1).getTrialId();
						if(trialList.size()>2){
							
							distractorTriaId2 = trialList.get(2).getTrialId();
						}
						else{
							distractorTriaId2 = distractorTriaId1;
						}
						
					}
					selectTestCount = testResultSyncMap.get(selectTrialid).getPromptTestCount();	
//					for(Entry<Integer, TestResultSync> testresult:testResultPromptSyncMapSort){
//						Log.d("testResultPromptCorrectPercentsort", String.valueOf(testresult.getValue().getPercentPrompted()));
//					}
					
				}else{
					testResultCorrectPercentsort(testResultSyncMapSort);
					selectTrialid = testResultSyncMapSort.get(0).getValue().getTrialId();
					selectCorrectPercent= testResultSyncMapSort.get(0).getValue().getPercentCorrect();
					if(selectCorrectPercent<0.5){
						trial = database.findById(selectTrialid,Trial.class);
						distractorTriaId1 = testResultSyncMapSort.get(1).getValue().getTrialId();
						if(testResultSyncMapSort.size()>2){
							
							distractorTriaId2 = testResultSyncMapSort.get(2).getValue().getTrialId();
						}
						else{
							distractorTriaId2 = distractorTriaId1;
						}
						
						
					}else{
						trial =trialList.get(0);
						selectTrialid=trial.getTrialId();
						
						distractorTriaId1 = trialList.get(1).getTrialId();
						if(trialList.size()>2){
							
							distractorTriaId2 = trialList.get(2).getTrialId();
						}
						else{
							distractorTriaId2 = distractorTriaId1;
						}
						
					}
					
//					for(Entry<Integer, TestResultSync> testresult:testResultSyncMapSort){
//						Log.d("testResultSyncMapSort", String.valueOf(testresult.getValue().getPercentCorrect()));
//					}
					
					selectTestCount = testResultSyncMap.get(selectTrialid).getTestCount();
				}
				
		
		HashMap<Integer,List<TrialAssetMap>> selectTriaAssetMap = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(selectTrialid);
		int rand = (int)(Math.random()*2)+1;//随机产生1,2
		//得到选中trial中的mapType为1或2的资源文件
		List<TrialAssetMap> selectTrialAssetMapList = selectTriaAssetMap.get(rand);
		//该trial中mapType为1或2的资源文件列表随机排序
		Collections.shuffle(selectTrialAssetMapList);
		//得到选中项资源，随机排序后得到的第一项即为随机值
		trialSelectAssetMap = selectTrialAssetMapList.get(0);
		
		//得到选中trial中的mapType为7的mp3资源文件
		//List<TrialAssetMap> selectMp3TrialAssetMapList = selectTriaAssetMap.get(7);
		//mp3AssetId = selectMp3TrialAssetMapList.get(random.nextInt(selectMp3TrialAssetMapList.size())).getAssetId();
		tiralSpeech = trial.getChineseName();
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
		
		//判断该lesson中有几个trial，如果trial数目为三个以上，则干扰项分别除选中的以外另项中抽两个作为干扰项
		
			//抽取除选中的trial另外选两个干扰项
			HashMap<Integer,List<TrialAssetMap>> distractortTriaAssetHashMap1 = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(distractorTriaId1);
			HashMap<Integer,List<TrialAssetMap>> distractortTriaAssetHashMap2 = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(distractorTriaId2);
			//选择mapType为1,2的资源作为干扰项
			List<TrialAssetMap> distractorTrialAssetList1 = distractortTriaAssetHashMap1.get(rand);
			List<TrialAssetMap> distractorTrialAssetList2 = distractortTriaAssetHashMap2.get(rand);
			//随机排序map类型为1,2的资源，为下一次随机做准备
			Collections.shuffle(distractorTrialAssetList1);
			Collections.shuffle(distractorTrialAssetList2);
			//得到随机干扰项资源
			trialDistractorAssetMap1 = distractorTrialAssetList1.get(0);
			trialDistractorAssetMap2 = distractorTrialAssetList2.get(0);
	

		int selectAssetId = trialSelectAssetMap.getAssetId();
		int matchAssetId;
		int distractorAssetId1 = trialDistractorAssetMap1.getAssetId();
		int distractorAssetId2 = trialDistractorAssetMap2.getAssetId();
		int rand1 = (int)(Math.random()*2)+3;//得到随机值3,4
		//如果为1，代表精准匹配，即选中项和匹配项资源文件相同
		if(trialList.get(0).getMa_ExactMatches()==1){
			 matchAssetId = selectAssetId;
			 trialMatchSelectAssetMap = trialSelectAssetMap;
		}
		//若为0，则代表相近匹配，选中项和匹配项属于同一类。匹配项选取mapType为3或4的资源
		else{
			List<TrialAssetMap> matchTriaAssetMapList= selectTriaAssetMap.get(rand1);
			Collections.shuffle(matchTriaAssetMapList);
			trialMatchSelectAssetMap = matchTriaAssetMapList.get(0);
			matchAssetId = trialMatchSelectAssetMap.getAssetId();
			
		}
		
		Map assetMap = new HashMap<Integer,Integer>();
		assetMap.put(AssetType.select, selectAssetId);
		assetMap.put(AssetType.match, matchAssetId);
		assetMap.put(AssetType.distractor1, distractorAssetId1);
		assetMap.put(AssetType.distractor2, distractorAssetId2);
		return assetMap;
	}
	class UpdateImageViewTask extends AsyncTask<String , Integer, Map>{
		List<Trial> trialList;
		Map trialHashMap;
		ImageView seletIv;
		List<Map<String ,ImageView>> distractorIvList;
		public UpdateImageViewTask(List<Trial> trialList, Map trialHashMap,ImageView seletIv,
		List<Map<String ,ImageView>> distractorIvList) {
			super();
			this.trialList = trialList;
			this.trialHashMap = trialHashMap;
			this.seletIv = seletIv;
			this.distractorIvList = distractorIvList;
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
			updateImageView(seletIv,distractorIvList,(HashMap<Integer, Integer>) map);
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
		
		rl_gameinterface_bottom.setBackgroundResource(0);
		System.gc(); 
	}
	
}
