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
		// ����ActionBar�Ƿ���ʾ����
		actionBar.setDisplayShowTitleEnabled(true);
   		// ����ActionBar�ĵ�����ʽ��Tab����
   		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
   		// �������3��Tabҳ����Ϊ3��Tab��ǩ����¼�������
   		actionBar.addTab(actionBar.newTab().setText("最新新闻").setTabListener(this));
   		actionBar.addTab(actionBar.newTab().setText("时事要点").setTabListener(this));
   		actionBar.addTab(actionBar.newTab().setText("TOP10").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("我的收藏").setTabListener(this));
   	}

   	@Override
   	public void onRestoreInstanceState(Bundle savedInstanceState){
   		if (savedInstanceState.containsKey(SELECTED_ITEM)){
   			// ѡ��ǰ�汣��������Ӧ��Fragmentҳ
   			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(SELECTED_ITEM));
   		}
   	}

   	@Override
   	public void onSaveInstanceState(Bundle outState){
   		// ����ǰѡ�е�Fragmentҳ������浽Bundle��
   		outState.putInt(SELECTED_ITEM, getActionBar().getSelectedNavigationIndex());
   	}

    // ��ָ��Tab��ѡ��ʱ�����÷���
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // ����һ���µ�Fragment����
        Fragment fragment = new ArticleListFragment();
        // ����һ��Bundle����������Fragment�������
        Bundle args = new Bundle();
        args.putInt(ArticleListFragment.ARG_SECTION_NUMBER,
                tab.getPosition() + 1);
        // ��fragment�������
        fragment.setArguments(args);
        // ��ȡFragmentTransaction����
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        // ʹ��fragment�����Activity�е�container���
        ft.replace(R.id.container, fragment);
        // �ύ����
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
   	// ѡ��˵��Ĳ˵��������Ļص�����
   	public boolean onOptionsItemSelected(MenuItem mi) {
        if(mi.isCheckable())
      		{
      			mi.setChecked(true);  //��
      		}
      		// �жϵ��������ĸ��˵��������Ե�������Ӧ��
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
                    Toast.makeText(this, "����˲˵���" + mi.toString(),Toast.LENGTH_SHORT).show();

            }

   		return true;
   	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }
}
