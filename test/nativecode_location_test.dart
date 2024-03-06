import 'package:flutter_test/flutter_test.dart';
import 'package:nativecode_location/nativecode_location.dart';
import 'package:nativecode_location/nativecode_location_platform_interface.dart';
import 'package:nativecode_location/nativecode_location_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockNativecodeLocationPlatform
    with MockPlatformInterfaceMixin
    implements NativecodeLocationPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<String?> getNativeCodeLocation() {
    // TODO: implement getNativeCodeLocation
    throw UnimplementedError();
  }
}

void main() {
  final NativecodeLocationPlatform initialPlatform = NativecodeLocationPlatform.instance;

  test('$MethodChannelNativecodeLocation is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelNativecodeLocation>());
  });

  test('getPlatformVersion', () async {
    NativecodeLocation nativecodeLocationPlugin = NativecodeLocation();
    MockNativecodeLocationPlatform fakePlatform = MockNativecodeLocationPlatform();
    NativecodeLocationPlatform.instance = fakePlatform;

    expect(await nativecodeLocationPlugin.getPlatformVersion(), '42');
  });
}
