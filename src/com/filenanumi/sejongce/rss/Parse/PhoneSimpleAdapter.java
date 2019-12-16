package com.filenanumi.sejongce.rss.Parse;

import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.filenanumi.sejongce.rss.*;
import com.filenanumi.sejongce.rss.Parse.ListInfo.*;
import java.util.*;


@SuppressWarnings("unchecked")
public class PhoneSimpleAdapter extends SimpleAdapter
{
    private List<? extends Map<String, ?>> datas;
    
    private HashMap<String, String> _data;

	private LayoutInflater inflater;
    
    public PhoneSimpleAdapter(Context context,List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
    {
        super(context, data, resource, from, to);
        datas = data;
		inflater = LayoutInflater.from(context);
        // TODO Auto-generated constructor stub
    }
    
	@Override
	public int getCount(){
		return datas.size();
	}
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        View view = super.getView(position, convertView, parent);
	/**	if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_board, null);
		}**/
        _data = (HashMap<String, String>) datas.get(position);
        
        String _title_fix = _data.get(listNote.SUBJECT);
		String _writer = _title_fix.substring(_title_fix.indexOf("[")+1,_title_fix.indexOf("]"));
		//	String _writer = _data.get(listNote.WRITER);
		String _title = _title_fix.substring(_title_fix.indexOf("]")+1,_title_fix.length());
        String _content = _data.get(listNote.CONTENT);
        
        TextView tvSubject = (TextView) view.findViewById(R.id.board_subject);
        TextView tvWriter = (TextView) view.findViewById(R.id.board_writer);
        TextView tvContent = (TextView) view.findViewById(R.id.board_date);
        
        tvSubject.setText(_title);
        tvWriter.setText(_writer);
        tvContent.setText(_content);
		
        //convertView.setBackgroundColor(position % 2 == 0 ? Color.parseColor("#FFFFFF") : Color.parseColor("0184FF"));
	/**
		if (position % 2 == 0)
		{
			LinearLayout listbg = (LinearLayout) view.findViewById(R.id.linearLayout2);
			listbg.setBackgroundColor(Color.parseColor("#0184FF"));
			view.setBackgroundColor(Color.parseColor("#0184FF"));
			parent.setBackgroundColor(Color.parseColor("#0184FF"));
		} else {
			LinearLayout listbg = (LinearLayout) view.findViewById(R.id.linearLayout2);
			listbg.setBackgroundColor(Color.parseColor("#FFFFFF"));
			view.setBackgroundColor(Color.parseColor("#FFFFFF"));
			parent.setBackgroundColor(Color.parseColor("#FFFFFF"));
		}
	**/
        return view;
    }
    
    private boolean isNewGroup(int position)
    {
        _data = (HashMap<String, String>) datas.get(position);
        String nowDate = _data.get(listNote.SUBJECT);
        
        _data = (HashMap<String, String>) datas.get(position - 1);
        String preDate = _data.get(listNote.SUBJECT);
        
        _data = (HashMap<String, String>) datas.get(position);
        
        if (!nowDate.equals(preDate)) { return true; }
        
        return false;
    }
    
}
