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
import org.jandroid.common.FontUtils;
import org.jandroid.common.async.AsyncResult;
import org.json.simple.JSONObject;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleHotCommentsFragment extends AbstractListFragment<Comment> {

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

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.listview_default, container, false);
        mListView = (ListView) rootView.findViewById(R.id.article_listview);
        // so button in Item can click
        ((ListView) mListView).setItemsCanFocus(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
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
                //最热评论 不显示楼号
                positionTextView.setVisibility(View.GONE);
                convertView.findViewById(R.id.positionWord).setVisibility(View.GONE);

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
                    parentCommentTextView.setVisibility(View.GONE);
                    parentCommentTextView.setText("");
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
}
