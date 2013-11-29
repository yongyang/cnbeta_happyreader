package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import org.jandroid.common.BaseActivity;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class AboutActivity extends CnBetaThemeActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // no options menu
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        // HOME menu
        return ((CnBetaApplication)getApplicationContext()).onOptionsItemSelected(this, mi);
    }
}
