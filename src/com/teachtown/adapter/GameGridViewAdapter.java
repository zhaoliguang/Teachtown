package com.teachtown.adapter;

import java.util.List;

import com.hfut.teachtown.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GameGridViewAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater inflater;
	private List<Integer> gameImageViewList ;
	public GameGridViewAdapter(Context context,List<Integer> gameImageViewList) {
		// TODO Auto-generated constructor stub
		super();
		this.context=context;
		this.inflater = LayoutInflater.from(context);
		this.gameImageViewList = gameImageViewList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gameImageViewList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Viewholder holder;
				if (convertView == null) {
					 convertView = inflater.inflate(R.layout.game_pop_activity,
					    		parent,false);
					    holder = new Viewholder();
					    holder.game_view = ((ImageView) convertView
					  	      .findViewById(R.id.kick_game));
					    
					    convertView.setTag(holder);
			   } else {
				   holder = (Viewholder) convertView.getTag();
			   }
				holder.game_view.setImageResource(gameImageViewList.get(position));
		
		return convertView;
	}
	class Viewholder{
		public ImageView game_view;
}

}
