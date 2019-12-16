package com.filenanumi.sejongce.rss;

import android.app.*;
import android.content.*;
import android.os.*;
import android.webkit.*;
import android.widget.*;
import com.filenanumi.sejongce.rss.*;
import android.view.*;
import android.net.*;

public class RSS_ContentView extends Activity 
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contentview);
        
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.1f;
		
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        String writer = intent.getStringExtra("writer");
        String date = intent.getStringExtra("date");
        String subject = intent.getStringExtra("subject");
	//	String link = intent.getStringExtra("link");
        
        WebView wv = (WebView)findViewById(R.id.webView1);
        ((TextView)findViewById(R.id.content_date)).setText(date);
        ((TextView)findViewById(R.id.content_writer)).setText(writer);
        ((TextView)findViewById(R.id.content_subject)).setText(subject);
        	
        wv.loadDataWithBaseURL("", content, "text/html", "UTF-8", "");
    }
	

	public void onClick(View v)
	{
		Intent intent = getIntent();
		String link = intent.getStringExtra("link");
		Toast.makeText(getBaseContext(),link,2000).show();
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
	}
	
	@Override
	protected void onDestroy() {
	    RecycleUtils.recursiveRecycle(getWindow().getDecorView());
		System.gc();
		super.onDestroy();
	}
	
	
}
