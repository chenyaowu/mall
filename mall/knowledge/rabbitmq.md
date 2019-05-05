# 安装rabbitmq

1. 安装[erlang](http:*//erlang.org/download/otp_src_20.0.tar.gz*)

2. 安装依赖

  ```bash
  yum -y install make gcc gcc-c++ kernel-devel m4 ncurses-devel openssl-devel
  yum -y install ncurses-devel unixODBC-devel
  ```

3. 解压erlang，配置，编译安装

  ```bash
  chmod +x otp_src_20.0.tar.gz
  tar -xzvf otp_src_20.0.tar.gz
  cd otp_src_20.0/
  ./configure --prefix=/usr/local/erlang --with-ssl --enable-threads --enable-smp-support --enable-kernel-poll --enable-hipe --without-javac  //不用java编译，故去掉java避免错误  
  ```

  

4.  配置环境变量

  ```bash
  vi /etc/profile
  
  #在文件末尾添加下面代码
  ERL_HOME=/usr/local/erlang  
  export PATH=$PATH:$ERL_HOME/bin
  
  #使环境变量生效
  source /etc/profile
  
  #输入命令检验是否安装成功
  erl
  ```

5. 安装[rabbitmq](http:*//www.rabbitmq.com/releases/rabbitmq-server/current/rabbitmq-server-generic-unix-3.6.10.tar.xz*)

6. 解压，配置，启动

   ```bash
   yum -y install xmlto
   chmod +x rabbitmq-server-generic-unix-3.6.10.tar.xz
   xz -d rabbitmq-server-generic-unix-3.6.10.tar.xz
   tar -xvf rabbitmq-server-generic-unix-3.6.10.tar
   
   #开启管理页面插件
   mv rabbitmq_server-3.6.10/ /usr/local/rabbitmq
   cd /usr/local/rabbitmq/sbin/
   ./rabbitmq-plugins enable rabbitmq_management
   
   #配置环境变量
   vi /etc/profile
   #在文件末尾添加下面代码
   RMQ_HOME=/usr/local/rabbitmq  
   export PATH=$PATH:$RMQ_HOME/sbin
   #使环境变量生效
   source /etc/profile
   
   #启动命令，该命令ctrl+c后会关闭服务
   rabbitmq-server
   #在后台启动Rabbit
   rabbitmq-server -detached 
   # Warning: PID file not written; -detached was passed.可忽略警告
   #关闭服务
   rabbitmqctl stop
   #关闭服务(kill) 找到rabbitmq服务的pid[不推荐]
   ps -ef|grep rabbitmq
   kill -9 ****
   
   #添加用户
   #rabbitmqctl add_user Username Password
   rabbitmqctl add_user rabbitadmin 123456
   #分配用户标签
   #rabbitmqctl set_user_tags User Tag
   #[administrator]:管理员标签
   rabbitmqctl set_user_tags rabbitadmin administrator
   
   #登录管理界面
   #浏览器输入地址：http://服务器IP地址:15672/
   
   #设置端口号，可供外部访问
   vi /etc/sysconfig/iptables
   #将下述两条规则添加到默认的22端口这条规则的下面
   -A INPUT -m state --state NEW -m tcp -p tcp --dport 5672 -j ACCEPT
   -A INPUT -m state --state NEW -m tcp -p tcp --dport 15672 -j ACCEPT
   
   iptables -I INPUT -p tcp --dport 5672 -j ACCEPT
   iptables -I INPUT -p tcp --dport 15672 -j ACCEPT
   ```

   

   
