package com.propertyapp;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactRootView;
import com.swmansion.gesturehandler.react.RNGestureHandlerEnabledRootView;
import com.facebook.react.ReactActivity;
import org.devio.rn.splashscreen.SplashScreen;

public class MainActivity extends ReactActivity {

  private static final int REQUEST_CODE_SETTING = 300;
  private static final int REQUEST_CODE_PERMISSION_SD = 101;

  /**
   * Returns the name of the main component registered from JavaScript.
   * This is used to schedule rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
      return "propertyapp";
  }

  @Override
  protected ReactActivityDelegate createReactActivityDelegate() {
    return new ReactActivityDelegate(this, getMainComponentName()) {
      @Override
      protected ReactRootView createRootView() {
        return new RNGestureHandlerEnabledRootView(MainActivity.this);
      }
    };
  }

  /**
   * 禁止apk字体跟随系统字体大小变化
   * @return
   */
  @Override
  public Resources getResources() {
    Resources res = super.getResources();
    Configuration config=new Configuration();
    config.setToDefaults();
    res.updateConfiguration(config,res.getDisplayMetrics() );
    return res;
  }

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.show(this, R.style.SplashTheme);  // here
        super.onCreate(savedInstanceState);
    }

}
