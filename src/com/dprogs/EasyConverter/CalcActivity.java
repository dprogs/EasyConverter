package com.dprogs.EasyConverter;

import com.pad.android.listener.*; // Leadbolt.AdListener;
import com.dprogs.EasyConverter.R;
import com.flurry.android.FlurryAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CalcActivity extends Activity implements AdListener
{
    public EditText CountEdit1, CountEdit2, MEdit1, MEdit2;
    public TextView DescriptionText, DescriptionName;
    public WebView wv;
    private static String TAG = "Calc screen";
    private static int SectorID;
    static CharSequence[] choiceList;
    static double decList[] = null;  
    static CharSequence[] descrList;
    private Context mContext;
    final int Selected[] = {0, 0};
    private int selectedUnit = 0; //?
    private int selectedDesc = 0;
    private int buffKey = 0; // add buffer value
    private int Active = 0;
    private boolean afterTextUsage = false;
	public String FormatString;
	Resources res;
	Dialog about;
	ListAdapter radio_adapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        //пишем в лог - checkpoint
    	Log.v(TAG, "Activity start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_screen);

        //
        res = getResources();
        //AdIsLoaded = false; //24.02.2012
        //Sector перенесен ниже после считывания selectedItem (saveInstance) 12/02/2012
        //
        String[] Category = res.getStringArray(R.array.Category_selection);
        //
        DescriptionText = (TextView) findViewById(R.id.DescriptionText);
        DescriptionName = (TextView) findViewById(R.id.DescriptionName);
        
        MEdit1 = (EditText) findViewById(R.id.LengthEdit1);
        MEdit2 = (EditText) findViewById(R.id.LengthEdit2);        
        //определяем поля ввода цифр
        CountEdit1 = (EditText) findViewById(R.id.LCountEdit1); 
        CountEdit2 = (EditText) findViewById(R.id.LCountEdit2); 
        
        CountEdit1.requestFocus();
        //
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
        //468 - <script type="text/javascript" src="http://ad.leadboltads.net/show_app_ad.js?section_id=588779451"></script>
        //640 - <script type="text/javascript" src="http://ad.leadboltads.net/show_app_ad.js?section_id=858890752"></script>
        //728 - <script type="text/javascript" src="http://ad.leadboltads.net/show_app_ad.js?section_id=761912925"></script>
        wv.loadData(html, "text/html", "utf-8");

        //сделать МАССИВ МАССИВОВ
		Log.v(TAG, "Selected Item = "+var_const.SelectedItem+" choiseList: "+choiceList);

	      if (savedInstanceState != null)
	      {
	       	var_const.SelectedItem = savedInstanceState.getInt("SelectedItem");
	       	Log.v(TAG, "Try to restore saved item = "+var_const.SelectedItem);
	      }	
	      SectorID = var_const.SelectedItem;			//сохранять??? (перенесен из=за ошибки, все равно нулю)
	    switch (var_const.SelectedItem)
		{
		case var_const.Length: 
		{
			choiceList = res.getStringArray(R.array.Category_Length);//var_const.choiceListLength;
			decList = var_const.decListLength;
			descrList = res.getStringArray(R.array.Description_Category_Length);
			setTitle(Category[0]);
			Selected[0] = 3;      //метр
			Selected[1] = 1;      //см
		}
		break;
		case var_const.Mass: 
		{
			choiceList = res.getStringArray(R.array.Category_Mass); 
			decList = var_const.decListMass;
			descrList = res.getStringArray(R.array.Description_Category_Mass);
			setTitle(Category[1]);
			Selected[0] = 2;      //кг
			Selected[1] = 3;      //тонна
		}
	    break;
		case var_const.Volume: 
		{
			choiceList = res.getStringArray(R.array.Category_Volume); 
			decList = var_const.decListVolume;
			descrList = res.getStringArray(R.array.Description_Category_Volume);
			setTitle(Category[2]);
			Selected[0] = 0;      //м.куб.
			Selected[1] = 3;      //литер
		}
	    break;
		case var_const.Square: 
		{
			choiceList = res.getStringArray(R.array.Category_Square); 
			decList = var_const.decListSquare;
			descrList = res.getStringArray(R.array.Description_Category_Square);
			setTitle(Category[3]);
			Selected[0] = 4;      //м.кв.
			Selected[1] = 5;      //м.кв.
		}
	    break;
		case var_const.Speed: 
		{
			choiceList = res.getStringArray(R.array.Category_Speed); 
			decList = var_const.decListSpeed;
			descrList = res.getStringArray(R.array.Description_Category_Speed);
			setTitle(Category[5]);
			Selected[0] = 0;      //км/ч
			Selected[1] = 1;      //м/с
		}
	    break;
		case var_const.Pressure: 
		{
			choiceList = res.getStringArray(R.array.Category_Pressure); 
			decList = var_const.decListPressure;
			descrList = res.getStringArray(R.array.Description_Category_Pressure);
			setTitle(Category[6]);
			Selected[0] = 0;      //бар
			Selected[1] = 1;      //паскаль
		}
	    break;
		case var_const.Power: 
		{
			choiceList = res.getStringArray(R.array.Category_Power); 
			decList = var_const.decListPower;
			descrList = res.getStringArray(R.array.Description_Category_Power);
			setTitle(Category[7]);
			Selected[0] = 0;      //ньютон
			Selected[1] = 2;      //кг-силы
		}
	    break;
		case var_const.Energy: 
		{
			choiceList = res.getStringArray(R.array.Category_Energy); 
			decList = var_const.decListEnergy;
			descrList = res.getStringArray(R.array.Description_Category_Energy);
			setTitle(Category[8]);
			Selected[0] = 0;      //джоули
			Selected[1] = 2;      //кВт/час
		}
	    break;
		case var_const.PWR: 
		{
			choiceList = res.getStringArray(R.array.Category_Pwr); 
			decList = var_const.decListPwr;
			descrList = res.getStringArray(R.array.Description_Category_Pwr);
			setTitle(Category[9]);
			Selected[0] = 0;      //ватт
			Selected[1] = 3;      //лошадиная сила
		}
	    break;
		case var_const.Time: 
		{
			choiceList = res.getStringArray(R.array.Category_Time); 
			decList = var_const.decListTime;
			descrList = res.getStringArray(R.array.Description_Category_Time);
			setTitle(Category[10]);
			Selected[0] = 0;      //секунды
			Selected[1] = 4;      //минуты
		}
	    break;
		case var_const.Freq: 
		{
			choiceList = res.getStringArray(R.array.Category_Freq); 
			decList = var_const.decListFreq;
			descrList = res.getStringArray(R.array.Description_Category_Freq);
			setTitle(Category[11]);
			Selected[0] = 2;      //герц
			Selected[1] = 4;      //мегаГерц
		}
	    break;
		case var_const.Angle: 
		{
			choiceList = res.getStringArray(R.array.Category_Angle); 
			decList = var_const.decListAngle;
			descrList = res.getStringArray(R.array.Description_Category_Angle);
			setTitle(Category[12]);
			Selected[0] = 1;      //градусы
			Selected[1] = 0;      //радианы
		}
	    break;
		case var_const.Data: 
		{
			choiceList = res.getStringArray(R.array.Category_Data); 
			decList = var_const.decListData;
			descrList = res.getStringArray(R.array.Description_Category_Data);
			//setTitle(this.getString(R.string.MainListData));
			setTitle(Category[14]);
			Selected[0] = 0;      //бит
			Selected[1] = 2;      //байт
		}
	    break;
		case var_const.Oil: 
		{
			choiceList = res.getStringArray(R.array.Category_Oil); 
			decList = var_const.decListOil;
			descrList = res.getStringArray(R.array.Description_Category_Oil);
			setTitle(Category[15]);
			Selected[0] = 0;      //баррель
			Selected[1] = 1;      //тонна
		}
	    break;
		case var_const.Gas: 
		{
			choiceList = res.getStringArray(R.array.Category_Gas); 
			decList = var_const.decListGas;
			descrList = res.getStringArray(R.array.Description_Category_Gas);
			setTitle(Category[16]);
			Selected[0] = 0;      //м.куб
			Selected[1] = 2;      //тонна СПГ
		}
	    break;
		}
	    selectedDesc = Selected[0];  //индекс описания, для первого запуска активити
	    TAG = var_const.TAG+ ": "+getTitle()+" screen";
    	FlurryAgent.logEvent(getTitle().toString());
    	Log.v(TAG, "after choiseList: "+choiceList+" decList0: "+decList[0]);
	/*  01-25 11:07:18.507: VERBOSE/eConeverter: Length screen(8041): Activity start
		01-25 11:07:18.687: DEBUG/dalvikvm(8041): GC_EXTERNAL_ALLOC freed 66K, 47% free 2881K/5379K, external 0K/0K, paused 57ms
		01-25 11:07:18.707: VERBOSE/eConeverter: Length screen(8041): Selected Item = 0 choiseList: null
		01-25 11:07:18.707: DEBUG/AndroidRuntime(8041): Shutting down VM
		01-25 11:07:18.707: WARN/dalvikvm(8041): threadid=1: thread exiting with uncaught exception (group=0x400205a0)
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041): FATAL EXCEPTION: main
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041): java.lang.RuntimeException: Unable to start activity ComponentInfo{com.android.EasyConverter/com.android.EasyConverter.CalcActivity}: java.lang.NullPointerException
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041):     at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:1830)
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041):     at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:1851)
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041):     at android.app.ActivityThread.access$1500(ActivityThread.java:132)
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041):     at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1038)
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041):     at android.os.Handler.dispatchMessage(Handler.java:99)
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041):     at android.os.Looper.loop(Looper.java:150)
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041):     at android.app.ActivityThread.main(ActivityThread.java:4277)
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041):     at java.lang.reflect.Method.invokeNative(Native Method)
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041):     at java.lang.reflect.Method.invoke(Method.java:507)
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041):     at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:839)
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041):     at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:597)
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041):     at dalvik.system.NativeStart.main(Native Method)
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041): Caused by: java.lang.NullPointerException
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041):     at com.android.EasyConverter.CalcActivity.onCreate(CalcActivity.java:178)
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041):     at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1072)
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041):     at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:1794)
		01-25 11:07:18.717: ERROR/AndroidRuntime(8041):     ... 11 more
        */
		MEdit1.setText(choiceList[Selected[0]]);   //<==опасно! нужно вернуть 0!
	    MEdit2.setText(choiceList[Selected[1]]);

    	CountEdit1.setFilters(new InputFilter[]{var_const.DigitFilter});
        CountEdit2.setFilters(new InputFilter[]{var_const.DigitFilter});
        //
        CountEdit2.setInputType(0);   //не редактируемый edit
        if (savedInstanceState != null)
        {
        	Log.v(TAG, "Restore instance state");
        	this.CountEdit1.setText(savedInstanceState.getString("Value1"));
        	this.CountEdit2.setText(savedInstanceState.getString("Value2"));
        	//индексы списка величин
        	this.Selected[0] = savedInstanceState.getInt("ListItem1");
        	this.Selected[1] = savedInstanceState.getInt("ListItem2");
            selectedDesc = savedInstanceState.getInt("Description unit");  //номер unita - для индекса описания при смене активити
        	var_const.SelectedItem = savedInstanceState.getInt("SelectedItem");
        	
        	this.MEdit1.setText(choiceList[Selected[0]]);
        	this.MEdit2.setText(choiceList[Selected[1]]);
        	CountEdit1.setSelection(CountEdit1.getText().length());
    		//AdvPlace.clearFocus();
        }
        mContext = this; // to use all around this class
        //описание, перенесено из-за ошибки индексов
        DescriptionName.setText(" ["+choiceList[selectedDesc]+"]");  //замена selectedUnit на Selected[0]
    	DescriptionText.setText(descrList[selectedDesc]);             //замена selectedUnit на Selected[1]         

        //обработчик изменения текста: при первом же внесении цифры,
        //результат автоматически пересчитывается
        CountEdit1.addTextChangedListener(new TextWatcher()
        {
                public void  afterTextChanged (Editable s)
                { 
                   //защита с флагом об использовании обработчика
                   //во избежание цикла рекурсии и переполения стека StackOverflowError
                   if (afterTextUsage==false)
                   {  
                	 afterTextUsage=true;
                	 Log.v(TAG, "The numeric value 1 is changed");
            		 
                	 //1. Определение количества разделителя [.] в тексте
         			 if (s.toString().contains("."))
        			   {
        				  Log.v(TAG, "Text contains .");
        				  if (var_const.CharCountFilter('.', s.toString()) > 1)
        				    {
        				     Log.v(TAG, "CharCounter > 1");
        				     Log.v(TAG, "Text (before)"+s.toString());
        				     //удаляет последний введеный символ
        				     s.delete(s.toString().length()-1, s.toString().length());
        				     Log.v(TAG, "Text (after) "+s.toString());
                       	     Log.v(TAG, "onTextChanged. Changed text is ["+s+"]");
        				    }
        			   }
                	 //2. Определение максимально допустимой длины
         			 if (s.length() > 12)
  				       s.delete(s.toString().length()-1, s.toString().length());

         			 var_const.iCount(mContext, CountEdit1, CountEdit2, Selected[0], Selected[1], SectorID);
                     Log.v(TAG, "C1: "+CountEdit1.toString()+" C2: "+CountEdit2.toString()+" S[0]: "+Selected[0]+" S[1]: "+Selected[1]+" Sector: "+SectorID);
                     afterTextUsage=false;                    
                   }
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

        //обработчик изменения текста: при первом же внесении цифры,
        //результат автоматически пересчитывается
       CountEdit2.addTextChangedListener(new TextWatcher()
        {
                public void  afterTextChanged (Editable s)
                { 
                    //защита с флагом об использовании обработчика
                    //во избежание цикла рекурсии и переполения стека StackOverflowError
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
        
        MEdit1.setOnFocusChangeListener(new View.OnFocusChangeListener() 
        {
    	    @Override
    	    public void onFocusChange(View v, boolean hasFocus) 
    	    {
    	        if (hasFocus) 
    	        {
                    Log.v(TAG, "Popup menu 1 is called, softKeyboard hiding");
    	            hideSoftKeyboard(v);
    	        }
    	    }
    	});
        
        MEdit2.setOnFocusChangeListener(new View.OnFocusChangeListener() 
        {
    	    @Override
    	    public void onFocusChange(View v, boolean hasFocus) 
    	    {
    	        if (hasFocus) //если EditText имеет фокус
    	        {
                    Log.v(TAG, "Popup menu 2 is called, softKeyboard hiding");
    	            hideSoftKeyboard(v);
    	        }
    	    }
    	});
        
        CountEdit2.setOnLongClickListener(new View.OnLongClickListener() 
        {
        	@Override
        	public boolean onLongClick(View v)
        	{
        		showClipboardMenu(v,  CountEdit2.getText().toString());
        		Log.v(TAG, "Long click: show clipboard menu");
        		// TODO Auto-generated method stub
				return true;        	
        	}});
        		
        CountEdit2.setOnFocusChangeListener(new View.OnFocusChangeListener() 
        {
    	    @Override
    	    public void onFocusChange(View v, boolean hasFocus) 
    	    {
    	        if (hasFocus) //если EditText имеет фокус
    	        {
                    Log.v(TAG, "Popup menu 2 is called, softKeyboard hiding");
    	            hideSoftKeyboard(v);
    	        }
    	    }
    	});
        
        radio_adapter = new ArrayAdapter<String>
        (getApplicationContext(), R.layout.list_row) 
        {

            ViewHolder holder;
            Drawable tile;

            class ViewHolder 
            {
                ImageView icon;
                TextView title;
            }

            public View getView(int position, View convertView, ViewGroup parent) 
            {
                final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);

                if (convertView == null) 
                {
                    convertView = inflater.inflate(R.layout.list_row, null);

                    holder = new ViewHolder();
                    holder.icon = (ImageView) convertView.findViewById(R.id.row_icon);
                    holder.title = (TextView) convertView.findViewById(R.id.row_title);
                    convertView.setTag(holder);
                } 
                else 
                {
                    // view already defined, retrieve view holder
                    holder = (ViewHolder) convertView.getTag();
                }       

                tile = getResources().getDrawable(R.drawable.custom_radio_button); //this is an image from the drawables folder

                holder.title.setText(choiceList[position]);
                holder.icon.setImageDrawable(tile);

                return convertView;
            }
        };     };
        
    public void onSaveInstanceState(Bundle outState)
    {
        Log.v(TAG, "Saving instance state");
    	//числовые значения 1,2 
    	outState.putString("Value1", CountEdit1.getText().toString());
    	outState.putString("Value2", CountEdit2.getText().toString());
    	//значение списка (номер)
    	outState.putInt("ListItem1", Selected[0]);
    	outState.putInt("ListItem2", Selected[1]);
    	outState.putInt("SelectedItem", var_const.SelectedItem);
    	outState.putInt("Description unit", selectedDesc);
    }

 // Copy EditCopy text to the ClipBoard
    private void copyToClipBoard(String strToClip) 
    {
        ClipboardManager ClipMan = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipMan.setText(strToClip);
        Toast.makeText(getBaseContext(), res.getString(R.string.PopupMenuResultCopied), 5).show();
    }

    /*
    private void ifSoftKeyboard()
    {
		//определяем какой EditText вызван из View
    	//EditText MEdit;
		//MEdit = (EditText) v;
        //непосредственно сам код убирания виртуальной клавиатуры
		//InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		 if (getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_YES)
		 {
        	Toast.makeText(getBaseContext(), "KEYBOARD SHOWN", 5).show();
        }
        else
        	Toast.makeText(getBaseContext(), "KEYBOARD HIDE", 5).show();
    }
    */
    
    private void hideSoftKeyboard(View v)
    {
		//определяем какой EditText вызван из View
    	EditText MEdit;
		MEdit = (EditText) v;
        //непосредственно сам код убирания виртуальной клавиатуры
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(MEdit.getWindowToken(), 0);
		//непосредственно список выбора единицы измерения
        if (MEdit.equals(MEdit1))
        {
        	Log.v(TAG, "Hide softkey, MEdit1");
            selectedUnit = Selected[0];
            showSelectLengthMeasure(v);
            Active = 0;
        }
        if (MEdit.equals(MEdit2))
        {
        	Log.v(TAG, "Hide softkey, MEdit2");
            selectedUnit = Selected[1];
            showSelectLengthMeasure(v);
            Active = 1;
        }
        
    }

    @Override
    public void onResume()
    {
    	super.onResume();
        
        //считать настройки
    	getPrefs();
    	Log.v(TAG, "clear focus");
    	//AdvPlace.clearFocus();
        //CountEdit1.requestFocus();    	
    	//FlurryAgent.onPageView();
    	
        //Toast.makeText(getBaseContext(), "onResume(1) getPrefs --> ReceiversOn ="+ReceiversOn, 3).show();
        //Log.v(TAG, "onResume(). ReceiversOn = "+ReceiversOn);
    }
 
    //unblocking method for Adv.
    /*
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event)
    {
    	if (keyCode == KeyEvent.KEYCODE_BACK)
    	{
    		if(myController.onBackPressed())
    		{
    			Log.v(TAG, "myController back");
    			// ie ad serving is in progress and back function
    			// 	to previous ad window successful.
    			// add additional app specific code here for this
    			// 	scenario...
    		}
    		else
    		{
    			// any app specific code here for back button handling
    			// outside the ad serving process
    			Log.v(TAG, "myApp back");
    			CalcActivity.this.finish();
    		}
    		return true;
    // i.e tells activity action has been performed for the click
    	}
    return super.onKeyDown(keyCode, event);
    }
    */    

    private void getPrefs() 
    {

    	Log.v(TAG, "Load preferences..");
        // Получаем xml/preferences.xml настройки
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        var_const.DecimalNumber = Integer.parseInt(prefs.getString("eConv_Decimal", "4"));        
        var_const.CheckCutZero = prefs.getBoolean("eConv_CutZero", true);
        var_const.CheckVibro = prefs.getBoolean("eConv_Vibro", true);
        //добавлено 5.02.2012
        var_const.iCount(mContext, CountEdit1, CountEdit2, Selected[0], Selected[1], SectorID);
        Log.v(TAG, "Recount after loading prefs..");
        
    }
    
    public void ConvertClick(View v)
	{
	  int Buf = 0;
	  Buf = Selected[0];
	  //меняем местами в памяти последние величины из выбранных списков
	  Selected[0] = Selected[1];
	  Selected[1] = Buf;
	  //
	  MEdit1.setText(choiceList[Selected[0]]);
	  MEdit2.setText(choiceList[Selected[1]]);
	  Log.v(TAG, "CHECK D1="+CountEdit1.getText()+" D2="+CountEdit2.getText());
	  //Count(CountEdit1);
  	  
	  var_const.vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
      if (var_const.CheckVibro) var_const.vibrator.vibrate(100);
	  
      var_const.iCount(mContext, CountEdit1, CountEdit2, Selected[0], Selected[1], SectorID);
      Log.v(TAG, "afterTextUsage set FALSE (end of Count)");
      afterTextUsage=false;                    
	}
	
	public void Length1Click(View v)
	{
		Log.v(TAG, "Unit 1 select");
		
		//сохраненное значение списка для ВЕРХНЕЙ таблицы
		selectedUnit = Selected[0];
		showSelectLengthMeasure(v);
		Active = 0;
	}

	public void Length2Click(View v)
	{
		Log.v(TAG, "Unit 2 select");

		//сохраненное значение списка для НИЖНЕЙ таблицы
		selectedUnit = Selected[1];
		showSelectLengthMeasure(v);
		Active = 1;
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
	
	//список велечин
    private void showSelectLengthMeasure(final View v) 
    {
        Log.v(TAG, "show ButtonClick");
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(res.getString(R.string.PopupMenuHeader2));
        //инициализация buffKey сделана для того, чтобы если пользователь не выберет новый элемент
        //списка, при нажатии на "Принять" текущее значение сохранилось... selectedItem
        buffKey = selectedUnit;
        //
//        ListAdapter mAdapter = new SimpleCursorAdapter(getBaseContext(),
//                R.drawable.custom_radio_button,
//                null,
//                (String[]) choiceList,
//                new int[] { selectedUnit });        

      //builder.setAdapter(radio_adapter, null);
        builder.setSingleChoiceItems(choiceList, selectedUnit, new DialogInterface.OnClickListener() 
        {
           
            @Override
            public void onClick(DialogInterface dialog, int which) 
            {
            	//set to buffKey instead of selected
                //(when cancel not save to selected)
                buffKey = which;
            }
        })//.setView(CountEdit1)
        
        .setCancelable(false) //can not click back

        //положительная кнопка
        .setPositiveButton(res.getString(R.string.PopupMenuOKbutton),
                new DialogInterface.OnClickListener()
                {
                  @Override
                  public void onClick(DialogInterface dialog, int which) 
                  {
                    //set buff to selected
                    selectedUnit = buffKey;
                    Log.v(TAG,"Which value="+which);
                    Log.v(TAG,"Selected value="+buffKey);
                    Log.v(TAG, "selectedUnit="+selectedUnit+"descrList: "+descrList[0]);
                    Toast.makeText(mContext, mContext.getString(R.string.PopupSelect)+" "+choiceList[buffKey], Toast.LENGTH_SHORT).show();
					//
                    DescriptionName.setText(" ["+choiceList[selectedUnit]+"]");
                    DescriptionText.setText(descrList[selectedUnit]);                    
    	            selectedDesc = selectedUnit;  //номер unita - для индекса описания при смене активити
                    //
                    EditText Lmeasure;
					Lmeasure = (EditText) v;
                    Lmeasure.setText(choiceList[selectedUnit]);
                    Selected[Active] = selectedUnit;
                    //пересчитать по новым значениям
                    Log.v(TAG, "Try to recount by Count(v)");
                	//почему при вызове через Count(v) приложение крОшится..
              		//так даже правильней... мы считаем для 1-го 2-ое значение
        			var_const.iCount(mContext, CountEdit1, CountEdit2, Selected[0], Selected[1], SectorID);
                    Log.v(TAG, "afterTextUsage set FALSE (end of Count)");
                    afterTextUsage=false;                    
                  }
                }
        );
       
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    //String[] items = {"airplanes", "animals", "cars", "colors", "flowers", "letters", "monsters", "numbers", "shapes", "smileys", "sports", "stars" };

 // Instead of String[] items, Here you can also use ArrayList for your custom object..


 
 //работа с меню
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
      		FlurryAgent.logEvent("Read about");
          	//	диалог подтверждения
        	var_const.showAbout(CalcActivity.this);
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
		//myController.setUseLocation(uL)
		//AdvPlace.setVisibility(4);  //visible=0, invisible=4
		//Log.v(TAG, "Hide AdvPlace");
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
		//AdvPlace.setVisibility(4);  //visible=0, invisible=4
		FlurryAgent.logEvent("Ad request to the ad server has failed");
	}

	@Override
	public void onAdLoaded() {
		// TODO Auto-generated method stub
		// add app specific code for this event here...
		// called when an ad is successfully displayed on device			
        //ifSoftKeyboard();
		//FlurryAgent.logEvent("Ad is successfully displayed on device");
		//-->AdIsLoaded = true;
		//Log.v(TAG,"Ad is successfully displayed on device");
		//CountEdit1.requestFocus(0); // .requestFocus();
		//Log.v(TAG,"2...................");
		//AdvPlace.setVisibility(View.VISIBLE);


    	//CountEdit1.setSelection(CountEdit1.getText().length());
		//Log.v(TAG,"clear focus Ad");
		
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

