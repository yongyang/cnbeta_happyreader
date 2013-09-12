package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

//TODO 版权，内容免责，捐赠, 技巧

public class AboutActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        return ((CnBetaApplication)getApplicationContext()).onOptionsItemSelected(this, mi);
    }

}
