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
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.common.BaseFragment;

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
                Comment comment = comments.get(position);
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

                TextView scoreTextView = (TextView)convertView.findViewById(R.id.score);
                scoreTextView.setText("" + comment.getScore());
                TextView reasonTextView = (TextView)convertView.findViewById(R.id.reason);
                reasonTextView.setText("" + comment.getReason());


                LinearLayout supportLinearLayout = (LinearLayout)convertView.findViewById(R.id.supportLinearLayout);
                LinearLayout againstLinearLayout = (LinearLayout)convertView.findViewById(R.id.againstLinearLayout);
                supportLinearLayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "support", Toast.LENGTH_SHORT).show();
                    }
                });
                againstLinearLayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "against", Toast.LENGTH_SHORT).show();
                    }
                });
                return convertView;
            }
        };

        commentsListView.setAdapter(adapter);

        commentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "setOnItemClickListener", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
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
