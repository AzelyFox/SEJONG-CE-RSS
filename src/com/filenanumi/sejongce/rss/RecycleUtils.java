package com.filenanumi.sejongce.rss;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

public class RecycleUtils {

	private RecycleUtils(){};

	public static void recursiveRecycle(View root) {
		if (root == null)
			return;
		if (root instanceof ViewGroup) {
			ViewGroup group = (ViewGroup)root;
			int count = group.getChildCount();
			for (int i = 0; i < count; i++) {
				recursiveRecycle(group.getChildAt(i));
			}

			if (!(root instanceof AdapterView)) {
				group.removeAllViews();
			}

		}

		if (root instanceof ImageView) {
			if(((ImageView)root).getBackground() != null) {
				((ImageView)root).getBackground().setCallback(null);
			}
			((ImageView)root).setImageDrawable(null);
		}
		if(root.getBackground() != null) {
			root.getBackground().setCallback(null);
		}
		root.setBackgroundDrawable(null);

		root = null;

		return;
	}
}
