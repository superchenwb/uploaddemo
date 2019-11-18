package com.propertyapp;

import android.support.multidex.MultiDexApplication;

import com.facebook.react.ReactApplication;
import it.innove.BleManagerPackage;
import com.horcrux.svg.SvgPackage;
import com.zy.upgrade.RNUpgradeModule;
import com.zy.upgrade.RNUpgradePackage;
import com.learnium.RNDeviceInfo.RNDeviceInfo;
import org.devio.rn.splashscreen.SplashScreenReactPackage;
import me.listenzz.modal.TranslucentModalReactPackage;

import com.facebook.react.modules.network.OkHttpClientProvider;
import com.swmansion.reanimated.ReanimatedPackage;
import io.realm.react.RealmReactPackage;
import com.zy.pickerview.PickerViewPackage;
import com.oblador.vectoricons.VectorIconsPackage;
import com.syanpicker.RNSyanImagePickerPackage;
import com.lmy.smartrefreshlayout.SmartRefreshLayoutPackage;
import com.swmansion.gesturehandler.react.RNGestureHandlerPackage;
import com.lugg.ReactNativeConfig.ReactNativeConfigPackage;
import org.reactnative.camera.RNCameraPackage;
import com.reactnativecommunity.netinfo.NetInfoPackage;
import com.reactnativecommunity.asyncstorage.AsyncStoragePackage;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import cn.jpush.reactnativejpush.JPushPackage;

import java.util.Arrays;
import java.util.List;


public class MainApplication extends MultiDexApplication implements ReactApplication {

  // 设置为 true 将不会弹出 toast
  private boolean SHUTDOWN_TOAST = true;
  // 设置为 true 将不会打印 log
  private boolean SHUTDOWN_LOG = true;

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
            new BleManagerPackage(),
            new SvgPackage(),
            new RNUpgradePackage(),
            new RNDeviceInfo(),
            new SplashScreenReactPackage(),
            new TranslucentModalReactPackage(),
            new ReanimatedPackage(),
            new RealmReactPackage(),
            new PickerViewPackage(),
            new VectorIconsPackage(),
            new RNSyanImagePickerPackage(),
            new SmartRefreshLayoutPackage(),
            new RNGestureHandlerPackage(),
            new ReactNativeConfigPackage(),
            new RNCameraPackage(),
            new NetInfoPackage(),
            new AsyncStoragePackage(),
            new JPushPackage(SHUTDOWN_TOAST, SHUTDOWN_LOG)
      );
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/PingFang Regular.ttf");
    FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/PingFang Regular.ttf");
    FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/PingFang Regular.ttf");
    FontsOverride.setDefaultFont(this, "SERIF", "fonts/PingFang Regular.ttf");
    SoLoader.init(this, /* native exopackage */ false);
    // https签名
    OkHttpClientProvider.setOkHttpClientFactory(new MyOkHttpClientFactory(getAssets()));
    RNUpgradeModule.init(this);
  }
}
