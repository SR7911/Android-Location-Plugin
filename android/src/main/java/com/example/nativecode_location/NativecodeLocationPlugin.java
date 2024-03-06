package com.example.nativecode_location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** NativecodeLocationPlugin */
public class NativecodeLocationPlugin implements FlutterPlugin, MethodCallHandler, LocationListener {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;
  private LocationManager locationManager;
  Location bestLocation;
  boolean isGPSEnabled = false;
  boolean isNetworkEnabled = false;
  boolean isPassiveEnabled = false;
  double latitude;
  double longitude;
  String providerType;
  LocationListener locationListener;
  private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;// 10 meters
  private static final long MIN_TIME_BW_UPDATES = 0; // 1 minute

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    context = flutterPluginBinding.getApplicationContext();
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "nativecode_location");
    channel.setMethodCallHandler(this);

    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    locationListener = new LocationListener() {
      @Override
      public void onLocationChanged(Location location) {
        if (location != null) {
          String locationString = location.getLatitude() + "," + location.getLongitude();
          channel.invokeMethod("updateLocation", locationString);
        }
      }

      @Override
      public void onStatusChanged(String s, int i, Bundle bundle) {

      }

      @Override
      public void onProviderEnabled(String s) {

      }

      @Override
      public void onProviderDisabled(String s) {

      }

    };
    // Request location updates
    requestLocationUpdates();

    // Register for location updates
    if (locationManager != null) {
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
      locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
      locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
    }

  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("getNativeCodeLocation")) {
      result.success(getLocation());
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);

    // Unregister the location listener
    if (locationManager != null) {
      locationManager.removeUpdates(this);
    }
  }

  private void requestLocationUpdates() {
    try {
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
      locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
      locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
    } catch (SecurityException e) {
      e.printStackTrace();
    }
  }

  public String getLocation() {
    try {
      // getting GPS status
      isGPSEnabled = locationManager
              .isProviderEnabled(LocationManager.GPS_PROVIDER);
      // getting network status
      isNetworkEnabled = locationManager
              .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
      isPassiveEnabled = locationManager
              .isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

      requestLocationUpdates();

// Check for providers in a more straightforward way
      if (isGPSEnabled) bestLocation = getBestLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
      if (isNetworkEnabled) bestLocation = getBestLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
      if (isPassiveEnabled) bestLocation = getBestLocation(locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER));

// Choose the most accurate location
      if (bestLocation != null) {
        latitude = bestLocation.getLatitude();
        longitude = bestLocation.getLongitude();
        providerType = bestLocation.getProvider();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return latitude + "," + longitude + "," + providerType;
  }

  private Location getBestLocation(Location currentLocation) {
    if (currentLocation == null) {
      return null; // No location found for this provider
    }

    // Handle null checks for previous best location
    if (bestLocation == null) {
      bestLocation = currentLocation; // Set initial best location
    } else {
      // Check for accuracy: Choose the location with the lower accuracy value
      float currentAccuracy = currentLocation.getAccuracy();
      float bestAccuracy = bestLocation.getAccuracy();
      if (currentAccuracy < bestAccuracy) {
        bestLocation = currentLocation; // Update best location if more accurate
      }
    }

    return bestLocation;
  }

  @Override
  public void onLocationChanged(Location location) {

  }

  @Override
  public void onStatusChanged(String s, int i, Bundle bundle) {

  }

  @Override
  public void onProviderEnabled(String s) {

  }

  @Override
  public void onProviderDisabled(String s) {

  }
}
