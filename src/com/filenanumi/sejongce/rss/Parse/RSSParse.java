package com.filenanumi.sejongce.rss.Parse;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.filenanumi.sejongce.rss.TimeUtility;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.widget.SimpleAdapter;
import android.view.*;
import android.app.*;
import android.widget.*;
import com.filenanumi.sejongce.rss.R;

public class RSSParse
{
    private ProgressDialog progressDialog;
    private HashMap<String, String> map;
    //private Thread trd;
    private ArrayList<String> contents,links;
    
    public RSSParse(ArrayList<String> contents,ArrayList<String> links)
    {
        this.contents = contents;
		this.links = links;
    }

    public void threadRssParse(final Context context, final PhoneSimpleAdapter sa, final ArrayList<HashMap<String, String>> list_board, final String url, final Button b3)
    {
		View parent = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
		final Handler mHandler = new Handler();
        try{
		list_board.clear();
		sa.notifyDataSetChanged();
		}catch(Exception e){}
		try{
        new Thread()
        {
                public void run(){
                    
				//	sa.notifyDataSetChanged();
                    try{
                        URL text = new URL(url);
                        
                        XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                        XmlPullParser parser = parserCreator.newPullParser();
                        
                        mHandler.post(new Runnable(){
                                public void run()
                                {
                                        progressDialog = ProgressDialog.show(context, "", "잠시만 기다려주세요!");
										progressDialog.setCancelable(true);
									//	progressDialog.setButton("cancel",);
										
                                }
                        });
                        
                        parser.setInput(text.openStream(), null);
						
                        int parserEvent = parser.getEventType();
                        String tag = "";
                        
                        boolean inTitle = false;    //��񿩺��Ǵ�
                        boolean inItem = false;     //�����ۺ��� �Ǵ�
                        boolean inWriter = false;   //�ۼ��� �Ǵ�
                        boolean inDate = false;     //�ۼ���
                        boolean inContent = false;  //����
                        boolean inLink = false;
						
                        contents.clear();      
						links.clear();
                        
                        do{
                            switch(parserEvent)
                            {
                                case XmlPullParser.TEXT:
                                    tag = parser.getName();
                                    
                                    if(inItem && inTitle)
                                    {
                                        map.put("subject", parser.getText());
                                    }
                                    
                                    if(inItem && inDate)
                                    {
                                        map.put("date", TimeUtility.getDateByString(parser.getText(), 0));
                                    }
                                    
                                    if(inItem && inWriter)
                                    {
                                        map.put("writer", parser.getText());
                                    }
                                    
                                    if(inItem && inContent)
                                    {
                                        contents.add( parser.getText() );
                                    }
									if(inItem && inLink)
									{
										links.add( parser.getText() );
									}
                                    break;
                                    
                                case XmlPullParser.END_TAG:
                                    tag = parser.getName();
                                    if(tag.compareTo("item") == 0)
                                    {
                                        inItem = false;
                                        list_board.add(map);
                                    }
                                    if(tag.compareTo("title") == 0)
                                    {
                                        inTitle = false;
                                    }
                                    if(tag.compareTo("dc:creator") == 0 || tag.compareTo("author") == 0)
                                    {
                                        inWriter = false;
                                    }
                                    if(tag.compareTo("pubDate") == 0)
                                    {
                                        inDate = false;
                                    }
                                    if(tag.compareTo("description") == 0)
                                    {
                                        inContent = false;
                                    }
									if(tag.compareTo("link") == 0)
									{
										inLink = false;
									}
                                    break;
                                    
                                case XmlPullParser.START_TAG:
                                    tag = parser.getName();
                                    if(tag.compareTo("item") == 0)
                                    {
                                        inItem = true;
                                        map = new HashMap<String, String>();
                                    }
                                    if(tag.compareTo("title") == 0)
                                    {
                                        inTitle = true;
                                    }
                                    if(tag.compareTo("dc:creator") == 0 || tag.compareTo("author") == 0)
                                    {
                                        inWriter = true;
                                    }
                                    if(tag.compareTo("pubDate") == 0)
                                    {
                                        inDate = true;
                                    }
                                    if(tag.compareTo("description") == 0)
                                    {
                                        inContent = true;
                                    }
									if(tag.compareTo("link") == 0)
									{
										inLink = true;
									}
                                    break;
                            }
                            
                            parserEvent = parser.next();
                            
                        }while(parserEvent != XmlPullParser.END_DOCUMENT);

                        mHandler.post(new Runnable(){
                                public void run()
                                {
                                	sa.notifyDataSetChanged();
                                    progressDialog.cancel();
									b3.setEnabled(true);
										
                                }
                        });
                        
                    }catch(Exception e){}
                }
        }.start();
		} catch(Exception e) {}
		sa.notifyDataSetChanged();
    }
	@Override
	public void onBackPressed() {
		
		progressDialog.dismiss();
		return;
	}
}
