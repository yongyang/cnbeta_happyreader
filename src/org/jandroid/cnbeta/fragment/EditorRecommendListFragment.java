package org.jandroid.cnbeta.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.CnBetaPreferences;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.Utils;
import org.jandroid.cnbeta.async.EditorRecommendListAsyncTask;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.HasAsyncDelegate;
import org.jandroid.cnbeta.entity.EditorRecommend;
import org.jandroid.cnbeta.view.PagingView;
import org.jandroid.common.FontUtils;
import org.jandroid.common.async.AsyncResult;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class EditorRecommendListFragment extends AbstractAsyncListFragment<EditorRecommend> {

    private PagingView footerPagingView;

    @Override
    protected BaseAdapter newAdapter() {
        return new BaseAdapter() {
            public int getCount() {
                return getDataSize();
            }

            public Object getItem(int position) {
                return getData(position);
            }

            public long getItemId(int position) {
                return position;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_editor_recommend_article_item, null);
                }
                EditorRecommend editorRecommend = getData(position);
                TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
                tvTitle.setText(editorRecommend.getTitle());
                TextView tvHomeText = (TextView) convertView.findViewById(R.id.hometext);
                tvHomeText.setText(editorRecommend.getHometextShowShort());

                TextView tvTime = (TextView) convertView.findViewById(R.id.time);
                tvTime.setText(editorRecommend.getTime());

                int fontSizeOffset = ((CnBetaApplicationContext)getActivity().getApplicationContext()).getCnBetaPreferences().getFontSizeOffset();
                FontUtils.updateTextSize(getActivity(), tvTitle, R.dimen.listitem_title_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), tvHomeText, R.dimen.listitem_description_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), tvTime, R.dimen.listitem_status_text_size, fontSizeOffset);

                updateTypeFace(convertView);
                return convertView;
            }
        };
    }

    @Override
    public void loadData() {
        executeAsyncTaskMultiThreading(new EditorRecommendListAsyncTask() {
            @Override
            protected int getPage() {
                return footerPagingView.getNextPage();
            }

            @Override
            public HasAsync<List<EditorRecommend>> getAsyncContext() {
                return EditorRecommendListFragment.this;
            }
        });
    }

    public void reloadData() {
        executeAsyncTaskMultiThreading(new EditorRecommendListAsyncTask() {
            @Override
            protected int getPage() {
                footerPagingView.resetPage();
                return footerPagingView.getNextPage();
            }

            @Override
            public HasAsync<List<EditorRecommend>> getAsyncContext() {
                return new HasAsyncDelegate<List<EditorRecommend>>(EditorRecommendListFragment.this) {
                    @Override
                    public void onSuccess(AsyncResult<List<EditorRecommend>> listAsyncResult) {
                        clearData();
                        super.onSuccess(listAsyncResult);
                        mListView.setSelection(0);
                    }
                };
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EditorRecommend article = getData(position);
        Utils.openContentActivity(getActivity(), article.getSid(), article.getTitle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        footerPagingView = PagingView.load(getActivity().getLayoutInflater(), R.layout.listvew_footbar_paging);

        ((ListView)mListView).addFooterView(footerPagingView.getRootView());

        footerPagingView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadData();
            }
        });

        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onSuccess(AsyncResult<List<EditorRecommend>> listAsyncResult) {
        super.onSuccess(listAsyncResult);
        footerPagingView.increasePage();
    }

    @Override
    public void onProgressShow() {
        super.onProgressShow();
        footerPagingView.onProgressShow();
    }

    @Override
    public void onProgressDismiss() {
        super.onProgressDismiss();
        footerPagingView.onProgressDismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTypeFace(footerPagingView.getRootView());
    }

}
