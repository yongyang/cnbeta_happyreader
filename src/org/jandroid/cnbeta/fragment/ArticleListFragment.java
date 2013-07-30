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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.adapter.AsyncImageBaseAdapter;
import org.jandroid.cnbeta.async.ArticleListAsyncTask;
import org.jandroid.cnbeta.async.AsyncResult;
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.loader.ArticleListLoader;
import org.jandroid.cnbeta.util.EnvironmentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class ArticleListFragment extends Fragment {

    private ListView lvArticleList;
    
    private AsyncImageBaseAdapter asyncImageAdapter;
    
    private List<Article> loadedArticles = new ArrayList<Article>();

    
    // 新闻分类
    private ArticleListLoader.Type category;
    private int loadedPage = 0;

    public ArticleListFragment(ArticleListLoader.Type category) {
        this.category = category;
    }

    public ArticleListLoader.Type getCategory() {
        return category;
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
        View rootView = inflater.inflate(R.layout.article_list_view, container, false);
        lvArticleList = (ListView)rootView.findViewById(R.id.article_listview);
		return rootView;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        LinearLayout llLoadMore = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.bar_load_more, lvArticleList,false);
        lvArticleList.addFooterView(llLoadMore);

        llLoadMore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO:
                loadArticles();
                
            }
        });
        
        llLoadMore.setOnClickListener(new View.OnClickListener() {
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
                return lvArticleList.getFirstVisiblePosition();
            }

            @Override
            public int getLastVisibleItemPosition() {
                return lvArticleList.getLastVisiblePosition();
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.article_list_item, null);
                }
                Article article = loadedArticles.get(position);
                TextView tvTitleShow = (TextView)convertView.findViewById(R.id.tile_show);
                tvTitleShow.setText(article.getTitleShow());
                TextView tvHometextShowShort = (TextView)convertView.findViewById(R.id.hometext_show_short);
                tvHometextShowShort.setText(article.getHometextShowShort());
                TextView tvComments = (TextView)convertView.findViewById(R.id.comments);
                tvComments.setText(""+article.getComments());

                ImageView ivLogo = (ImageView) convertView.findViewById(R.id.item_logo);
                // queue to image load list
                queueLoadImage(position, ivLogo, article.getLogo());
                return convertView;
            }
        };
        //TODO: other textview
        lvArticleList.setAdapter(asyncImageAdapter);
        lvArticleList.setOnScrollListener(asyncImageAdapter);
        //TODO: for listview loadimage on the firstpage loaded
//        asyncImageAdapter.onScrollStateChanged(listView, AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
        loadArticles();
        
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    
    private void loadArticles(){
        EnvironmentUtils.checkConnectionStatus(getActivity());
        
        //loading data
        new ArticleListAsyncTask(getCategory(), ++loadedPage) {

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
                    appendArticles(articles);
                }
                else {
                    Toast.makeText(getActivity(), listAsyncResult.getErrorMsg(), Toast.LENGTH_LONG).show();
                }
            }
        }.executeMultiThread();        
    }

    // refresh
    private void refresh() {
        loadedArticles.clear();
        loadedPage = 0;
        loadArticles();
    }

    // load more
    private void appendArticles(List<Article> articles){
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
                refresh();
                Toast.makeText(getActivity(), "您点击了" + item.toString(), Toast.LENGTH_SHORT).show();
            default:
        }
        return true;
    }

    public static interface ArticleListListener {
        void onArticleItemClick(long articleId);
    }
}
