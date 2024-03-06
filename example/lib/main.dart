import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:nativecode_location/nativecode_location.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _nativecodeLocationPlugin = NativecodeLocation();
  String _location = 'Null';
  Timer? timer;
  String? location;
  Set<String> uniqueData = <String>{};

  @override
  void initState() {
    super.initState();
    timer = Timer.periodic(const Duration(seconds: 5), (Timer t) => checkNewLocUpdate());
  }

  checkNewLocUpdate() async {
    try {
      location =
          await _nativecodeLocationPlugin.getNativeCodeLocation() ?? 'Location error';
    } catch (e) {
      location = "error";
      print(e);
    }
    setState(() {
      _location = location!;
      uniqueData.add(location!);
      print(uniqueData);
      print(location);
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
          appBar: AppBar(
            title: const Text('Plugin example app'),
          ),
          body: Column(
            children: [Center(
              child: Text('Running on: $_location'),
            ), GestureDetector(onTap: () async {
              try {
                location =
                    await _nativecodeLocationPlugin.getNativeCodeLocation() ?? 'Location error';
                setState(() {
                  _location = location!;
                  uniqueData.add(location!);
                  print(uniqueData);
                  print(location);
                });
              } catch (e) {
                location = "error";
                print(e);
              }
            }, child: Container(child: Text('click here'), color: Colors.deepOrange,)
            ),
            ],
          )
      ),
    );
  }
}
