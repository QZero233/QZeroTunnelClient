# QZeroTunnel客户端命令帮助文档

注：有些指令未提供用法，这就意味着这条指令不需要参数，直接输入即可

## 本地Token管理类

### 1.get_all_token

获取本地储存了的所有token

输出格式为：

序号) 用户名 tokenId

如

0)qzero ba438b32-23c0-425d-b3df-0511e0b5d401

### 2.delete_token

用法：`delete_token <tokenId>`

用于删除某个token

其中tokenId可以通过`get_all_token`获取

### 3.delete_all_token

删除全部token

## Tunnel管理类

### 1.new_tunnel

用法：`new_tunnel <tunnel端口> <加密模块名称> <tunnel的类别>`

新建一个tunnel

其中tunnel的类别有

- NATTraverse
- Proxy
- VirtualNetwork

不区分大小写

### 2.update_tunnel_crypto

用法：`update_tunnel_crypto <tunnel端口> <新的加密模块名称>`

更改tunnel的加密模块的名称，之前已建立的连接不会受影响，之后的连接会用新的加密模块

### 3.delete_tunnel

用法：`delete_tunnel <tunnel端口>`

用于删除一个tunnel

### 4.open_tunnel

用法：`open_tunnel <tunnel端口>`

用于开启一个tunnel

### 5.close_tunnel

用法：`close_tunnel <tunnel端口>`

用于关闭一个tunnel

### 6.get_tunnel_config

用法：`get_tunnel_config <tunnel端口>`

用于获取一个tunnel的相关信息

其中tunnelType对应关系为

1 内网穿透 NATTraverse

2 代理 Proxy

3 虚拟局域网 VirtualNetwork

### 7.get_all_tunnel_config

获取该用户的所有tunnel的信息

## 内网穿透映射管理类

### 1.new_nat_traverse_mapping

用法：`new_nat_traverse_mapping <tunnel端口> <本地IP> <本地端口>`

用于配置一个内网穿透映射

这里有个本地IP的选项，可以使得内网映射不只是映射到本机

比如`new_nat_traverse_mapping 8888 192.168.3.3 90`

发往服务器的8888的流量会被转发至192.168.3.3的90端口

### 2.update_nat_traverse_mapping

用法：`update_nat_traverse_mapping <tunnel端口> <本地IP> <本地端口>`

用于更新一个内网穿透映射

### 3.delete_nat_traverse_mapping

用法：`delete_nat_traverse_mapping <tunnel端口>`

用于删除一个内网穿透映射

### 4.get_nat_traverse_mapping

用法：`get_nat_traverse_mapping <tunnel端口>`

获取该端口的内网穿透映射信息

## 虚拟局域网映射管理类

### 1.new_virtual_network_mapping

用法：`new_virtual_network_mapping <目标标识符> <目标用户名>`

改命令用于添加一条虚拟局域网映射

这会导致ip或域名为 目标标识符 的流量都会转发至 目标用户名 对应的用户的设备上

比如使用`new_virtual_network_mapping test Bob`

当用浏览器（已开启Bridge并配置好了代理）访问http://test:80时，就会访问到Bob机器上运行着的http服务器

### 2.update_virtual_network_mapping

用法：`update_virtual_network_mapping <目标标识符> <目标用户名>`

更新映射

### 3.delete_virtual_network_mapping

用法：`delete_virtual_network_mapping <目标标识符>`

删除映射

## 代理类

### 1.start_proxy_bridge

用法：`start_proxy_bridge <本地端口> <tunnel端口>`

该命令在本地启动一个socks5服务器，发往该服务器的流量会发往作为代理的tunnel上

### 2.stop_proxy_bridge

用法：`stop_proxy_bridge <本地端口>`

关闭代理桥

## 虚拟局域网桥接类

### 1.start_virtual_network_bridge

用法：`start_virtual_network_bridge <本地端口> <tunnel端口>`

该命令在本地启动一个socks5服务器充当虚拟局域网的网卡

### 2.stop_virtual_network_bridge

用法：`stop_virtual_network_bridge <本地端口>`

关闭虚拟局域网桥

## 其他

### 1.reconnect_to_remind_server

通常客户端会与服务器有个用于提醒连接的长tcp连接，如果该连接断线，控制台会提醒使用该命令重连，否则某些功能将无法正常使用