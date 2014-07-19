package com.dprogs.EasyConverter;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dprogs.EasyConverter.list.EntryItem;
import com.dprogs.EasyConverter.list.Item;
import com.dprogs.EasyConverter.list.SectionItem;
import com.dprogs.EasyConverter.R;
import com.flurry.android.FlurryAgent;

public class Main extends ListActivity 
{
    //private static final int FFEEDD = 0;
	public static final String TAG = var_const.TAG;
	/** Called when the activity is first created. */
	TextView Selection;
    //private ListView list1;
	
	ArrayList<Item> items = new ArrayList<Item>();

	Dialog about;
	int myDisplay;

	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //setListAdapter(new IconAdapter());
        Log.v(TAG, "Dynamic menu list creation");
        int k;
        
        Resources res = getResources();
        String[] Category = res.getStringArray(R.array.Category_selection);
        String[] Header = res.getStringArray(R.array.Category_Header);
        
        //1
        items.add(new SectionItem(Header[0]));
        for (k=0; k < 13; k++)
           items.add(new EntryItem(Category[k], ""));
        
        items.add(new SectionItem(Header[1]));			//14
        //items.add(new EntryItem("Римские и арабские числа", ""));	//15
        items.add(new EntryItem(Category[13], ""));//16
        items.add(new EntryItem(Category[14], ""));			//17
        
        items.add(new SectionItem(Header[2]));			//18
        items.add(new EntryItem(Category[15], ""));						//19
        items.add(new EntryItem(Category[16], ""));						//20

        //items.add(new SectionItem("Финансы"));						//21
        //items.add(new EntryItem("Валюта", ""));						//22
        
        EntryAdapter adapter = new EntryAdapter(this, items);
        
        setListAdapter(adapter);

        Selection = (TextView) findViewById(R.id.selection);

        //MainLayout = (Layout)findViewByLayout(R.layout);

        //list1 = (ListView)findViewById(R.id.OptionsList);
        //drawable = mapPic.getDrawable(1);
        //.setImageDrawable(drawable);
       
        //1. Статический (ОБЩИЙ) фон
        //list1.setBackgroundResource(R.drawable.rounded_corner);
        //2. Выделение кликнутого объекта
        //list1.setSelector(R.drawable.rounded_corner);
        
        //list1.setCacheColorHint(FFEEDD);
        
        // С помощью метода setAdpater мы добавляем пункты в ListView.
     /**   list1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_arr));    
        
    	
 	    // пример установки обработчика короткого клика на элементе
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, // адаптер 
			   				   		View arg1, // виев в котором отображается элемент 
			   				   		int position, // номер элемента
			   				   		long id // ид элемента 
	                           		)
	      {
      		// наш код             
            ShowItem(position);		
	      }
        }); **/
	
    }
	
	//public void onListItemClick(ListView parent, View v, int position, long id)
	//{
		//Selection.setText(list_arr[position]);
	//	ShowItem(position);
	//}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
	{
    	
    	if(!items.get(position).isSection())
    	{
    		
    		EntryItem item = (EntryItem)items.get(position);
    		
    		ShowItem(position);
    		//Toast.makeText(this, "You clicked " + item.title , Toast.LENGTH_SHORT).show();
    		Log.v(TAG, "ListItem clicked: "+position);
    		
    	}
    	
    	super.onListItemClick(l, v, position, id);
    }
	
	/**class IconAdapter extends ArrayAdapter<String> 
	{
		IconAdapter() 
		{
		  super(Main.this, R.layout.row, R.id.label, list_arr);
		}
		
  		  public View getView(int position, View convertView, ViewGroup parent) 
		  {
		    View row = super.getView(position, convertView, parent);
		    ImageView icon=(ImageView)row.findViewById(R.id.icon);
		    //if (position <= 4)
		    switch (position)
		    {
		      case 5: icon.setImageResource(R.drawable.speed);
		      break;
		      case 1: icon.setImageResource(R.drawable.icon_weight_9);
		      break;
		      case 4: icon.setImageResource(R.drawable.temp_9);
		      break;
		    }
		    
		    //{
		    //if (list_arr[position].length() > 4) 
		    //{
		    //  icon.setImageResource(R.drawable.econverter);
		    //}
		    //else 
		    //{
		    //  icon.setImageResource(R.drawable.icon);
		    //}
		
		    return(row);
	      }
	}  //IconAdapter
	**/

	public class EntryAdapter extends ArrayAdapter<Item> 
	{

		private Context context;
		private ArrayList<Item> items;
		private LayoutInflater vi;

		public EntryAdapter(Context context,ArrayList<Item> items) 
		{
			super(context,0, items);
			//super(Main.this, R.layout.row, R.id.label, items);
			this.context = context;
			this.items = items;
			vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			View v = convertView;

			final Item i = items.get(position);
		    
		    if (i != null) 
		    {
				if(i.isSection())
				{
					SectionItem si = (SectionItem)i;
					v = vi.inflate(R.layout.list_item_section, null);

					v.setOnClickListener(null);
					v.setOnLongClickListener(null);
					v.setLongClickable(false);
					
					final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
					sectionView.setText(si.getTitle());
				}
				else
				{
					EntryItem ei = (EntryItem)i;
					v = vi.inflate(R.layout.row, null);
					final TextView title = (TextView)v.findViewById(R.id.label);
					// **
					LinearLayout vv = (LinearLayout)v.findViewById(R.id.selectionBg);
					// **
					//View row = super.getView(position, convertView, parent);
				    
					//Input icons code-part
					ImageView icon  = (ImageView)v.findViewById(R.id.icon);
					//ImageView icon2 = (ImageView)v.findViewById(R.id.icon2);
			    	Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
					//screen dimension [240, 320, 400, 480, 540, 600, 640, 720, 800, 960, 1024, 1280]
			    	
			    	DisplayMetrics metrics = new DisplayMetrics();    
			    	getWindowManager().getDefaultDisplay().getMetrics(metrics);    
			    	int screenDensity = metrics.densityDpi;  
			    	Log.v(TAG, "[width:"+display.getWidth()+";height:"+display.getHeight()+"], Density:"+screenDensity);	
			    	
//			    	if (display.getWidth() <= 360)
//			    		{
//			    		 if ((screenDensity == 120) & (display.getOrientation()==1))
//			    		 	{
// 			    		        vv.setBackgroundResource(R.drawable.menu_item_line_600);
//			    		        Log.v(TAG, "Select 480**");
//			    		 	}
//			    		 else
//			    		 {
//			    			 	vv.setBackgroundResource(R.drawable.menu_item_slide_b2);
//			    			 	Log.v(TAG, "Select 320");
//			    		 }
//			    		}
//			    	
//			    	if ((display.getWidth() > 360) & (display.getWidth() <= 480))
//				        {
//			    		 if (screenDensity == 240)
//			    		 	{
//			    			 	vv.setBackgroundResource(R.drawable.menu_item_slide_b2);
//			    			 	Log.v(TAG, "Select **320");
//			    		 	}
//			    		 else
//			    		 	{
//			    			 	vv.setBackgroundResource(R.drawable.menu_item_line_480);
//			    			 	Log.v(TAG, "Select 480");
//			    		 	}
//				        }
//					
//			    	if ((display.getWidth() > 480) & (display.getWidth() <= 640))
//				        {
//			    		vv.setBackgroundResource(R.drawable.menu_item_line_600);
//			    		Log.v(TAG, "Select 640");
//				        }
//
//			    	if ((display.getWidth() > 640) & (display.getWidth() <= 840))
//				        {
//			    			vv.setBackgroundResource(R.drawable.menu_item_line_800);
//			    			Log.v(TAG, "Select 800. Current w="+display.getWidth());
//				        }
//			    	if (display.getWidth() == 854)
//			    	{
//     	    		  vv.setBackgroundResource(R.drawable.menu_item_line_854);
//  	    			  Log.v(TAG, "Select 854");
//			    	}
//			    	  
//			    	if ((display.getWidth() > 854) & (display.getWidth() <= 1280))
//				        {
//			    		vv.setBackgroundResource(R.drawable.menu_item_line_1024);//1024);
//			    		Log.v(TAG, "Select 1024");
//				        }

					switch (position)
				    {
				      case 1: icon.setImageResource(R.drawable.length);
				      break;

				      case 2: icon.setImageResource(R.drawable.mass);
				      break;

				      case 3: icon.setImageResource(R.drawable.volume);
				      break;

				      case 4: icon.setImageResource(R.drawable.square);
				      break;

				      case 5: icon.setImageResource(R.drawable.temp);
				      break;

				      case 6: icon.setImageResource(R.drawable.speed);
				    		  //icon2.setImageResource(R.drawable.temp_9); НО ВСЕ ЖЕ ПОЧЕМУ ТУТ ОШИБКА??? 8.11.2011
				      break;

				      case 7: icon.setImageResource(R.drawable.pressure);
				      break;
				      case 8: icon.setImageResource(R.drawable.power);
				      break;
				      case 9: icon.setImageResource(R.drawable.energy);
				      break;
				      case 10: icon.setImageResource(R.drawable.pwr);
				      break;
				      case 11: icon.setImageResource(R.drawable.time);
				      break;
				      case 12: icon.setImageResource(R.drawable.freq);
				      break;
				      case 13: icon.setImageResource(R.drawable.item_13_angel_72x);
				      break;
				      case 15: icon.setImageResource(R.drawable.bin_hex);
				      break;
				      case 16: icon.setImageResource(R.drawable.data);
				      break;
				      case 18: icon.setImageResource(R.drawable.oil);
				      break;
				      case 19: icon.setImageResource(R.drawable.gas);
				      break;
				      case 21: icon.setImageResource(R.drawable.item_18_money_72x);
				      break;
				    }
					// ***
				    
				    //final TextView subtitle = (TextView)v.findViewById(R.id.list_item_entry_summary);
					
					if (title != null) 
						title.setText(ei.title);
					//if(subtitle != null)
						//subtitle.setText(ei.subtitle);
				}
			}
			return v;
		}

	}

	public void ShowItem(int Item)
	{
    	int Mode = 1;
		Log.v(TAG, "Item selected = "+Item);
		//делаем универсальный идентификатор темы
    	var_const.SelectedItem = Item;
    	//для универсальной активити
    	if (Mode==1)
    	{
    		if ((var_const.SelectedItem!=var_const.Temp) & (var_const.SelectedItem!=var_const.BINHEX))
    			{
    				Log.d(TAG, "Calc activity");
    				Intent newCalcScreen = new Intent(this, CalcActivity.class);
    				startActivity(newCalcScreen);
    			}
    		else
    		switch (var_const.SelectedItem)
    		{
    		case var_const.BINHEX: 
    		{
	    		Log.d(TAG, "BINHEX activity");
	    		Intent newBINHEX = new Intent(this, CountSystemActivity.class);
		    	startActivity(newBINHEX);
    		}
    		break;
    		case var_const.Temp: 
    		{
    			Log.d(TAG, "Temperature activity");
    			Intent newTemp = new Intent(this, TempActivity.class);
    			startActivity(newTemp);
    		}
    		break;
    		}
    	}
		
	}

    @Override
    public void onStart()
    {
    	super.onStart();
    	FlurryAgent.setReportLocation(true);
    	FlurryAgent.onStartSession(this, var_const.API_Key_flurry);
      	FlurryAgent.logEvent("New session");
      	FlurryAgent.setUserId(FlurryAgent.getPhoneId());
    }

    @Override
    public void onResume()
    {
    	super.onResume();
        
        //считать настройки
    	getPrefs();
    	FlurryAgent.onPageView();
    	
    }
    
    @Override
    public void onStop()
    {
    	super.onStop();
   	    FlurryAgent.onEndSession(this);
    }    
    
    private void getPrefs() 
    {

    	Log.v(TAG, "Load preferences..");
        // Получаем xml/preferences.xml настройки
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        var_const.DecimalNumber = Integer.parseInt(prefs.getString("eConv_Decimal", "4"));        
        var_const.CheckCutZero = prefs.getBoolean("eConv_CutZero", true);
        var_const.CheckVibro = prefs.getBoolean("eConv_Vibro", false);
    }
    
    public void showPreferences()
	{
    	Log.v(TAG, "Show preferences screen");
    	FlurryAgent.logEvent("Show preferences screen");
    	//новое задание (цель), класс Prefs (там описан экран настроек)
    	Intent i = new Intent(this,Preferences.class);
    	startActivity(i);
	}
	
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
      		Log.v(TAG, "ShowSettings button pressed");
      		showPreferences();		//вызываем функция показа экрана настроек		
      	}
      	return true;
      	case R.id.menu_about:
      	{
      	   FlurryAgent.logEvent("Read about");
           //	диалог подтверждения
           var_const.showAbout(Main.this);
      	}
      	return true;
      }
      return false;
    }
 }
