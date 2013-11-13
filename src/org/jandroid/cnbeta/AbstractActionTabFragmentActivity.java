package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
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

    protected void initActionBar() {
        final ActionBar actionBar = getActionBar();
        for (int resourceId : getTabResourceIds()) {

            // 使用 customview，一遍可以更改字体
            ViewGroup tabContainer = (ViewGroup)getLayoutInflater().inflate(R.layout.actiontab_tabtext_customview, null);
            TextView tabTextView = (TextView)tabContainer.findViewById(R.id.tabTextView);
            tabTextView.setText(resourceId);
            actionBar.addTab(actionBar.newTab().setCustomView(tabContainer).setTabListener(pagerAdapter));

//            actionBar.addTab(actionBar.newTab().setText(resourceId).setTabListener(pagerAdapter));
        }
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // update action tab text font
        for(int i=0; i<getActionBar().getTabCount(); i++){
            FontUtils.updateFont(getActionBar().getTabAt(i).getCustomView(), ((CnBetaApplicationContext) getApplicationContext()).getCnBetaPreferences().getCustomFontTypeface());
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
