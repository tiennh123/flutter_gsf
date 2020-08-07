import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_gsf/flutter_gsf.dart';

void main() {
  const MethodChannel channel = MethodChannel('flutter_gsf');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getGsf', () async {
    expect(await FlutterGsf.getGsf, '42');
  });
}
