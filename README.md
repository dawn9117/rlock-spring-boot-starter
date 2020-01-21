# rlock-spring-boot-starter
基于Redisson的分布式锁starter

### 特别提示: 配置项使用优先级(1 > 2 > 3): 
* 1. 优先使用通过 RedissonProperties.config 指定的配置
* 2. 配置文件中 spring.redis 前缀的配置
* 3. 默认配置
    
默认的锁会使用WatchDog自动延长锁的持有时间, 且无限等待获取锁, 如需修改, 可修改@Rlock中的参数

### 简介
该项目主要利用Spring Boot的自动化配置特性来实现快速的将redis分布式锁引入spring boot应用，简化Redisson的操作。

### 源码地址
GitHub：https://github.com/dawn9117/rlock-spring-boot-starter

##### 小工具一枚，欢迎使用和Star支持，如使用过程中碰到问题，可以提出Issue，我会尽力完善该Starter

### 版本基础
* spring-boot-starter-parent: 2.2.4.RELEASE
* redisson: 3.12.0
* fastjson: 1.2.62
* commons-collections4: 4.3
* commons.lang3: 3.9

### 如何使用
在该项目的帮助下，我们的Spring Boot可以轻松的引入redis分布式锁，主需要做下面两个步骤：

step1. 在pom.xml中引入依赖：
``` java
<dependency>
  <groupId>com.github.dawn9117</groupId>
  <artifactId>rlock-spring-boot-starter</artifactId>
  <version>${version}</version>
</dependency>
```

step2. 根据需求配置参数

参考:
* com.github.dawn9117.rlock.config.RlockProperties
* org.springframework.boot.autoconfigure.data.redis.RedisProperties
* org.redisson.config.Config


step3. 在需要加分布式锁的方法上增加@Rlock注解

``` java
@Service
public class UserService implements IUserService {

	@Rlock(keys = "#user.name")
	@Override
	public User add(User user) {
		// do something
		return user;
	}
	
}
```

#### 锁说明: 默认使用ReentrantLock(可通过LockModal指定)<br/> 规则如下(key构建规则可自定义, 参考 com.github.dawn9117.rlock.core.lock.name.LockNameBuilder): 
* 注解未指定keys: prefix + 项目名称 + 类全路径 + 方法名, 例如: /locks/project/com.github.dawn.UserService/add
* 指定keys: prefix + 项目名称 + 类全路径 + 方法名 + key, 例如 /locks/project/com.github.dawn.UserService/add/zhangsan
* SPEL: prefix + 项目名称 + 类全路径 + 方法名 + SPEL, 例如 @Rlock("#param1"): /locks/project/com.github.dawn.UserService/add/解析#param1得到的值