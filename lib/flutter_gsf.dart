import 'dart:async';

import 'package:flutter/services.dart';

class FlutterGsf {
  static const MethodChannel _channel = const MethodChannel('flutter_gsf');

  static Future<String> get getGsf async {
    final String version = await _channel.invokeMethod('getGsf');
    return version;
  }
}
