/**
 * @format
 */

import App from "./App";
import { name as appName } from "./app.json";

import { Navigation } from "react-native-navigation";
Navigation.registerComponent(`navigation.playground.WelcomeScreen`, () => App);

Navigation.events().registerAppLaunchedListener(() => {
  Navigation.setRoot({
    root: {
      component: {
        name: "navigation.playground.WelcomeScreen"
      }
    }
  });
});

// AppRegistry.registerComponent(appName, () => App);
