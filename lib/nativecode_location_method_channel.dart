import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'nativecode_location_platform_interface.dart';

/// An implementation of [NativecodeLocationPlatform] that uses method channels.
class MethodChannelNativecodeLocation extends NativecodeLocationPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('nativecode_location');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<String?> getNativeCodeLocation() async {
    final result = await methodChannel.invokeMethod<String>('getNativeCodeLocation');
    return result;
  }
}
