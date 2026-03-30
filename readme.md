<img width="811" height="504" alt="Snipaste_2026-03-30_13-07-14" src="https://github.com/user-attachments/assets/127aec21-d2e9-4eb8-9624-d171d5148fa7" />

## v0.0.2 插件说明
```
edit http msg demo with UI v0.0.2
用于快速实现报文修改demo, 用于处理前端加密等常见场景
已实现 常用的请求过滤方案 、 配置UI 、 持久化配置和加载
```

##  v0.0.2 使用方式
```
1.下载项目代码
2.修改ProcessAction中的updateBodyData函数代码
3.使用 mvn命令 或 idea maven插件进行编译打包 
```


## v0.0.1  插件说明 
```
edit http msg v0.0.1
扩展burp suite自带的替换规则 
根据edit_http_msg.config.yml中配置的规则替换通过burp模块的请求报文.
提示：v0.0.1 版本未实现UI配置
```

##  v0.0.1  规则示例
```
EXACT_REPLACE_RULE: "/-> /-> /admin.php"
含义：精确匹配替换
请求URI为【/】时，替换【 /】为【 /admin.php】

ROUGH_REPLACE_RULE: " /-> /admin.php/"
含义：粗略匹配替换
请求URI为【任意, 但不为/】时，替换【 /】为【 /admin.php/】

目前每种匹配方式仅支持单个规则.
```


## 最终报文查看
```
较新的 burpsuit 版本请通过 自带的logger模块 查看最终发送的最终报文记录.

较旧的 burpsuit 版本请通过 安装logger++查看 查看最终发送的最终报文记录.
```
