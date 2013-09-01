package org.jandroid.cnbeta.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import org.jandroid.common.ToastUtils;
import org.jandroid.common.async.AsyncResult;
import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleCommentsFragment extends BaseFragment {


    private ListView commentsListView;

    private final List<Comment> comments = new ArrayList<Comment>();

    private BaseAdapter adapter;

    private TextView scoreTextView;
    private TextView reasonTextView;


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

        View rootView = inflater.inflate(R.layout.comments_listview, container, false);
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
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.comment_item, null);
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

                scoreTextView = (TextView)convertView.findViewById(R.id.score);
                scoreTextView.setText("" + comment.getScore());
                reasonTextView = (TextView)convertView.findViewById(R.id.reason);
                reasonTextView.setText("" + comment.getReason());

                LinearLayout supportLinearLayout = (LinearLayout)convertView.findViewById(R.id.supportLinearLayout);
                LinearLayout againstLinearLayout = (LinearLayout)convertView.findViewById(R.id.againstLinearLayout);
                TextView replyButton = (TextView)convertView.findViewById(R.id.reply);
                supportLinearLayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        supportComment(comment, true);
                    }
                });
                againstLinearLayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        supportComment(comment, false);
                    }
                });
                replyButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Utils.openReplyCommentActivity(getActivity(), ((ContentActivity)getActivity()).getContent(), comment);
                    }
                });
                return convertView;
            }
        };

        commentsListView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void supportComment(final Comment comment, final boolean isSupport) {
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
                        //TODO: +1
                        ToastUtils.showShortToast(getActivity(), "supported");

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

    //TODO: 点击 Parent 跳转到 parent comment
}
