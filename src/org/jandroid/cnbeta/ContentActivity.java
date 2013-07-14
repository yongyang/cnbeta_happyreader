package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.fragment.ArticleContentFragment;
import org.jandroid.cnbeta.fragment.ArticleListFragment;
import org.jandroid.cnbeta.task.EntityLoader;

public class ContentActivity extends Activity implements ActionBar.TabListener, ActionBar.OnNavigationListener {
    private static final String SELECTED_ITEM = "selected_item";

   	@Override
   	public void onCreate(Bundle savedInstanceState){
   		super.onCreate(savedInstanceState);

   		setContentView(R.layout.content);
   		final ActionBar actionBar = getActionBar();
		// 设置ActionBar是否显示标题
		actionBar.setDisplayShowTitleEnabled(true);
   		// 设置ActionBar的导航方式：Tab导航
   		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);
   		// 依次添加3个Tab页，并为3个Tab标签添加事件监听器
   		actionBar.addTab(actionBar.newTab().setText("正文").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("评论").setTabListener(this));
   	}

   	@Override
   	public void onRestoreInstanceState(Bundle savedInstanceState){
   		if (savedInstanceState.containsKey(SELECTED_ITEM)){
   			// 选中前面保存的索引对应的Fragment页
   			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(SELECTED_ITEM));
   		}
   	}

   	@Override
   	public void onSaveInstanceState(Bundle outState){
   		// 将当前选中的Fragment页的索引保存到Bundle中
   		outState.putInt(SELECTED_ITEM, getActionBar().getSelectedNavigationIndex());
   	}

    // 当指定Tab被选中时激发该方法
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // 创建一个新的Fragment对象
        Fragment fragment = new ArticleContentFragment();
        // 创建一个Bundle对象，用于向Fragment传入参数
        Bundle args = new Bundle();
        args.putInt(ArticleListFragment.ARG_SECTION_NUMBER,
                tab.getPosition() + 1);
        // 向fragment传入参数
        fragment.setArguments(args);
        // 获取FragmentTransaction对象
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        // 使用fragment代替该Activity中的container组件
        ft.replace(R.id.contentLayout, fragment);
        // 提交事务
        ft.commit();
    }

   	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
   	}

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

	// 当导航项被选中时激发该方法
	public boolean onNavigationItemSelected(int position, long id) {
        Toast.makeText(this, "abc", 1000).show();
        return true;
	}
    private void execAsyncEntityLoader(final EntityLoader entityLoader){
        final ProgressDialog dialog = new ProgressDialog(this);

        new AsyncTask<EntityLoader, Integer, Article>(){

            @Override
            protected Article doInBackground(EntityLoader... params) {
                params[0].load();

                return new Article();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setTitle("");
                dialog.setMessage(entityLoader.getTitle());
                dialog.show();
            }

            @Override
            protected void onPostExecute(Article article) {
                super.onPostExecute(article);
                dialog.dismiss();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, entityLoader);
    }

    @Override
   	public boolean onCreateOptionsMenu(Menu menu)
   	{
   		getMenuInflater().inflate(R.menu.article_content_menu, menu);
   		return true;
   	}
    @Override
   	// 选项菜单的菜单项被单击后的回调方法
   	public boolean onOptionsItemSelected(MenuItem mi) {
        if(mi.isCheckable())
      		{
      			mi.setChecked(true);  //②
      		}
      		// 判断单击的是哪个菜单项，并针对性的作出响应。
      		switch (mi.getItemId()) {
                case android.R.id.home:
                    Intent intent = new Intent(this, MainActivity.class);
                    this.startActivity(intent);
                    break;
                case R.id.more_item:
                default:
                    Toast.makeText(this, "您单击了菜单项" + mi.toString(),Toast.LENGTH_SHORT).show();

            }

   		return true;
   	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }
}
