package org.jandroid.cnbeta.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

//TODO: 使用 flipper 显示各种不同分类的 article 列表

public class ArticleListFragment extends Fragment
{
	public static final String ARG_SECTION_NUMBER = "section_number";
	// 该方法的返回值就是该Fragment显示的View组件
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		TextView textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		// 获取创建该Fragment时传入的参数Bundle
		Bundle args = getArguments();
		// 设置TextView显示的文本
		textView.setText(args.getInt(ARG_SECTION_NUMBER) + "");
		textView.setTextSize(30);
		// 返回该TextView
		return textView;
	}
}
