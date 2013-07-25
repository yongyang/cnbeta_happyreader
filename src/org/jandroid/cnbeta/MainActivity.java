package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import org.jandroid.cnbeta.async.ArticleListAsyncTask;
import org.jandroid.cnbeta.async.AsyncResult;
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.fragment.ArticleListFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements ActionBar.TabListener {
    private static final String SELECTED_ITEM = "selected_item";

    public final static int[] tabs = new int[]{R.string.tab_quanbuzixun, R.string.tab_shishigengxin};
    public final static int[] tags = new int[]{R.string.tab_tag_quanbuzixun, R.string.tab_tag_shishigengxin};
    
    // all articles for tabs, tab_tag => articles
    private Map<String, List<Article>> articles = new HashMap<String, List<Article>>();
    
    
   	@Override
   	public void onCreate(Bundle savedInstanceState){
   		super.onCreate(savedInstanceState);

   		setContentView(R.layout.main);
   		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
   		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for(int i=0; i< tabs.length; i++) {
            int resourceId = tabs[i];
            int tagResourceId = tags[i];
            actionBar.addTab(actionBar.newTab().setText(resourceId).setTag(getResources().getString(tagResourceId)).setTabListener(this));
        }
//   		actionBar.addTab(actionBar.newTab().setText("全部资讯").setTabListener(this));
//        actionBar.addTab(actionBar.newTab().setText("实时更新").setTabListener(this));
//   		actionBar.addTab(actionBar.newTab().setText("DIG").setTabListener(this));
//        actionBar.addTab(actionBar.newTab().setText("软件").setTabListener(this));
//        actionBar.addTab(actionBar.newTab().setText("热点").setTabListener(this));
   	}

   	@Override
   	public void onRestoreInstanceState(Bundle savedInstanceState){
   		if (savedInstanceState.containsKey(SELECTED_ITEM)){
   			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(SELECTED_ITEM));
   		}
   	}

   	@Override
   	public void onSaveInstanceState(Bundle outState){
   		outState.putInt(SELECTED_ITEM, getActionBar().getSelectedNavigationIndex());
   	}

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        Fragment fragment = getFragmentManager().findFragmentByTag(tab.getTag().toString());
        if(fragment == null) {
            fragment = new ArticleListFragment();
        }
        Bundle args = new Bundle();
//        args.putInt(ArticleListFragment.ARG_SECTION_NUMBER, tab.getPosition() + 1);
        fragment.setArguments(args);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
//        ft.addToBackStack(null);
        ft.commit();
    }

   	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
   	}

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        //TODO: update datas or ignore
    }

    public void loadArticleList(final String category, final int page) {
        new ArticleListAsyncTask(category, page){
            @Override
            public ProgressDialog getProgressDialog() {
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("加载新闻列表");
                return progressDialog;
            }

            @Override
            protected void onPostExecute(AsyncResult<List<Article>> listAsyncResult) {
                super.onPostExecute(listAsyncResult);
                List<Article> articles = listAsyncResult.getResult();
            }
        }.executeMultiThread();
    }

    @Override
   	public boolean onCreateOptionsMenu(Menu menu) {
   		getMenuInflater().inflate(R.menu.article_list_menu, menu);
   		return true;
   	}
    @Override
   	public boolean onOptionsItemSelected(MenuItem mi) {
        if(mi.isCheckable())
      		{
      			mi.setChecked(true);
      		}
      		switch (mi.getItemId()) {
                case android.R.id.home:
                case R.id.more_item:
                    break;
                case R.id.aboutus_item:
                    //TODO:  for test
                    Intent intent = new Intent(this, ArticleContentActivity.class);
                    this.startActivity(intent);
                    break;
                default:
                    Toast.makeText(this, "点击了" + mi.toString(),Toast.LENGTH_SHORT).show();            }
   		return true;
   	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // 每次都会调用该方法, 可以动态改变 menu
        return super.onPrepareOptionsMenu(menu);
    }
    
    public void reloadArticles(ActionBar.Tab tab) {
        
    }
    
}
