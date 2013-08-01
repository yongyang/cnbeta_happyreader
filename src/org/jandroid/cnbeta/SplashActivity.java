package org.jandroid.cnbeta;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import org.jandroid.cnbeta.util.EnvironmentUtils;
import org.jandroid.cnbeta.util.IntentUtils;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class SplashActivity extends Activity {

    private Handler mHandler = new Handler() {
   		public void handleMessage(android.os.Message msg) {
   			startMainActivity();
   		}
   	};

   	@Override
   	protected void onCreate(Bundle savedInstanceState) {
   		super.onCreate(savedInstanceState);
   		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
   		getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.splash_load));

        //TODO: setup all needed configurations, network and others, such directories
        EnvironmentUtils.checkNetworkConnected(this);
        EnvironmentUtils.checkSdCardMounted(this);
   	}

   	protected void onResume() {
   		super.onResume();
   		mHandler.removeMessages(0);
   		mHandler.sendEmptyMessageDelayed(0, 2000);
   	}

   	private void startMainActivity() {
        IntentUtils.startActivity(this, MainActivity.class);
        finish();
   	}

}
