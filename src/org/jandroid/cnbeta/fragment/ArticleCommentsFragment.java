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
public class ArticleCommentsFragment extends Fragment
{
	public static final String ARG_SECTION_NUMBER = "section_number";
	// �÷����ķ���ֵ���Ǹ�Fragment��ʾ��View���
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		TextView textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		// ��ȡ������Fragmentʱ����Ĳ���Bundle
		Bundle args = getArguments();
		// ����TextView��ʾ���ı�
		textView.setText(args.getInt(ARG_SECTION_NUMBER) + "");
		textView.setTextSize(30);
		// ���ظ�TextView
		return textView;
	}
}
