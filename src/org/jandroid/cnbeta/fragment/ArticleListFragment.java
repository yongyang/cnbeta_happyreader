package org.jandroid.cnbeta.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;
import org.jandroid.cnbeta.R;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

//TODO: use flipper to display article category
public class ArticleListFragment extends Fragment {
    @Override
    public void onAttach(Activity activity) {
        if(!(activity instanceof ArticleListListener)) {

        }
        super.onAttach(activity);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewFlipper viewFlipper = (ViewFlipper)inflater.inflate(R.layout.article_list_fragment, container);
        viewFlipper.addView();
        viewFlipper.addView();
		return viewFlipper;
	}

    public static interface ArticleListListener {

    }
}
