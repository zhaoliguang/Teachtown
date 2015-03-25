package com.teachtown.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.devsmart.android.ui.HorizontalListView;
import com.hfut.teachtown.R;
import com.teachtown.adapter.CurriculumListAdapter;
import com.teachtown.component.HeaderHolder;
import com.teachtown.component.IconTreeItemHolder;
import com.teachtown.component.IconTreeItemHolder.IconTreeItem;
import com.teachtown.component.PlaceHolderHolder;
import com.teachtown.component.ProfileHolder;
import com.teachtown.component.SocialViewHolder;
import com.teachtown.model.Domain;
import com.teachtown.model.Lesson;
import com.teachtown.utils.DatabaseUtil;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

public class CurriculumActivity extends FinalActivity implements OnClickListener{
	
	private FinalDb dataBase;
	private CurriculumListAdapter curriculumlistAdapter;
	private List<Lesson> lessonList;
	private List<Domain> domainList;
	private AndroidTreeView tView;
	private PopupWindow popupWindow;
	private ViewGroup containerView;
	private View ll_curriculum;
	@ViewInject(id=R.id.im_domain) ImageView im_domain;
	@ViewInject(id=R.id.lv_curriculum) ListView lv_curriculum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.computer_curriculum);
		ll_curriculum = findViewById(R.id.ll_curri);
		dataBase =  DatabaseUtil.getDatabase(this);
		lessonList = dataBase.findAllByWhere(Lesson.class, "module='Matching' or module='ReceptiveLabel'", "groupId");
		domainList = dataBase.findAll(Domain.class);
		curriculumlistAdapter = new CurriculumListAdapter(CurriculumActivity.this,lessonList,dataBase);
		lv_curriculum.setAdapter(curriculumlistAdapter);
		im_domain.setOnClickListener(this);
        buildDomainTree(lessonList); 

    }
  
    
    private void showWindow(View parentView) {
    	 DisplayMetrics metric = new DisplayMetrics();
         getWindowManager().getDefaultDisplay().getMetrics(metric);
         int width = metric.widthPixels; // 屏幕宽度（像素）
         int height = metric.heightPixels; // 屏幕高度（像素）
    	
        if (popupWindow == null) {
        	LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	 
            View viewLayout = layoutInflater.inflate(R.layout.layout_pop_window, null, false);
             containerView = (ViewGroup) viewLayout.findViewById(R.id.container);
             containerView.addView(tView.getView());
             popupWindow = new PopupWindow();
             popupWindow.setContentView(containerView);
             // 使其聚集
             popupWindow.setFocusable(true);
             // 设置允许在外点击消失
             popupWindow.setOutsideTouchable(true);

             popupWindow.setWidth(Integer.parseInt(new DecimalFormat("0").format(width * 0.8)));
             popupWindow.setHeight(Integer.parseInt(new DecimalFormat("0").format(height * 0.8)));
             // 设置半透明灰色  
             ColorDrawable dw = new ColorDrawable(0xFFFFFFFF);  
             popupWindow.setBackgroundDrawable(dw);  
             popupWindow.setClippingEnabled(true);  
        }
       
     // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = width / 2 - height / 2;
        popupWindow.showAtLocation(ll_curriculum, Gravity.CENTER, 0, 0);
        containerView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (popupWindow != null) {
                    popupWindow.dismiss();
                }
			}
		});
        
   } 
    private void buildDomainTree(List<Lesson> lessonList){
    	 TreeNode root = TreeNode.root();
    	 TreeNode rootNode = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_arrow_drop_down,0,0,0, "All learining domain")).setViewHolder(new ProfileHolder(CurriculumActivity.this));
    	Map domainMap = new HashMap<Integer, String>();
    	for(Domain domain:domainList){
    		domainMap.put(domain.getDomainId(), domain.getName());
    	}
    	Map rootMap = new HashMap<Integer, List<TreeNode>>();
    	Map domainHashMap = new HashMap<Integer, List<TreeNode>>();
    	Map subDomainHashMap = new HashMap<Integer, List<TreeNode>>();
    	Map subSubdomainHashMap = new HashMap<Integer, List<TreeNode>>();
    	
    	List domainList = new ArrayList<TreeNode>();
    	

    	for(Lesson lesson:lessonList){
    		int subSubDomainId = lesson.getSubSubDomainId();
    		int subDomainId = lesson.getSubDomainId();
    		int domainId = lesson.getDomainId();
    		TreeNode lessonNode = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_list,subSubDomainId,subDomainId, domainId,(String) domainMap.get(subSubDomainId))).setViewHolder(new ProfileHolder(CurriculumActivity.this));
    		if(subSubdomainHashMap.get(subSubDomainId)==null){
    			List subSubDomainList = new ArrayList<TreeNode>();
    			subSubDomainList.add(lessonNode);
    			subSubdomainHashMap.put(lesson.getSubSubDomainId(),subSubDomainList);
    			
    		}
    		else
    			((List)subSubdomainHashMap.get(lesson.getSubSubDomainId())).add(lessonNode); 
    		
    		if(subDomainHashMap.get(subDomainId) == null)
    			subDomainHashMap.put(lesson.getSubDomainId(),new ArrayList<TreeNode>());
    		if(domainHashMap.get(domainId) == null)
    			domainHashMap.put(lesson.getDomainId(),new ArrayList<TreeNode>());
    		
    	}
    	Set subSubDomainSet = subSubdomainHashMap.keySet();
    	for(Iterator iterator = subSubDomainSet.iterator();iterator.hasNext();)
        {
    		
	       	 int key = (Integer) iterator.next();
	       	 List tempNodeList = (List) subSubdomainHashMap.get(key);
	       	 TreeNode tempNode = (TreeNode) tempNodeList.get(0);
		     int subSubDomainId = ((IconTreeItem)tempNode.getValue()).subSubDomainId;
			 int subDomainId = ((IconTreeItem)tempNode.getValue()).subDomainId;
			 int domainId = ((IconTreeItem)tempNode.getValue()).domainId;
	       	 TreeNode subSubNode = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_favorite,subSubDomainId,subDomainId,domainId, (String) domainMap.get(key))).setViewHolder(new ProfileHolder(CurriculumActivity.this));
	       	subSubNode.setClickListener(subSubNodeListener);
	       	 subSubNode.addChildren((List)subSubdomainHashMap.get(subSubDomainId));
	       	((List)subDomainHashMap.get(subDomainId)).add(subSubNode);
        }
    	Set subDomainSet = subDomainHashMap.keySet();
    	for(Iterator iterator = subDomainSet.iterator();iterator.hasNext();)
        {
    		
       	 int key = (Integer) iterator.next();
       	 List tempNodeList = (List) subDomainHashMap.get(key);
       	 TreeNode tempNode = (TreeNode) tempNodeList.get(0);
       	int subSubDomainId = ((IconTreeItem)tempNode.getValue()).subSubDomainId;
		int subDomainId = ((IconTreeItem)tempNode.getValue()).subDomainId;
		int domainId = ((IconTreeItem)tempNode.getValue()).domainId;
       	 TreeNode subNode = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_domain,subSubDomainId,subDomainId,domainId, (String) domainMap.get(key))).setViewHolder(new ProfileHolder(CurriculumActivity.this));
       	
       	 subNode.addChildren((List)subDomainHashMap.get(subDomainId));
        ((List)domainHashMap.get(domainId)).add(subNode);
        }
    	
    	Set domainSet = domainHashMap.keySet();
    	for(Iterator iterator = domainSet.iterator();iterator.hasNext();)
        {
    		
       	 int key = (Integer) iterator.next();
       	 List tempNodeList = (List) domainHashMap.get(key);
       	 TreeNode tempNode = (TreeNode) tempNodeList.get(0);
       	int subSubDomainId = ((IconTreeItem)tempNode.getValue()).subSubDomainId;
		int subDomainId = ((IconTreeItem)tempNode.getValue()).subDomainId;
		int domainId = ((IconTreeItem)tempNode.getValue()).domainId;
       	 TreeNode domainNode = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_arrow_drop_down_circle,subSubDomainId,subDomainId,domainId, (String) domainMap.get(key))).setViewHolder(new ProfileHolder(CurriculumActivity.this));
       	domainNode.addChildren((List)domainHashMap.get(domainId));
       	
       	rootNode.addChild(domainNode);
       	rootNode.setExpanded(true);
        }
    	root.addChild(rootNode);
    	tView = new AndroidTreeView(CurriculumActivity.this, root);
    	 tView.setDefaultAnimation(true);
         tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
        tView.setDefaultNodeClickListener(nodeClickListener);
         
         
    }
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		showWindow(im_domain);
	}
	private TreeNode.TreeNodeClickListener subSubNodeListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
            int subSubNodeId = item.subSubDomainId;
            List subSubLessonList = dataBase.findAllByWhere(Lesson.class, "subSubDomainId="+subSubNodeId, "rank");
            curriculumlistAdapter.setLessonList(subSubLessonList);
            curriculumlistAdapter.notifyDataSetChanged();
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
            tView.collapseAll();
            tView.expandLevel(1);
        
            
        }
	};
	
	private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
	        @Override
	        public void onClick(TreeNode node, Object value) {
	            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
	           // statusBar.setText("Last clicked: " + item.text);
	  }
	};
}
