# QZeroTunnelClient
## Description

The client of **QZeroTunnel**

Server project is [here][https://github.com/QZero233/QZeroTunnelServer]

It's a command based project

It mainly use Socks5 as local proxy server

## What can it do

### NAT traverse

There may be a firewall that blocks the outer client from connecting to your server deployed in local server.

Imagine that you have a http server on your personal computer but the firewall of your company does not allow the outer client to connect to the server.

Then all you need to do is to deploy a QZeroTunnel server on an outer server that both your PC and the client can connect to, add a new tunnel and a nat traverse mapping.

For example, your QZeroTunnel is deployed on 888.888.888.888, you use commands

```
new_tunnel 9999 plain NATTraverse
new_nat_traverse_mapping 9999 127.0.0.1 80
open_tunnel 9999
```

You can use your browser and enter http://888.888.888.888:9999 to visit the server running on 127.0.0.1:80

### Act as a proxy

The QZeroTunnelServer can act as a proxy server.

To access it, you need to create a new tunnel with type Proxy, the open it, and start proxy bridge on your client, and now you can use socks5 tool such as SwitchyOmega to use the proxy

For example:

```
new_tunnel 9999 plain Proxy
open_tunnel 9999
start_proxy_bridge 6666 9999
```

Then config socks5 server with ip 127.0.0.1 and port 6666, you can use proxy

### Build virtual local network

If two devices are in different different network environments where they can not connect to each other, QZeroTunnel can serve as a platform to help establish connection.

For example, Alice and Bob need to connect to each other

First, Alice(Or Bob, it does not matter who create the tunnel) should create a new tunnel with type VirtualNetwork

```
new_tunnel 9999 plain VirtualNetwork
```

Second, Alice need to specialize a dstIdentity to config Bob's ip

```
new_virtual_network_mapping 8.8.8.8 Bob
```

Then Alice should start virtual network bridge on her device

```
start_virtual_network_bridge 6666 9999
```

Finally, Bob should start a QZeroTunnel client and log in

Alice now can use socks5 tool such as Proxifier to access Bob's device, 8.8.8.8 now is Bob's ip address

Moreover, 8.8.8.8 can be replaced by any identity such as test, if you use command

```
new_virtual_network_mapping test Bob
```

Then data that send to domain name "test" will be sent to Bob's device.

## Compile

Since client and server have some common components, you should first clone server project

And install the module named TunnelCommon into local maven  repository by

```shell
mvn install
```



Then you can normally compile using maven

```shell
mvn package
```



## Deploy

#### Start jar via java command

```shell
java -jar QZeroTunnelClient.jar
```

By starting with `--prefer.checkServerAlive=false`, the program will not check if the server in database is online, which will save some launch time

#### Create or select server

(Notice: offline server won't be listed, it may take some time to check if the server is online)

#### Login or register

Follow the instructions to login or register

The password will get hashed when send to server to verify it



After that, you can use commands to do whatever you want.

Command guide are [here][commands.md]

## Features

### Transport security

All the data including handshake data (which contains the destination when the tunnel is a proxy) can be encrypted by customized crypto modules

You can write your own crypto module to bypass firewalls
