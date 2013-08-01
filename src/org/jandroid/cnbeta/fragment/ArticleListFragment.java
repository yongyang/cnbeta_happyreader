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
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.loader.ArticleListLoader;
import org.jandroid.util.EnvironmentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class ArticleListFragment extends Fragment {

    private ListView lvArticleList;
    
    private AsyncImageAdapter asyncImageAdapter;
    
    private List<Article> loadedArticles = new ArrayList<Article>();

    private Handler handler = new Handler();
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

        asyncImageAdapter = new AsyncImageAdapter() {
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
            public Bitmap getDefaultBitmap() {
                return BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.default_img);
            }

            @Override
            protected void loadImageAsync(final String imageUrl, final OnAsyncImageLoadListener onAsyncImageLoadListener) {
                new LoadImageAsyncTask() {

                    @Override
                    public CnBetaApplicationContext getCnBetaApplicationContext() {
                        return (CnBetaApplication)getActivity().getApplication();
                    }

                    @Override
                    protected String getImageUrl() {
                        return imageUrl;
                    }

                    @Override
                    protected void onPostExecute(final AsyncResult bitmapAsyncResult) {
                        super.onPostExecute(bitmapAsyncResult);
                        if (bitmapAsyncResult.isSuccess()) {
                            Bitmap bitmap = (Bitmap) bitmapAsyncResult.getResult();
                            onAsyncImageLoadListener.onLoaded(bitmap);
                        }
                        else {
                            onAsyncImageLoadListener.onLoadFailed(bitmapAsyncResult.getErrorMsg(), bitmapAsyncResult.getException());
                        }
                    }

                    @Override
                    protected void onCancelled() {
                        onAsyncImageLoadListener.onLoadFailed("Cancelled", null);
                    }

                    @Override
                    protected void onCancelled(AsyncResult bitmapAsyncResult) {
                        onAsyncImageLoadListener.onLoadFailed("Cancelled", null);
                    }
                }.executeMultiThread();
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.lv_article_item, null);
                }
                Article article = loadedArticles.get(position);
                TextView tvTitleShow = (TextView)convertView.findViewById(R.id.tile_show);
                tvTitleShow.setText(article.getTitleShow());
                TextView tvHometextShowShort = (TextView)convertView.findViewById(R.id.hometext_show_short);
                tvHometextShowShort.setText(article.getHometextShowShort());
                TextView tvComments = (TextView)convertView.findViewById(R.id.comments);
                tvComments.setText(""+article.getComments());
                TextView tvCounter = (TextView)convertView.findViewById(R.id.counter);
                tvCounter.setText(""+article.getCounter());
                TextView tvTime = (TextView)convertView.findViewById(R.id.time);
                tvTime.setText(""+article.getTime());
/*
                TextView tvScore = (TextView)convertView.findViewById(R.id.score);
                tvScore.setText(""+article.getScore());
*/


                ImageView ivLogo = (ImageView) convertView.findViewById(R.id.item_logo);
                // queue to image load list or set a cached bitmap if has been cached
                ivLogo.setImageBitmap(queueImageView(position, ivLogo, article.getLogo()));
                return convertView;
            }
        };
        lvArticleList.setAdapter(asyncImageAdapter);
        lvArticleList.setOnScrollListener(asyncImageAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        reloadArticles();
    }
    
    private void loadArticles(){
        EnvironmentUtils.checkNetworkConnected(getActivity());
        
        //loading data
        new ArticleListAsyncTask() {

            @Override
            public CnBetaApplicationContext getCnBetaApplicationContext() {
                return (CnBetaApplication)getActivity().getApplication();
            }

            @Override
            protected ArticleListLoader.Type getCategory() {
                return ArticleListFragment.this.getCategory();
            }

            @Override
            protected int getPage() {
                return ++loadedPage;
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
    private void reloadArticles() {
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
                reloadArticles();
                Toast.makeText(getActivity(), "您点击了" + item.toString(), Toast.LENGTH_SHORT).show();
            default:
        }
        return true;
    }

    public static interface ArticleListListener {
        void onArticleItemClick(long articleId);
    }
}
