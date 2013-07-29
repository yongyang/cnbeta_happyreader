package org.jandroid.cnbeta.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.adapter.AsyncImageBaseAdapter;
import org.jandroid.cnbeta.async.ArticleListAsyncTask;
import org.jandroid.cnbeta.async.AsyncResult;
import org.jandroid.cnbeta.entity.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class ArticleListFragment extends Fragment {

    private ListView listView;
    
    private AsyncImageBaseAdapter asyncImageAdapter;
    
    private List<Article> loadedArticles = new ArrayList<Article>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        if(!(activity instanceof ArticleListListener)) {

        }
        super.onAttach(activity);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.article_list_view, container, false);
        listView = (ListView)rootView.findViewById(R.id.article_listview);
//        TextView emptyText = (TextView)rootView.findViewById(R.id.empty_textview);
//        listView.setEmptyView(emptyText);
/*
        simpleAdapter = new SimpleAdapter(getActivity(), articleMapList, R.layout.article_list_item, new String[]{}, new int[]{});
        listView.setAdapter(simpleAdapter);
        listView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
*/

//        View loadMoreView = inflater.inflate(R.layout.bar_load_more, listView, false);
		return rootView;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.bar_load_more, listView,false);
        listView.addFooterView(loadMoreView);
        loadMoreView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "load more", Toast.LENGTH_LONG).show();

            }
        });

        asyncImageAdapter = new AsyncImageBaseAdapter() {
            @Override
            public int getCount() {
                return loadedArticles.size();
            }

            @Override
            public Object getItem(int position) {
                return loadedArticles.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public int getFirstVisibleItemPosition() {
                return listView.getFirstVisiblePosition();
            }

            @Override
            public int getLastVisibleItemPosition() {
                return listView.getLastVisiblePosition();
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.article_list_item, null);
                }
                Article article = loadedArticles.get(position);
                TextView tv = (TextView)convertView.findViewById(R.id.tile_show);
                tv.setText(article.getTitleShow());
                ImageView iv = (ImageView) convertView.findViewById(R.id.item_logo);
                // queue to image load list
                queueLoadImage(position, iv, article.getLogo());
                return convertView;
            }
        };
        //TODO: other textview
        listView.setAdapter(asyncImageAdapter);
        listView.setOnScrollListener(asyncImageAdapter);
        //TODO: for listview loadimage on the firstpage loaded
//        asyncImageAdapter.onScrollStateChanged(listView, AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
    }

    @Override
    public void onStart() {
        super.onStart();
                //TODO: loading data

        new ArticleListAsyncTask("all", 1) {
            @Override
            public ProgressDialog getProgressDialog() {
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("loading articles...");
                return progressDialog;
            }

            @Override
            protected void onPostExecute(AsyncResult listAsyncResult) {
                super.onPostExecute(listAsyncResult);
                if(listAsyncResult.isSuccess()) {
                    List<Article> articles = (List<Article>)listAsyncResult.getResult();
                    updateArticles(articles);
                }
                else {
                    Toast.makeText(getActivity(), listAsyncResult.getErrorMsg(), Toast.LENGTH_LONG).show();
                }
            }
        }.executeMultiThread();
    }

    public void updateArticles(List<Article> articles){
        loadedArticles.clear();
        for(Article article : articles){
            loadedArticles.addAll(articles);
        }
        asyncImageAdapter.notifyDataSetChanged();
    }

    public void appendArticles(List<Article> articles){
        loadedArticles.addAll(articles);
        asyncImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //add  refresh actionitem
        inflater.inflate(R.menu.article_list_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.isCheckable()) {
            item.setChecked(true);
        }
        switch (item.getItemId()) {
            case R.id.more_item:
                break;
            case R.id.refresh_item:
                //TODO:
                Toast.makeText(getActivity(), "您点击了" + item.toString(), Toast.LENGTH_SHORT).show();
            default:
        }
        return true;
    }

    public static interface ArticleListListener {
        void onArticleItemClick(long articleId);
    }
}
