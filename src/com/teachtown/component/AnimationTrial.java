package com.teachtown.component;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.hfut.teachtown.R;
import com.plattysoft.leonids.ParticleSystem;
import com.plattysoft.leonids.modifiers.ScaleModifier;


public class AnimationTrial {
	private static int count = 40;
	
	public static void OneShot(View view,Activity context ,int fadeTime){
//		ParticleSystem ps = new ParticleSystem(context, 100, R.drawable.particial_flower, 800);
//		ps.setScaleRange(0.7f, 1.3f);
//		ps.setSpeedRange(90, 180);
//		ps.setAcceleration(0.0001f, 90);
//		ps.setRotationSpeedRange(90, 180);
//		ps.setInitialRotationRange(0, 360);
//		ps.setFadeOut(fadeTime);
//		ps.oneShot(view, count);
		new ParticleSystem(context, 10, R.drawable.particial_flower, 3000)		
		.setSpeedByComponentsRange(-0.1f, 0.1f, -0.1f, 0.02f)
		.setAcceleration(0.000003f, 90)
		.setInitialRotationRange(0, 360)
		.setRotationSpeed(120)
		.setFadeOut(fadeTime)
		.addModifier(new ScaleModifier(0f, 1.5f, 0, 1500))
		.oneShot(view, count);
	}
	
	public static void animatedParticle(View view,Activity context,int fadeTime){
		new ParticleSystem(context, 100, R.drawable.paritcial_sugar, 5000)		
		.setSpeedRange(0.1f, 0.25f)
		.setRotationSpeedRange(90, 180)
		.setInitialRotationRange(0, 360)
		.setFadeOut(fadeTime)
		.oneShot(view, count);
		
	}
	
	public static void fireWorks(View view,Activity context,int fadeTime){
		ParticleSystem ps = new ParticleSystem(context, 100, R.drawable.paritcial_sugar, fadeTime);
		ps.setScaleRange(0.7f, 1.3f);
		ps.setSpeedRange(0.1f, 0.25f);
		ps.setRotationSpeedRange(90, 180);
		ps.setInitialRotationRange(0, 180);
		ps.setFadeOut(fadeTime, new AccelerateInterpolator());
		ps.oneShot(view, count);

		ParticleSystem ps2 = new ParticleSystem(context, 100, R.drawable.particial_ring, fadeTime);
		ps2.setScaleRange(0.7f, 1.3f);
		ps2.setSpeedRange(0.1f, 0.25f);
	
		ps2.setInitialRotationRange(180, 360);
		ps2.setFadeOut(fadeTime, new AccelerateInterpolator());
		ps2.oneShot(view, count);
	}
	
	public static void stars(View view,Activity context,int fadeTime){
		new ParticleSystem(context, 10, R.drawable.particial_star, 3000)		
		.setSpeedByComponentsRange(-0.1f, 0.1f, -0.1f, 0.02f)
		.setAcceleration(0.000003f, 90)
		.setInitialRotationRange(0, 360)
		.setRotationSpeed(120)
		.setFadeOut(fadeTime)
		.addModifier(new ScaleModifier(0f, 1.5f, 0, 1500))
		.oneShot(view, count);
	}
	
	public static void Confetti(View view1,View view2,Activity context,int fadeTime){
		new ParticleSystem(context, 80, R.drawable.particial_ring, 10000)
		.setSpeedModuleAndAngleRange(0.3f, 0.6f, 0, 360)
		.setFadeOut(fadeTime)
		.setRotationSpeed(144)
		.setAcceleration(0.000017f, 90)	
		.emit(view1, count,fadeTime);
				
		new ParticleSystem(context, 80, R.drawable.paritcial_sugar, fadeTime)
		.setSpeedModuleAndAngleRange(0.3f, 0.6f, 0, 360)
		.setRotationSpeed(144)
		.setFadeOut(fadeTime)
		.setAcceleration(0.000017f, 90)		
		.emit(view2, 8,fadeTime);
	}
	
	public static void EmiterTimeLimited(View view,Activity context,int fadeTime){
		ParticleSystem ps = new ParticleSystem(context, 100, R.drawable.pariticial_prawl, 1000);
		ps.setScaleRange(0.7f, 1.3f);
		ps.setSpeedModuleAndAngleRange(0.07f, 0.16f, 0, 360);
		ps.setRotationSpeedRange(90, 180);
		ps.setAcceleration(0.00013f, 90);
		ps.setFadeOut(fadeTime);
			}	
}
