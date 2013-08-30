package org.jandroid.cnbeta.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.ContentActivity;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.Utils;
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

    private final Handler handler = new Handler();

    private BaseAdapter adapter;

    private ProgressBar progressBarRefresh;
    private LinearLayout lineLayoutRefresh;
    private TextView tvLastTimeRefresh;

    private TextView scoreTextView;
    private TextView reasonTextView;

    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private MenuItem refreshMenuItem;
    private ImageView refreshActionView;
    private Animation clockWiseRotationAnimation;

    private LinearLayout footbarRefresh;

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

        refreshActionView = (ImageView) inflater.inflate(R.layout.iv_refresh_action_view, null);
        clockWiseRotationAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotation_clockwise_refresh);
        clockWiseRotationAnimation.setRepeatCount(Animation.INFINITE);

        View rootView = inflater.inflate(R.layout.comments_listview, container, false);
        commentsListView = (ListView)rootView.findViewById(R.id.comments_listview);
        commentsListView.setItemsCanFocus(true);
		return rootView;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        footbarRefresh = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.lv_footbar_refresh, commentsListView,false);
        progressBarRefresh = (ProgressBar) footbarRefresh.findViewById(R.id.progressBar_refresh);
        lineLayoutRefresh = (LinearLayout)footbarRefresh.findViewById(R.id.linelayout_refresh);
        tvLastTimeRefresh = (TextView)footbarRefresh.findViewById(R.id.refresh_last_time);

        commentsListView.addFooterView(footbarRefresh);

        footbarRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                reloadArticles();
            }
        });

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
                        Utils.openCommentActivity(getActivity(), null);
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
            public void showProgressUI() {

            }

            @Override
            public void dismissProgressUI() {

            }

            @Override
            public CnBetaApplicationContext getCnBetaApplicationContext() {
                return ((CnBetaApplicationContext)getActivity().getApplicationContext());
            }

            @Override
            protected void onPostExecute(AsyncResult<JSONObject> jsonObjectAsyncResult) {
                super.onPostExecute(jsonObjectAsyncResult);
                if(jsonObjectAsyncResult.isSuccess()) {
                    //TODO: +1
                    ToastUtils.showShortToast(getActivity(), "supported");
                }
                else {
                    ToastUtils.showLongToast(getActivity(), "supported failed");
                }
            }
        });
    }

/*
    private void reloadArticles(){
        EnvironmentUtils.checkNetworkConnected(getActivity());

        ((BaseActivity)getActivity()).executeAsyncTaskMultiThreading(new RealtimeArticleListAsyncTask() {
            @Override
            public CnBetaApplicationContext getCnBetaApplicationContext() {
                return (CnBetaApplicationContext) getActivity().getApplication();
            }

            @Override
            public void showProgressUI() {
                // should call setProgressBarIndeterminate(true) each time before setProgressBarVisibility(true)
                getActivity().setProgressBarIndeterminate(true);
                getActivity().setProgressBarVisibility(true);
                footbarRefresh.setClickable(false);
                progressBarRefresh.setVisibility(View.VISIBLE);
                lineLayoutRefresh.setVisibility(View.GONE);
                // rotate refresh item
                rotateRefreshActionView();
            }

            @Override
            public void dismissProgressUI() {
                getActivity().setProgressBarVisibility(false);
                progressBarRefresh.setVisibility(View.GONE);
                lineLayoutRefresh.setVisibility(View.VISIBLE);
                //Stop refresh animation anyway
                dismissRefreshActionView();
                footbarRefresh.setClickable(true);
            }

            @Override
            protected void onPostExecute(AsyncResult<List<RealtimeArticle>> asyncResult) {
                super.onPostExecute(asyncResult);
                if (asyncResult.isSuccess()) {
                    List<RealtimeArticle> articles = asyncResult.getResult();
                    if (articles != null) {
                        tvLastTimeRefresh.setText(dateFormat.format(new Date()));
                        comments.clear();
                        comments.addAll(articles);
                        adapter.notifyDataSetChanged();
                        commentsListView.smoothScrollToPosition(0);
                    }
                }
                else {
                    logger.w(asyncResult.getErrorMsg(), asyncResult.getException());
                    Toast.makeText(getActivity(), asyncResult.getErrorMsg(), Toast.LENGTH_LONG).show();
                }
            }
        }
        );
    }
*/

/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //refresh actionitem
        inflater.inflate(R.menu.article_list_fragment_menu, menu);
        refreshMenuItem = menu.findItem(R.id.refresh_item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.isCheckable()) {
            item.setChecked(true);
        }
        switch (item.getItemId()) {
            case R.id.more_item:
                break;
            case R.id.refresh_item:
//                reloadArticles();
            default:
        }
        return true;
    }
*/

    private void rotateRefreshActionView() {
        if(refreshMenuItem != null) {
            /* Attach a rotating ImageView to the refresh item as an ActionView */
            refreshActionView.startAnimation(clockWiseRotationAnimation);
            refreshMenuItem.setActionView(refreshActionView);
        }
    }

    private void dismissRefreshActionView() {
        if(refreshMenuItem != null) {
            View actionView = refreshMenuItem.getActionView();
            if(actionView != null) {
                actionView.clearAnimation();
                refreshMenuItem.setActionView(null);
            }
        }
    }

    public void updateComments(List<Comment> comments){
        this.comments.clear();
        this.comments.addAll(comments);
        adapter.notifyDataSetChanged();
    }

    //TODO: 点击 Parent 跳转到 parent comment
}
