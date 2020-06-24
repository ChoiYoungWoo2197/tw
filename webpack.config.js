const path = require('path');
const {CleanWebpackPlugin} = require('clean-webpack-plugin');
const webpack = require('webpack');
const TerserPlugin = require('terser-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const OptimizeCSSAssetsPlugin = require('optimize-css-assets-webpack-plugin');
const CopyPlugin = require('copy-webpack-plugin');
const RobotstxtPlugin = require('robotstxt-webpack-plugin');

const uiSrcDirPath = path.resolve(__dirname, "src/main/resources/ui-src/");
const staticBuildDirPath = path.resolve(__dirname, "src/main/resources/static/build");

module.exports = {
    entry: {
        admin: path.resolve(uiSrcDirPath, "js/admin.js"),
        main: path.resolve(uiSrcDirPath, "js/main.js"),
    },

    output: {
        path: staticBuildDirPath,
        filename: 'js/[name].js'
    },
    plugins: [
        new CleanWebpackPlugin(),
        new MiniCssExtractPlugin({
            filename: 'css/[name].css',
            chunkFilename: '[id].css'
        }),
        new CopyPlugin([
            {from: path.resolve(uiSrcDirPath, "images"), to: 'images'}
        ]),
        new webpack.ProvidePlugin({
            $: 'jquery',
            jQuery: 'jquery',
            'window.jQuery': 'jquery',
            'window.$': 'jquery'
        }),
        new RobotstxtPlugin({
            policy: [
                {
                    userAgent: '*',
                    allow: '/',
                    crawlDelay: 10
                },
                {
                    userAgent: 'Twitterbot',
                    disallow: '*',
                    allow: '/images'
                }
            ],
            host: "https://spring.io"
        })
    ],
    module: {
        rules: [
            {
                test: /\.css$/,
                use: [
                    {
                        loader: MiniCssExtractPlugin.loader,
                        options: {publicPath: '../'},
                    },
                    'css-loader',
                    // 'sass-loader'
                ]
            },
            {
                test: /\.(sa|sc)ss$/,
                use: [
                    {
                        loader: MiniCssExtractPlugin.loader,
                        options: {publicPath: '../'},
                    },
                    'css-loader',
                    'sass-loader'
                ]
            },
            {
                test: /.*\/webfonts\/.*$/,
                loader: 'file-loader',
                options: {
                    name: 'css/[name].[ext]',
                },
            },

            {
                test: /.*\/fonts\/.*/,
                loader: 'file-loader',
                options: {
                    name: 'fonts/[name].[ext]',
                },
            },
            {
                test: /^images/,
                loader: 'file-loader',
                options: {
                    name: '[path][name].[ext]',
                },
            },
            {
                test: /\.(woff|woff2|ttf|eot)$/,
                use: 'file-loader?name=fonts/[name].[ext]!static'
            },
            {
                test: /\.svg$/,
                use: {
                    loader: 'svg-url-loader',
                    options: {
                        encoding: 'base64'
                    }
                }
            }
        ]
    },
    optimization: {
        minimize: true,
        runtimeChunk: false,
        minimizer: [
            new OptimizeCSSAssetsPlugin({}),
            new TerserPlugin()
        ]
    }
};