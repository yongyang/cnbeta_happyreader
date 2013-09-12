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
import org.jandroid.common.BaseFragment;
import org.jandroid.common.ToastUtils;
import org.jandroid.common.async.AsyncResult;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleCommentsFragment extends AbstractAsyncListFragment<Comment> {

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

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.listview_comments, container, false);
        mListView = (ListView) rootView.findViewById(R.id.comments_listview);
        // so button in Item can click
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

    private int getCommentCount(){
        return ((ContentActivity)getActivity()).getContent().getCommentNum();
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

                //comment一页100条
                if(getCommentCount() > 100) {
                    positionTextView.setText("" + (getCommentCount() - position));
                }
                else {
                    positionTextView.setText("" + (getDataSize() - position));
                }

                TextView nameTextView = (TextView) convertView.findViewById(R.id.name);
                nameTextView.setText(comment.getName());

                TextView hostNameTextView = (TextView) convertView.findViewById(R.id.host_name);
                hostNameTextView.setText("[" + comment.getHostName() + "]");
                TextView dateTextView = (TextView) convertView.findViewById(R.id.date);
                dateTextView.setText(comment.getDate());

                TextView commentTextView = (TextView) convertView.findViewById(R.id.comment);
                commentTextView.setText(comment.getComment());

                LinearLayout supportAgainstLinearLayout = (LinearLayout) convertView.findViewById(R.id.support_against_lineLayout);
                if (comment.getTid() == 0) { //新发表的评论，只是暂存，无法支持和反对
                    supportAgainstLinearLayout.setVisibility(View.INVISIBLE);
                }
                else {
                    supportAgainstLinearLayout.setVisibility(View.VISIBLE);
                    final TextView supportTextView = (TextView) convertView.findViewById(R.id.supportText);
                    final TextView againstTextView = (TextView) convertView.findViewById(R.id.againstText);

                    final TextView scoreTextView = (TextView) convertView.findViewById(R.id.score);
                    scoreTextView.setText("" + comment.getScore());
                    final TextView reasonTextView = (TextView) convertView.findViewById(R.id.reason);
                    reasonTextView.setText("" + comment.getReason());

                    final LinearLayout supportLinearLayout = (LinearLayout) convertView.findViewById(R.id.supportLinearLayout);
                    final LinearLayout againstLinearLayout = (LinearLayout) convertView.findViewById(R.id.againstLinearLayout);
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

                    final TextView toParentTextView = (TextView) convertView.findViewById(R.id.toParent);
                    if (comment.getPid() == 0) {
                        toParentTextView.setVisibility(View.GONE);
                    }
                    else {
                        toParentTextView.setVisibility(View.VISIBLE);
                        toParentTextView.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                logger.d("position: " + position);
                                showParentComment(position, comment.getPid());
                            }
                        });
                    }
                }
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
                        supportLinearLayout.setClickable(false);

                        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.8f, 1.0f, 1.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        scaleAnimation.setFillAfter(false);
                        scaleAnimation.setDuration(200);
                        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                            public void onAnimationStart(Animation animation) {
                            }

                            public void onAnimationEnd(Animation animation) {
                                if (isSupport) {
                                    supportTextView.setText("已支持");
                                    scoreTextView.setText("" + (comment.getScore() + 1));
                                }
                                else {
                                    supportTextView.setText("已反对");
                                    scoreTextView.setText("" + (comment.getReason() + 1));
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

    @Override
    public void reloadData() {
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
                    return new HasAsyncDelegate<List<Comment>>(ArticleCommentsFragment.this){
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


    private void showParentComment(int position, long parentId) {
        int parentPos = position;
        int index = position;
        while (index < getDataSize()) {
            if (getData(index).getTid() == parentId) {
                parentPos = index;
                break;
            }
            index++;
        }
        mListView.setItemChecked(parentPos, true);
        mListView.setSelection(parentPos);
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
        ((ContentActivity) getActivity()).updateCommentNumbers();

    }

    @Override
    public void onProgressShow() {
        //TODO: refresh action view only page=1
        super.onProgressShow();
        footerPagingView.onProgressShow();
        if (footerPagingView.getPage() == 1) { //page 1 is reload
//            startRotateRefreshActionView();
        }

    }

    @Override
    public void onProgressDismiss() {
        //TODO: refresh action view only page=1
        super.onProgressDismiss();
        footerPagingView.onProgressDismiss();
        // stop refresh rotation anyway
        if (footerPagingView.getPage() == 1) { //page 1 is reload
//            stopRotateRefreshActionView();
        }
    }

}
