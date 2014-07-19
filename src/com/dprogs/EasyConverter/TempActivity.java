package com.dprogs.EasyConverter;

import java.math.BigDecimal;

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
import android.os.Vibrator;
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
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TempActivity extends Activity implements AdListener
{
    public EditText CountEdit1, CountEdit2, MEdit1, MEdit2;
    public TextView DescriptionText, DescriptionName;
    public WebView wv;
    private static final String TAG = ": Temp screen";
    private Context mContext;
    CharSequence[] choiceList;
    final int decList[]             = {0, 1, 2, 3, 4, 5, 6, 7};       //100
    static CharSequence[] descrList;
    final int Selected[] = {0, 0};
    private int selectedUnit = 0;
    private int selectedDesc = 0;
    private int buffKey = 0; // add buffer value
    private int Active = 0;
    private boolean afterTextUsage = false;
	private RelativeLayout AdvPlace;
	//private int myOrientation;
	public String FormatString;
	Resources res;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        //пишем в лог - checkpoint
    	Log.i(TAG, "Start Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_screen);
        res = getResources();
        DescriptionText = (TextView) findViewById(R.id.DescriptionText);
        DescriptionName = (TextView) findViewById(R.id.DescriptionName);
        //

        //определяем поля ввода цифр
        CountEdit1 = (EditText) findViewById(R.id.LCountEdit1); 
        CountEdit2 = (EditText) findViewById(R.id.LCountEdit2); 
        
        //CountEdit1.setFocusable(false);
        CountEdit1.requestFocus();

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


        //
        MEdit1 = (EditText) findViewById(R.id.LengthEdit1);
        MEdit2 = (EditText) findViewById(R.id.LengthEdit2);
        // ******* ЭТО НАДО ЧТОБ ВСПЛЫВАЛ СПИСОК, ПРИ ОЧИСТКЕ ФОКУСА CountEdit1 ***** ///
        MEdit1.setFocusable(false);
        MEdit2.setFocusable(false);
        // ***************************************************************************
        
        //сделать МАССИВ МАССИВОВ
		Log.v(TAG, "Selected Item = "+var_const.SelectedItem+" choiseList: "+choiceList);

		if (savedInstanceState != null)
	      {
	       	var_const.SelectedItem = savedInstanceState.getInt("selectedUnit");
	       	Log.v(TAG, "Try to restore saved item = "+var_const.SelectedItem);
	      }	
		
        String[] Category = res.getStringArray(R.array.Category_selection);
		setTitle(Category[4]);
    	//
		FlurryAgent.logEvent(getTitle().toString());

		choiceList = res.getStringArray(R.array.Category_Temp); 
		descrList = res.getStringArray(R.array.Description_Category_Temp);
		Selected[0] = 0;      //цельсий
		Selected[1] = 1;      //фаренгейт
        
    	MEdit1.setText(choiceList[Selected[0]]);
        MEdit2.setText(choiceList[Selected[1]]);
        
        CountEdit1.setFilters(new InputFilter[]{var_const.MinusDigitFilter});
        CountEdit2.setFilters(new InputFilter[]{var_const.MinusDigitFilter});

        CountEdit2.setInputType(0);
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
        	
        }
        //описание, перенесено из-за ошибки индексов
        DescriptionName.setText(" ["+choiceList[selectedDesc]+"]");  //замена selectedUnit на Selected[0]
    	DescriptionText.setText(descrList[selectedDesc]);             //замена selectedUnit на Selected[1]         

        mContext = this; // to use all around this class

        //обработчик изменения текста: при первом же внесении цифры,
        //результат автоматически пересчитывается
        
        CountEdit1.addTextChangedListener(new TextWatcher()
        {
                public void  afterTextChanged (Editable s)
                { 
                	//защита с флагом об использовании обработчика
                   //во избежание цикла рекурсии и переполения стека StackOverflowError
                    Log.v(TAG, "afterTextChanged::usage = "+afterTextUsage);
                    
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
                 	 //2. Определение количества [-] в тексте
          			 if (s.toString().contains("-"))
         			   {
         				  Log.v(TAG, "Text contains -");
         				  if ((var_const.CharCountFilter('-', s.toString()) > 1) || ((s.toString().charAt(s.toString().length()-1) == '-') && (s.toString().length() > 1)))
         				    {
         				     Log.v(TAG, "CharCounter > 1");
         				     Log.v(TAG, "Text (before)"+s.toString());
         				     //удаляет последний введеный символ
         				     s.delete(s.toString().length()-1, s.toString().length());
         				     Log.v(TAG, "Text (after) "+s.toString());
                        	     Log.v(TAG, "onTextChanged. Changed text is ["+s+"]");
         				    }
         			   }
                 	 //3. Определение максимально допустимой длины
          			 if (s.length() > 10)
   				       s.delete(s.toString().length()-1, s.toString().length());
                   Count(CountEdit1);
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
    	            hideSoftKeyboard(v);
    	            //1
    	            selectedUnit = Selected[0];
   	                showSelectLengthMeasure(v);
   	                Active = 0;
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
    	            hideSoftKeyboard(v);
    	            //2
    	            selectedUnit = Selected[1];
    	            showSelectLengthMeasure(v);
    	            Active = 1;
    	        }
    	    }
    	});

        CountEdit2.setOnLongClickListener(new View.OnLongClickListener() 
        {
        	@Override
        	public boolean onLongClick(View v)
        	{
        		showClipboardMenu(v,  CountEdit2.getText().toString());
        		Log.v(TAG, "Edit 2 looong click");
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
    };
    
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
    
    //unblocking method for Adv.
    /*
    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event)
    {
    	if (keyCode == KeyEvent.KEYCODE_BACK)
    	{
    		if(myController.onBackPressed())
    		{
    			// ie ad serving is in progress and back function
    			// 	to previous ad window successful.
    			// add additional app specific code here for this
    			// 	scenario...
    		}
    		else
    		{
    			// any app specific code here for back button handling
    			// outside the ad serving process
    			TempActivity.this.finish();
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
        
        Count(CountEdit1);
        Log.v(TAG, "Recount after loading prefs..");
    }

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

    private void hideSoftKeyboard(View v)
    {
		
    	//определяем какой EditText вызван из View
    	EditText MEdit;
		MEdit = (EditText) v;

		//непосредственно сам код убирания виртуальной клавиатуры
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(MEdit.getWindowToken(), 0);
		//непосредственно список выбора единицы измерения
     /*   if (MEdit.equals(MEdit1))
        {
        	//Toast.makeText(mContext, "MEdit1 last", 3).show();
            selectedUnit = Selected[0];
            showSelectLengthMeasure(v);
            Active = 0;
        }
        if (MEdit.equals(MEdit2))
        {
        	//Toast.makeText(mContext, "MEdit2 last", 3).show();
            selectedUnit = Selected[1];
            showSelectLengthMeasure(v);
            Active = 1;
        }  */
        
    }

    /*
    private void showSoftKeyboard(View v)
    {
		//определяем какой EditText вызван из View
    	//EditText MEdit;
		//MEdit = (EditText) v;
        //непосредственно сам код убирания виртуальной клавиатуры
        Toast.makeText(getBaseContext(), "Try to SHOW", 3).show();
    	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(CountEdit1, InputMethodManager.SHOW_FORCED);
    }
	*/
    
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
	  Log.d("CHECK", "D1="+CountEdit1.getText()+" D2="+CountEdit2.getText());
  	  
	  var_const.vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
      if (var_const.CheckVibro) var_const.vibrator.vibrate(100);
	  
      Count(CountEdit1);	
	}
	
	public void Length1Click(View v)
	{
		Log.i(TAG, "ClickState set is 1 and finish()");
		
		//сохраненное значение списка для ВЕРХНЕЙ таблицы
		selectedUnit = Selected[0];
		showSelectLengthMeasure(v);
		Active = 0;
	}

	public void Length2Click(View v)
	{
		Log.i(TAG, "ClickState set is 2 and finish()");

		//сохраненное значение списка для НИЖНЕЙ таблицы
		selectedUnit = Selected[1];
		showSelectLengthMeasure(v);
		Active = 1;
	}

	//список велечин
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

    private void showSelectLengthMeasure(final View v) 
    {
        Log.i(TAG, "show LengthDialog ButtonClick");
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(res.getString(R.string.PopupMenuHeader2));
        //инициализация buffKey сделана для того, чтобы если пользователь не выберет новый элемент
        //списка, при нажатии на "Принять" текущее значение сохранилось... selectedUnit
        buffKey = selectedUnit;
        //
        builder.setSingleChoiceItems(choiceList, selectedUnit, new DialogInterface.OnClickListener() 
        {
           
            @Override
            public void onClick(DialogInterface dialog, int which) 
            {
            	//set to buffKey instead of selected
                //(when cancel not save to selected)
                buffKey = which;
            }
        })
        
        .setCancelable(false) //can not click back

        //положительная кнопка
        .setPositiveButton(res.getString(R.string.PopupMenuOKbutton),
                new DialogInterface.OnClickListener()
                {
                  @Override
                  public void onClick(DialogInterface dialog, int which) 
                  {
                    Log.d(TAG,"Which value="+which);
                    Log.d(TAG,"Selected value="+buffKey);
                    Toast.makeText(mContext, mContext.getString(R.string.PopupSelect)+" "+choiceList[buffKey], Toast.LENGTH_SHORT).show();
                                       //set buff to selected
                                        selectedUnit = buffKey;
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
                    Log.i(TAG, "Selected[Active]="+selectedUnit+" Active="+Active);
                	//почему при вызове через Count(v) приложение крОшится..
              		//так даже правильней... мы считаем для 1-го 2-ое значение
                    Count(CountEdit1);
                    
                  }
                }
        );
        
        //отрицательная кнопка
        /**.setNegativeButton("Отмена",
                new DialogInterface.OnClickListener()
                {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                int which) {
                                        //Toast.makeText(
                                mContext,
                                "Cancel click",
                                Toast.LENGTH_SHORT
                                )
                                .show();
                                }
                }
        );**/
       
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    public void Count(View v)
    {
   	BigDecimal bd, bdRes;
    	//сделать проверку на наличие 0  и воообще цифровых значений
    	EditText CountEdit = (EditText) v;
 
    	double b1 = 0;
    	double C = 0;
    	double F=0; 
    	double K=0; 
    	double Ra=0;
    	double De=0;
    	double N=0;
    	double Re=0;
    	double Ro=0; 
    	
        	if (CountEdit.getText().toString().length() == 0) 
    		{
    		  //Toast.makeText(mContext, res.getString(R.string.PopupEmptyField), Toast.LENGTH_SHORT).show();
    		}
    		else
    		{
    				Log.d("void Count", "D1="+CountEdit.getText()+" S[0]="+Selected[0]+" S[1]="+Selected[1]);
        			
    				switch (Selected[0])
        			{
    				//Цельсий в 
        			case 0:
    				    {
    	        			try 
    	        			{  
    				    	  C = Double.parseDouble((CountEdit.getText().toString().replace(',', '.')));
    	        			}
    	    			    catch(NumberFormatException  dbException) 
    	    			    {  
    	    			      Log.e(TAG, dbException.toString()+" for C");  
    	    				  afterTextUsage=false;
    	    			      return;  
    	    			    }
    	        			
    				    	Log.d(TAG, "C = "+C);
    				    	switch (Selected[1])
    				    	{
    				    	//Цельсий
    				    	case 0: b1 = C; break;
    				    	//Фаренгейт
    				    	case 1: b1 =C*9/5+32; break;
    				    	//Кельвин
    				    	case 2: b1=C+273.15; break;
    				    	//Ранкин
    				    	case 3: b1=(C+273.15)*9/5; break;
    				    	//Денисле
    				    	case 4: b1=(100-C)*3/2; break;
    				    	//Ньютон
    				    	case 5: b1=C*33/100; break;
    				    	//Реюмюр
    				    	case 6: b1=C*4/5; break;
    				    	//Рёмер 
    				    	case 7: b1=C*21/40+7.5; break;
    				    	    				    	
    				    	}
    				    } //case 0
    				    break;
        				//Фаренгейт в ...
        			case 1:
    				    {
    				    
    				    try
    				    {
    				    	F = Double.parseDouble((CountEdit.getText().toString().replace(',', '.')));
    				    }
	    			    catch(NumberFormatException  dbException) 
	    			    {  
	    			      Log.e(TAG, dbException.toString()+" for F");  
	    				  afterTextUsage=false;
	    			      return;  
	    			    }
    	    				Log.d(TAG, "F = "+F);
    				    	switch (Selected[1])
    				    	{
    				    	//Цельсий
    				    	case 0: b1 =(F-32)*5/9; break;
    				    	//Фаренгейт
    				    	case 1: b1 = F; break;
    				    	//Кельвин
    				    	case 2: b1=(F+459.67)*5/9; break;
    				    	//Ранкин
    				    	case 3: b1=F+459.67; break;
    				    	//Денисле
    				    	case 4: b1=(212-F)*5/6; break;
    				    	//Ньютон
    				    	case 5: b1=(F-32)*11/60; break;
    				    	//Реюмюр
    				    	case 6: b1=(F-32)*4/9; break;
    				    	//Рёмер 
    				    	case 7: b1=(F-32)*7/24+7.5; break;
		    	    		} //switch selected[1]
    				    } //case 1
    				    break;
        				//Кельвин в..
        			case 2:
    				    {
    				    
    				    try
    				    {
    				    	K = Double.parseDouble((CountEdit.getText().toString().replace(',', '.')));
	        			}
	    			    catch(NumberFormatException  dbException) 
	    			    {  
	    			      Log.e(TAG, dbException.toString()+" for K");  
	    				  afterTextUsage=false;
	    			      return;  
	    			    }
    	    				Log.d(TAG, "K = "+K);
    				    	switch (Selected[1])
    				    	{
    				    	//Цельсий
    				    	case 0: b1 = K-273.15; break;
    				    	//Фаренгейт
    				    	case 1: b1 = K*9/5-459.67; break;
    				    	//Кельвин
    				    	case 2: b1= K; break;
    				    	//Ранкин
    				    	case 3: b1= K*9/5;; break;
    				    	//Денисле
    				    	case 4: b1=(373.15-K)*3/2; break;
    				    	//Ньютон
    				    	case 5: b1=(K-273.15)*33/100; break;
    				    	//Реамюр
    				    	case 6: b1=(K-273.15)*4/5; break;
    				    	//Рёмер 
    				    	case 7: b1=(K-273.15)*21/40+7.5; break;
		    	    		} //switch selected[1]
				    	
    				    } //case 2
    				    break;
        				//Ранкин в..
        			case 3:
				    {
				    try
				    {
				    	Ra = Double.parseDouble((CountEdit.getText().toString().replace(',', '.')));
        			}
    			    catch(NumberFormatException  dbException) 
    			    {  
    			      Log.e(TAG, dbException.toString()+" for R");  
    				  afterTextUsage=false;
    			      return;  
    			    }
	    				Log.d(TAG, "R = "+Ra);
				    	switch (Selected[1])
				    	{
				    	//Цельсий
				    	case 0: b1 =(Ra-491.67)*5/9; break;
				    	//Фаренгейт
				    	case 1: b1 = Ra-459.67; break;
				    	//Кельвин
				    	case 2: b1= Ra*5/9; break;
				    	//Ранкин
				    	case 3: b1= Ra; break;
				    	//Денисле
				    	case 4: b1=(671.67-Ra)*5/6; break;
				    	//Ньютон
				    	case 5: b1=(Ra-491.67)*11/60; break;
				    	//Реамюр
				    	case 6: b1=(Ra-491.67)*4/9; break;
				    	//Рёмер 
				    	case 7: b1=(Ra-491.67)*7/24+7.5; break;
	    	    		} //switch selected[1]
				    } //case 3
				    break;
    				//Денисле в..
        			case 4:
				    {
				    
				    try
				    {
				    	De = Double.parseDouble((CountEdit.getText().toString().replace(',', '.')));
        			}
    			    catch(NumberFormatException  dbException) 
    			    {  
    			      Log.e(TAG, dbException.toString()+" for De");  
    				  afterTextUsage=false;
    			      return;  
    			    }
	    				Log.d(TAG, "De = "+De);
				    	switch (Selected[1])
				    	{
				    	//Цельсий
				    	case 0: b1 = 100-De*2/3; break;   //при De=150, C=0
				    	//Фаренгейт
				    	case 1: b1 = 212-De*6/5; break;
				    	//Кельвин
				    	case 2: b1= 373.15-De*2/3; break;
				    	//Ранкин
				    	case 3: b1= 671.67-De*6/5; break;
				    	//Денисле
				    	case 4: b1=De; break;
				    	//Ньютон
				    	case 5: b1=33-De*11/50; break;
				    	//Реамюр
				    	case 6: b1=80-De*8/15; break;
				    	//Рёмер 
				    	case 7: b1=60-De*7/20; break;
	    	    		} //switch selected[1]
				    } //case 4
				    break;
    				//Ньютон в..
        			case 5:
				    {
				    
				    try
				    {
				    	N = Double.parseDouble((CountEdit.getText().toString().replace(',', '.')));
        			}
    			    catch(NumberFormatException  dbException) 
    			    {  
    			      Log.e(TAG, dbException.toString()+" for N");  
    				  afterTextUsage=false;
    			      return;  
    			    }
	    				Log.d(TAG, "N = "+N);
				    	switch (Selected[1])
				    	{
				    	//Цельсий
				    	case 0: b1 =N*100/33; break;
				    	//Фаренгейт
				    	case 1: b1 = N*60/11+32; break;
				    	//Кельвин
				    	case 2: b1= N*100/33+273.15; break;
				    	//Ранкин
				    	case 3: b1= N*60/11+491.67; break;
				    	//Денисле
				    	case 4: b1=(33-N)*50/11; break;
				    	//Ньютон
				    	case 5: b1=N; break;
				    	//Реамюр
				    	case 6: b1=N*80/33; break;
				    	//Рёмер 
				    	case 7: b1=N*35/22+7.5; break;
	    	    		} //switch selected[1]
				    } //case 5
				    break;
        			case 6:
				    {
				    
				    try
				    {
				    	Re = Double.parseDouble((CountEdit.getText().toString().replace(',', '.')));
        			}
    			    catch(NumberFormatException  dbException) 
    			    {  
    			      Log.e(TAG, dbException.toString()+" for Re");  
    				  afterTextUsage=false;
    			      return;  
    			    }
	    				Log.d(TAG, "Re = "+Re);
				    	switch (Selected[1])
				    	{
				    	//Цельсий
				    	case 0: b1 = Re*5/4; break;
				    	//Фаренгейт
				    	case 1: b1 = Re*9/4+32; break;
				    	//Кельвин
				    	case 2: b1= Re*5/4+273.15; break;
				    	//Ранкин
				    	case 3: b1= Re*9/4+491.67; break;
				    	//Денисле
				    	case 4: b1= (80-Re)*15/8; break;
				    	//Ньютон
				    	case 5: b1= Re*33/80; break;
				    	//Реамюр
				    	case 6: b1= Re; break;
				    	//Рёмер 
				    	case 7: b1= Re*21/32+7.5; break;
	    	    		} //switch selected[1]
				    } //case 6
				    break;

        			case 7:
				    {
				    try
				    {
				    	Ro = Double.parseDouble((CountEdit.getText().toString().replace(',', '.')));
        			}
    			    catch(NumberFormatException  dbException) 
    			    {  
    			      Log.e(TAG, dbException.toString()+" for Ro");  
    				  afterTextUsage=false;
    			      return;  
    			    }
	    				Log.d(TAG, "Ro = "+Ro);
				    	switch (Selected[1])
				    	{
				    	//Цельсий
				    	case 0: b1 = (Ro-7.5)*40/21; break;
				    	//Фаренгейт
				    	case 1: b1 = (Ro-7.5)*24/7+32; break;
				    	//Кельвин
				    	case 2: b1= (Ro-7.5)*40/21+273.15; break;
				    	//Ранкин
				    	case 3: b1= (Ro-7.5)*24/7+491.67; break;
				    	//Денисле
				    	case 4: b1= (60-Ro)*20/7;break;
				    	//Ньютон
				    	case 5: b1= (Ro-7.5)*22/35; break;
				    	//Реамюр
				    	case 6: b1= (Ro-7.5)*32/21; break;
				    	//Рёмер 
				    	case 7: b1= Ro; break;
	    	    		} //switch selected[1]
				    } //case 7
				    break;
      			} //switch selected[0]
        			
        			//приводим число к виду, с округлением до необходимого количества знаков после запятой
    				try 
        			{  
        				//Перевод в BigDecimal (окургление до 4х знаков разрядности после запятой)
        				bd = new BigDecimal(b1);
        				
        				bdRes = bd.setScale(var_const.DecimalNumber, 4);
        				//bdRes = (BigDecimal) Double.parseDouble("1.0-E7");
        				Log.v(TAG, "DecimalNumber "+var_const.DecimalNumber);
    			    } 
    			    catch(NumberFormatException  dbException) 
    			    {  
    			      Log.e(TAG, dbException.toString());  
    				      Log.v(TAG, "afterTextUsage set FALSE (NumberFormatException)");
    				  //afterTextUsage=false;
    			      return;  
    			    }

    			    Log.d("eConverter", "Before FORMAT STRING 11, b1="+Double.toString(b1));

        			FormatString = String.format("%.10f", bdRes).toString().replace(',', '.');//bdRes.toString(); //Double.toString(b1);//tring.format("%.04f", b1);  //<----????
        			
        			//Format
        			Log.d(TAG, "before CutZero, FormatString ="+FormatString);
        			if (var_const.CheckCutZero)
        			  FormatString = var_const.CutZ(FormatString);
        			Log.d(TAG, "before CutZero, FormatString ="+FormatString);
        			afterTextUsage = true; //принудительно, по неизвестной причине!!!!!!
        			//if (CountEdit.equals(CountEdit1)) 
        			//  {
        				CountEdit2.setText(FormatString);
        				Log.i("Temp... ","Count1 is called.. "+" b1="+b1+ " C="+C);
        			//  }
        			//else
        			/*  {
        				CountEdit1.setText(FormatString);
        				Log.i("Temp... ","Count2 is called.. "+" b1="+b1+ " C="+C);
        			  } */
        			Log.i(TAG, "Sel[1]="+Selected[1]+" Sel[0]="+Selected[0]+" b1="+b1);
    		} //if..else 	
    //снимаем флаг
    afterTextUsage=false;
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
        	var_const.showAbout(TempActivity.this);
      	}
      	return true;
      }
      return false;
    }

    @Override
    public void onDestroy()
    {
    	//leadbolt finish
        
    	/*
    	if (myController != null) 
    	{
    		myController.destroyAd();
    	}
    	else
    	{
    	}
    	*/
		
    	//myController.destroyAd();
    		
    		
    	super.onDestroy();
    }

	@Override
	public void onAdClicked() {
		// TODO Auto-generated method stub
		// add app specific code for this event here...
		// called when an ad is clicked by user		
		FlurryAgent.logEvent("Ad is clicked by user");
		//if (myController != null)
		//{
		//AdvPlace.requestFocus();
		//}
		//CountEdit1.requestFocus();

		AdvPlace.setVisibility(4);  //visible=0, invisible=4
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

        CountEdit1.requestFocus();  // СОХРАНЯЕМ КУРСОР В ПОЛЕ ВВОДА

    	FlurryAgent.logEvent("Ad is successfully displayed on device");
		//ПРИНУДИТЕЛЬНЫЙ ВОЗВРАТ ЕСЛИ ТОЛЬКО ВЕРТИКАЛЬНО
	}

	@Override
	public void onAdProgress() {
		// TODO Auto-generated method stub
		// add app specific code for this event here...
		// call every x seconds while ad loading is in progress
		// x must be set by calling myController.setOnProgressInterval(x);
		// this function is off by default		
        //CountEdit1.requestLayout();
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

