package com.teachtown.activity;

import com.hfut.teachtown.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity  {
	private EditText et_Name;
	private EditText et_Password;
	private CheckBox ck_CheckBox;
	private Button btn_Register;
	private Button btn_Login;
	private TextView tv_result;
//    private SharedPreferences settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		TeachTownApp.mApp.initApp();
		et_Name=(EditText)findViewById(R.id.app_login_name);
		et_Password=(EditText)findViewById(R.id.app_login_edit_pass);
		btn_Register=(Button)findViewById(R.id.app_register);
		btn_Login=(Button)findViewById(R.id.app_login_btn_submit);
		ck_CheckBox=(CheckBox)findViewById(R.id.app_login_check_remember);
        tv_result=(TextView)findViewById(R.id.tv);
        btn_Login.setOnClickListener(new View.OnClickListener(){
            @Override
        	public void onClick(View v){
        	    if(v==btn_Login)
        	    {
        			String et_name=et_Name.getText().toString();
        			String et_password=et_Password.getText().toString();
        		
        			if(et_name.equals(""))
        			{
        				if(et_password.equals("")) 
        				{     				
        				    Intent intent=new Intent(LoginActivity.this,MainMenuActivity.class);
        				    startActivity(intent);
        					
        				}
        				else
        					tv_result.setText("密码错误");     					
        			}
        			else
        				tv_result.setText("无该用户");        		
        	   }       	
           }
      });      
    }
}
