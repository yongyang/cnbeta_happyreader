package org.jandroid.cnbeta;

import android.os.Bundle;
import android.widget.TextView;
import org.jandroid.cnbeta.entity.Comment;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ReplyCommentActivity extends PublishCommentActivity {

    private Comment parentComment;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.comment_reply;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentComment = (Comment)getIntent().getSerializableExtra("comment");
        TextView replyCommentTitleTextView = (TextView)findViewById(R.id.re_comment);
        replyCommentTitleTextView.setText(parentComment.getComment());
    }

    @Override
    public long getParentCommentTid() {
        return parentComment.getTid();
    }
}
