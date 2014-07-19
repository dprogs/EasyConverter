package com.dprogs.EasyConverter;

import java.math.BigDecimal;

import com.dprogs.EasyConverter.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Vibrator;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//0202FF
public class var_const 
{
   //ProjectApiKey for EasyConverter
   public final static String API_Key_flurry = "3ZAUGEHYIZYLKCUE3G62";
   public final static String Leadbolt_Section_ID = "671778193";//"404621415";
   public static final String TAG = "eConeverter"; 
   // ***** *****
   //1-ANGEL
   final static CharSequence[] choiceListAngle= {"радиан",      "градус","минута",       "секунда",            "оборот","град","тысячная"};
   final static double decListAngle[]=          {57.29577951308,1,       0.0166666666666,0.0002777777777777778,360,     0.9,   0.05998868115019719};       //100
   final static int Angle = 13;
   //2-DATA
   final static CharSequence[] choiceListData = 
   {
    "бит","полубайт","байт","слово","двойное слово","Учетвернное слово","килобит","мегабит","гигабит", "терабит",
    "Килобайт (КБ)","Мегабайт (МБ)","Гигабайт (ГБ)","Терабайт (ТБ)",  "Петабайт (ПБ)",   "Эксабайт (ЭБ)"};
   final static double decListData[]      = 
   {1,    4,          8,    16,     32,             64,                 1024,     1048576,  1073741824,1099511627776.0,
    8192,           8388608,        8589934592.0,   8796093022208.0, 9007199254740990.0, 9223372036854780000.0};       //100
   final static int Data = 16;
   //3-ENERGY
   final static CharSequence[] choiceListEnergy = {"джоуль","ЭРГ", "килоВатт/час","электронвольт","калория",          "килограмм TNT","БТЕ",          "килограмм-сила м.", "фут-фунт силы"};
   final static double decListEnergy[]          = {1,       1.0E-7,3600000,       1.60217653E-19, 4.18679999999999986,4612000,        1055.0558525735,9.80665,             1.3558179483314003};       //100
   final static int Energy = 9;
   //4-FREQ
   final static CharSequence[] choiceListFreq = {"микрогерц","милигерц","герц","килогерц","мегагерц","гигагерц","терагерц"};
   final static double decListFreq[]          = {1.0E-6,     1.0E-3,    1,     1.0E+3,    1.0E+6,    1.0E+9,    1.0E+12};       //100
   final static int Freq = 12;
   //5-GAS
   final static CharSequence[] choiceListGas = {"метр³",  "фут³",     "тонна СПГ",  "литр", "американский галлон", "английский галлон"};
   final static double decListGas[]          = {1,        0.02832059, 1400,         0.001,  0.00378501,            0.00454545};
   final static int Gas = 19;
   //6-LENGTH
   final static CharSequence[] choiceListLength = {"мм",  "см" , "дм", "м" ,"км", "фут (си)", "дюйм",      "миля",   "ярд",  "кабельтов"};
   final static double decListLength[]          = {0.001, 0.01,  0.1,  1,   1000, 0.3048,     0.0254,      1609.344, 0.9144, 185.2};
   final static int Length = 1;
   //7-Mass
   final static CharSequence[] choiceListMass = {"мг",     "г" ,  "кг" , "тонна", "гран",       "унция",     "фунт",     "стоун",    "пуд",   "карат"};
   final static double decListMass[]          = {0.000001, 0.001, 1,     1000,    0.0000647989099,  0.02834952,  0.45359237, 6.35029318, 16.3805, 0.0002 };
   final static int Mass = 2;//0.0000647989099999999943340403326408
   //8-OIL
   final static CharSequence[] choiceListOil = {"баррель",  "тонна", "литр"};
   final static double decListOil[]          = {0.13642565, 1,       8.5807692307692307692307692307692e-4};
   final static int Oil = 18;
   //9-POWER
   final static CharSequence[] choiceListPwr = {"Ватт","килоВатт","эрга/сек","лошадиная сила","лошадиная сила англ.","калории в секунду","килокалории в час","БТЕ в секунду", "килограмм-силы на м/с"};
   final static double decListPwr[]          = {1,     1000,      1E-13,     735.49875,       745.69987,             4.1868,             1.163,              1055.0559,       9.80665};       //100
   final static int PWR = 10;

   //10-PRESSURE
   final static CharSequence[] choiceListPressure = {"бар", "Паскаль", "мм рт. столба", "физ. атм.", "техн. атм.", "psi",      "дюйм рт. столба", "дина на кв. см"};
   final static double decListPressure[]          = {1,     0.00001,   0.00133322,      1.01325,     0.980665,     0.06894757, 0.033863788,       0.000001};       //100
   final static int Pressure = 7;
   //11-PWR
   final static CharSequence[] choiceListPower = {"ньютон","дина","килограмм сила","фунт сила",    "кип"};
   final static double decListPower[]          = {1,       1.0E-5,9.80665,         4.4482216152605,4448.2216152604997};       //100
   final static int Power = 8;
   //12-SPEED
   final static CharSequence[] choiceListSpeed = {"километр в час", "метр в секунду", "миля в час",  "морская миля в час(узел)", "фут в секунду"};
   final static double decListSpeed[]          = {0.277777778,      1,                0.44704,       0.5144444444,               0.3048         };       //100
   final static int Speed = 6;
   //13-SQUARE
   final static CharSequence[] choiceListSquare = {"см²", "дюйм²",       "фут",        "ярд²",     "м²","акр" ,      "ирландский акр","шотландский акр", "гектар", "дунам", "миля кв.",         "сотка", "ар"};
   final static double decListSquare[]          = {0.0001,0.00064515999, 0.09290271813,0.83612736, 1 ,  4046.8564224,6553.8512396694, 5000,              10000,    1000,    2589988.11033600001, 100,    100};       //100
   final static int Square = 4;
   //14-TEMP
   final static CharSequence[] choiceListTemp = {"°C (Цельсий)","°F (Фаренгейт)","°K (Кельвин)","°R (Ранкин)","°De (Делисле)","°N (Ньютон)","°Re (Реамюр)","°Ro (Рёмер)"};
   final static int decListTemp[]             = {0, 1, 2, 3, 4, 5, 6, 7};       //100
   final static int Temp = 5;
   //15-TIME
   final static CharSequence[] choiceListTime = {"секунда","наносекунда","микросекунда","милисекунда","минута","час","сутки","неделя","месяц(30 дней)","год",   "век",   "тысячелетие"};
   final static double decListTime[]          = {1,        1.0E-9,       1.0E-6,        1.0E-3,       60,      3600, 86400,  604800,  2628000,         31536000,3.153E+9,3.153E+10};       //100
   final static int Time = 11;
   //16-VOLUME
   final static CharSequence[] choiceListVolume = {"метр³","сантиметр³","миллиметр³","литр","декалитр","миллилитр","амер. жидк. галлон","чайная ложка","столовая ложка"};
   final static double decListVolume[]          = {1,      0.000001,    0.00000001,  0.001, 0.01,      0.000001,   0.003785411784,      0.00000493,    0.00001479};       //100
   final static int Volume = 3;
   final static int BINHEX = 15;
   
   // ***** *****
   public static int DecimalNumber;
   public static boolean CheckCutZero;
   public static boolean CheckVibro;
   public static int SelectedItem = 0;
   public static Vibrator vibrator;
   static Dialog about;

   public static void showAbout(final Context myContext)
   {
 		//FlurryAgent.logEvent("Read about");
     	//	диалог подтверждения
     	  	about = new Dialog(myContext);

     	  	about.setContentView(R.layout.info_dialog);
     	  	about.setTitle(myContext.getText(R.string.AboutTitleStr));

           //set up button
           Button button = (Button) about.findViewById(R.id.info_button);
           
           button.setWidth(60);
           button.setOnClickListener(new OnClickListener() 
           {
           	public void onClick(View v) 
           	{
           		about.dismiss();
           	}
           });

     	  try {
     		   PackageManager manager = myContext.getPackageManager();
     		   PackageInfo info = manager.getPackageInfo(myContext.getPackageName(), 0);
     		   /**Toast.makeText(
     		     this,
     		     "PackageName = " + info.packageName + "\nVersionCode = "
     		       + info.versionCode + "\nVersionName = "
     		       + info.versionName + "\nPermissions = "+info.permissions, Toast.LENGTH_SHORT).show(); */
        		TextView text1 = (TextView) about.findViewById(R.id.info_prod_ver);
         	  	text1.setText(myContext.getText(R.string.AboutProdVerStr)+info.versionName);
     		  } 
     	      catch (Exception e) 
     	      {
     		   System.out.println(" Exception in onCreate() : e = " + e);
     	      }
     	  	
      		TextView text2 = (TextView) about.findViewById(R.id.info_prod);
     	  	text2.setText(myContext.getText(R.string.AboutProdNameStr));
     	  	//text2.setTextColor(getResources().getColor(R.color.PitbulOrange));
     	    
     	  	final ImageView image = (ImageView) about.findViewById(R.id.info_image);
           //image.setPressed(pressed)
           image.setClickable(true);
     	  	if (myContext.getText(R.string.LANG_DEF).toString().equals("RUS"))
     	  	  image.setImageResource(R.drawable.technol_ban_ru_320x96);
           else
       	  image.setImageResource(R.drawable.technol_ban_en_320x96);
     	  	
     	  	image.setOnTouchListener(new OnTouchListener()
     	  	{
     	  		@Override
				public boolean onTouch(View v, MotionEvent m) 
     	  		{
       	  		//обводим рамку
                   if (m.getAction() == MotionEvent.ACTION_DOWN)
       	  		   image.setBackgroundResource(R.drawable.item_border);
       	  		//стераем рамку
                   if (m.getAction() == MotionEvent.ACTION_UP)
        	  		   image.setBackgroundResource(0);
       	  			
					// TODO Auto-generated method stub
					return false;
				}
     	  		
     	  	} );
     	  	image.setOnClickListener(new OnClickListener() 
           {
           	public void onClick(View v) 
           	{
           	//ImageView CompanyImage = (ImageView) v;
           	//CompanyImage.setPressed(true);
           	var_const.vibrator = (Vibrator) myContext.getSystemService(Context.VIBRATOR_SERVICE);
               var_const.vibrator.vibrate(80);            	
				//FlurryAgent.logEvent("User open TECHNOLOGIES website");
           	//1. ОТКРЫВАЕМ САЙТ
           	Intent viewIntent = new Intent("android.intent.action.VIEW",
           	            			Uri.parse("http://www.technologies.org.ua"));
           	myContext.startActivity(viewIntent);
           	//тут сделать переход на сайт, зеленую светящуюся рамку вокург Image и может добавить вибро
           		Log.v(TAG, "Image pressed");
           	}
           });
     	  	about.show();
     		Log.v(TAG, "About..");
	   
   }
   
   public static void iCount(Context mContext, View v, View vc2, int Sel0, int Sel1, int Sector)
   {
   	//сделать проверку на наличие 0  и воообще цифровых значений
   	EditText CountEdit = (EditText) v;
   	EditText CountEdit2 = (EditText) vc2;
    //			    мм    см  м   км    фут (си)    дюйм       миля      ярд
	//decList[] = {0.01, 0.1, 1, 1000,  0.3048,     0.0254,    1609.344, 0.9144};

	double b1 = 0;
	String FormatString;
	BigDecimal bd, bdRes;
	Resources res;
   
	//vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    //if (CheckVibro) vibrator.vibrate(100);
    	res = mContext.getResources();
		if (CountEdit.getText().toString().length() == 0) 
		{
			//Toast.makeText(mContext, res.getString(R.string.PopupEmptyField), Toast.LENGTH_SHORT).show();
		}
		else
		{
				Log.d(TAG, "D1="+CountEdit.getText());

    			switch (Sector)
    			{
    				case Angle: b1 = decListAngle[Sel0] / decListAngle[Sel1]; 
    				break;
    				case Data: b1 = decListData[Sel0] / decListData[Sel1]; 
    				break;
    				case Energy: b1 = decListEnergy[Sel0] / decListEnergy[Sel1]; 
    				break;
    				case Freq: b1 = decListFreq[Sel0] / decListFreq[Sel1]; 
    				break;
    				case Gas: b1 = decListGas[Sel0] / decListGas[Sel1]; 
    				break;
    				case Length: b1 = decListLength[Sel0] / decListLength[Sel1]; 
    				break;
    				case Mass: b1 = decListMass[Sel0] / decListMass[Sel1]; 
    				break;
    				case Oil: b1 = decListOil[Sel0] / decListOil[Sel1]; 
    				break;
    				case Power: b1 = decListPower[Sel0] / decListPower[Sel1]; 
    				break;
    				case Pressure: b1 = decListPressure[Sel0] / decListPressure[Sel1]; 
    				break;
    				case PWR: b1 = decListPwr[Sel0] / decListPwr[Sel1]; 
    				break;
    				case Speed: b1 = decListSpeed[Sel0] / decListSpeed[Sel1]; 
    				break;
    				case Square: b1 = decListSquare[Sel0] / decListSquare[Sel1]; 
    				break;
    				case Temp: b1 = decListTemp[Sel0] / decListTemp[Sel1]; 
    				break;
    				case Time: b1 = decListTime[Sel0] / decListTime[Sel1]; 
    				break;
    				case Volume: b1 = decListVolume[Sel0] / decListVolume[Sel1]; 
    				break;
    			}
    			//приводим число к виду, с округлением до необходимого количества знаков после запятой
				try 
    			{  
    				b1 = b1 * Double.parseDouble((CountEdit.getText().toString().replace(',', '.')));
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
    			Log.d("eConverter", "Before FORMAT STRING 11, b1="+bdRes);
    			
    			FormatString = String.format("%.10f", bdRes).toString().replace(',', '.');//bdRes.toString(); //Double.toString(b1);//tring.format("%.04f", b1);  //<----????
    			
    			//Format
    			Log.d(TAG, "before CutZero, FormatString ="+FormatString);
    			if (var_const.CheckCutZero)
    			  FormatString = var_const.CutZ(FormatString);
    			Log.d(TAG, "before CutZero, FormatString ="+FormatString);
    			CountEdit2.setText(FormatString /*Double.toString(b1)*/);
    			Log.i(TAG, "Sel[1]="+Sel1+" Sel[0]="+Sel0+" b1(bdRes)="+bdRes); //+b1
			}

//снимаем флаг
//Log.v(TAG, "afterTextUsage set FALSE (end of Count)");
//afterTextUsage=false;
	   
   }

   //вырезаем нули в конце: до точки (и тогда ее включительно) или до любого числа не нуля
   public static String CutZ(String s)
   {
   	String temp = null;
   	int strLen;
   	//если цикла не будет сразу на выход
   	temp = s;
    //задача: убрать все "пустые" нули после запятой...	
   do {
   	 strLen = s.length(); 
   	 Log.v(TAG, "CutZero Before if.. [temp]="+temp+ " strLen="+strLen);
   	 //Если дробное число �? в конце НОЛЬ - отбрасываем его 
   	 if ((s.charAt(strLen-1) == '0') & (s.indexOf(".") >= 0))
   	 {
   	 	 temp = s.substring(0, strLen-1);
   		 Log.v(TAG, "ZERO_CUT indexOf"+s.indexOf(".")+" temp is "+temp);
   		 s = temp; //измененное
       	 strLen = s.length(); //новая длина (во избежание выхода за пределы индекса)
   		 //если точка - тоже удаляем
   		 if (s.charAt(strLen-1) == '.') 
   		   {
   			   //удаляем последний символ
   			   temp = s.substring(0, strLen-1);
  	    	       Log.v(TAG, "ZERO_CUT2 indexOf"+s.indexOf(".")+" temp is "+temp);
   			   s = temp;
  	        	   strLen = s.length(); //новая длина 
   		   }
   	 }
    //если запись формата E2, то выкидываем Е
   	/*if (s.indexOf("E") >= 0)
       {
		 //удаляем последний символ
		 temp = s.substring(0, strLen-1);
   	     Log.v(TAG, "E_CUT indexOf"+s.indexOf("E")+" temp is "+temp);
		 s = temp;
       	 strLen = s.length(); //новая длина 
       }*/

   	//Ц�?КЛ пока не будет в результате Е-шек или НУЛЕЙ (0) в конце, если число дробное
     } while (((s.charAt(strLen-1) == '0') & (s.indexOf(".") > -1)) /*| (s.indexOf("E") > -1)*/); 
		return temp;
   } //cutZ
   
   //считает сколько раз символ c встречается в строке str
   public static int CharCountFilter(char c, String str)
   {
	   int count = 0;
	   
	   for (int i = 0; i < str.length(); i++) 
       {
    	   if (str.charAt(i) == c)
    	   {
    		   count++;
    	   }
       }
	   Log.v(TAG, "Char '"+c+"' (str = "+str+") count is "+count);
	   return count;
      
   }
  
   public static InputFilter DigitFilter = new InputFilter() 
   { 
     public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) 
     { 
        for (int i = start; i < end; i++) 
            { 
            String indS;
            indS = String.valueOf(source.charAt(i));

            //если не цифра
              if ((!Character.isDigit(source.charAt(i))) & ((!indS.equals("."))) ) 
                { 
                  Log.v(TAG, "Not is digit and not is point, ["+indS.toString()+"] pressed");
           	      return ""; 
                }

              Log.d(TAG, "source = "+source+" input = "+indS+ "length is "+source.length());
              //если . но уже есть в наборе . (добавлено 7.01.12)
              //if ((source.toString().contains(".")) & (source.length() == 1) & (indS.equals(".")))
              //{
              //    Log.v(TAG, "Point canceled, point");
              //	  return "";
              //}

              //откомментировано 07.12.2012
              /*if ((source.length() > 1) & indS.equals("."))
              {
                  if (source.toString().contains("."))
                  {  
            	    Log.v(TAG, "point cannot be typed twice");
           	        return "";
                  }  
                  //return "0."; 
              }*/

            	  Log.v(TAG, "["+indS.toString()+"] pressed");
            } 
            return null;
     }
   }; //DigitFilter        
   
   public static InputFilter MinusDigitFilter = new InputFilter() 
   { 
     public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) 
     { 
        for (int i = start; i < end; i++) 
            { 
            String indS;
            indS = String.valueOf(source.charAt(i));

            //если не цифра
              if ((!Character.isDigit(source.charAt(i))) & ((!indS.equals("."))) & ((!indS.equals("-"))) ) 
                { 
                  Log.v(TAG, "Not is digit and not is point, ["+indS.toString()+"] pressed");
           	      return ""; 
                }
              Log.v(TAG, "["+indS.toString()+"] pressed");
            } 
            return null;
     }
   }; //MinusDigitFilter

}