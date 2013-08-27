package org.jandroid.cnbeta.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.jandroid.cnbeta.R;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleCommentsFragment extends Fragment
{
	public static final String ARG_SECTION_NUMBER = "section_number";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		TextView textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		Bundle args = getArguments();
//		textView.setText(args.getInt(ARG_SECTION_NUMBER) + "");
		textView.setTextSize(30);
		return textView;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.article_content_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    
    //TODO: 点击 Parent 跳转到 parent comment
}
