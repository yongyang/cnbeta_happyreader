package org.jandroid.cnbeta;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.widget.Button;
import android.widget.Toast;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jandroid.common.ToastUtils;

public class CnBetaPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 该方法用于为该界面设置一个标题按钮
        if (hasHeaders()) {
            Button button = new Button(this);
            button.setText("一键清除缓存和浏览记录");
            // 将该按钮添加到该界面上
            setListFooter(button);
        }
    }

    // 重写该该方法，负责加载页面布局文件
    @Override
    public void onBuildHeaders(List<Header> target) {
        // 加载选项设置列表的布局文件
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    public static class PrefsCacheFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs_cache);

            Preference button = findPreference(getString(R.string.pref_key_cleanCache));
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference arg0) {
                    new AsyncTask<Object, Integer, Boolean>() {

                        @Override
                        protected void onPreExecute() {
                            ToastUtils.showShortToast(getActivity(), "正在清除缓存，请稍等...");
                        }

                        @Override
                        protected Boolean doInBackground(Object... params) {
                            return FileUtils.deleteQuietly(((CnBetaApplicationContext)getActivity().getApplicationContext()).getBaseDir());
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                            super.onPostExecute(aBoolean);
                            if(aBoolean) {
                                ToastUtils.showShortToast(getActivity(), "缓存清除成功！");
                            }
                            else {
                                ToastUtils.showShortToast(getActivity(), "缓存清除失败，请重启软件后重试！");
                            }
                        }
                    }.execute();
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
            // 获取传入该Fragment的参数
//            String website = getArguments().getString("site_url");
//            Toast.makeText(getActivity(), "网站域名是：" + website, Toast.LENGTH_LONG).show();

        }


    }
}
