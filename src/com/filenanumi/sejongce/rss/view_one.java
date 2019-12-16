package com.filenanumi.sejongce.rss;

import android.annotation.*;
import android.support.v4.app.*;
import android.content.*;
import android.view.*;
import android.os.*;
import android.graphics.*;
import android.widget.*;
import java.util.*;
import android.view.View.*;
import android.net.*;
import android.view.animation.*;
import com.filenanumi.sejongce.rss.Parse.*;
import com.roger.match.library.*;
import com.twotoasters.jazzylistview.*;

@SuppressLint("ValidFragment")
public class view_one extends Fragment {
 Context mcontext;
	Typeface mTypeface;

	private ArrayList<HashMap<String, String>> list_board = new ArrayList<HashMap<String, String>>();
    private PhoneSimpleAdapter sa;
    private ArrayList<String> contents,links;
    int firstload =0;
    private JazzyListView lvn;
	private LinearLayout listv,loadv,alertv;

 public view_one(Context context) {
	 mcontext = context;
 }
 	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sub_one, container,false);
		
		mTypeface = Typeface.createFromAsset(this.getActivity().getAssets(), "NanumGothic.ttf");
	//	ViewGroup root = (ViewGroup) view.findViewById(android.R.id.content);
	    ViewGroup root = (ViewGroup) view.getRootView();
		setGlobalFont(root);
		
		contents = new ArrayList<String>();
		links = new ArrayList<String>();
        lvn = (JazzyListView)view.findViewById(R.id.listView1_i);
		sa = new PhoneSimpleAdapter(getActivity(), list_board, R.layout.row_board, new String[]{"subject", "date", "writer"}, new int[]{R.id.board_subject, R.id.board_date, R.id.board_writer});
        lvn.setAdapter(sa); lvn.setOnItemClickListener(listener);
		listv = (LinearLayout) view.findViewById(R.id.listlay_i);
		loadv = (LinearLayout) view.findViewById(R.id.loadlay_i);
		alertv= (LinearLayout) view.findViewById(R.id.alertlay_i);
		listv.setVisibility(LinearLayout.GONE);
        alertv.setVisibility(LinearLayout.GONE);

	    Button b1 = (Button) view.findViewById(R.id.btn1_i);
		Button b2 = (Button) view.findViewById(R.id.btn2_i);
		final Button b3 = (Button) view.findViewById(R.id.btn3_i);

		LinearLayout listlay = (LinearLayout) view.findViewById(R.id.linearLayout2);

		b1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					ConnectivityManager connMgr = (ConnectivityManager) getActivity()
						.getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

					if (networkInfo != null && networkInfo.isConnected()) {
						// fetch data
						loadv.setVisibility(LinearLayout.GONE);
						alertv.setVisibility(LinearLayout.GONE);
						listv.setVisibility(LinearLayout.VISIBLE);
						Animation listanim = AnimationUtils.loadAnimation(mcontext,R.anim.listanim);
						lvn.setAnimation(listanim);
						RSSParse rss = new RSSParse(contents,links);
						rss.threadRssParse(getActivity(), sa, list_board, "http://ce.sejong.ac.kr/main/skin/board/total_bbs/rss.skin.php?bo_table=se02&route=/main/bbs/board.php?bo_table=se02",b3);
						//		b3.setEnabled(true);
					} else {
						// display error
						Toast.makeText(mcontext,"네트워크를 확인해주세요",2000).show();
						loadv.setVisibility(LinearLayout.GONE);
						alertv.setVisibility(LinearLayout.VISIBLE);
						listv.setVisibility(LinearLayout.GONE);
					}
					//	sa.notifyDataSetChanged();
					//	lvn.requestLayout();
				}			});
		b2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://ce.sejong.ac.kr/main/bbs/board.php?bo_table=se02")));
				}			
			});
		b3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					lvn.clearAnimation();
					lvn.destroyDrawingCache();
					list_board.clear();
					sa.notifyDataSetChanged();
					b3.setEnabled(false);
					loadv.setVisibility(LinearLayout.VISIBLE);
					alertv.setVisibility(LinearLayout.GONE);
					listv.setVisibility(LinearLayout.GONE);
				}			
			});


		if (sa.getCount() == 0) {
            b3.setEnabled(false);
		} else {
			b3.setEnabled(true);
		}
		
		return view;
	}
		
	void setGlobalFont(ViewGroup root) {
		for (int i = 0; i < root.getChildCount(); i++) {
			View child = root.getChildAt(i);
			if (child instanceof TextView)
				((TextView)child).setTypeface(mTypeface);
			else if (child instanceof ViewGroup)
				setGlobalFont((ViewGroup)child);
		}
	}
	
	AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> list, View view, int position, long id)
        {
        	View _view = sa.getView(position, view, list);
        	String writer = ((TextView)_view.findViewById(R.id.board_writer)).getText().toString();
        	String subject = ((TextView)_view.findViewById(R.id.board_subject)).getText().toString();
        	String date = ((TextView)_view.findViewById(R.id.board_date)).getText().toString();
            try{
                Intent intent = new Intent(getActivity(), RSS_ContentView.class);
                intent.putExtra("content", contents.get(position));
                intent.putExtra("writer", writer);
                intent.putExtra("subject", subject);
                intent.putExtra("date", date);
				intent.putExtra("link",links.get(position));
                startActivity(intent);
				//	finish();
            }catch(Exception ex)
            {
                Toast.makeText(getActivity(), "ERROR! " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }        
    };
	
	
	@Override
	public void onDestroyView() {
	    RecycleUtils.recursiveRecycle(getActivity().getWindow().getDecorView());
		System.gc();
		super.onDestroy();
	}
}
