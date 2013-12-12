package org.jandroid.cnbeta;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import org.jandroid.cnbeta.entity.HistoryArticle;
import org.jandroid.cnbeta.loader.HistoryArticleListLoader;
import org.jandroid.common.AnimateUtils;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.EnvironmentUtils;
import org.jandroid.common.ToastUtils;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class SplashActivity extends BaseActivity {

//    private TextView infoTextView;

    View logoBanner;

   	@Override
   	protected void onCreate(Bundle savedInstanceState) {
   		super.onCreate(savedInstanceState);
   		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
//        infoTextView = (TextView)findViewById(R.id.infoTextView);

        logoBanner = findViewById(R.id.logoBanner);

        checkEnvironment();
   	}

    private void checkEnvironment(){
        //TODO: setup all needed configurations, network and others, such directories
        switch (EnvironmentUtils.getConnectionStatus(this)){
            case WIFI:
                ToastUtils.showShortToast(this, "使用WIFI网络");
//                infoTextView.setText("使用WIFI网络");
                break;
            case NET:
            case MOBILE:
                ToastUtils.showShortToast(this, "使用手机网络");
//                infoTextView.setText("使用手机网络");
                break;
            case WAP:
            case NONE:
            default:
                ToastUtils.showShortToast(this, "无法连接网络，访问离线数据");
//                infoTextView.setText("无法连接网络，只能访问本地数据");
        }

        if(EnvironmentUtils.checkSdCardMounted(this)){
            ToastUtils.showShortToast(this, "使用SD卡缓存离线数据");
//            infoTextView.setText("缓存数据将保存到SD卡！");
        }
        else {
            ToastUtils.showShortToast(this, "使用内置存储缓存离线数据");
//            infoTextView.setText("缓存数据将保存到内置存储！");
        }
    }

   	protected void onResume() {
   		super.onResume();
        AnimateUtils.move(logoBanner, (int)logoBanner.getX(), (int)logoBanner.getX(), (int)logoBanner.getY() - 200, (int)logoBanner.getY(), 1500);
        handler.postDelayed(new Runnable() {
            public void run() {
                CnBetaApplication application = (CnBetaApplication)getApplication();
                try {
                    // add all read history article sid
                    List<HistoryArticle> historyArticles = new HistoryArticleListLoader().fromDisk(application.getHistoryDir());
                    application.addHistoryArticle(historyArticles.toArray(new HistoryArticle[historyArticles.size()]));
                }
                catch (Exception e) {
                    ToastUtils.showShortToast(SplashActivity.this, "加载阅读历史失败，请尝试清空历史记录！");
                }
                startMainActivity();
            }
        }, 2000);
   	}

   	private void startMainActivity() {
        Utils.openMainActivity(this);
   	}

    //TODO: 初始化阅读记录，用来给已读文章加颜色
}
