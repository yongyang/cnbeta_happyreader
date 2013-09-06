package org.jandroid.cnbeta;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.jandroid.cnbeta.async.CaptchaAsyncTask;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.PublishCommentAsyncTask;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.DateFormatUtils;
import org.jandroid.common.ToastUtils;
import org.jandroid.common.async.AsyncResult;
import org.json.simple.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ReplyCommentActivity extends BaseActivity {

    private ProgressBar captchaProgressBar;
    private ProgressBar postProgressBar;

    private ImageView captchaImageView;

    private TextView commentTextView;
    private TextView captchaTextView;


    private Comment comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_reply);

        comment = (Comment)getIntent().getSerializableExtra("comment");

        captchaImageView = (ImageView)findViewById(R.id.seccode_image);

        captchaImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initCaptcha();
            }
        });

        captchaProgressBar = (ProgressBar)findViewById(R.id.captcha_progessBar);

        TextView replyCommentTitleTextView = (TextView)findViewById(R.id.re_comment);
        replyCommentTitleTextView.setText(comment.getComment());

        commentTextView = (TextView)findViewById(R.id.comment);
        captchaTextView = (TextView)findViewById(R.id.seccode);

        TextView cancelTextView = (TextView)findViewById(R.id.cancel);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        TextView sendTextView = (TextView)findViewById(R.id.send);
        sendTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reply();
            }
        });

        postProgressBar = (ProgressBar)findViewById(R.id.post_progessBar);
    }

    public Comment getComment() {
        return comment;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initCaptcha();
    }

    private void initCaptcha() {
        executeAsyncTaskMultiThreading(new CaptchaAsyncTask() {
            @Override
            protected long getSid() {
                return comment.getSid();
            }

            @Override
            public HasAsync<Bitmap> getAsyncContext() {
                return new HasAsync<Bitmap>() {
                    public CnBetaApplicationContext getCnBetaApplicationContext() {
                        return (CnBetaApplication) ReplyCommentActivity.this.getApplicationContext();
                    }

                    public void onProgressShow() {
                        captchaProgressBar.setVisibility(View.VISIBLE);
                        captchaImageView.setVisibility(View.GONE);
                    }

                    public void onProgressDismiss() {
                        captchaProgressBar.setVisibility(View.GONE);
                    }

                    public void onSuccess(AsyncResult<Bitmap> bitmapAsyncResult) {
                        captchaImageView.setImageBitmap(bitmapAsyncResult.getResult());
                        captchaImageView.setVisibility(View.VISIBLE);
                    }

                    public void onFailure(AsyncResult<Bitmap> bitmapAsyncResult) {
                        //TODO: 显示请刷新提示图片
                        captchaImageView.setVisibility(View.VISIBLE);
                        logger.e("获取验证码失败，点击刷新！");
                    }
                };
            }
        });
    }


    private void reply(){

        if(commentTextView.getText().toString().isEmpty() || captchaTextView.getText().toString().isEmpty() ) {
            ToastUtils.showShortToast(ReplyCommentActivity.this, "请输入评论内容和验证码！");
            return;
        }

        executeAsyncTaskMultiThreading(new PublishCommentAsyncTask() {
            @Override
            protected long getSid() {
                return getComment().getSid();
            }

            @Override
            protected long getPid() {
                return getComment().getTid();
            }

            @Override
            protected String getCommentContent() {
                return commentTextView.getText().toString();
            }

            @Override
            protected String getSeccode() {
                return captchaTextView.getText().toString();
            }

            @Override
            public HasAsync<JSONObject> getAsyncContext() {
                return new HasAsync<JSONObject>() {
                    public CnBetaApplicationContext getCnBetaApplicationContext() {
                        return (CnBetaApplication) ReplyCommentActivity.this.getApplicationContext();
                    }

                    public void onProgressShow() {
                        postProgressBar.setVisibility(View.VISIBLE);
                    }

                    public void onProgressDismiss() {
                        postProgressBar.setVisibility(View.INVISIBLE);
                    }

                    public void onSuccess(AsyncResult<JSONObject> jsonObjectAsyncResult) {
                        JSONObject resultJSON = jsonObjectAsyncResult.getResult();
                        if(!resultJSON.get("status").equals("success")) {
                            initCaptcha(); // 重新生成验证码
                            String message = ((JSONObject)resultJSON.get("result")).get("message").toString();
                            ToastUtils.showShortToast(ReplyCommentActivity.this, message);
                        }
                        else {
                            Comment newComment = new Comment();
                            newComment.setName("匿名人士");
                            newComment.setComment(getCommentContent());
                            newComment.setSid(getSid());
                            newComment.setDate(DateFormatUtils.getDefault().format(new Date()));
                            newComment.setHostName("");
                            //设置结果给 ContentActivity, 新comment 将添加到 comments list 中，并刷新 List
                            Intent intent = new Intent();
                            intent.putExtra("comment", newComment);
                            ReplyCommentActivity.this.setResult(0, intent);
                            finish();
                        }
                    }

                    public void onFailure(AsyncResult<JSONObject> jsonObjectAsyncResult) {

                    }
                };
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
