
import 'nativecode_location_platform_interface.dart';

class NativecodeLocation {
  Future<String?> getPlatformVersion() {
    return NativecodeLocationPlatform.instance.getPlatformVersion();
  }

  Future<String?> getNativeCodeLocation() {
    return NativecodeLocationPlatform.instance.getNativeCodeLocation();
  }
}
