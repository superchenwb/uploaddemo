module.exports = {
  "presets": ["module:metro-react-native-babel-preset"],
  "plugins": [
    "@babel/transform-flow-strip-types",
    ["@babel/plugin-syntax-decorators", { "legacy": true }],
    ["@babel/plugin-proposal-decorators", { "legacy": true }],
    ["@babel/plugin-proposal-class-properties", { "loose" : true }],
    ["import", { "libraryName": "@ant-design/react-native" }]
  ],
  "env": {
    "production": {
      "plugins": ["transform-remove-console"]
    }
  }
};
