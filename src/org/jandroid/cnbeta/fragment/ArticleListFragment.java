package org.jandroid.cnbeta.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.ViewFlipper;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.entity.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

//TODO: NOT use flipper to display article category for now
public class ArticleListFragment extends Fragment {
    
    private ListView listView;
    
    private SimpleAdapter simpleAdapter;
    
    private List<Map<String, ?>> articleMapList = new ArrayList<Map<String, ?>>();
        
    @Override
    public void onAttach(Activity activity) {
        if(!(activity instanceof ArticleListListener)) {

        }
        super.onAttach(activity);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listView = (ListView)inflater.inflate(R.layout.article_list_view, container);
        simpleAdapter = new SimpleAdapter(getActivity(), articleMapList, R.layout.article_list_item, new String[]{}, new int[]{});
        listView.setAdapter(simpleAdapter);
        listView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
		return listView;
	}
    
    public void updateArticles(List<Article> articles){
        articleMapList.clear();
        for(Article article : articles){
            articleMapList.add(article.toMap());
        }
        simpleAdapter.notifyDataSetChanged();
    }
   
    public static interface ArticleListListener {

    }
}
