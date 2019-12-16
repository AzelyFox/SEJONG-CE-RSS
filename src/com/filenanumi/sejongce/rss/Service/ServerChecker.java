package com.filenanumi.sejongce.rss.Service;

import android.app.*;
import android.os.*;
import java.util.*;
import android.content.*;
import java.text.*;
import android.widget.*;
import android.net.*;
import org.apache.http.params.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.*;
import java.io.*;
import org.apache.http.impl.client.*;
import android.util.*;
import android.support.v4.app.*;
import com.filenanumi.sejongce.rss.*;
import android.view.*;
import android.graphics.*;

public class ServerChecker extends Service {

    public static final long NOTIFY_INTERVAL = 2 * 60 * 60 * 1000;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

	@Override
	public int onStartCommand(Intent intent,int flags,int startId){
		super.onStartCommand(intent,flags,startId);
	    return START_STICKY;
	}

    @Override
    public void onCreate() {
		super.onCreate();
        if(mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 3000, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
					@Override
					public void run() {
						tryGetData();
					}
				});
        }
    }

	public void tryGetData(){
		ConnectivityManager connMgr = (ConnectivityManager)
			getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()){
			try{
				doGetData();
			} catch (Exception e){
			}
		} else {
		}
	}

	public void doGetData(){
		NetTask nett = new NetTask();
		nett.execute();
	}

	private class NetTask extends AsyncTask<String, Void, String> {

		String sResult;
		@Override
		protected String doInBackground(String... sId) {
			sResult = "NO";
			try{
				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters,4000);
				HttpConnectionParams.setSoTimeout(httpParameters, 5000);
				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpGet httpget = new HttpGet("http://ce.sejong.ac.kr/main/skin/board/total_bbs/rss.skin.php?bo_table=se01&route=/main/bbs/board.php?bo_table=se01"); // Set the action you want to do
				HttpResponse response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity(); 
				InputStream is = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "euc-kr"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null)
					sb.append(line + "\n");
				String resString = sb.toString();
				is.close();
				sResult = resString;
			}catch (Exception e){	
			}
			return sResult;
		}

		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute ( result );
			checkNetResult(sResult);
		}
	}

	public void checkNetResult(String sResult){
		checkData(sResult);
	}

	public void checkData(String respond){
		final SharedPreferences sizepref = getSharedPreferences("size", MODE_PRIVATE);
		int orilength = sizepref.getInt("size_server",0);
		int nowlength = respond.length();
		if (nowlength > 100){
			if (nowlength != orilength){
				SharedPreferences.Editor editor = sizepref.edit();
				editor.putInt("size_server",nowlength);
				editor.commit();
				String[] newsData = getLatestNews(respond);
				Message msg = Message.obtain();
				msg.obj = newsData;
				msg.setTarget(myHandler);
				msg.sendToTarget();
				showAlert(newsData);
			}
		}
	}

	public String[] getLatestNews(String HTML){
		String[] newsdata = new String[]{"","","",""};

		String ITEMAREA = HTML.substring(HTML.indexOf("<item>")+6,HTML.indexOf("</item>"));
		String TITLEAREA = ITEMAREA.substring(ITEMAREA.indexOf("<title>")+7,ITEMAREA.indexOf("</title>"));
		String DATEAREA = ITEMAREA.substring(ITEMAREA.indexOf("<dc:date>")+9,ITEMAREA.indexOf("</dc:date>"));
		String LINKAREA = ITEMAREA.substring(ITEMAREA.indexOf("<link>")+6,ITEMAREA.indexOf("</link>"));

		String TITLE = TITLEAREA.substring(TITLEAREA.indexOf("]")+1,TITLEAREA.length());
		String WRITER = TITLEAREA.substring(TITLEAREA.indexOf("[")+1,TITLEAREA.indexOf("]"));
		String DATE = DATEAREA;
		String LINK = LINKAREA;

		newsdata[0] = TITLE;
		newsdata[1] = WRITER;
		newsdata[2] = DATE;
		newsdata[3] = LINK;

		return newsdata;
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String[] newsData = (String[]) msg.obj;
			NotificationManager manager =
				(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			NotificationCompat.Builder builder =
				new NotificationCompat.Builder(ServerChecker.this);
			builder.setContentTitle(newsData[0]);
			builder.setSmallIcon(R.drawable.minilogo);
			builder.setContentText(newsData[2]);
			builder.setContentInfo(newsData[1]);
			builder.setPriority(1000);
			builder.setWhen(0);
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsData[3]));
			PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			builder.setContentIntent(pIntent);
			manager.notify(20152, builder.build());
		};
	};

	public void showAlert(String[] newsData){
		final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		Display mdp = wm.getDefaultDisplay();
		int width=mdp.getWidth();
		int height=mdp.getHeight();

		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ViewGroup mView =  (ViewGroup) inflater.inflate(R.layout.notifier, null);

		final WindowManager.LayoutParams mParams = new  WindowManager.LayoutParams(
		    width,
			60,
		    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
			PixelFormat.TRANSLUCENT);
		if (width > height){
			mParams.width = 60;
			mParams.height = width;
		}

		mParams.gravity = Gravity.TOP | Gravity.CENTER;
		mParams.x = 0;
		mParams.y = 0;

		LinearLayout noti_parent = (LinearLayout) mView.findViewById(R.id.noti_parent);
		TextView noti_title = (TextView) mView.findViewById(R.id.noti_title);
		TextView noti_writer = (TextView) mView.findViewById(R.id.noti_writer);

		noti_title.setText(newsData[0]);
		noti_writer.setText(newsData[1]);

		wm.addView(mView,mParams);

		Handler mhan = new Handler();
		mhan.postDelayed(new Runnable(){
			@Override
			public void run(){
				wm.removeView(mView);
			}
		},4000);
	}

}
