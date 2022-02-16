# My-site的学习--Springboot2+Thyme leaf+Mybatis

## 2022.01.16 --开始动手第一天
-   按照原来的sql文件，创建了数据库，添加了些注释
-   开始写需要用到的Models(复制粘贴了些，感觉没意思)
-   了解到了Model和DTO的一些区别
    两者中都是一些实体类。Model是面向业务，也就是在项目中存在的一些是实体类，
    而DTO是面向界面的，是一些页面中会用到的类？也许是，暂时这样
-   了解到了dao和Service的一些区别
    dao层是最底层的和数据库连接的一些操作，
    Service则是将好多DAO的功能结合起来。
## 2022.01.17 --今日计划 
-   dao层、dto层和service层的写完,注意配置mybatis的locations
-   写到了Dao层，发现有些类是DTo的，于是先去写Dto了。
-   出现Error querying database.  Cause: java.lang.IndexOutOfBoundsException的错误;
    由于没有给返回值给无参构造器（按理来说无参构造器是自己生成的，但是我手贱写了个@AllargsConstructor）。
    所以mybatis在根据select出的属性来构造类时，使用的是无参构造器，所以才要加setter，getter
    @Reposity和@Mapper的区别，Reposity还要将mapper的.xml文件扫描进Spring，即还需要加上
    @MapperScan(mapper)注解
    mybatis中的resultMap得属性可以好好学学
-   完成了dao层和dto层，mapper和service还没写完

## 2022.01.18 -- 今日计划
-   继续完成mapper和service得编写
    写mapper.xml的时候多注意
    
## 2022.01.19 -- 写作业了
## 2022.01.20 -- 回家没事时间

## 2022.01.21 --今日继续
-   还在继续完成mapper，dao的实现的编写，service层暂时还没有动手，慢慢来

## 2022.01.22 
-   Spring中引入了对Cache的支持，缓存就是为了解决对同一个数据进行重复访问的问题。
    对于一个支持缓存的方法，Spring会在其被调用后将其返回值缓存起来，以保证下次利
    用同样的参数来执行该方法时可以直接从缓存中获取结果，而不需要再次执行该方法。
    Spring在缓存方法的返回值时是以键值对进行缓存的，值就是方法的返回结果，至于键
    的话，Spring又支持两种策略，默认策略和自定义策略，这个稍后会进行说明。需要注
    意的是当一个支持缓存的方法在对象内部被调用时是不会触发缓存功能的
-   引入了@Cacheable,@CacheEvict,@CachePut
-   @Cacheable : 可以标注在一个类上或方法上，  表示该类中所有方法或该方法是支持缓存的
    三个参数 value : 当前方法的返回值是会被缓存在哪个Cache上的，Cache的名称
            key : key属性是用来指定Spring缓存方法的返回结果时对应的key,
                自定义策略是指我们可以通过Spring的EL表达式来指定我们的key。
                这里的EL表达式可以使用方法参数及它们对应的属性。使用方法参
                数时我们可以直接使用“#参数名”或者“#p参数index”
            condition:表明是在哪种情况下能进行缓存处理，用EL表达式
    
-   @CachePut : 对比于@Cacheable,cacheable每次在执行前都会检查Cache中有没有相同key的缓存元素，如果存在，则直接返回，
                不存在再执行，并将返回结果插入到指定缓存中。而@CachePut则在每次执行前不会去检查，而去执行方法
    
-   @CacheEvict:是用来标注在需要清除缓存元素的方法或类上的。
        新的属性:allEntries:表示是否需要清除缓存中的所有元素
                beforeInvocation:表示在执行前进行清除，默认为false，即执行后清除
    
## 2022.01.23
-   Spring中的事务，事务具有原子性，一致性，隔离性，持久性
    Spring中的事务管理分为：
        编程式事务：允许用户在代码中精确的的定义事务的边界。
        声明式事务：基于AOP,有助于用户将操作与事务规则进行解耦。其本质是对方法前后进行拦截，
                    然后在目标方法开始之前创建或者加入一个事务，在执行完目标方法之后根据执
                    行情况提交或者回滚事务。可以在xml中进行配置，也可以通过@Transactional
                    注解的方法进行。
    
- @Transactional:可以加在类或者接口上，@Transactional注解会对其中的public方法起作用。
                1.建议将@Transactional注解直接用在**public**方法上，直接用在类上一影响性能，二需要配置的参数不同。
                2.有关函数之间的相互调用和Transaction的关系。https://www.jianshu.com/p/befc2d73e487这篇博客。
                    简单说一下同一个类中的调用，如果函数被内部调用则Transaction失效，如果外部则成功。
                    不同类之间建议看博客
  还在搞TaleUtils 直接粘贴了，太多了，就是些繁琐的功能。
  
## 2022.01.28
-   最近搞完了service等从明天开始写Controller

## 2022.02.02
-   最近过年，同时为前端如何写感到很头疼，最终决定后台管理还是采用人家的，因为后台的模板没找好，同时感觉比较复杂，以后有时间再改
-   现在的话就开始着手先把后台写完，然后稍微测试下，博客的模板已经找好，慢慢改。

-   @Controller注解 + @**Mapping + @ResponseBody 注解最终会返回对应的类型，
    @Controller注解 + @**Mapping 注解会对应跳转到对应的html中。
    
关于Interceptor 需要先 implements HandlerInterceptor 实现自己的拦截器，然后在其中的方法中进行拦截
最后还要将myInterceptor加入在WebMvcConfigurer中的addInterceptors中加入。

以后写mapper.xml的时候不要往sql语句后面加分号;因为比如说PageHepler就是根据后面加个limit x,来实现的，一加分号就直接寄了。


今天把后台整合了一部分，还要继续完善

#2022.02.03
在通过mybatis进行数据的insert操作之后，如何在拿到自增后的主键编号呢？ 可以通过keyProperty属性，进行设置，他会把的到的主键值，绑定到改实体类的对象上
#2022.02.04

#2022.02.05
关于Model,HttpServletRequest 和HttpServletRequest.getSession() ,ModelMap(没用过)的用法
但取值的优先级不同，优先取Model和ModelMap的，Model和ModelMap是同一个东西，谁最后赋值的就取谁的，然后是request，最后是从session中获取

# 202202.06
下午搞了搞docsify 现在ubuntu上试，有复习了创建软连接  ln -s (当前的文件名) (拷贝至软连接的文件夹)
然后还有就是关于sources.list中的软件源的问题，不同版本的Ubuntu对应的那个前缀不一样，我是20.04的好像，然后把facal改为了xentail好像，我也给忘了
然后又学了vim中批量替换 在V模式下 输入% s/(替换前)/(替换后)/g(全局)
然后在githubPage上配置好了docs仓库以后就能方便写文档了，唉，不知道什么时候才能有自己的东西
以后假如给某个项目要配置githubPage的文档，就要在改项目目录下 用docsify init ./docs创建文档目录，然后在githu上要设置可见资源为/docs
最后关于docsify的东西暂时还没学

然后就是继续写thymeleaf了，关于fragment中可以设置参数，可以用th:with来引入参数等

现在卡在了那个blog的问题上明天继续解决，其实还是路径的问题。