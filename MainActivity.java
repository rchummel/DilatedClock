package hummel.specialtopics.net;

import java.io.Console;
import java.util.Calendar;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends Activity {
	final double FEETINMETER = .3048;
	long init, now, time, millis, seconds, minutes, hours;
	
	Handler handler;
	
	
	Location oldloc; //old location
	
	double distancediff, actualspeed;
	
	double c=10, v=0;
	
	long dtime, dinit;//time for dilated clock, needs to be updated carefully
	long dmillis, dseconds, dminutes, dhours;//m/s/h for dilated
	
	long oldmillicount,gap;
	double fixrate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
       init = System.currentTimeMillis();
 
       handler = new Handler();

        final Calendar ca = Calendar.getInstance();
        
        final int curhours = ca.get(Calendar.HOUR);
        final int curminutes = ca.get(Calendar.MINUTE);
        final int curseconds = ca.get(Calendar.SECOND);
        final int curmillis = ca.get(Calendar.MILLISECOND);
      //  final int millis = (int) System.currentTimeMillis();
        
        init -= (curhours*3600000 + curminutes*60000+curseconds*1000+curmillis);
        
        
        final RadioButton metric = (RadioButton)findViewById(R.id.radioButton1);
        final RadioButton english = (RadioButton)findViewById(R.id.radioButton2);
        final CheckBox constantV = (CheckBox)findViewById(R.id.checkBox1);
        final TextView velocityunitbox  = (TextView)findViewById(R.id.TextViewUnit);
        final TextView lightspeedunitbox  = (TextView)findViewById(R.id.TextViewUnitC);
        final TextView hourClock  = (TextView)findViewById(R.id.textView0);
        final TextView colonClock = (TextView)findViewById(R.id.textView1);
        final TextView minuteClock  = (TextView)findViewById(R.id.textView2);
        final TextView secondClock  = (TextView)findViewById(R.id.textView3);
        final TextView milliClock  = (TextView)findViewById(R.id.textView4);
        final TextView hourDilate  = (TextView)findViewById(R.id.textView01);
        final TextView colonDilate = (TextView)findViewById(R.id.textView11);
        final TextView minuteDilate  = (TextView)findViewById(R.id.textView21);
        final TextView secondDilate  = (TextView)findViewById(R.id.textView31);
        final TextView milliDilate  = (TextView)findViewById(R.id.textView41);
        
        final TextView vlabel = (TextView)findViewById(R.id.Velocity_Viewer);
        
        final TextView clocklabel  = (TextView)findViewById(R.id.clocklabel);
        
        final EditText vbox = (EditText)findViewById(R.id.editTextv);
        final EditText cbox = (EditText)findViewById(R.id.editText1);
        
        final TextView TextViewV = (TextView)findViewById(R.id.TextViewV);
        final TextView TextViewC = (TextView)findViewById(R.id.TextViewC);
        
        final RadioButton engrishradio = (RadioButton)findViewById(R.id.radioButton2);
        final RadioButton metricradio = (RadioButton)findViewById(R.id.radioButton1);
        
        final Button resetButton = (Button) findViewById(R.id.backbutton);
/*
 yourView.setVisibility(View.GONE);
yourView.setVisibility(View.VISIBLE);
 */
        dinit=init;
        vbox.setVisibility(View.INVISIBLE);
        
        vlabel.setVisibility(View.VISIBLE);
        vlabel.setText(v+"");
        
        dtime = System.currentTimeMillis()-dinit;
        oldmillicount=dtime;
        
      
       //nope v=8.66;//fornow
        class SecondThread extends Thread {

        	  @Override
        	  public void run() {
        		  System.out.println("Beofre anythign in run");
  				// Acquire a reference to the system Location Manager
  				LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
  				System.out.println("Past LocationManager constructor");
  				// Define a listener that responds to location updates
  				LocationListener locationListener = new LocationListener() {
  				    public void onLocationChanged(Location location) {
  				    	System.out.println("Past onLocationChanged");
  				      // Called when a new location is found by the network location provider.
  				      //distancediff = (oldloc.distanceTo(location));
  				      actualspeed = location.getSpeed();
  				    	System.out.println("Past actualspeed calculation");
  				    }
  				    
  				    public void onStatusChanged(String provider, int status, Bundle extras) {}

  				    public void onProviderEnabled(String provider) {}

  				    public void onProviderDisabled(String provider) {}
  				  };
  				  System.out.println("before locationmanager requestlocationupdates");
  				// Register the listener with the Location Manager to receive location updates
  				//locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
  				  if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
  					    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
  				 //locationManager.requestLocationUpdates (LocationManager.NETWORK_PROVIDER, 0, 0, locationListener, null);
  				  System.out.println("after locationmanager requestlocationupdates");
        	  }
        	} 

        	
        	
        final Runnable updater = new Runnable(){
        	
			public void run() {
				
				new SecondThread().start();
				
				
				  
				
				/****************************************************
				//Below constantly updates the values for c and v
				if(constantV.isChecked() && vbox.getText().equals("")==false)
					v= Integer.parseInt(vbox.getText().toString());
				//else calculatev(); addtoTotal();updatecount(); then later divide total/count
					if (vbox.getText().equals("")==false)v=Integer.parseInt(vbox.getText().toString());
				//****************************************************
					//^ thsi crashed it too.
					*/
					//
				
				
				now=System.currentTimeMillis();
                time=now-init;
              
                millis = time%1000;
        	    seconds = time%60000/1000;
        	    minutes = time%3600000/60000;
        	    hours = time%(24*3600000)/3600000;
        	    
        	    gap=time-oldmillicount;
        	    if(gap>1000)
        	    {
        	    	oldmillicount=time;
	
        	    	
        	    	if (c>v)fixrate = Math.sqrt(1-(Math.pow(v,2)/Math.pow(c,2)));else fixrate=0;
        	    	dtime+=Math.round(gap*fixrate);
        	    }
        	    
        	    dmillis = dtime%1000;
        	    dseconds = dtime%60000/1000;
        	    dminutes = dtime%3600000/60000;
        	    dhours = dtime%(12*3600000)/3600000;
        	    
        	    
        	    
        	    if(hours==0)hours=12;
        	    if(dhours==0)dhours=12;
        	    
        	    String smillis = ""+millis, sseconds=""+seconds, sminutes=""+minutes, shours=""+hours;
        	    
        	    if (millis<10)
        	    	smillis="00"+smillis;
          	    else if (millis<100)
        	    	smillis="0" + smillis;
        	    
        	    if(seconds<10)
        	    	sseconds="0"+sseconds;
        	    
        	    if(minutes<10)
        	    	sminutes="0"+sminutes;
        	    
        	    if(hours<10)
        	    	shours="0"+shours;
        	    
        	    
        	    
        	    
        	    String dsmillis = ""+dmillis, dsseconds=""+dseconds, dsminutes=""+dminutes, dshours=""+dhours;
        	    
        	    if (dmillis<10)
        	    	dsmillis="00"+dsmillis;
          	    else if (dmillis<100)
        	    	dsmillis="0" + dsmillis;
        	    
        	    if(dseconds<10)
        	    	dsseconds="0"+dsseconds;
        	    
        	    if(dminutes<10)
        	    	dsminutes="0"+dsminutes;
        	    
        	    if(dhours<10)
        	    	dshours="0"+dshours;
        	    
        	    	
                milliDilate.setText(dsmillis);
                secondDilate.setText(dsseconds);
                minuteDilate.setText(dsminutes);
                hourDilate.setText(dshours);
                
             	
                milliClock.setText(smillis);
                secondClock.setText(sseconds);
                minuteClock.setText(sminutes);
               hourClock.setText(shours);
                
                handler.postDelayed(this,1);
                
				
			}
        };
        handler.post(updater);
       

        	constantV.setOnClickListener(new View.OnClickListener() {
			public void onClick(View va) {
				
				if (constantV.isChecked()){
						vbox.setVisibility(View.VISIBLE);
						TextViewV.setText("Set Velocity:");
						vbox.requestFocus();
						
						if (vbox.getText().toString().compareTo("")!=0){
							v = Integer.parseInt(vbox.getText().toString());
						}
				        vlabel.setVisibility(View.INVISIBLE);
						
						
						
						//vbox.setText("0");
				}
				else{
					vbox.setVisibility(View.INVISIBLE);
					vlabel.setVisibility(View.VISIBLE);
					v = actualspeed;//distancediff/1;//per 1 second
					
					//vbox.setText("");
					TextViewV.setText("Current Velocity:");
					cbox.requestFocus();
					}
				
				}
        	});
        	
        	english.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        		public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
    				//change the velocityunitbox and lighstpeedunit box to ft/s instead of m/s
        			
	        			if (engrishradio.isChecked())
	        			{
			    				velocityunitbox.setText("ft/s");
			    				lightspeedunitbox.setText("ft/s");
			    				c=c/FEETINMETER;
			    				cbox.setText(String.format("%.2f", c));
			    				v=v/FEETINMETER;
			    				
			    					if (constantV.isChecked())// && !vbox.getText().toString().equals(""))
				    				{
				    			
				    					vbox.setText(String.format("%.2f", v));
				    				}
	        			}
	        			//below else clause replaces other onclick
	        			else
	        			{
	        				velocityunitbox.setText("m/s");
		    				lightspeedunitbox.setText("m/s");
		    				
		    				c=c*FEETINMETER;
		    				cbox.setText(String.format("%.2f", c));
		    				v=v*FEETINMETER;
		    				
		    					if (constantV.isChecked())// && !vbox.getText().toString().equals(""))
			    				{
			    					
			    					vbox.setText(String.format("%.2f", v));
			    				}
	        			}
		    			
	        			
	        			
    				}
            });
        	
        	/*metric.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        		public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
    				//change the velocityunitbox and lightspeedunit box to m/s instead of ft/s
        			
        			
		    				velocityunitbox.setText("m/s");
		    				lightspeedunitbox.setText("m/s");
		    				
		    				c=c*FEETINMETER;
		    				cbox.setText("" + c);
		    				v=v*FEETINMETER;
		    				
		    					if (constantV.isChecked())// && !vbox.getText().toString().equals(""))
			    				{
			    					
			    					vbox.setText(""+v);
			    				}
		    					
		    					
		    				}
            });
        	Don't need these lines of code up here because the checkchanged must fire off once 
        	and this would be double covering it like not getting rid of Medicare becuase 
        	under the affordable care act seniors would be double covered. */ 
        	
    
        	
/************************************************************************************************
        	 vbox.addTextChangedListener(new TextWatcher() {
                 public void afterTextChanged(Editable s) {
                   //      clocklabel.setText(s);
                	 if (s.equals("") == false) v=Integer.parseInt(s.toString());
                 }
                 public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                         //XXX do something
                 }
                 public void onTextChanged(CharSequence s, int start, int before, int count) {
                         //XXX do something
                 }
 });        	
 \
 vbox.setOnEditorActionListener(new TextView.OnEditorActionListener() {

	public boolean onEditorAction(TextView va, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		
        if (actionId == EditorInfo.IME_ACTION_DONE) {

        	if (vbox.getText().equals("") == false) v=Integer.parseInt(vbox.getText().toString());

            return true;
        }
		
		return false;
	}
});

/************************************************************************************************/
        	
        	
vbox.setOnFocusChangeListener(new OnFocusChangeListener() {

    public void onFocusChange(View vw, boolean hasFocus) {
        if (hasFocus) {

        } else {
        	if (!vbox.getText().toString().equals("")) v=Double.parseDouble(vbox.getText().toString());
        	else vbox.setText(""+v);
        }
    }
});
        	
/*cbox.setOnEditorActionListener(new TextView.OnEditorActionListener() {

	public boolean onEditorAction(TextView va, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		
        if (actionId == EditorInfo.IME_ACTION_DONE) {

        	if (cbox.getText().equals("") == false) c=Integer.parseInt(cbox.getText().toString());
        	else cbox.setText(""+c);

            return true;
        }
		
		return false;
	}
});*/
cbox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
    public boolean onEditorAction(TextView va, int actionId, KeyEvent event) {
      //  if (actionId ==  EditorInfo.IME_ACTION_GO) {
        	if (cbox.getText().equals("") == false) c=Double.parseDouble(cbox.getText().toString());
        	else cbox.setText(""+c);
        //    return true;
     //   }
        return false;
    }
});

/*cbox.setOnFocusChangeListener(new OnFocusChangeListener() {

    public void onFocusChange(View vw, boolean hasFocus) {
        if (hasFocus) {

        } else {
        	if (!cbox.getText().toString().equals("")) c=Double.parseDouble(cbox.getText().toString());
        	else cbox.setText(""+c);
        }
    }
});
   */

		resetButton.setOnClickListener(new OnClickListener() {
		
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dtime=time;
			}
		});




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
