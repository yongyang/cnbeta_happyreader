package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import org.apache.commons.io.FileUtils;
import org.jandroid.common.FileChooserDialog;
import org.jandroid.common.FontUtils;
import org.jandroid.common.ToastUtils;
import org.jandroid.common.WindowUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrefsActivity extends PreferenceActivity {
    private Handler handler = new Handler();

    private View maskView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupTheme();

        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 该方法用于为该界面设置一个标题按钮
        if (hasHeaders()) {
            Button button = new Button(this);
            button.setText("一键清除缓存和浏览记录");
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    cleanHistory();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            cleanCache();
                        }
                    }, 1000);

                }
            });
            // 将该按钮添加到该界面上
            setListFooter(button);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMaskView();
    }

    protected void onStop() {
        super.onStop();
        if (maskView != null) {
            WindowUtils.removeMaskView(this, maskView);
            maskView = null;
        }
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        return ((CnBetaApplication) getApplicationContext()).onOptionsItemSelected(this, mi);
    }

    // 重写该该方法，负责加载页面布局文件
    @Override
    public void onBuildHeaders(List<Header> target) {
        // 加载选项设置列表的布局文件
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    private CnBetaApplication getCnBetaApplication() {
        return (CnBetaApplication) getApplication();
    }

    public static class PrefsCacheFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs_cache);

            Preference button = findPreference(getString(R.string.pref_key_cleanCache));
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference arg0) {
                    ((PrefsActivity)getActivity()).cleanCache();
                    return true;
                }
            });
        }
    }

    public static class PrefsHistoryFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs_history);

            Preference button = findPreference(getString(R.string.pref_key_cleanHistory));

            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference arg0) {
                    return true;
                }
            });

        }
    }

    public static class PrefsNightModeFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs_nightmode);

            Preference brightPref = findPreference(getString(R.string.pref_key_nightmode_brightness));
            brightPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    PrefsActivity prefsActivity = ((PrefsActivity)getActivity());
                    CnBetaApplicationContext applicationContext = prefsActivity.getCnBetaApplicationContext();
                    if(applicationContext.isDarkThemeEnabled()) {
                        if (prefsActivity.maskView != null) {
                            WindowUtils.removeMaskView(prefsActivity, prefsActivity.maskView);
                        }
                        prefsActivity.maskView = WindowUtils.addMaskView(prefsActivity, newValue.toString());
                    }
                    return true;
                }
            });
        }
    }

    public static class PrefsNetworkFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs_network);
        }
    }

    public static class PrefsUIFragment extends PreferenceFragment {

        private ListPreference chooseFontListPreference;
        private CharSequence[] defaultEntries = new String[]{};
        private CharSequence[] defaultEntryValues = new String[]{};

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs_ui);

            chooseFontListPreference = (ListPreference)findPreference(getString(R.string.pref_key_customFont));
            defaultEntries = chooseFontListPreference.getEntries();
            defaultEntryValues = chooseFontListPreference.getEntryValues();

            loadInstalledFonts();

            Preference installFontButton = findPreference(getString(R.string.pref_key_installFont));
            installFontButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    showFontChooserDialog();
                    return true;
                }
            });

            Preference deleteFontsButton = findPreference(getString(R.string.pref_key_deleteFont));
            deleteFontsButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("删除所有安装的个性化字体?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ((CnBetaApplication) (getActivity().getApplicationContext())).cleanFonts();
                                    chooseFontListPreference.setEntries(defaultEntries);
                                    chooseFontListPreference.setEntryValues(defaultEntryValues);
                                    ToastUtils.showShortToast(getActivity(), "字体清除成功");
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                    return true;
                }
            });

        }

        private void loadInstalledFonts() {
            List<CharSequence> entries = new ArrayList<CharSequence>();
            entries.addAll(Arrays.asList(defaultEntries));
            List<CharSequence> entryValues = new ArrayList<CharSequence>();
            entryValues.addAll(Arrays.asList(defaultEntryValues));

            // load installed fonts
            for(File fontFile : ((CnBetaApplication) (getActivity().getApplicationContext())).getFontsDir().listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith("ttf");
                }
            })) {
                try {
                    String fontName = FontUtils.parseFontFile(fontFile).getFontName();
                    if(!entries.contains(fontName)) {
                        entries.add(fontName);
                        entryValues.add("file://"+ ((CnBetaApplication)getActivity().getApplication()).getFontsDir().getAbsolutePath() + "/" + fontFile.getName());
                    }
                }
                catch (Exception e) {
                    ToastUtils.showShortToast(getActivity(), "获取字体列表时发生异常, " + e.getMessage());
                }
            }
            chooseFontListPreference.setEntries(entries.toArray(new CharSequence[entries.size()]));
            chooseFontListPreference.setEntryValues(entryValues.toArray(new CharSequence[entryValues.size()]));
        }

        private void showFontChooserDialog()     {
            FileChooserDialog fileChooserDialog = new FileChooserDialog(Environment.getExternalStorageDirectory(), ".ttf");
            fileChooserDialog.setOnFileSelectedListener(new FileChooserDialog.OnFileSelectedListener() {
                public void onFileSelected(File ttfFile) {
                    // movie ttf file to /mnt/cnBeta_jandroid/fonts/
                    try {
                        FileUtils.copyFileToDirectory(ttfFile, ((CnBetaApplication) (getActivity().getApplicationContext())).getFontsDir(), true);
                        ToastUtils.showShortToast(getActivity(), "字体 " + ttfFile.getName() + " 安装成功");
                        loadInstalledFonts();
                    }
                    catch (IOException e) {
                        ToastUtils.showShortToast(getActivity(), "字体 " + ttfFile.getName() + " 安装失败, " + e.getMessage());
                    }
                }
            });
            fileChooserDialog.showDialog(getActivity());
        }
    }


    public static class PrefsPostFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs_post);
            final EditTextPreference sigPreference = (EditTextPreference)findPreference(getString(R.string.pref_key_signature));
            sigPreference.setSummary("当前签名档: " + ((CnBetaApplication)getActivity().getApplicationContext()).getPrefsObject().getSignature());
            sigPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    sigPreference.setSummary("当前签名档: " + newValue);
                    return true;
                }
            });
        }
    }

    private void cleanCache(){
        new AsyncTask<Object, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                ToastUtils.showShortToast(PrefsActivity.this, "正在清除缓存，请稍等...");
            }

            @Override
            protected Boolean doInBackground(Object... params) {
                return getCnBetaApplication().cleanCache();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    ToastUtils.showShortToast(PrefsActivity.this, "缓存清除成功！");
                }
                else {
                    ToastUtils.showShortToast(PrefsActivity.this, "缓存清除失败，请重启软件后重试！");
                }
            }
        }.execute();

    }

    private void cleanHistory(){
        new AsyncTask<Object, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                ToastUtils.showShortToast(PrefsActivity.this, "正在清除历史记录，请稍等...");
            }

            @Override
            protected Boolean doInBackground(Object... params) {
                return getCnBetaApplication().cleanHistory();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    ToastUtils.showShortToast(PrefsActivity.this, "历史记录清除成功！");
                }
                else {
                    ToastUtils.showShortToast(PrefsActivity.this, "历史记录清除失败，请重启软件后重试！");
                }
            }
        }.execute();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    protected void setupTheme(){
        //setTheme
        CnBetaApplicationContext applicationContext = getCnBetaApplicationContext();
        if(applicationContext.isDarkThemeEnabled()) {
            this.setTheme(R.style.Theme_cnBeta_Dark);
        }
        else {
            this.setTheme(R.style.Theme_cnBeta_Light);
        }
        updateMaskView();
    }

    protected void updateMaskView() {
        CnBetaApplicationContext applicationContext = getCnBetaApplicationContext();
        if(applicationContext.isDarkThemeEnabled()) {
            if (this.maskView != null) {
                if(((ColorDrawable)maskView.getBackground()).getColor() != getMaskViewBackgroundColor()) {
                // maskView bg color changed, re-add
                    WindowUtils.removeMaskView(this, maskView);
                    maskView = null;
                    this.maskView = WindowUtils.addMaskView(this, getMaskViewBackgroundColor());
                }
            }
           else {
                this.maskView = WindowUtils.addMaskView(this, getMaskViewBackgroundColor());
            }
        }
        else {
            if (maskView != null) {
                WindowUtils.removeMaskView(this, maskView);
                maskView = null;
            }
        }

    }

    protected int getMaskViewBackgroundColor() {
        return getCnBetaApplicationContext().getPrefsObject().getNightModeBrightnessInteger();
    }

    public CnBetaApplicationContext getCnBetaApplicationContext() {
        return (CnBetaApplicationContext) getApplication();
    }

}
