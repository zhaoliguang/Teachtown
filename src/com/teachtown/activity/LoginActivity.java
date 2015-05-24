package com.teachtown.activity;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hfut.teachtown.R;
import com.teachtown.model.Domain;
import com.teachtown.model.Lesson;
import com.teachtown.model.Student;
import com.teachtown.model.TestResultSync;
import com.teachtown.utils.DatabaseUtil;
import com.teachtown.utils.Md5Utils;
import com.teachtown.utils.UnityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends FinalActivity  {

//    private SharedPreferences settings;
	@ViewInject(id=R.id.app_login_name) EditText et_Name;
	@ViewInject(id=R.id.app_login_edit_pass) EditText et_Password;
	@ViewInject(id=R.id.forget_password,click = "buttonClick") CheckBox ck_CheckBox;
	@ViewInject(id=R.id.app_register,click = "buttonClick") ImageView btn_Register;
	@ViewInject(id=R.id.app_login_btn_submit,click = "buttonClick") ImageView btn_Login;
	
	private FinalDb dataBase;
	private List<Student> studentInfos;
	private List<Lesson> lessonList;
	private List<Domain> domainList;
	private RequestQueue mVolleyQueue;
	private JsonObjectRequest jsonObjRequest;
	
	private String et_name;
	private String et_password;
	private final String TAG_REQUEST = "MY_TAG";
	
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		
		TeachTownApp.mApp.initApp();
		float xdpi = getResources().getDisplayMetrics().xdpi;
		float ydpi = getResources().getDisplayMetrics().ydpi;
		Log.d("LoginActivity", "xdpi is " + xdpi);
		Log.d("LoginActivity", "ydpi is " + ydpi); 
		dataBase =  DatabaseUtil.getDatabase(LoginActivity.this);
		mVolleyQueue = Volley.newRequestQueue(this);
		et_Name=(EditText)findViewById(R.id.app_login_name);
		et_Password=(EditText)findViewById(R.id.app_login_edit_pass);
		btn_Register=(ImageView)findViewById(R.id.app_register);
		btn_Login=(ImageView)findViewById(R.id.app_login_btn_submit);
		ck_CheckBox=(CheckBox)findViewById(R.id.forget_password);
		dataBase =  DatabaseUtil.getDatabase(LoginActivity.this);
		lessonList = dataBase.findAll(Lesson.class);
		
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		
		if(sp.getBoolean("ISCHECK", false))  
		{  
			          //设置默认是记录密码状态  
			ck_CheckBox.setChecked(true);  
			et_Name.setText(sp.getString("USER_NAME", ""));  
			et_Password.setText(sp.getString("PASSWORD", "")); 
			 			          
	     } 
	}

	public void buttonClick(View view){
		et_name=et_Name.getText().toString().trim();
		et_password=et_Password.getText().toString().trim();
		
		switch(view.getId()){
		case R.id.app_register:
		{
			studentInfos = dataBase.findAllByWhere(Student.class, "name='"+et_name+"'");
			if(studentInfos.size()>0){
				Toast.makeText(getApplicationContext(), "用户名已存在，请重新输入", Toast.LENGTH_SHORT).show(); 
				return;
			}
//			Student student=new Student();
//			student.setName(et_name);
//			student.setPassword(et_password);
//			dataBase.save(student);
			
			UnityUtils.showProgress(LoginActivity.this,"正在注册，请稍后..."); 
			String url = "http://101.200.177.122:8080/childProject/Register";
			String user_pwd = Md5Utils.getEncode32MD5(et_password);
			 Uri.Builder builder = Uri.parse(url).buildUpon();
			 builder.appendQueryParameter("name", et_name);
			 builder.appendQueryParameter("password", user_pwd);
			 jsonObjRequest = new JsonObjectRequest(Request.Method.GET, builder.toString(), null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							//成功
							//parseFlickrImageResponse(response);
							//mAdapter.notifyDataSetChanged();
							UnityUtils.stopProgress();
							Gson gson = new Gson();
							
							if(response.length()!=0){
								Student newStudent = gson.fromJson(response.toString(), Student.class);
								dataBase.save(newStudent);
								
								UnityUtils.showToast(LoginActivity.this, "注册成功，请登陆");
							}
							else{
								UnityUtils.showToast(LoginActivity.this, "用户名已存在，请重新注册");
							}
							
						} catch (Exception e) {
							e.printStackTrace();
							UnityUtils.showToast(LoginActivity.this, "注册失败，请重新输入");
						}
						
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
						// For AuthFailure, you can re login with user credentials.
						// For ClientError, 400 & 401, Errors happening on client side when sending api request.
						// In this case you can check how client is forming the api and debug accordingly.
						// For ServerError 5xx, you can do retry or handle accordingly.
						if( error instanceof NetworkError) {
						} else if( error instanceof ClientError) { 
						} else if( error instanceof ServerError) {
						} else if( error instanceof AuthFailureError) {
						} else if( error instanceof ParseError) {
						} else if( error instanceof NoConnectionError) {
						} else if( error instanceof TimeoutError) {
						}

						UnityUtils.stopProgress();
						UnityUtils.showToast(LoginActivity.this, "注册失败，请检查网络设置");
					
					}
				});
			//Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
				jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
				jsonObjRequest.setTag(TAG_REQUEST);	
				mVolleyQueue.add(jsonObjRequest);
		}
			
			
			
			break;
		case R.id.app_login_btn_submit:
		{
			if(ck_CheckBox.isChecked())  
			{  
				                    //记住用户名、密码、  
				 Editor editor = sp.edit();  
				 editor.putString("USER_NAME", et_name);  
				 editor.putString("PASSWORD",et_password);  
				 editor.commit();  
			}  
			UnityUtils.showProgress(LoginActivity.this,"正在登陆，请稍后..."); 
			String url = "http://101.200.177.122:8080/childProject/Login";
			 final String user_pwd = Md5Utils.getEncode32MD5(et_password);
			 Uri.Builder builder = Uri.parse(url).buildUpon();
			 builder.appendQueryParameter("name", et_name);
			 builder.appendQueryParameter("passwordMD5", user_pwd);
			 jsonObjRequest = new JsonObjectRequest(Request.Method.GET, builder.toString(), null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							//成功
							//parseFlickrImageResponse(response);
							//mAdapter.notifyDataSetChanged();
							UnityUtils.stopProgress();
							Gson gson = new Gson();
							if(response.length()!=0){
								Student newStudent = gson.fromJson(response.toString(), Student.class);
								
								dataBase.save(newStudent);
								TeachTownApp.setStudentId(newStudent.getStudentId());
								getTestResult();
								Intent intent=new Intent(LoginActivity.this,MainMenuActivity.class);
							    startActivity(intent);
								
							}
							else{
								UnityUtils.showToast(LoginActivity.this, "登陆失败，用户名或密码错误");
							}
							
						} catch (Exception e) {
							e.printStackTrace();
							UnityUtils.showToast(LoginActivity.this, "登陆异常,请重新登录");
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
						// For AuthFailure, you can re login with user credentials.
						// For ClientError, 400 & 401, Errors happening on client side when sending api request.
						// In this case you can check how client is forming the api and debug accordingly.
						// For ServerError 5xx, you can do retry or handle accordingly.
						studentInfos = dataBase.findAllByWhere(Student.class, "name="+"'"+et_name+"'"+" and password="+"'"+user_pwd+"'");
						if(studentInfos.size()>0){
							Student currentStudent = studentInfos.get(0);
							TeachTownApp.setStudentId(currentStudent.getStudentId());
							//System.out.println("currentStudent.getId"+currentStudent.getId());
							
							
							Toast.makeText(getApplicationContext(), "网络连接失败，离线登录", Toast.LENGTH_SHORT).show(); 
							Intent intent=new Intent(LoginActivity.this,MainMenuActivity.class);
						    startActivity(intent);
						}
						else
							Toast.makeText(getApplicationContext(), "用户名或密码错误，请重新输入", Toast.LENGTH_SHORT).show(); 
						if( error instanceof NetworkError) {
						} else if( error instanceof ClientError) { 
						} else if( error instanceof ServerError) {
						} else if( error instanceof AuthFailureError) {
						} else if( error instanceof ParseError) {
						} else if( error instanceof NoConnectionError) {
						} else if( error instanceof TimeoutError) {
						}
						UnityUtils.stopProgress();
						UnityUtils.showToast(LoginActivity.this, error.getMessage());					
					}
				});
			//Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
				jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
				jsonObjRequest.setTag(TAG_REQUEST);	

				mVolleyQueue.add(jsonObjRequest);
				
		}
		
		
			break;
			
		case R.id.forget_password:			          
			if (ck_CheckBox.isChecked()) {  
				                       
				 System.out.println("记住密码已选中");  
				 sp.edit().putBoolean("ISCHECK", true).commit();  
			}else {  
				  System.out.println("记住密码没有选中");  
				  sp.edit().putBoolean("ISCHECK", false).commit();  
			}  
				   
		}  		
	}
	public void getTestResult(){
			String url = "http://101.200.177.122:8080/childProject/GetInfo";
		
			 Uri.Builder builder = Uri.parse(url).buildUpon();
			 builder.appendQueryParameter("studentId", String.valueOf(TeachTownApp.getStudentId()));
			// builder.appendQueryParameter("dateTaken", "");
			 
			 JsonArrayRequest JsonArrayRequest = new JsonArrayRequest(builder.toString(), new Listener<JSONArray>() {

				@Override
				public void onResponse(JSONArray jsonData) {
					// TODO Auto-generated method stub
					String json="";
					try {
						json=new String(jsonData.toString().getBytes("iso-8859-1"), "utf-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Type listType = new TypeToken<LinkedList<TestResultSync>>(){}.getType(); 
					Gson gson = new Gson(); 
					if(jsonData.length()!=0){
						LinkedList<TestResultSync> results = gson.fromJson(json, listType); 
						dataBase.deleteByWhere(TestResultSync.class, "studentId="+TeachTownApp.getStudentId());
						for (Iterator iterator = results.iterator(); iterator.hasNext();) { 
							TestResultSync testResultSync = (TestResultSync) iterator.next(); 
							
							dataBase.save(testResultSync);
					}
					
					} 
					
					
				}
			}, new Response.ErrorListener(){

				@Override
				public void onErrorResponse(VolleyError arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			 
			 	//Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
				jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
				jsonObjRequest.setTag(TAG_REQUEST);	
				mVolleyQueue.add(JsonArrayRequest);			
		}	
}
