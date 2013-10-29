package org.jandroid.cnbeta;

import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import org.jandroid.common.ActionTabFragmentActivity;
import org.jandroid.common.FontUtils;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class AbstractActionTabFragmentActivity extends ActionTabFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_PROGRESS);
        this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        this.setProgressBarIndeterminate(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // update action tab text font
        for(int i=0; i<getActionBar().getTabCount(); i++){
            FontUtils.changeFont(getActionBar().getTabAt(i).getCustomView(), ((CnBetaApplicationContext) getApplicationContext()).getCnBetaPreferences().getCustomFontTypeface());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        //add  refresh actionitem
        getMenuInflater().inflate(R.menu.default_action_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public CnBetaApplicationContext getCnBetaApplicationContext() {
        return (CnBetaApplicationContext) getApplication();
    }
}
