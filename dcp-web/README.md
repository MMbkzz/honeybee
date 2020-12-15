1. 项目启动
* 下载依赖
    ### npm install

* 启动项目
    ### npm run dev
    ### 本地打开地址：http://localhost:9527.
* 打包项目
    ### npm run build:sit
    * build for test environment
    ### npm run build:sit

    * build for production environment
    ### npm run build:prod

2. 项目描述
* 项目名称：DSP大数据服务平台
* 项目功能模块：
    数据源管理（datasouceManage）、服务模型管理（modelmana.vue）、数据资产管理（dataassetmanage）、数据服务管理（dataserviceAPI）、运维监控（operationmonitor）、平台管理（platform）、安全管理（permission）、系统参数配置（paramConfig）
* 项目基础模块：
    布局（layout）、登录（login）

3. 项目结构
* 项目后台api接口基础url配置：
    ### config/dev.env.js 本地运行接口api地址配置
    ### config/sit.env.js 生产环境接口api地址配置
* 路由与菜单栏（菜单由路由生成）：
    ### src/router/index.js
    ### constantRouterMap中设置 平台基本路由
    ### 项目所需菜单由后端提供，在登录后提供
    ### 更改路由compnent有三处： login/index、 App.vue、permission.js
    ### 然后在前端界面的安全管理/资源管理 添加路由、图标、名称
* 登录权限

* 角色权限
    ### src/permission.js
4. 项目备注
* 数据交互：
    ### src/utils/request.js 创建axios实例并返回 service
    ### src/api/*.js 用于存储各个模块的各个请求方法（data、url） 需要应用时在页面import（也可以不用
    ### views/login/index.vue 登录请求 设置授权头
* 交互地址：
    ### config/dev.env.js 开发环境
    ### config/prod.env.js 生产环境
* 启服务、打包：
    ### build/*.js
* 数据存储VUEX
    #### src/store/indes.js 实例化vuex
    #### src/store/getters.js 所有state
    #### src/modules/*.js 其他所有的mutations、actions
    ### user.js 用户登录信息会话存储，token cookies存储  RegisterUserInfo
    ### permission.js 存储路由，匹配是否有权限  addRoutes
5.  技术
* 前端框架 vue
* UI框架 element
* 作图 echarts
* 复制粘贴 vue-clipboard2


```

## Advanced
```bash
# --report to build with bundle size analytics
npm run build:prod --report

# --preview to start a server in local to preview
npm run build:prod --preview

# lint code
npm run lint

# auto fix
npm run lint -- --fix
```
