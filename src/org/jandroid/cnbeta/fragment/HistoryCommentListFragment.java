package org.jandroid.cnbeta.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.CnBetaPreferences;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.Utils;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.HistoryCommentListAsyncTask;
import org.jandroid.cnbeta.entity.HistoryComment;
import org.jandroid.common.FontUtils;
import org.jandroid.common.async.AsyncResult;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class HistoryCommentListFragment extends AbstractAsyncListFragment<HistoryComment> {

    public HistoryCommentListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HistoryComment comment = (HistoryComment) getAdapter().getItem(position);
        Utils.openContentActivity(getActivity(), comment, getAllDatas());
    }

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
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_history_comment_item, null);
                }
                HistoryComment comment = getData(position);

                TextView toTextView = (TextView) convertView.findViewById(R.id.to);
                toTextView.getPaint().setFakeBoldText(true);

                TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
                tvTitle.setText(comment.getTitle());
                TextView tvComment = (TextView) convertView.findViewById(R.id.comment);
                tvComment.setText(comment.getComment());

                TextView dateTextView = (TextView) convertView.findViewById(R.id.date);
                dateTextView.setText(comment.getDate());

                int fontSizeOffset = ((CnBetaApplicationContext)getActivity().getApplicationContext()).getCnBetaPreferences().getFontSizeOffset();
                FontUtils.updateTextSize(getActivity(), toTextView, R.dimen.listitem_description_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), tvTitle, R.dimen.listitem_description_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), tvComment, R.dimen.listitem_description_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), dateTextView, R.dimen.listitem_status_text_size, fontSizeOffset);

                updateTypeFace(convertView);
                return convertView;
            }
        };
    }

    @Override
    public void loadData() {
        executeAsyncTaskMultiThreading(new HistoryCommentListAsyncTask() {

            @Override
            public HasAsync<List<HistoryComment>> getAsyncContext() {
                return HistoryCommentListFragment.this;
            }
        });
    }

    @Override
    public void onSuccess(AsyncResult<List<HistoryComment>> listAsyncResult) {
        clearData();
        // reverse!!!
        Collections.reverse(listAsyncResult.getResult());
        super.onSuccess(listAsyncResult);
    }

    @Override
    public void reloadData() {
        loadData();
        mListView.setSelection(0);
    }

}
