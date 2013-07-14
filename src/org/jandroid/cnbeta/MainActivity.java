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
import org.jandroid.cnbeta.fragment.ArticleListFragment;
import org.jandroid.cnbeta.task.EntityLoader;

public class MainActivity extends Activity implements ActionBar.TabListener {
    private static final String SELECTED_ITEM = "selected_item";

   	@Override
   	public void onCreate(Bundle savedInstanceState){
   		super.onCreate(savedInstanceState);

   		setContentView(R.layout.main);
   		final ActionBar actionBar = getActionBar();
		// 设置ActionBar是否显示标题
		actionBar.setDisplayShowTitleEnabled(true);
   		// 设置ActionBar的导航方式：Tab导航
   		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
   		// 依次添加3个Tab页，并为3个Tab标签添加事件监听器
   		actionBar.addTab(actionBar.newTab().setText("热点新闻").setTabListener(this));
   		actionBar.addTab(actionBar.newTab().setText("实时更新").setTabListener(this));
   		actionBar.addTab(actionBar.newTab().setText("TOP10").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("我的收藏").setTabListener(this));
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
        Fragment fragment = new ArticleListFragment();
        // 创建一个Bundle对象，用于向Fragment传入参数
        Bundle args = new Bundle();
        args.putInt(ArticleListFragment.ARG_SECTION_NUMBER,
                tab.getPosition() + 1);
        // 向fragment传入参数
        fragment.setArguments(args);
        // 获取FragmentTransaction对象
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        // 使用fragment代替该Activity中的container组件
        ft.replace(R.id.container, fragment);
        // 提交事务
        ft.commit();
    }

   	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
   	}

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

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
   		getMenuInflater().inflate(R.menu.article_list_menu, menu);
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
                case R.id.more_item:
                    break;
                case R.id.aboutus_item:
                    //TODO:  for test
                    Intent intent = new Intent(this, ContentActivity.class);
                    this.startActivity(intent);
                    break;
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
