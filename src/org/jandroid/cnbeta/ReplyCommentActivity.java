package org.jandroid.cnbeta;

import android.os.Bundle;
import android.widget.TextView;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.common.FontUtils;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ReplyCommentActivity extends PublishCommentActivity {

    private Comment parentComment;
    private TextView replyCommentTitleTextView;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.comment_reply;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentComment = (Comment)getIntent().getSerializableExtra("comment");
        replyCommentTitleTextView = (TextView)findViewById(R.id.re_comment);
        replyCommentTitleTextView.setText(parentComment.getComment());
    }

    @Override
    public long getParentCommentTid() {
        return parentComment.getTid();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PrefsObject pref = ((CnBetaApplicationContext)getApplicationContext()).getCnBetaPreferences();
        FontUtils.updateTextSize(this, replyCommentTitleTextView, R.dimen.listitem_comment_text_size, pref.getFontSizeOffset());
    }
}
