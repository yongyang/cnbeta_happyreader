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
		actionBar.setDisplayShowTitleEnabled(true);
   		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);
   		actionBar.addTab(actionBar.newTab().setText("正文").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("评论").setTabListener(this));
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
        Fragment fragment = new ArticleContentFragment();
        // ����һ��Bundle����������Fragment�������
        Bundle args = new Bundle();
        args.putInt(ArticleListFragment.ARG_SECTION_NUMBER,
                tab.getPosition() + 1);
        // ��fragment�������
        fragment.setArguments(args);
        // ��ȡFragmentTransaction����
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        // ʹ��fragment�����Activity�е�container���
        ft.replace(R.id.contentLayout, fragment);
        // �ύ����
        ft.commit();
    }

   	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
   	}

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

	// �������ѡ��ʱ�����÷���
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
   	// ѡ��˵��Ĳ˵��������Ļص�����
   	public boolean onOptionsItemSelected(MenuItem mi) {
        if(mi.isCheckable())
      		{
      			mi.setChecked(true);  //��
      		}
      		// �жϵ��������ĸ��˵��������Ե�������Ӧ��
      		switch (mi.getItemId()) {
                case android.R.id.home:
                    Intent intent = new Intent(this, MainActivity.class);
                    this.startActivity(intent);
                    break;
                case R.id.more_item:
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
