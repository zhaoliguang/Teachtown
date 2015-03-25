package com.teachtown.activity;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

public class TrialMatchingActivity extends FinalActivity {
	
	private IflytekSpeech iflytekSpeech;
	private FinalDb database;
	private int lessonHandle;
	private List<Trial> trialList;
	private HashMap<Integer, HashMap<Integer,List<TrialAssetMap>>> trialHashmap;
	private ArrayList<List<TrialAssetMap>> allTrialAssetList;
	private List<ImageView> imageViewList;
	private String sdRootFolder ="/storage/sdcard0" ;
	private int duringTime = 3000;
	private int fadeTime = 2000;
	private int waiteTime =4000;
	private  int MapTypeSize = 8;
	private int matchId;
	private ImageView matchView,distractorView1,distractorView2 ;
	private UpdateImageViewTask updateImageViewTask;
	private  Timer timer = new Timer();
	private MediaPlayer mediaPlayer;
	private int mp3AssetId;
	private String tiralSpeech;
	private Random random;
	int rand;
	@ViewInject(id = R.id.iv_selected) ImageView iv_selected;
	@ViewInject(id = R.id.iv_distractor1, click = "imageViewlick") ImageView im_distractor1;
	@ViewInject(id = R.id.iv_distractor2, click = "imageViewlick") ImageView im_distractor2;
	@ViewInject(id = R.id.iv_distractor3, click = "imageViewlick") ImageView im_distractor3;
	//private Map<Integer, List<TrialAssetMap>> assetMap;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trial_matching);
		
		trialHashmap = new HashMap<Integer, HashMap<Integer,List<TrialAssetMap>>>();
		 mediaPlayer = new MediaPlayer();
		 iflytekSpeech = new IflytekSpeech(TrialMatchingActivity.this);
		 random = new Random();
		//初始化allTrialAssetList 
		allTrialAssetList = new ArrayList<List<TrialAssetMap>>();
		for(int i=0;i<=MapTypeSize;i++){   
			List<TrialAssetMap> trialAssetMapList = new ArrayList<TrialAssetMap>();
			allTrialAssetList.add(trialAssetMapList);   
	    }   
		Bundle bundle = this.getIntent().getExtras();
		lessonHandle = bundle.getInt("lessonHandle");
		database = DatabaseUtil.getDatabase(TrialMatchingActivity.this);
		//对TrialAssetMap按照mapType进行分类
		trialList = database.findAllByWhere(Trial.class, "lessonHandle="+lessonHandle, "trialId");
		constructorTrialMap(trialList,trialHashmap);
		imageViewList = new ArrayList<ImageView>();
		imageViewList.add(im_distractor1);
		imageViewList.add(im_distractor2);
		imageViewList.add(im_distractor3);
		new UpdateImageViewTask(trialList, trialHashmap, iv_selected, imageViewList).execute("");
	
		//setImageView(trialList,trialHashmap,iv_selected,imageViewList);
		
	}
	
	public void imageViewlick(View view){

		int clickId = view.getId();
		
		ObjectAnimator.ofFloat(matchView, "rotationY", 0, 180, 0).setDuration(duringTime).start();
		animate(distractorView1).setDuration(fadeTime);
		animate(distractorView2).setDuration(fadeTime);
		animate(distractorView1).alpha(0);
		animate(distractorView2).alpha(0);
		if(clickId == matchId){
			//正确
			
		}
		else
		{
			//错误
			
		}
		
		
		im_distractor1.setEnabled(false);
		
		im_distractor2.setEnabled(false);
		im_distractor3.setEnabled(false);
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
			List<TrialAssetMap> trialAssetMapList= (List<TrialAssetMap>) database.findAllByWhere(TrialAssetMap.class, "trialId="+trial.getTrialId());
			Map trialAssetHashMap = new HashMap<Integer, List<TrialAssetMap>>();
			for(int i=1;i<=8;i++)
				trialAssetHashMap.put(i, new ArrayList<TrialAssetMap>());
			for(TrialAssetMap trialAssetMap:trialAssetMapList)
				((ArrayList<TrialAssetMap>)(trialAssetHashMap.get(trialAssetMap.getMapType()))).add(trialAssetMap);
			trialHashmap.put(trial.getTrialId(), trialAssetHashMap);
	
		}
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

	public void updateImageView(ImageView seletIv,List<ImageView> distractorIvList,HashMap<Integer, Integer> map){
		
		Collections.shuffle(distractorIvList);//随机排序ImageView，使每次匹配项出现位置不同
		
		matchView = distractorIvList.get(0);
		distractorView1 = distractorIvList.get(1);
		distractorView2 = distractorIvList.get(2);
		matchView.setEnabled(true);
		distractorView1.setEnabled(true);
		distractorView2.setEnabled(true);
		animate(matchView).setDuration(0);
		animate(distractorView1).setDuration(0);
		animate(distractorView2).setDuration(0);
		animate(matchView).alpha(1);
		animate(distractorView1).alpha(1);
		animate(distractorView2).alpha(1);
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
		//IflytekSpeech.startSpeech(TrialMatchingActivity.this, "请给我找出");
	}
	
	private Map getRandomImage(List<Trial> trialList,Map trialHashMap){
		TrialAssetMap trialSelectAssetMap;//选中的资源
		TrialAssetMap trialMatchSelectAssetMap;//匹配选中的资源
		TrialAssetMap trialDistractorAssetMap1;//干扰资源1
		TrialAssetMap trialDistractorAssetMap2;//干扰资源2
		Collections.shuffle(trialList);//trialList重新随机排序
		//得到选中的trial，因为已经随机排序所以取第一个即为随机值
		Trial trial = trialList.get(0);
		int selectid = trial.getTrialId();
		HashMap<Integer,List<TrialAssetMap>> selectTriaAssetMap = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(trialList.get(0).getTrialId());
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
		iflytekSpeech.startSpeech( "请找出"+tiralSpeech);
		//判断该lesson中有几个trial，如果trial数目为三个以上，则干扰项分别除选中的以外另项中抽两个作为干扰项
		if(trialList.size()>2){
			//抽取除选中的trial另外选两个干扰项
			HashMap<Integer,List<TrialAssetMap>> distractortTriaAssetHashMap1 = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(trialList.get(1).getTrialId());
			HashMap<Integer,List<TrialAssetMap>> distractortTriaAssetHashMap2 = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(trialList.get(2).getTrialId());
			//选择mapType为1,2的资源作为干扰项
			List<TrialAssetMap> distractorTrialAssetList1 = distractortTriaAssetHashMap1.get(rand);
			List<TrialAssetMap> distractorTrialAssetList2 = distractortTriaAssetHashMap2.get(rand);
			//随机排序map类型为1,2的资源，为下一次随机做准备
			Collections.shuffle(distractorTrialAssetList1);
			Collections.shuffle(distractorTrialAssetList2);
			//得到随机干扰项资源
			trialDistractorAssetMap1 = distractorTrialAssetList1.get(0);
			trialDistractorAssetMap2 = distractorTrialAssetList2.get(0);
		}
		//若该lesson中trial数目为2个以下，则从另外一个trial中抽两个做为干扰项
		else{
			HashMap<Integer,List<TrialAssetMap>> distractortTriaAssetHashMap1 = (HashMap<Integer, List<TrialAssetMap>>) trialHashMap.get(trialList.get(1).getTrialId());
			List<TrialAssetMap> distractorTrialAssetList1 = distractortTriaAssetHashMap1.get(rand);
			Collections.shuffle(distractorTrialAssetList1);
			trialDistractorAssetMap1 = distractorTrialAssetList1.get(0);
			trialDistractorAssetMap2 = distractorTrialAssetList1.get(1);
		}

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
		List<ImageView> distractorIvList;
		public UpdateImageViewTask(List<Trial> trialList, Map trialHashMap,ImageView seletIv,
		List<ImageView> distractorIvList) {
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
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		 mediaPlayer.release();
	     mediaPlayer = null;
	     super.onDestroy();
	}
	
}
