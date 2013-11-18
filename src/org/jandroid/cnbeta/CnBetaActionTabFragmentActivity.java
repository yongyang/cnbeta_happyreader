package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import org.jandroid.common.ActionTabFragmentActivity;
import org.jandroid.common.FontUtils;
import org.jandroid.common.IntentUtils;
import org.jandroid.common.WindowUtils;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class CnBetaActionTabFragmentActivity extends ActionTabFragmentActivity {

    private Menu optionsMenu;

    protected void initActionBar() {
        final ActionBar actionBar = getActionBar();
        for (int resourceId : getTabResourceIds()) {

            // 使用 customview，一遍可以更改字体
            ViewGroup tabContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.actiontab_tabtext_customview, null);
            TextView tabTextView = (TextView) tabContainer.findViewById(R.id.tabTextView);
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
        for (int i = 0; i < getActionBar().getTabCount(); i++) {
            FontUtils.updateFont(getActionBar().getTabAt(i).getCustomView(), ((CnBetaApplicationContext) getApplicationContext()).getCnBetaPreferences().getCustomFontTypeface());
        }

        //update menu status
        if (optionsMenu != null) {
            updateOptionsMenu();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        //add  refresh actionitem
        getMenuInflater().inflate(R.menu.default_action_item, menu);
        this.optionsMenu = menu;
        // call updateOptionsMenu to make sure menu updated after menu created
        updateOptionsMenu();
        return super.onCreateOptionsMenu(menu);
    }

    protected void updateOptionsMenu() {
        if (optionsMenu != null) {
            optionsMenu.findItem(R.id.eye_friendly_mode_menu).setChecked(((CnBetaApplicationContext) getApplicationContext()).isEyeFriendlyModeEnabled());
            optionsMenu.findItem(R.id.night_mode_menu).setChecked(isDarkTheme());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        CnBetaApplicationContext application = getCnBetaApplicationContext();
        switch (item.getItemId()) {
            case R.id.eye_friendly_mode_menu:
                if (!application.isEyeFriendlyModeEnabled()) {
                    item.setChecked(true);
                    application.setEyeFriendlyModeEnabled(true);
                }
                else {
                    item.setChecked(false);
                    application.setEyeFriendlyModeEnabled(false);
                }
                updateMaskView();
                return true;
            case R.id.night_mode_menu:
                if(item.isChecked()) {
                    item.setChecked(false);
                    application.setThemeId(getLightThemeId());
                }
                else {
                    item.setChecked(true);
                    application.setThemeId(getDarkThemeId());
                }
                recreate();
                return true;
        }
        return false;
    }

    public CnBetaApplicationContext getCnBetaApplicationContext() {
        return (CnBetaApplicationContext) getApplication();
    }
}
