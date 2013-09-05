package org.jandroid.cnbeta;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.ToastUtils;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ReplyCommentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_reply);

        Comment comment = (Comment)getIntent().getSerializableExtra("comment");

        TextView replyCommentTitleTextView = (TextView)findViewById(R.id.re_comment);
        replyCommentTitleTextView.setText(comment.getComment());

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
    }

    public void reply(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
