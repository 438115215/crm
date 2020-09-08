使用dubbo+springboot+mysql+zookeeper
基本目录结构
 
017是接口工程，普通maven创建，用来存放服务层接口
 
018是提供者，使用springboot创建web项目，用来存放接口实现类，在依赖中需要加入接口工程

	添加依赖
	<dependencies>
<!--        Springboot起步依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--Dubbo 集成 SpringBoot 框架起步依赖-->
        <dependency>
        <groupId>com.alibaba.spring.boot</groupId>
        <artifactId>dubbo-spring-boot-starter</artifactId>
        <version>2.0.0</version>
        </dependency>

        <!--Zookeeper 客户端依赖-->
        <dependency>
            <groupId>com.101tec</groupId>
            <artifactId>zkclient</artifactId>
            <version>0.10</version>
        </dependency>

        <!--MyBatis 集成 SpringBoot 框架起步依赖-->
        <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.0.1</version>
        </dependency>

        <!--MySQL 数据库驱动-->
        <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
            <version>5.1.39</version>
        </dependency>
<!--        接口工程-->
        <dependency>
            <groupId>com.shuhan</groupId>
            <artifactId>017_springboot-dubbo-interface</artifactId>
            <version>1.0.0</version>
        </dependency>


    </dependencies>


019是消费者，使用springboot创建web项目，用来存放控制器，在依赖中需要加入接口工程
同上创建
添加依赖
<dependencies>
<!--        springboot起步依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--Dubbo 集成 SpringBoot 框架起步依赖-->
        <dependency>
            <groupId>com.alibaba.spring.boot</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
            <version>2.0.0</version>
        </dependency>

        <!--Zookeeper 客户端依赖-->
        <dependency>
            <groupId>com.101tec</groupId>
            <artifactId>zkclient</artifactId>
            <version>0.10</version>
        </dependency>
<!--        接口工程-->
        <dependency>
            <groupId>com.shuhan</groupId>
            <artifactId>017_springboot-dubbo-interface</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>

接下来在接口工程017里编写service层的接口
public interface StudentService {
    Integer queryAllStudentCount();
}


在提供者018里编写改接口实现类

018配置文件application.properties
#设置内嵌 Tomcat 端口号
 server.port=8090 
#设置上下文根
 server.servlet.context-path=/
#配置数据源
 spring.datasource.driver-class-name=com.mysql.jdbc.Driver
 spring.datasource.url=jdbc:mysql://localhost:3306/springboot?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=true
 spring.datasource.username=root
 spring.datasource.password=123456
#配置 dubbo 的服务提供者信息 #服务提供者应用名称(必须写，且不能重复)
 spring.application.name=018_springboot-dubbo-provider
#设置当前工程为服务提供者
 spring.dubbo.server=true
#设置注册中心
 spring.dubbo.registry=zookeeper://localhost:2181

 

接口实现类
import com.alibaba.dubbo.config.annotation.Service;
import com.shuhan.service.StudentService;
import org.springframework.stereotype.Component;
@Component
@Service(interfaceClass = StudentService.class,version = "1.0.0",timeout = 15000)
public class StudentServiceImpl implements StudentService {

    @Override
    public Integer queryAllStudentCount() {
        return 1250;
    }
}

@Service里的interfaceClass是接口的.class，version是版本，timeout是每隔多少毫秒告诉注册中心连接未断开
@Component是将该类加入Bean


在消费者019里编写控制器

019配置文件application.properties
#设置内嵌 Tomcat 端口号
server.port=8081
#设置上下文根
server.servlet.context-path=/
#配置 dubbo 的服务提供者信息 #服务提供者应用名称(必须写，且不能重复)
spring.application.name=019-springboot-dubbo-consumer
#设置注册中心
spring.dubbo.registry=zookeeper://localhost:2181

编写控制器
import com.alibaba.dubbo.config.annotation.Service;
import com.shuhan.service.StudentService;
import org.springframework.stereotype.Component;
@Component
@Service(interfaceClass = StudentService.class,version = "1.0.0",timeout = 15000)
public class StudentServiceImpl implements StudentService {
    @Override
    public Integer queryAllStudentCount() {
        return 1250;
    }
}




Zookeeper启动在zookeeper官网下载安装包
 
解压缩
进入conf文件夹复制zoo_sample.cfg到当前目录下，并将复制后的副本文件改名为zoo.cfg
 
编辑zoo.cfg
 ticktime=2000，每隔2000毫秒向注册中心发送未断开连接
 dataDir存放产生数据的地址，推荐在zookeeper下bin的同级目录创建一个data文件夹，进入该文件夹，复制目录到zoo.cfg，将\改为/
 clientport=2181是zookeeper的服务端口
 admin.Serverport=8888，zookeeper有2个服务这个端口占用8080端口，推荐修改
 
启动zookeeper，进入bin目录下，双击，cmd结尾是windows操作系统，sh结尾是linux操作系统，这里是windows系统，所以我们双击zkServer.cmd
 
