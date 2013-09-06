package org.jandroid.cnbeta.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.ContentActivity;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.Utils;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.SupportCommentAsyncTask;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.loader.SupportCommentPoster;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.BaseFragment;
import org.jandroid.common.async.AsyncResult;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleCommentsFragment extends BaseFragment {


    private ListView commentsListView;

    private final List<Comment> comments = new ArrayList<Comment>();

    private BaseAdapter adapter;

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

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.listview_comments, container, false);
        commentsListView = (ListView)rootView.findViewById(R.id.comments_listview);
        commentsListView.setItemsCanFocus(true);
		return rootView;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new BaseAdapter() {
            public int getCount() {
                return comments.size();
            }

            public Object getItem(int position) {
                return comments.get(position);
            }

            public long getItemId(int position) {
                return position;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_comment_item, null);
                }
                final Comment comment = comments.get(position);
                TextView positionTextView = (TextView)convertView.findViewById(R.id.position);
                positionTextView.setText("" + (adapter.getCount() - position));
                TextView nameTextView = (TextView)convertView.findViewById(R.id.name);
                nameTextView.setText(comment.getName());

                TextView hostNameTextView = (TextView)convertView.findViewById(R.id.host_name);
                hostNameTextView.setText("[" + comment.getHostName() + "]");
                TextView dateTextView = (TextView)convertView.findViewById(R.id.date);
                dateTextView.setText(comment.getDate());

                TextView commentTextView = (TextView)convertView.findViewById(R.id.comment);
                commentTextView.setText(comment.getComment());

                LinearLayout supportAgainstLinearLayout = (LinearLayout)convertView.findViewById(R.id.support_against_lineLayout);
                if(comment.getTid() == 0) { //新发表的评论，只是暂存，无法支持和反对
                    supportAgainstLinearLayout.setVisibility(View.INVISIBLE);
                }
                else {

                    final TextView supportTextView = (TextView)convertView.findViewById(R.id.supportText);
                    final TextView againstTextView = (TextView)convertView.findViewById(R.id.againstText);

                    final TextView scoreTextView = (TextView)convertView.findViewById(R.id.score);
                    scoreTextView.setText("" + comment.getScore());
                    final TextView reasonTextView = (TextView)convertView.findViewById(R.id.reason);
                    reasonTextView.setText("" + comment.getReason());

                    final LinearLayout supportLinearLayout = (LinearLayout)convertView.findViewById(R.id.supportLinearLayout);
                    final LinearLayout againstLinearLayout = (LinearLayout)convertView.findViewById(R.id.againstLinearLayout);
                    TextView replyButton = (TextView)convertView.findViewById(R.id.reply);
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
                    replyButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Utils.openReplyCommentActivityForResult(getActivity(), comment);
                        }
                    });
                }
                return convertView;
            }
        };

        commentsListView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void supportComment(final LinearLayout supportLinearLayout, final TextView supportTextView, final TextView scoreTextView, final Comment comment, final boolean isSupport) {
        ((BaseActivity)getActivity()).executeAsyncTaskMultiThreading(new SupportCommentAsyncTask() {
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
                        return ((CnBetaApplicationContext)getActivity().getApplicationContext());
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

    public void updateComments(List<Comment> comments){
        this.comments.clear();
        this.comments.addAll(comments);
        adapter.notifyDataSetChanged();
    }

    public void appendComment(Comment comment) {
        this.comments.add(0, comment); // 放在最上面
        adapter.notifyDataSetChanged();
        commentsListView.smoothScrollToPosition(0);
    }

    //TODO: 点击 Parent 跳转到 parent comment
}
