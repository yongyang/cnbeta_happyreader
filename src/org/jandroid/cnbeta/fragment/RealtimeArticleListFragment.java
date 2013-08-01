package org.jandroid.cnbeta.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.jandroid.cnbeta.CnBetaApplication;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.adapter.AsyncImageAdapter;
import org.jandroid.cnbeta.async.ArticleListAsyncTask;
import org.jandroid.cnbeta.async.AsyncResult;
import org.jandroid.cnbeta.async.LoadImageAsyncTask;
import org.jandroid.cnbeta.async.RealtimeArticleListAsyncTask;
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.entity.RealtimeArticle;
import org.jandroid.cnbeta.loader.ArticleListLoader;
import org.jandroid.util.EnvironmentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class RealtimeArticleListFragment extends Fragment {
    //TODO: 刷新的时候，将新文章添加到List顶部，而不是完全刷新

    private ListView lvArticleList;

    private List<RealtimeArticle> loadedArticles = new ArrayList<RealtimeArticle>();

    private Handler handler = new Handler();

    private BaseAdapter adapter;

    public RealtimeArticleListFragment() {
    }

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
        View rootView = inflater.inflate(R.layout.lv_article, container, false);
        lvArticleList = (ListView)rootView.findViewById(R.id.article_listview);
		return rootView;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        LinearLayout linearLayoutLoadMore = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.bar_load_more, lvArticleList,false);
        lvArticleList.addFooterView(linearLayoutLoadMore);

        linearLayoutLoadMore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO:
                loadArticles();

            }
        });

        adapter = new BaseAdapter() {
            public int getCount() {
                return loadedArticles.size();
            }

            public Object getItem(int position) {
                return loadedArticles.get(position);
            }

            public long getItemId(int position) {
                return position;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.lv_realtime_article_item, null);
                }
                RealtimeArticle article = loadedArticles.get(position);
                TextView tvTitle = (TextView)convertView.findViewById(R.id.tile);
                tvTitle.setText(article.getTitle());
                TextView tvHometextShowShort2 = (TextView)convertView.findViewById(R.id.hometext_show_short2);
                tvHometextShowShort2.setText(article.getHometextShowShort2());
                TextView tvTime = (TextView)convertView.findViewById(R.id.time);
                tvTime.setText("" + article.getTime());

                TextView tvTimeShow = (TextView)convertView.findViewById(R.id.time_show);
                tvTimeShow.setText("" + article.getTimeShow());

                return convertView;

            }
        };
        lvArticleList.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadArticles();
    }
    
    private void loadArticles(){
        EnvironmentUtils.checkNetworkConnected(getActivity());

        new RealtimeArticleListAsyncTask(){
            @Override
            public CnBetaApplicationContext getCnBetaApplicationContext() {
                return (CnBetaApplicationContext)getActivity().getApplication();
            }

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
                    List<RealtimeArticle> articles = (List<RealtimeArticle>)listAsyncResult.getResult();
                    if(articles != null) {
                        loadedArticles.addAll(articles);
                        adapter.notifyDataSetChanged();
                    }
                }
                else {
                    Toast.makeText(getActivity(), listAsyncResult.getErrorMsg(), Toast.LENGTH_LONG).show();
                }
            }
        }.executeMultiThread();
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
                loadArticles();
                Toast.makeText(getActivity(), "您点击了" + item.toString(), Toast.LENGTH_SHORT).show();
            default:
        }
        return true;
    }

    public static interface ArticleListListener {
        void onArticleItemClick(long articleId);
    }
}
