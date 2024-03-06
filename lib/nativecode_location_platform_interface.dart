import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'nativecode_location_method_channel.dart';

abstract class NativecodeLocationPlatform extends PlatformInterface {
  /// Constructs a NativecodeLocationPlatform.
  NativecodeLocationPlatform() : super(token: _token);

  static final Object _token = Object();

  static NativecodeLocationPlatform _instance = MethodChannelNativecodeLocation();

  /// The default instance of [NativecodeLocationPlatform] to use.
  ///
  /// Defaults to [MethodChannelNativecodeLocation].
  static NativecodeLocationPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [NativecodeLocationPlatform] when
  /// they register themselves.
  static set instance(NativecodeLocationPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<String?> getNativeCodeLocation() {
    throw UnimplementedError('getNativeCodeLocation() has not been implemented.');
  }
}
