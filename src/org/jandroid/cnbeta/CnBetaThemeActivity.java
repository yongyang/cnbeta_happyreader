package org.jandroid.cnbeta;

import android.view.Menu;
import android.view.MenuItem;
import org.jandroid.common.ThemeActivity;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class CnBetaThemeActivity extends ThemeActivity {

    private Menu optionsMenu;

    @Override
    protected void onResume() {
        super.onResume();

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
            optionsMenu.findItem(R.id.night_mode_menu).setChecked(isDarkThemeEnabled());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        CnBetaApplicationContext application = getCnBetaApplicationContext();
        switch (item.getItemId()) {
            case R.id.night_mode_menu:
                if(item.isChecked()) {
                    item.setChecked(false);
                    application.setDarkThemeEnabled(false);

                }
                else {
                    item.setChecked(true);
                    application.setDarkThemeEnabled(true);
                }
                onThemeChanged();
                return true;
        }
        return false;
    }

    public CnBetaApplicationContext getCnBetaApplicationContext() {
        return (CnBetaApplicationContext) getApplication();
    }

    public boolean isDarkThemeEnabled() {
        return getCnBetaApplicationContext().isDarkThemeEnabled();
    }

    public int getDarkThemeId() {
        return R.style.Theme_cnBeta_Dark;
    }

    public int getLightThemeId() {
        return R.style.Theme_cnBeta_Light;
    }

    @Override
    protected String getPrefsFontPath() {
        return PrefsObject.getInstance(getApplication()).getCustomFont();
    }

    @Override
    protected int getFontSizeOffset() {
        return PrefsObject.getInstance(getApplication()).getFontSizeOffset();
    }

    @Override
    protected int getPrefsThemeId() {
        if(isDarkThemeEnabled()) {
            return getDarkThemeId();
        }
        else {
            return getLightThemeId();
        }
    }

    @Override
    protected boolean isMaskViewEnabled() {
        return getCnBetaApplicationContext().isDarkThemeEnabled();
    }

    @Override
    protected int getMaskViewBackgroundColor() {
        return getCnBetaApplicationContext().getPrefsObject().getNightModeBrightnessInteger();
    }
}
