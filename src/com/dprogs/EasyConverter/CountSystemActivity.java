package com.dprogs.EasyConverter;

import com.pad.android.listener.*;
import com.dprogs.EasyConverter.R;
import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

public class CountSystemActivity extends Activity implements AdListener {
	
	public String TAG = var_const.TAG+": CountSystem screen";
    public EditText EditDEC, EditHEX, EditOCT, EditBIN;
    public WebView wv;
    private Context mContext;
    private boolean afterTextUsage = false;
	Resources res;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        //пишем в лог - checkpoint
    	Log.v(TAG, "Start Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bin_dec_hex_view);
        res = getResources();
        String[] Category = res.getStringArray(R.array.Category_selection);
		setTitle(Category[13]);
		//
		FlurryAgent.logEvent(getTitle().toString());

        //определяем поля ввода цифр
        EditDEC = (EditText) findViewById(R.id.CountSystemDEC); 
        EditHEX = (EditText) findViewById(R.id.CountSystemHEX); 
        EditOCT = (EditText) findViewById(R.id.CountSystemOCT); 
        EditBIN = (EditText) findViewById(R.id.CountSystemBIN); 
        
        mContext = this; // to use all around this class

        //Leadbolt WebBanner
        wv = (WebView) findViewById(R.id.webbanner);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setBackgroundColor(Color.TRANSPARENT);
        String html = "<html><body style='margin:0;padding:0;'><script type='text/javascript' src='http://ad.leadbolt.net/show_app_ad.js?section_id=238174962'></script></body></html>";
        //Определение размеров экрана
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;        
        //Другие размеры экрана
        if	(screenWidth >= 720) 
        	{
        		html = "<html><body style='margin:0;padding:0;'><script type='text/javascript' src='http://ad.leadbolt.net/show_app_ad.js?section_id=761912925'></script></body></html>";
        	}
        	else if(screenWidth >= 640) 
        	{
        		html = "<html><body style='margin:0;padding:0;'><script type='text/javascript' src='http://ad.leadbolt.net/show_app_ad.js?section_id=858890752'></script></body></html>";
        	}
        	else if(screenWidth >= 468) 
        	{
        		html = "<html><body style='margin:0;padding:0;'><script type='text/javascript' src='http://ad.leadbolt.net/show_app_ad.js?section_id=588779451'></script></body></html>";
        	}        
        wv.loadData(html, "text/html", "utf-8");
        // // // // // // // //
        
        //
        InputFilter BINfilter = new InputFilter() 
        { 
          public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) 
          { 
             for (int i = start; i < end; i++) 
                 { 
                   int temp; 
                   String indS;
                   indS = String.valueOf(source.charAt(i));
            	   if (!Character.isDigit(source.charAt(i))) 
                     { 
                         return ""; 
                     }
            	   else
            	     {
                		 temp = Integer.parseInt(indS);
              		     //BIN
                		 if (temp > 1)
              		    	return "";	 
            	     }
            	   
                 } 
                 return null;
          }
        };         

        InputFilter OCTfilter = new InputFilter() 
        { 
          public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) 
          { 
             for (int i = start; i < end; i++) 
                 { 
                   int temp; 
                   String indS;
                   indS = String.valueOf(source.charAt(i));
            	   //если не цифра
                   if (!Character.isDigit(source.charAt(i))) 
                     { 
                         return ""; 
                     }
            	   else
            	     {
                		 temp = Integer.parseInt(indS);
              		     //OCT
                		 if (temp > 7)
              		    	return "";	 
            	     }
            	   
                 } 
                 return null;
          }
        };         

        InputFilter DECfilter = new InputFilter() 
        { 
          public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) 
          { 
             for (int i = start; i < end; i++) 
                 { 
            	   //если не цифра
                   if (!Character.isDigit(source.charAt(i))) 
                     { 
                         return ""; 
                     }
                 } 
                 return null;
          }
        };         

        InputFilter HEXfilter = new InputFilter() 
        { 
          public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) 
          { 
             for (int i = start; i < end; i++) 
                 { 
                   String indS;
                   indS = String.valueOf(source.charAt(i));
            	   //если не цифра
                   if (!Character.isDigit(source.charAt(i))) 
                     { 
                       Log.v(TAG, "(HEX filter) IndS: "+indS); 
                	   if (! (indS.equals("a") | indS.equals("b")  | indS.equals("c")  | indS.equals("d")
                			   | indS.equals("e")  | indS.equals("f")) )
                        {
                    	    return ""; 
                        }
                     }
                 } 
                 return null;
          }
        };         

        //УСТАНОВКА ФИЛЬТРОВ ДЛЯ КАЖДОЙ СИСТЕМЫ ИСЧИСЛЕНИЯ 2,8,10,16
        EditBIN.setFilters(new InputFilter[]{BINfilter});
        EditOCT.setFilters(new InputFilter[]{OCTfilter});
        EditDEC.setFilters(new InputFilter[]{DECfilter});
        EditHEX.setFilters(new InputFilter[]{HEXfilter});

        //*** 10-ИЧНАЯ СИСТЕМА ИСЧИСЛЕНИЯ
        //*** обработчик изменения текста: при первом же внесении цифры,
        //*** результат автоматически пересчитывается
        EditDEC.addTextChangedListener(new TextWatcher()
        {
                public void  afterTextChanged (Editable s)
                { 
                   //защита с флагом об использовании обработчика
                   //во избежание цикла рекурсии и переполения стека StackOverflowError
                    if (afterTextUsage==false)
                    {  
                  	   //перевод OCT(8) в восьмиричное значение..
                 	   long i = 0;
                	   //Если длина больше нуля (если не пустое, защита от 1-го эксепшена)
                 	   if (EditDEC.getText().toString().length() > 0)
                	   {  
                		   //обработка исключений (на переполнение разрядности)
                		   try	
                		   {
                			   i = Long.parseLong(EditDEC.getText().toString());
                			   Log.v(TAG, "dec(int) parse: i="+i+" i(text)="+EditDEC.getText().toString());

                			   afterTextUsage=true;
                			   EditHEX.setText(Long.toHexString(i));
                			   EditOCT.setText(Long.toOctalString(i));
                			   EditBIN.setText(Long.toBinaryString(i));
                			   afterTextUsage=false;
                		   }
                		   //если словили ошибку
                		   catch (Exception e)
                		   {
                			   afterTextUsage=true;
                			   EditHEX.setText("1");
                			   EditOCT.setText("1");
                			   EditBIN.setText("1");
                			   //
                			   EditDEC.setText("1");
                			   //
                			   afterTextUsage=false;
                			   Log.v(TAG, "DEC calc. Parsing error: stack overflow");
                		   }  //catch
                	   }  //if
                  
                    }  //if.. afterTextUsage
                } 
                
                public void  beforeTextChanged  (CharSequence s, int start, int count, int after)
                { 
                	//do something
                } 

                public void  onTextChanged  (CharSequence s, int start, int before, 
                        int count) 
                { 
                	//do something
                }
        });

        //*** 16-ИРИЧНАЯ СИСТЕМА ИСЧИСЛЕНИЯ
        //*** обработчик изменения текста: при первом же внесении цифры,
        //*** результат автоматически пересчитывается
        EditHEX.addTextChangedListener(new TextWatcher()
        {
                public void  afterTextChanged (Editable s)
                { 
                    //защита с флагом об использовании обработчика
                    //во избежание цикла рекурсии и переполения стека StackOverflowError
                    if (afterTextUsage==false)
                    {  
                  	   //перевод OCT(8) в восьмиричное значение..
                 	   long i = 0;
                	   //Если длина больше нуля (если не пустое, защита от 1-го эксепшена)
                 	   if (EditHEX.getText().toString().length() > 0)
                	   {  
                		   //обработка исключений (на переполнение разрядности)
                		   try	
                		   {
                			   i = Long.parseLong(EditHEX.getText().toString(), 16);
                			   Log.v(TAG, "hex(int) parse: i="+i+" i(text)="+EditHEX.getText().toString());
                			   afterTextUsage=true;
                			   EditDEC.setText(Long.toString(i));
                			   EditOCT.setText(Long.toOctalString(i));
                			   EditBIN.setText(Long.toBinaryString(i));
                			   afterTextUsage=false;
                		   }
                		   //если словили ошибку
                		   catch (Exception e)
                		   {
                			   afterTextUsage=true;
                			   EditDEC.setText("1");
                			   EditOCT.setText("1");
                			   EditBIN.setText("1");
                			   //
                			   EditHEX.setText("1");
                			   //
                			   afterTextUsage=false;
                			   Log.v(TAG, "HEX calc. Parsing error: stack overflow");
                		   }  //catch
                	   }  //if
                  
                    }  //if.. afterTextUsage
                  
                } 
                
                public void  beforeTextChanged  (CharSequence s, int start, int count, int after)
                { 
                	//do something
                } 
                

                public void  onTextChanged  (CharSequence s, int start, int before, 
                        int count) 
                { 
                	//do something
                }
        });

        //*** 8-ИРИЧНАЯ СИСТЕМА ИСЧИСЛЕНИЯ
        //*** обработчик изменения текста: при первом же внесении цифры,
        //*** результат автоматически пересчитывается
        EditOCT.addTextChangedListener(new TextWatcher()
        {
                public void  afterTextChanged (Editable s)
                { 
                    //защита с флагом об использовании обработчика
                    //во избежание цикла рекурсии и переполения стека StackOverflowError
                    if (afterTextUsage==false)
                    {  
                  	   //перевод OCT(8) в восьмиричное значение..
                 	   long i = 0;
                	   //Если длина больше нуля (если не пустое, защита от 1-го эксепшена)
                 	   if (EditOCT.getText().toString().length() > 0)
                	   {  
                		   //обработка исключений (на переполнение разрядности)
                		   try	
                		   {
                			   i = Long.parseLong(EditOCT.getText().toString(), 8);
                			   Log.v(TAG, "oct(int) parse: i="+i+" i(text)="+EditOCT.getText().toString());

                			   afterTextUsage=true;
                			   EditHEX.setText(Long.toHexString(i));
                			   EditDEC.setText(Long.toString(i));
                			   EditBIN.setText(Long.toBinaryString(i));
                			   afterTextUsage=false;
                		   }
                		   //если словили ошибку
                		   catch (Exception e)
                		   {
                			   afterTextUsage=true;
                			   EditHEX.setText("1");
                			   EditDEC.setText("1");
                			   EditBIN.setText("1");
                			   //
                			   EditOCT.setText("1");
                			   //
                			   afterTextUsage=false;
                			   Log.v(TAG, "OCT calc. Parsing error: stack overflow");
                		   }  //catch
                	   }  //if
                  
                    }  //if.. afterTextUsage
                }  //public void
                
                public void  beforeTextChanged  (CharSequence s, int start, int count, int after)
                { 
                	//do something
                } 
                

                public void  onTextChanged  (CharSequence s, int start, int before, 
                        int count) 
                { 
                	//do something
                }
        });
       
        //*** 2-ИЧНАЯ СИСТЕМА ИСЧИСЛЕНИЯ
        //*** обработчик изменения текста: при первом же внесении цифры,
        //*** результат автоматически пересчитывается
        EditBIN.addTextChangedListener(new TextWatcher()
        {
                public void  afterTextChanged (Editable s)
                { 
                    //защита с флагом об использовании обработчика
                    //во избежание цикла рекурсии и переполения стека StackOverflowError
                  
                    if (afterTextUsage==false)
                    {  
                  	   //перевод OCT(8) в восьмиричное значение..
                 	   long i = 0;
                	   //Если длина больше нуля (если не пустое, защита от 1-го эксепшена)
                 	   if (EditBIN.getText().toString().length() > 0)
                	   {  
                		   //обработка исключений (на переполнение разрядности)
                		   try	
                		   {
                			   i = Long.parseLong(EditBIN.getText().toString(), 2);
                			   Log.v(TAG, "bin(int) parse: i="+i+" i(text)="+EditBIN.getText().toString());

                			   afterTextUsage=true;
                			   EditHEX.setText(Long.toHexString(i));
                			   EditDEC.setText(Long.toString(i));
                			   EditOCT.setText(Long.toOctalString(i));
                			   afterTextUsage=false;
                		   }
                		   //если словили ошибку
                		   catch (Exception e)
                		   {
                			   afterTextUsage=true;
                			   EditHEX.setText("1");
                			   EditDEC.setText("1");
                			   EditOCT.setText("1");
                			   //
                			   EditBIN.setText("1");
                			   //
                			   afterTextUsage=false;
                			   Log.v(TAG, "BIN calc. Parsing error: stack overflow");
                		   }  //catch
                	   }  //if
                  
                    }  //if.. afterTextUsage
                } 
                
                public void  beforeTextChanged  (CharSequence s, int start, int count, int after)
                { 
                	//do something
                } 

                public void  onTextChanged  (CharSequence s, int start, int before, 
                        int count) 
                { 
                	//do something
                }
        });

        // ***
        EditDEC.setOnFocusChangeListener(new View.OnFocusChangeListener() 
        {
    	    @Override
    	    public void onFocusChange(View v, boolean hasFocus) 
    	    {
    	        if (hasFocus) 
    	        {
                    EditDEC.setSelection(EditDEC.getText().length());
    	        }
    	    }
    	});
        EditHEX.setOnFocusChangeListener(new View.OnFocusChangeListener() 
        {
    	    @Override
    	    public void onFocusChange(View v, boolean hasFocus) 
    	    {
    	        if (hasFocus) 
    	        {
                    EditHEX.setSelection(EditHEX.getText().length());
    	        }
    	    }
    	});
        EditOCT.setOnFocusChangeListener(new View.OnFocusChangeListener() 
        {
    	    @Override
    	    public void onFocusChange(View v, boolean hasFocus) 
    	    {
    	        if (hasFocus) 
    	        {
                    EditOCT.setSelection(EditOCT.getText().length());
    	        }
    	    }
    	});
        EditBIN.setOnFocusChangeListener(new View.OnFocusChangeListener() 
        {
    	    @Override
    	    public void onFocusChange(View v, boolean hasFocus) 
    	    {
    	        if (hasFocus) 
    	        {
                    EditBIN.setSelection(EditBIN.getText().length());
    	        }
    	    }
    	});
        //***************
        EditBIN.setOnLongClickListener(new View.OnLongClickListener() 
        {
        	@Override
        	public boolean onLongClick(View v)
        	{
        		showClipboardMenu(v,  EditBIN.getText().toString());
        		Log.v(TAG, "BIN long click, call a clipboard menu");
        		// TODO Auto-generated method stub
				return true;        	
        	}});

        EditOCT.setOnLongClickListener(new View.OnLongClickListener() 
        {
        	@Override
        	public boolean onLongClick(View v)
        	{
        		showClipboardMenu(v,  EditOCT.getText().toString());
        		Log.v(TAG, "OCT long click, call a clipboard menu");
        		// TODO Auto-generated method stub
				return true;        	
        	}});

        EditDEC.setOnLongClickListener(new View.OnLongClickListener() 
        {
        	@Override
        	public boolean onLongClick(View v)
        	{
        		showClipboardMenu(v,  EditDEC.getText().toString());
        		Log.v(TAG, "DEC long click, call a clipboard menu");
        		// TODO Auto-generated method stub
				return true;        	
        	}});

        EditHEX.setOnLongClickListener(new View.OnLongClickListener() 
        {
        	@Override
        	public boolean onLongClick(View v)
        	{
        		showClipboardMenu(v,  EditHEX.getText().toString());
        		Log.v(TAG, "HEX long click, call a clipboard menu");
        		// TODO Auto-generated method stub
				return true;        	
        	}});
}

    // Copy EditCopy text to the ClipBoard
    private void copyToClipBoard(String strToClip) 
    {
        ClipboardManager ClipMan = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipMan.setText(strToClip);
        Toast.makeText(getBaseContext(), res.getString(R.string.PopupMenuResultCopied), 5).show();
    }

    private void showClipboardMenu(final View v, final String str) 
    {
        Log.v(TAG, "show ClipboardMenu");
        CharSequence[] menuList = {res.getString(R.string.PopupMenuText1)};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(res.getString(R.string.PopupMenuHeader));
        builder.setItems(menuList, new DialogInterface.OnClickListener() 
        {
           
            @Override
            public void onClick(DialogInterface dialog, int which) 
            {
                Log.v(TAG, "Clipboard parameters: which = "+which+" str = "+str);
            	if (which == 0) copyToClipBoard(str);
            }
        })
        
        .setCancelable(true); //can not click back

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onResume()
    {
    	super.onResume();
        
        //считать настройки
    	getPrefs();
    	//FlurryAgent.onPageView();
    	
        //Toast.makeText(getBaseContext(), "onResume(1) getPrefs --> ReceiversOn ="+ReceiversOn, 3).show();
        //Log.v(TAG, "onResume(). ReceiversOn = "+ReceiversOn);
    }
    
    private void getPrefs() 
    {

    	Log.v(TAG, "Load preferences..");
        // Получаем xml/preferences.xml настройки
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        var_const.DecimalNumber = Integer.parseInt(prefs.getString("eConv_Decimal", "4"));        
        var_const.CheckCutZero = prefs.getBoolean("eConv_CutZero", true);
        var_const.CheckVibro = prefs.getBoolean("eConv_Vibro", true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
      MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.menu, menu);
      return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) 
    {
      Log.v(TAG, "item = "+item.getItemId());
      switch (item.getItemId()) 
      {
      	case R.id.menu_settings:
      	{
        	Log.v(TAG, "Show preferences from calc screen");
        	//FlurryAgent.logEvent("Show preferences screen");
        	//ChangeOrientation = false;
        	//новое задание (цель), класс Prefs (там описан экран настроек)
        	Intent i = new Intent(this,Preferences.class);
        	startActivity(i);
      	}
      	return true;
      	case R.id.menu_about:
      	{
      		//FlurryAgent.logEvent("Read about");
          	//	диалог подтверждения
        	var_const.showAbout(CountSystemActivity.this);
      	}
      	return true;
      }
      return false;
    }

    @Override
    public void onDestroy()
    {
    	//leadbolt finish
    	//myController.destroyAd();
    	super.onDestroy();
    }

	@Override
	public void onAdClicked() {
		// TODO Auto-generated method stub
		// add app specific code for this event here...
		// called when an ad is clicked by user		
		FlurryAgent.logEvent("Ad is clicked by user");
		//wv.setVisibility(4);
		//AdvPlace.setVisibility(4);  //visible=0, invisible=4
		Log.v(TAG, "Hide AdvPlace");
	}

	@Override
	public void onAdClosed() {
		// TODO Auto-generated method stub
		// add app specific code for this event here...
		// called when the ad window is closed by user		
		FlurryAgent.logEvent("Ad window is closed by user");

	}

	@Override
	public void onAdCompleted() {
		// TODO Auto-generated method stub
		// add app specific code for this event here...
		// supported for App Unlocker template types only.
		// called when ad window is closed with successful conversion
		FlurryAgent.logEvent("Ad window is closed with successful conversion");
	}

	@Override
	public void onAdFailed() {
		// TODO Auto-generated method stub
		// add app specific code for this event here...
		// called when the ad request to the ad server has failed
		// e.g No Internet connection		
		FlurryAgent.logEvent("Ad request to the ad server has failed");
	}

	@Override
	public void onAdLoaded() {
		// TODO Auto-generated method stub
		// add app specific code for this event here...
		// called when an ad is successfully displayed on device			
		FlurryAgent.logEvent("Ad is successfully displayed on device");
		//AdvPlace.clearFocus();
	}

	@Override
	public void onAdProgress() {
		// TODO Auto-generated method stub
		// add app specific code for this event here...
		// call every x seconds while ad loading is in progress
		// x must be set by calling myController.setOnProgressInterval(x);
		// this function is off by default		
		FlurryAgent.logEvent("Ad loading is in progress...");
	}

	@Override
	public void onAdAlreadyCompleted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdHidden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdPaused() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdResumed() {
		// TODO Auto-generated method stub
		
	}
}
