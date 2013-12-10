package org.jandroid.cnbeta.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.CnBetaPreferences;
import org.jandroid.cnbeta.ContentActivity;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.Utils;
import org.jandroid.cnbeta.async.ArticleCommentsAsyncTask;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.HasAsyncDelegate;
import org.jandroid.cnbeta.async.SupportCommentAsyncTask;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.loader.SupportCommentPoster;
import org.jandroid.cnbeta.view.PagingView;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.FontUtils;
import org.jandroid.common.async.AsyncResult;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleCommentsFragment extends AbstractAsyncListFragment<Comment> {

    //TODO: 如果有评论数目，但是没有取回评论，则显示评论已关闭. 检测 joinNumber !=0, 但是 commentList.length()==0

    private PagingView footerPagingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ((ListView) mListView).setItemsCanFocus(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        footerPagingView = PagingView.load(getActivity().getLayoutInflater(), R.layout.listvew_footbar_paging);

        ((ListView) mListView).addFooterView(footerPagingView.getRootView());

        footerPagingView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadData();
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    private int getCommentCount() {
        return ((ContentActivity) getActivity()).getContent().getJoinNum();
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

            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_comment_item, null);
                }
                final Comment comment = (Comment) getItem(position);
                TextView positionTextView = (TextView) convertView.findViewById(R.id.position);

                positionTextView.setText("" + (getCommentCount() - position));

                TextView nameTextView = (TextView) convertView.findViewById(R.id.name);
                nameTextView.setText(comment.getName());

                TextView hostNameTextView = (TextView) convertView.findViewById(R.id.host_name);
                hostNameTextView.setText("[" + comment.getHostName() + "]");
                TextView dateTextView = (TextView) convertView.findViewById(R.id.date);
                dateTextView.setText(comment.getDate());

                TextView commentTextView = (TextView) convertView.findViewById(R.id.comment);
                commentTextView.setText(comment.getComment());

                TextView parentCommentTextView = (TextView) convertView.findViewById(R.id.parentComment);

                LinearLayout supportAgainstLinearLayout = (LinearLayout) convertView.findViewById(R.id.support_against_lineLayout);
                final TextView supportTextView = (TextView) convertView.findViewById(R.id.supportText);
                final TextView againstTextView = (TextView) convertView.findViewById(R.id.againstText);
                supportTextView.setText(R.string.support);
                againstTextView.setText(R.string.against);
                final TextView scoreTextView = (TextView) convertView.findViewById(R.id.score);
                scoreTextView.setText("" + comment.getScore());
                final TextView reasonTextView = (TextView) convertView.findViewById(R.id.reason);
                reasonTextView.setText("" + comment.getReason());
                final LinearLayout supportLinearLayout = (LinearLayout) convertView.findViewById(R.id.supportLinearLayout);
                final LinearLayout againstLinearLayout = (LinearLayout) convertView.findViewById(R.id.againstLinearLayout);

                if (comment.getTid() == 0) { //新发表的评论，只是暂存，无法支持和反对
                    supportAgainstLinearLayout.setVisibility(View.INVISIBLE);
                    supportLinearLayout.setEnabled(false);
                    againstLinearLayout.setEnabled(false);
                    parentCommentTextView.setText("");
                    parentCommentTextView.setVisibility(View.GONE);
                }
                else {
                    supportAgainstLinearLayout.setVisibility(View.VISIBLE);
                    supportLinearLayout.setEnabled(true);
                    againstLinearLayout.setEnabled(true);

                    if (comment.isSupported()) { // 已支持
                        supportTextView.setText(R.string.supported);
                        supportLinearLayout.setEnabled(false);
                    }
                    if (comment.isAgainsted()) { // 已反对
                        againstTextView.setText(R.string.againsted);
                        againstLinearLayout.setEnabled(false);
                    }

                    supportLinearLayout.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            supportComment(supportLinearLayout, supportTextView, scoreTextView, comment, true);
                        }
                    });
                    againstLinearLayout.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            supportComment(againstLinearLayout, againstTextView, reasonTextView, comment, false);
                        }
                    });
                    if (comment.getPid() == 0) { // 没有父评论
                        parentCommentTextView.setVisibility(View.GONE);
                        parentCommentTextView.setText("");
                    }
                    else { // 有父评论
                        final int parentPos = getParentPosition(position, comment.getPid());
                        if(parentPos != position && parentPos < getDataSize()) { //多页时，避免调至还未加载的评论
                            parentCommentTextView.setText(getData(parentPos).getComment());
                            parentCommentTextView.setVisibility(View.VISIBLE);
                            parentCommentTextView.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    mListView.setItemChecked(parentPos, true);
                                    mListView.setSelection(parentPos);
                                }
                            });
                        }
                    }
                }

                int fontSizeOffset = ((CnBetaApplicationContext) getActivity().getApplicationContext()).getCnBetaPreferences().getFontSizeOffset();
                FontUtils.updateTextSize(getActivity(), positionTextView, R.dimen.listitem_description_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), nameTextView, R.dimen.listitem_description_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), hostNameTextView, R.dimen.listitem_description_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), dateTextView, R.dimen.listitem_status_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), commentTextView, R.dimen.listitem_comment_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), supportTextView, R.dimen.listitem_comment_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), againstTextView, R.dimen.listitem_comment_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), scoreTextView, R.dimen.listitem_comment_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), reasonTextView, R.dimen.listitem_comment_text_size, fontSizeOffset);

                updateTypeFace(convertView);
                return convertView;
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Comment comment = getData(position);
        Utils.openReplyCommentActivityForResult(getActivity(), comment);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void supportComment(final LinearLayout supportLinearLayout, final TextView supportTextView, final TextView scoreTextView, final Comment comment, final boolean isSupport) {
        ((BaseActivity) getActivity()).executeAsyncTaskMultiThreading(new SupportCommentAsyncTask() {
            @Override
            protected Comment getComment() {
                return comment;
            }

            @Override
            protected SupportCommentPoster.Op getOp() {
                return isSupport ? SupportCommentPoster.Op.SUPPORT : SupportCommentPoster.Op.AGAINST;
            }

            @Override
            public HasAsync<JSONObject> getAsyncContext() {
                return new HasAsync<JSONObject>() {
                    public CnBetaApplicationContext getCnBetaApplicationContext() {
                        return ((CnBetaApplicationContext) getActivity().getApplicationContext());
                    }

                    public void onProgressShow() {

                    }

                    public void onProgressDismiss() {

                    }

                    public void onFailure(AsyncResult<JSONObject> jsonObjectAsyncResult) {

                    }

                    public void onSuccess(AsyncResult<JSONObject> jsonObjectAsyncResult) {
                        supportLinearLayout.setEnabled(false);

                        if (isSupport) {
                            // supported
                            getComment().setSupported(true);
                            getComment().setScore(getComment().getScore() + 1);
                        }
                        else {
                            getComment().setAgainsted(true);
                            getComment().setReason(getComment().getReason() + 1);
                        }

                        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.8f, 1.0f, 1.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        scaleAnimation.setFillAfter(false);
                        scaleAnimation.setDuration(200);
                        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                            public void onAnimationStart(Animation animation) {
                            }

                            public void onAnimationEnd(Animation animation) {
                                if (isSupport) {
                                    supportTextView.setText(R.string.supported);
                                    scoreTextView.setText("" + comment.getScore());
                                }
                                else {
                                    supportTextView.setText(R.string.againsted);
                                    scoreTextView.setText("" + comment.getReason());
                                }
                            }

                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        scoreTextView.startAnimation(scaleAnimation);
                    }
                };
            }
        });
    }

    @Override
    public void loadData() {
        if (getActivity() != null) {
            if (((ContentActivity) getActivity()).isPageLoaded()) {
                executeAsyncTaskMultiThreading(new ArticleCommentsAsyncTask() {
                    @Override
                    protected Content getArticleContent() {
                        return ((ContentActivity) getActivity()).getContent();
                    }

                    @Override
                    protected int getPage() {
                        return footerPagingView.getNextPage();
                    }

                    @Override
                    public HasAsync<List<Comment>> getAsyncContext() {
                        return new HasAsyncDelegate<List<Comment>>(ArticleCommentsFragment.this);
                    }
                }
                );
            }
        }
    }

    @Override
    public void reloadData() {
        if (getActivity() != null) {
            if (((ContentActivity) getActivity()).isPageLoaded()) {
                executeAsyncTaskMultiThreading(new ArticleCommentsAsyncTask() {
                    @Override
                    protected Content getArticleContent() {
                        return ((ContentActivity) getActivity()).getContent();
                    }

                    @Override
                    protected int getPage() {
                        footerPagingView.resetPage();
                        return footerPagingView.getNextPage();
                    }

                    @Override
                    public HasAsync<List<Comment>> getAsyncContext() {
                        return new HasAsyncDelegate<List<Comment>>(ArticleCommentsFragment.this) {
                            @Override
                            public void onSuccess(AsyncResult<List<Comment>> listAsyncResult) {
                                clearData();
                                super.onSuccess(listAsyncResult);
                                // scroll to top
                                mListView.setSelection(0);
                            }
                        };
                    }
                }
                );
            }
        }
    }


    // return parent position or self position
    private int getParentPosition(int position, long parentId) {
        int index = position;
        while (index < getDataSize()) {
            if (getData(index).getTid() == parentId) {
                break;
            }
            index++;
        }
        return index;
    }


    public void newPostedComment(Comment comment) {
        addData(0, comment); // 放在最上面
        adapter.notifyDataSetChanged();
        mListView.setSelection(0);
    }

    @Override
    public void onSuccess(AsyncResult<List<Comment>> listAsyncResult) {
        super.onSuccess(listAsyncResult);
        footerPagingView.increasePage();
        // update comment count in ContentFragment
        if (footerPagingView.getPage() == 1) { // 仅第一页需要 update comment numbers
            if(getActivity() != null) {
                ((ContentActivity) getActivity()).updateCommentNumbers();
            }
        }
        if(getActivity() != null) {
            ((ContentActivity) getActivity()).updateHotComments(listAsyncResult.getResult());
        }
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

    public void setCommentClosed() {
        emptyView.setText("评论功能已关闭");
        setDatas(Collections.EMPTY_LIST);
    }

}
