import 'package:flutter/material.dart';

class Home extends StatefulWidget {
  Home({Key key}) : super(key: key);

  static const title = 'FZYZ Flutter Demo Home Page';

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<Home> {
  int _counter = 0;
  var indicator = 'You have pushed the button this many times ->:';

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(Home.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(indicator),
            Text(
              '$_counter',
              style: Theme
                  .of(context)
                  .textTheme
                  .display1,
            ),
            Card(
              child: Column(
                children: <Widget>[
                  ListTile(
                    title: Text('???'),
                    subtitle: Text('sub'),
                    leading: Icon(
                        Icons.add_shopping_cart
                    ),
                  ),
                  ListTile(
                    title: Text('???'),
                    subtitle: Text('sub'),
                    leading: Icon(
                        Icons.add_shopping_cart
                    ),
                  ),
                  Divider(),
                  ListTile(
                    title: Text('???'),
                    subtitle: Text('sub'),
                    leading: Icon(
                        Icons.settings
                    ),
                    onTap: () {
                      Navigator.push(context, MaterialPageRoute(
                          builder: (context) {
                            return Scaffold(
                              appBar: AppBar(
                                  title: Text("??")),
                            );
                          }
                      ));
                    },
                  ),
                ],
              ),
            )
          ],
        ),
      ),
      bottomNavigationBar: BottomAppBar(
        shape: CircularNotchedRectangle(),
        child: Row(
          mainAxisSize: MainAxisSize.max,
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: <Widget>[
            IconButton(
              icon: Icon(
                Icons.settings,
                color: Colors.redAccent,
              ),
              onPressed: () {},
            ),
            Text('02'),
          ],
        ),
        color: Colors.white,
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: Icon(Icons.add),
        elevation: 2,
      ),
    );
  }
}
