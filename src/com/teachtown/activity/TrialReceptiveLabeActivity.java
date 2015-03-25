package com.teachtown.activity;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.hfut.teachtown.R;
import com.nineoldandroids.animation.ObjectAnimator;
import com.teachtown.component.AssetType;
import com.teachtown.component.IflytekSpeech;
import com.teachtown.model.Asset;
import com.teachtown.model.Trial;
import com.teachtown.model.TrialAssetMap;
import com.teachtown.utils.DatabaseUtil;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;

public class TrialReceptiveLabeActivity extends FinalActivity implements OnClickListener {
	private IflytekSpeech iflytekSpeech;
	private FinalDb database;
	private List<Trial> trialList;
	private HashMap<Integer, HashMap<Integer,List<TrialAssetMap>>> trialHashmap;
	private ArrayList<List<TrialAssetMap>> allTrialAssetList;
	private int numDistractors ;
	private int selectAssetId;//记录每次应该选中的资源id
	private int lessonHandle;
	private  int MapTypeSize = 8;
	private View view;
	private int duringTime = 3000;
	private int fadeTime = 2000;
	private int waiteTime =4000;
	private String sdRootFolder ="/storage/sdcard0" ;
	private Random random;
	private ImageView selectView,distractorView1,distractorView2,distractorView3;
	private List<ImageView> imageViewList;
	private  Timer timer = new Timer();
	private MediaPlayer mediaPlayer;
	private String tiralSpeech;
	private int mp3AssetId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Bundle bundle = this.getIntent().getExtras();
		numDistractors = bundle.getInt("numDistractors");
		lessonHandle = bundle.getInt("lessonHandle");
		random = new Random();
		 mediaPlayer = new MediaPlayer();
		 iflytekSpeech = new IflytekSpeech(TrialReceptiveLabeActivity.this);
		 imageViewList = new ArrayList<ImageView>();
		switch(numDistractors){
		case 0:
			view = inflater.inflate(R.layout.receptive_label_0, null,true);
			setContentView(view);
			selectView = (ImageView) view.findViewById(R.id.im_select0);
			selectView.setOnClickListener(this);
			imageViewList.add(selectView);
			break;
		case 1:
			view = inflater.inflate(R.layout.receptive_label_1, null,true);
			setContentView(view);
			selectView = (ImageView) view.findViewById(R.id.im_select1);
			distractorView1 = (ImageView) view.findViewById(R.id.im_distractor1_distractor1);
			selectView.setOnClickListener(this);
			distractorView1.setOnClickListener(this);
			imageViewList.add(selectView);
			imageViewList.add(distractorView1);
			break;
		case 2:
			view = inflater.inflate(R.layout.receptive_label_2, null,true);
			setContentView(view);
			selectView = (ImageView) view.findViewById(R.id.im_select2);
			distractorView1 = (ImageView) view.findViewById(R.id.im_distractor2_distractor1);
			distractorView2 = (ImageView) view.findViewById(R.id.im_distractor2_distractor2);
			selectView.setOnClickListener(this);
			distractorView1.setOnClickListener(this);
			distractorView2.setOnClickListener(this);
			imageViewList.add(selectView);
			imageViewList.add(distractorView1);
			imageViewList.add(distractorView2);
			break;
		case 3:
			view = inflater.inflate(R.layout.receptive_lable_3, null,true);
			setContentView(view);
			selectView = (ImageView) view.findViewById(R.id.im_select3);
			distractorView1 = (ImageView) view.findViewById(R.id.im_distractor3_distractor1);
			distractorView2 = (ImageView) view.findViewById(R.id.im_distractor3_distractor2);
			distractorView3 = (ImageView) view.findViewById(R.id.im_distractor3_distractor3);
			selectView.setOnClickListener(this);
			distractorView1.setOnClickListener(this);
			distractorView2.setOnClickListener(this);
			distractorView3.setOnClickListener(this);
			imageViewList.add(selectView);
			imageViewList.add(distractorView1);
			imageViewList.add(distractorView2);
			imageViewList.add(distractorView3);
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
			List<TrialAssetMap> trialAssetMapList= (List<TrialAssetMap>) database.findAllByWhere(TrialAssetMap.class, "trialId="+trial.getTrialId());
			Map trialAssetHashMap = new HashMap<Integer, List<TrialAssetMap>>();
			for(int i=1;i<=8;i++)
				trialAssetHashMap.put(i, new ArrayList<TrialAssetMap>());
			for(TrialAssetMap trialAssetMap:trialAssetMapList)
				((ArrayList<TrialAssetMap>)(trialAssetHashMap.get(trialAssetMap.getMapType()))).add(trialAssetMap);
			trialHashmap.put(trial.getTrialId(), trialAssetHashMap);
	
		}
	}
	
	public void updateImageView(List<ImageView> imageViewIvList,HashMap<Integer, Integer> map){
		
		Collections.shuffle(imageViewIvList);//随机排序ImageView，使每次匹配项出现位置不同
		
		int selectAssetId,distractorAssetId1,distractorAssetId2,distractorAssetId3;
		switch(imageViewIvList.size()){
		case 1:
			selectView = imageViewIvList.get(0);
			selectView.setEnabled(true);
			selectAssetId = map.get(AssetType.select);
			selectView = imageViewIvList.get(0);
			animate(selectView).setDuration(0);
			animate(selectView).alpha(1);
			selectView.setId(selectAssetId);
			selectView.setImageBitmap(getImageFromFile(getFileName(selectAssetId)));
			break;
		case 2:
			selectView = imageViewIvList.get(0);
			distractorView1 = imageViewIvList.get(1);
			selectView.setEnabled(true);
			distractorView1.setEnabled(true);
			animate(selectView).setDuration(0);
			animate(selectView).alpha(1);
			animate(distractorView1).setDuration(0);
			animate(distractorView1).alpha(1);
			selectAssetId = map.get(AssetType.select);
			distractorAssetId1 = map.get(AssetType.distractor1);
			selectView.setId(selectAssetId);
			selectView.setImageBitmap(getImageFromFile(getFileName(selectAssetId)));
			distractorView1.setId(distractorAssetId1);
			distractorView1.setImageBitmap(getImageFromFile(getFileName(distractorAssetId1)));
			
			break;
		case 3:
			selectView = imageViewIvList.get(0);
			distractorView1 = imageViewIvList.get(1);
			distractorView2 = imageViewIvList.get(2);
			selectView.setEnabled(true);
			distractorView1.setEnabled(true);
			distractorView2.setEnabled(true);
			animate(selectView).setDuration(0);
			animate(selectView).alpha(1);
			animate(distractorView1).setDuration(0);
			animate(distractorView2).setDuration(0);
			animate(distractorView1).alpha(1);
			animate(distractorView2).alpha(1);
			selectAssetId = map.get(AssetType.select);
			distractorAssetId1 = map.get(AssetType.distractor1);
			distractorAssetId2 = map.get(AssetType.distractor2);
			selectView.setId(selectAssetId);
			selectView.setImageBitmap(getImageFromFile(getFileName(selectAssetId)));
			distractorView1.setId(distractorAssetId1);
			distractorView1.setImageBitmap(getImageFromFile(getFileName(distractorAssetId1)));
			
			distractorView2.setId(distractorAssetId2);
			distractorView2.setImageBitmap(getImageFromFile(getFileName(distractorAssetId2)));
			break;
		case 4:
			selectView = imageViewIvList.get(0);
			distractorView1 = imageViewIvList.get(1);
			distractorView2 = imageViewIvList.get(2);
			distractorView3 = imageViewIvList.get(3);
			selectView.setEnabled(true);
			distractorView1.setEnabled(true);
			distractorView2.setEnabled(true);
			distractorView3.setEnabled(true);
			animate(selectView).setDuration(0);
			animate(selectView).alpha(1);
			animate(distractorView1).setDuration(0);
			animate(distractorView2).setDuration(0);
			animate(distractorView3).setDuration(0);
			animate(distractorView1).alpha(1);
			animate(distractorView2).alpha(1);
			animate(distractorView3).alpha(1);
			selectAssetId = map.get(AssetType.select);
			distractorAssetId1 = map.get(AssetType.distractor1);
			distractorAssetId2 = map.get(AssetType.distractor2);
			distractorAssetId3 = map.get(AssetType.distractor3);
			selectView.setId(selectAssetId);
			selectView.setImageBitmap(getImageFromFile(getFileName(selectAssetId)));
			distractorView1.setId(distractorAssetId1);
			distractorView1.setImageBitmap(getImageFromFile(getFileName(distractorAssetId1)));
			
			distractorView2.setId(distractorAssetId2);
			distractorView2.setImageBitmap(getImageFromFile(getFileName(distractorAssetId2)));
			
			distractorView3.setId(distractorAssetId3);
			distractorView3.setImageBitmap(getImageFromFile(getFileName(distractorAssetId3)));
			break;
		}
		
		/**
		mediaPlayer.reset();
         try {
			mediaPlayer.setDataSource(getFileName(mp3AssetId));
			mediaPlayer.prepare();
			mediaPlayer.start();
			IflytekSpeech.startSpeech(TrialReceptiveLabeActivity.this, "请找出");
		} catch (IllegalArgumentException e) {
			 TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			 TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			 TODO Auto-generated catch block
			e.printStackTrace();
		}
      **/
        
		
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
            Bitmap bitmap=BitmapFactory.decodeFile(fileName);
            return bitmap;
	
	  }
	private Map getRandomImage(List<Trial> trialList,Map trialHashMap){
		TrialAssetMap trialSelectAssetMap;//选中的资源
		TrialAssetMap trialMatchSelectAssetMap;//匹配选中的资源
		TrialAssetMap trialDistractorAssetMap1;//干扰资源1
		TrialAssetMap trialDistractorAssetMap2;//干扰资源2
		TrialAssetMap trialDistractorAssetMap3;//干扰资源3
		Collections.shuffle(trialList);//trialList重新随机排序
		//得到选中的trial，因为已经随机排序所以取第一个即为随机值
		Trial trial = trialList.get(0);
		int selectid = trial.getTrialId();
		HashMap<Integer,List<TrialAssetMap>> selectTriaAssetMap = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(trialList.get(0).getTrialId());
		
		//得到选中trial中的mapType为7的mp3资源文件
		//List<TrialAssetMap> selectMp3TrialAssetMapList = selectTriaAssetMap.get(7);
		//mp3AssetId = selectMp3TrialAssetMapList.get(random.nextInt(selectMp3TrialAssetMapList.size())).getAssetId();
		
		tiralSpeech = trial.getChineseName();
		iflytekSpeech.startSpeech( "请找出"+tiralSpeech);
		int rand = random.nextInt(2)+3;//随机产生3,4
		//得到选中trial中的mapType为3或4的资源文件
		List<TrialAssetMap> selectTrialAssetMapList = selectTriaAssetMap.get(rand);
		//该trial中mapType为3或4的资源文件列表随机排序
		Collections.shuffle(selectTrialAssetMapList);
		//得到选中项资源，随机排序后得到的第一项即为随机值
		trialSelectAssetMap = selectTrialAssetMapList.get(0);
		
		
		//如果选中trial的rl_ShareDistractors属性为1，则干扰项从另外相同lessonhandle中选中，若为0，则从自己assetMap中选取mapType为5,6的作为干扰项
		if(trial.getRl_ShareDistractors()==1){
				
			//抽取除选中的trial另外选三个干扰项
			int randDistractor = random.nextInt(trialList.size()-1)+1;//随机产生选取distractor的数值，除选中的第0个除外		
			HashMap<Integer,List<TrialAssetMap>> distractortTriaAssetHashMap1 = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(trialList.get(randDistractor).getTrialId());
			randDistractor = random.nextInt(trialList.size()-1)+1;//随机产生选取distractor的数值，除选中的第0个除外	
			HashMap<Integer,List<TrialAssetMap>> distractortTriaAssetHashMap2 = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(trialList.get(randDistractor).getTrialId());
			randDistractor = random.nextInt(trialList.size()-1)+1;//随机产生选取distractor的数值，除选中的第0个除外	
			HashMap<Integer,List<TrialAssetMap>> distractortTriaAssetHashMap3 = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(trialList.get(randDistractor).getTrialId());
			
			
			rand = random.nextInt(2)+3;//随机产生3,4
			List<TrialAssetMap> distractorTrialAssetList1 = distractortTriaAssetHashMap1.get(rand);
			Collections.shuffle(distractorTrialAssetList1);
			trialDistractorAssetMap1 = distractorTrialAssetList1.get(random.nextInt(distractorTrialAssetList1.size()));//得到随机干扰项资源
			rand = random.nextInt(2)+3;//随机产生3,4
			List<TrialAssetMap> distractorTrialAssetList2 = distractortTriaAssetHashMap2.get(rand);
			Collections.shuffle(distractorTrialAssetList2);
			trialDistractorAssetMap2 = distractorTrialAssetList2.get(random.nextInt(distractorTrialAssetList2.size()));
			rand = random.nextInt(2)+3;//随机产生3,4
			List<TrialAssetMap> distractorTrialAssetList3 = distractortTriaAssetHashMap3.get(rand);
			Collections.shuffle(distractorTrialAssetList3);
			trialDistractorAssetMap3 = distractorTrialAssetList3.get(random.nextInt(distractorTrialAssetList3.size()));
		
			
		}
		else{
			int randDistra = random.nextInt(2)+5;//随机产生5,6
			List<TrialAssetMap> distractorTrialAssetList1 = selectTriaAssetMap.get(randDistra);
			Collections.shuffle(distractorTrialAssetList1);
			trialDistractorAssetMap1 = distractorTrialAssetList1.get(random.nextInt(distractorTrialAssetList1.size()));
			randDistra = random.nextInt(2)+5;//随机产生5,6
			List<TrialAssetMap> distractorTrialAssetList2 = selectTriaAssetMap.get(randDistra);
			Collections.shuffle(distractorTrialAssetList2);
			trialDistractorAssetMap2 = distractorTrialAssetList2.get(random.nextInt(distractorTrialAssetList2.size()));
			randDistra = random.nextInt(2)+5;//随机产生5,6
			List<TrialAssetMap> distractorTrialAssetList3 = selectTriaAssetMap.get(randDistra);
			Collections.shuffle(distractorTrialAssetList3);
			trialDistractorAssetMap3 = distractorTrialAssetList3.get(random.nextInt(distractorTrialAssetList3.size()));
			
		}

		selectAssetId = trialSelectAssetMap.getAssetId();
		int distractorAssetId1 = trialDistractorAssetMap1.getAssetId();
		int distractorAssetId2 = trialDistractorAssetMap2.getAssetId();
		int distractorAssetId3 = trialDistractorAssetMap3.getAssetId();

		Map assetMap = new HashMap<Integer,Integer>();
		assetMap.put(AssetType.select, selectAssetId);
		assetMap.put(AssetType.distractor1, distractorAssetId1);
		assetMap.put(AssetType.distractor2, distractorAssetId2);
		assetMap.put(AssetType.distractor3, distractorAssetId3);
		return assetMap;
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		
		int clickId = view.getId();

		switch(imageViewList.size()){
		case 1:
			ObjectAnimator.ofFloat(selectView, "rotationY", 0, 180, 0).setDuration(duringTime).start();
			selectView.setEnabled(false);
			break;
		case 2:
			ObjectAnimator.ofFloat(selectView, "rotationY", 0, 180, 0).setDuration(duringTime).start();
			animate(distractorView1).setDuration(fadeTime);
			animate(distractorView1).alpha(0);
			selectView.setEnabled(false);
			distractorView1.setEnabled(false);
			break;
		case 3:
			ObjectAnimator.ofFloat(selectView, "rotationY", 0, 180, 0).setDuration(duringTime).start();
			animate(distractorView1).setDuration(fadeTime);
			animate(distractorView2).setDuration(fadeTime);
			animate(distractorView1).alpha(0);
			animate(distractorView2).alpha(0);
			selectView.setEnabled(false);
			distractorView1.setEnabled(false);
			distractorView2.setEnabled(false);
			break;
		case 4:
			ObjectAnimator.ofFloat(selectView, "rotationY", 0, 180, 0).setDuration(duringTime).start();
			animate(distractorView1).setDuration(fadeTime);
			animate(distractorView2).setDuration(fadeTime);
			animate(distractorView3).setDuration(fadeTime);
			animate(distractorView1).alpha(0);
			animate(distractorView2).alpha(0);
			animate(distractorView3).alpha(0);
			selectView.setEnabled(false);
			distractorView1.setEnabled(false);
			distractorView2.setEnabled(false);
			distractorView3.setEnabled(false);
			break;
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
		List<ImageView> imageIvList;
		public UpdateImageViewTask(List<Trial> trialList, Map trialHashMap,
		List<ImageView> imageIvList) {
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
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		 mediaPlayer.release();
	     mediaPlayer = null;
	     super.onDestroy();
	}
}
