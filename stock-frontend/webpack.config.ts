import webpack from "webpack"

module.exports = {
  resolve: {
    fallback: {
      global: require.resolve("global")
    }
  },
  plugins: [
    new webpack.ProvidePlugin({
      global: "global" 
    })
  ]
};
