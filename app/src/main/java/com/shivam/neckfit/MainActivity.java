package com.shivam.neckfit;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {
    final int NAVY = Color.rgb(11,31,58), SAFFRON = Color.rgb(255,153,51), GREEN = Color.rgb(19,136,8);
    LinearLayout root, content, bottom;
    SharedPreferences prefs;
    ArrayList<Exercise> exercises = new ArrayList<>();
    CountDownTimer timer; int current = 0, secondsLeft = 0;

    static class Exercise { String name, instructions, benefits; int seconds, sets; Exercise(String n,int s,int set,String i,String b){name=n;seconds=s;sets=set;instructions=i;benefits=b;} }

    @Override public void onCreate(Bundle b){ super.onCreate(b); prefs=getSharedPreferences("neckfit",MODE_PRIVATE); seed(); buildShell(); showHome(); if(Build.VERSION.SDK_INT>=33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED) requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1); }

    void seed(){
        exercises.add(new Exercise("Chin Tuck",30,2,"Sit straight and gently pull chin backward like making a double chin. Do not force.","Improves forward head posture."));
        exercises.add(new Exercise("Shoulder Blade Squeeze",30,2,"Pull shoulders slightly backward and squeeze shoulder blades.","Opens chest and improves upper back posture."));
        exercises.add(new Exercise("Neck Side Stretch",20,2,"Tilt head gently to one side. Keep shoulder relaxed. Repeat both sides.","Reduces neck tightness."));
        exercises.add(new Exercise("Neck Flexion Stretch",20,2,"Slowly bring chin toward chest.","Relaxes back of neck."));
        exercises.add(new Exercise("Wall Posture Hold",45,2,"Stand against wall with head, upper back, and hips aligned.","Trains straight posture."));
        exercises.add(new Exercise("Chest Opener Stretch",30,2,"Clasp hands behind back and gently open chest.","Reduces rounded shoulders."));
        exercises.add(new Exercise("Phone Posture Practice",60,1,"Hold phone at eye level and keep neck straight.","Prevents tech neck."));
    }

    void buildShell(){
        root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(Color.rgb(248,248,248));
        content=new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL); content.setPadding(24,24,24,16);
        ScrollView scroll=new ScrollView(this); scroll.addView(content); root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        bottom=new LinearLayout(this); bottom.setGravity(Gravity.CENTER); bottom.setBackgroundColor(NAVY); root.addView(bottom,new LinearLayout.LayoutParams(-1,72));
        nav("Home", v->showHome()); nav("Exercises", v->showExercises()); nav("Workout", v->showWorkout()); nav("Progress", v->showProgress()); nav("Profile", v->showProfile());
        setContentView(root);
    }
    void nav(String t, View.OnClickListener l){ TextView v=new TextView(this); v.setText(t); v.setTextColor(Color.WHITE); v.setGravity(Gravity.CENTER); v.setTextSize(13); v.setOnClickListener(l); bottom.addView(v,new LinearLayout.LayoutParams(0,-1,1)); }
    TextView tv(String s,int sp,int color,int style){ TextView v=new TextView(this); v.setText(s); v.setTextSize(sp); v.setTextColor(color); v.setPadding(0,8,0,8); v.setTypeface(null,style); return v; }
    Button btn(String s){ Button b=new Button(this); b.setText(s); b.setTextColor(Color.WHITE); b.setBackgroundColor(SAFFRON); return b; }
    void clear(String title){ if(timer!=null)timer.cancel(); content.removeAllViews(); content.addView(tv(title,26,NAVY,1)); }
    LinearLayout card(){ LinearLayout c=new LinearLayout(this); c.setOrientation(LinearLayout.VERTICAL); c.setPadding(24,20,24,20); c.setBackgroundColor(Color.WHITE); LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,10,0,16); c.setLayoutParams(lp); return c; }

    void showHome(){ clear("🇮🇳 NeckFit"); content.addView(tv("Improve posture daily — not medical treatment.",16,NAVY,0)); LinearLayout c=card(); c.addView(tv("Today’s Routine",20,NAVY,1)); c.addView(tv(exercises.size()+" safe posture exercises • around 7–10 minutes",15,Color.DKGRAY,0)); Button start=btn("Start Workout"); start.setOnClickListener(v->showWorkout()); c.addView(start); content.addView(c); LinearLayout p=card(); p.addView(tv("Stats",20,NAVY,1)); p.addView(tv("Completed days: "+prefs.getInt("days",0),16,Color.DKGRAY,0)); p.addView(tv("Current streak: "+prefs.getInt("streak",0),16,Color.DKGRAY,0)); p.addView(tv("Safety: Stop if pain, dizziness, numbness or tingling happens.",14,GREEN,1)); content.addView(p); }

    void showExercises(){ clear("Exercise Library"); for(Exercise e: exercises){ LinearLayout c=card(); c.addView(tv("✅ "+e.name,19,NAVY,1)); c.addView(tv("Duration: "+e.seconds+" sec • Sets: "+e.sets,14,SAFFRON,1)); c.addView(tv("How: "+e.instructions,14,Color.DKGRAY,0)); c.addView(tv("Benefit: "+e.benefits,14,GREEN,0)); content.addView(c);} }

    void showWorkout(){ clear("Workout Mode"); current=0; showCurrentExercise(false); }
    void showCurrentExercise(boolean running){ content.removeAllViews(); content.addView(tv("Workout Mode",26,NAVY,1)); if(current>=exercises.size()){ finishWorkout(); return; } Exercise e=exercises.get(current); LinearLayout c=card(); c.addView(tv((current+1)+"/"+exercises.size()+"  "+e.name,21,NAVY,1)); c.addView(tv("Sets: "+e.sets+" • Time: "+e.seconds+" seconds",15,SAFFRON,1)); TextView time=tv(String.valueOf(running?secondsLeft:e.seconds),44,GREEN,1); time.setGravity(Gravity.CENTER); c.addView(time); c.addView(tv(e.instructions,15,Color.DKGRAY,0)); c.addView(tv(e.benefits,14,GREEN,0)); Button start=btn(running?"Running...":"Start Timer"); Button next=btn("Next Exercise"); start.setOnClickListener(v->{ if(timer!=null)timer.cancel(); secondsLeft=e.seconds; timer=new CountDownTimer(e.seconds*1000L,1000){ public void onTick(long ms){ secondsLeft=(int)(ms/1000)+1; time.setText(String.valueOf(secondsLeft)); } public void onFinish(){ time.setText("Done"); ((android.os.Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(200); }}; timer.start(); }); next.setOnClickListener(v->{ if(timer!=null)timer.cancel(); current++; showCurrentExercise(false); }); c.addView(start); c.addView(next); content.addView(c); }
    void finishWorkout(){ String today=new SimpleDateFormat("yyyy-MM-dd",Locale.US).format(new Date()); if(!today.equals(prefs.getString("last",""))){ prefs.edit().putString("last",today).putInt("days",prefs.getInt("days",0)+1).putInt("streak",prefs.getInt("streak",0)+1).putInt("total",prefs.getInt("total",0)+1).apply(); } content.addView(tv("🎉 Workout completed!",24,GREEN,1)); content.addView(tv("Great job. Keep your posture straight and avoid forceful neck movement.",16,NAVY,0)); }

    void showProgress(){ clear("Progress"); LinearLayout c=card(); int days=prefs.getInt("days",0), streak=prefs.getInt("streak",0), total=prefs.getInt("total",0); c.addView(tv("Total workouts: "+total,18,NAVY,1)); c.addView(tv("Completed days: "+days,16,Color.DKGRAY,0)); c.addView(tv("Current streak: "+streak,16,Color.DKGRAY,0)); c.addView(tv("Weekly progress: "+Math.min(100, days*14)+"%",16,GREEN,1)); content.addView(c); }
    void showProfile(){ clear("Profile & Reminder"); LinearLayout c=card(); EditText name=new EditText(this); name.setHint("Your name"); name.setText(prefs.getString("name","")); c.addView(name); Button save=btn("Save Name"); save.setOnClickListener(v->{ prefs.edit().putString("name",name.getText().toString()).apply(); Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();}); c.addView(save); Button rem=btn("Set Daily Reminder at 7:00 AM"); rem.setOnClickListener(v->setReminder()); c.addView(rem); Button reset=btn("Reset Progress"); reset.setOnClickListener(v->{ prefs.edit().clear().apply(); Toast.makeText(this,"Progress reset",Toast.LENGTH_SHORT).show(); showHome();}); c.addView(reset); content.addView(c); LinearLayout s=card(); s.addView(tv("Safety",20,NAVY,1)); s.addView(tv("Do not force neck movement. Stop if pain, dizziness, numbness, or tingling happens. Consult a doctor/physiotherapist if pain continues.",15,Color.DKGRAY,0)); content.addView(s); }
    void setReminder(){ Intent i=new Intent(this,ReminderReceiver.class); PendingIntent pi=PendingIntent.getBroadcast(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE); Calendar cal=Calendar.getInstance(); cal.set(Calendar.HOUR_OF_DAY,7); cal.set(Calendar.MINUTE,0); cal.set(Calendar.SECOND,0); if(cal.before(Calendar.getInstance())) cal.add(Calendar.DATE,1); AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE); am.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pi); Toast.makeText(this,"Reminder set for 7:00 AM",Toast.LENGTH_LONG).show(); }
}
