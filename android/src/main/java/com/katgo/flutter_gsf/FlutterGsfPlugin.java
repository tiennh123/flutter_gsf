package com.katgo.flutter_gsf;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaDrm;
import android.net.Uri;
import android.os.Build;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.UUID;

/** FlutterGsfPlugin */
public class FlutterGsfPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context applicationContext;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_gsf");
    channel.setMethodCallHandler(this);
    onAttachedToEngine(flutterPluginBinding.getApplicationContext(), flutterPluginBinding.getBinaryMessenger());
  }

  private void onAttachedToEngine(Context applicationContext, BinaryMessenger messenger) {
    this.applicationContext = applicationContext;
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_gsf");
    channel.setMethodCallHandler(new FlutterGsfPlugin());
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getGsf")) {
      result.success(getGsfAndroidId(this.applicationContext));
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    applicationContext = null;
  }

  private static String getGsfAndroidId(Context context)
  {
    Uri URI = Uri.parse("content://com.google.android.gsf.gservices");
    String ID_KEY = "android_id";
    String params[] = {ID_KEY};
    Cursor c = context.getContentResolver().query(URI, null, null, params, null);
    if (!c.moveToFirst() || c.getColumnCount() < 2)
      return null;
    try
    {
      return Long.toHexString(Long.parseLong(c.getString(1)));
    }
    catch (NumberFormatException e)
    {
      return null;
    }
  }
}
