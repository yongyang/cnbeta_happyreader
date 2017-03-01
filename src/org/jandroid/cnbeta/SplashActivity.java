package org.jandroid.cnbeta;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import org.jandroid.common.AnimateUtils;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.EnvironmentUtils;
import org.jandroid.common.ToastUtils;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class SplashActivity extends BaseActivity {

//    private TextView infoTextView;

    private View logoBanner;
    private ProgressBar splashProgressBar;

   	@Override
   	protected void onCreate(Bundle savedInstanceState) {
   		super.onCreate(savedInstanceState);
   		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
//        infoTextView = (TextView)findViewById(R.id.infoTextView);

        logoBanner = findViewById(R.id.logoBanner);
        splashProgressBar = (ProgressBar)findViewById(R.id.splashProgressBar);
        splashProgressBar.setVisibility(View.VISIBLE);

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
        AnimateUtils.move(logoBanner, (int)logoBanner.getX(), (int)logoBanner.getX(), (int)logoBanner.getY() - 200, (int)logoBanner.getY(), 1200);
        handler.postDelayed(new Runnable() {
            public void run() {
                startMainActivity();
            }
        }, 2000);
   	}

   	private void startMainActivity() {
        splashProgressBar.setVisibility(View.GONE);
        Utils.openMainActivity(this);
   	}

    //TODO: 初始化阅读记录，用来给已读文章加颜色
}
