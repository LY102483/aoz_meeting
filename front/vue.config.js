const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8099,
    allowedHosts: [
      'ly1024.top', // 允许访问的域名地址，即内网穿透的地址
      '.ly1024.top',   // .是二级域名的通配符
        'aoz.com',
        '.aoz.com',
    ],
    client: {
      overlay: false,//关闭全局报错提示
    },
  }
})

