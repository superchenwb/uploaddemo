/** @format */
import React from 'react';
import { AppRegistry, StatusBar, View, Text } from 'react-native';

import { name as appName } from './app.json';

class Root extends React.PureComponent {
  render() {
    return (
      <View>
        <Text>demo</Text>
      </View>
    )
  }
}

AppRegistry.registerComponent(appName, () => Root);
