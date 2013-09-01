package org.jandroid.cnbeta.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.Utils;
import org.jandroid.cnbeta.async.EditorRecommendListAsyncTask;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.LoadingAsyncTask;
import org.jandroid.cnbeta.entity.EditorRecommend;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class EditorRecommendListFragment extends AbstractPagingListFragment<EditorRecommend> {

    @Override
    protected BaseAdapter newAdapter() {
        return new BaseAdapter() {
            public int getCount() {
                return loadedDatas.size();
            }

            public Object getItem(int position) {
                return loadedDatas.get(position);
            }

            public long getItemId(int position) {
                return position;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.lv_editor_recommend_article_item, null);
                }
                EditorRecommend article = loadedDatas.get(position);
                TextView tvTitle = (TextView) convertView.findViewById(R.id.tile);
                tvTitle.setText(article.getTitle());
                TextView tvHometextShowShort = (TextView) convertView.findViewById(R.id.hometext_show_short);
                tvHometextShowShort.setText(article.getHometextShowShort());
                return convertView;
            }
        };
    }

    @Override
    public LoadingAsyncTask<List<EditorRecommend>> newAsyncTask() {
        return new EditorRecommendListAsyncTask() {
            @Override
            protected int getPage() {
                return EditorRecommendListFragment.this.getNextPage();
            }

            @Override
            public HasAsync<List<EditorRecommend>> getAsyncContext() {
                return EditorRecommendListFragment.this;
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EditorRecommend article = loadedDatas.get(position);
        Utils.openContentActivity(getActivity(), article.getSid(), article.getTitle());
    }

    @Override
    public void onProgressShow() {
        super.onProgressShow();
    }

    @Override
    public void onProgressDismiss() {
        super.onProgressDismiss();
    }
}
