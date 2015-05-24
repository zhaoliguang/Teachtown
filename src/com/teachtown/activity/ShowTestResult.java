package com.teachtown.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.RangeCategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;


import com.hfut.teachtown.R;
import com.teachtown.adapter.LessonGridViewAdapter;
import com.teachtown.model.Lesson;
import com.teachtown.model.TestResultSync;
import com.teachtown.utils.DatabaseUtil;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShowTestResult extends Activity {
	LinearLayout li1;
	int lessonHandle;
	String lessonName;
	private FinalDb dataBase;	
	TestResultSync testResult;
	TestResultSync percenttestResult;
	private double[] percentCorrect;
	private double[] promptPercentCorrect;
	private double[] blank;
	private String[] title;
	private ArrayList titleList;
	private ArrayList<Double> value;
	private List<TestResultSync> resultList;
	private int[] xlabel;
	private TextView textView;
	private int maxsize;
   
	@Override
	public void onCreate(Bundle savedInstanceState) {    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_test_result);	
		textView=(TextView)findViewById(R.id.histogram_title);
		//得到资源
	
		li1 = (LinearLayout) findViewById(R.id.li1);
		Bundle bundle = this.getIntent().getExtras();
		lessonHandle = bundle.getInt("lessonHandle");
		lessonName = bundle.getString("lessonName");
		titleList = new ArrayList<String>();			
		dataBase = DatabaseUtil.getDatabase(this);
		resultList = dataBase.findAllByWhere(TestResultSync.class, "lessonHandle="+lessonHandle+" and studentId="+TeachTownApp.getStudentId());
		//初始化柱状图
		initView();
	}

		private void initView() {
			int i=0;			
			title = new String[resultList.size()];
			percentCorrect = new double[resultList.size()];
			promptPercentCorrect=new double[resultList.size()];
			blank=new double[resultList.size()];		
			for(TestResultSync testResult:resultList){
				//titleList.add(testResult.getExerciseName());
				title[i]=testResult.getExerciseName();
				percentCorrect[i]=testResult.getPercentCorrect();
				percentCorrect[i] = (double)Math.round(percentCorrect[i]*100)/100;  
				promptPercentCorrect[i]=testResult.getPercentPrompted();
				promptPercentCorrect[i] = (double)Math.round(promptPercentCorrect[i]*100)/100; 
				blank[i]=0;
				i++;
			}	
			
			//x轴上的坐标刻度值
		    	xlabel=new int[resultList.size()];
		    	for(int j=0;j<resultList.size();j++)
		    	{
		    		xlabel[j]=j+1;
		    	}
		    	
		    	textView.setText(lessonName);
			
			    //柱状图的两个序列的名字
			    String[] titles = new String[] { "未提示的正确率  ","1", "提示后的正确率" };
			        //存放柱状图两个序列的值
			    ArrayList<double[]> value = new ArrayList<double[]>();
			    value.add(percentCorrect);
			    value.add(blank);
			    value.add(promptPercentCorrect);
			    
			        //两个状的颜色
			    int[] colors = { Color.rgb(255,0,255),Color.rgb(193,240,255), Color.rgb(50,205,50)};
			    //为li1添加柱状图
			    /*li1.addView(
			        xychar(titles, value, colors, 6, 1, new double[] { 0,
			        		titles.length+0.5, 0, 1 }, titles[i], lessonName, true),
			        new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
			            LayoutParams.MATCH_PARENT));*/
			    
			    li1.addView(
				        xychar(titles, value, colors, resultList.size(), 8, new double[] { 0.2,
				        		resultList.size()+0.5, 0, 100 }, xlabel, lessonName, true),
				        new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				            LayoutParams.MATCH_PARENT));
			  }

			  public GraphicalView xychar(String[] titles, ArrayList<double[]> value,
			      int[] colors, int x, int y,double[] range, int []xLable ,String xtitle, boolean f) {
			    //多个渲染
			    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
			    //多个序列的数据集
			    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
			    //构建数据集以及渲染
			    for (int i = 0; i < titles.length; i++) {
			    
			      XYSeries series = new XYSeries(titles[i]);
			      double [] yLable= value.get(i);
			      for (int j=0;j<yLable.length;j++) {
			        series.add(xLable[j],yLable[j]*100);
			      }
			      dataset.addSeries(series);
			      XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
			      // 设置颜色
			      xyRenderer.setColor(colors[i]);
			      // 设置点的样式 //
			      xyRenderer.setPointStyle(PointStyle.SQUARE);
			      // 将要绘制的点添加到坐标绘制中

			      renderer.addSeriesRenderer(xyRenderer);
			      renderer.setLegendHeight(120);
			      renderer.setMargins(new int[]{50,50,60,50});//上,左,下,右       
			    }
			    //设置x轴标签数
			    renderer.setXLabels(0);
			    //设置Y轴标签数
			    renderer.setYLabels(y);
			    //设置x轴的最大值
			    renderer.setXAxisMax(x - 0.5);
			    //设置轴的颜色
			    renderer.setAxesColor(Color.BLACK);
			    //设置x轴和y轴的标签对齐方式
			    renderer.setXLabelsAlign(Align.LEFT);
			    renderer.setYLabelsAlign(Align.RIGHT);
			    // 设置现实网格
			    renderer.setShowGrid(true); 
			    renderer.setShowGridY(true); 		  
			    renderer.setShowAxes(true); 
			    // 设置条形图之间的距离
			    renderer.setBarSpacing(1.3);
			    renderer.setInScroll(false);
			    renderer.setPanEnabled(false, false);
			    renderer.setClickEnabled(false);
			    //设置x轴和y轴标签的颜色
			    renderer.setXLabelsColor(Color.BLACK);
			    renderer.setYLabelsColor(0,Color.BLACK);
			/*    maxsize=title[0].length();
			    for(int k = 1; k < resultList.size(); k++)
			    {
			    	if(title[k].length()>maxsize)
			    	{
			    		maxsize=title[k].length();
			    	}
			    }
			    	
			    
			    renderer.setXLabelsAlign(7,maxsize/2)*/;
			      
			    int length = renderer.getSeriesRendererCount();
			    //设置图标的标题
			   /* renderer.setChartTitle(xtitle);*/
			    renderer.setLabelsColor(Color.BLACK);
			    for(int k = 0; k < resultList.size(); k++){
				     renderer.addTextLabel(k+1,title[k]); 
				     if(title[k].length()>=6)
				     {
				    	 renderer.setXLabelsAngle(90); 
				     }       
				    }

			    //设置图例的字体大小
			    renderer.setLegendTextSize(30);			   
			    renderer.setAxisTitleTextSize(25);
			    renderer.setChartTitleTextSize(30);
			    renderer.setLabelsTextSize(30); 

			    //设置x轴和y轴的最大最小值
			    renderer.setRange(range);
			    renderer.setMarginsColor(0x00888888);
			    for (int i = 0; i < length; i++) {
			      SimpleSeriesRenderer ssr = renderer.getSeriesRendererAt(i);
			      ssr.setChartValuesTextAlign(Align.RIGHT);
			      ssr.setChartValuesTextSize(30);
			      ssr.setDisplayChartValues(f);
			    }
			    GraphicalView mChartView = ChartFactory.getBarChartView(getApplicationContext(),
			        dataset, renderer, Type.DEFAULT);
			    return mChartView;			  
			  }
}

