# mybatis1.0
  
## 手写简易版mybatis框架  
  
### Mybatis的部分组件及查询运行原理源码分析  
  
#### SqlSession  
SqlSession为所有的对外操作的出口，地位极高。其中包含两个重要属性，分别是Executor与Configuration。 
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/Mybatis%E6%9E%B6%E6%9E%84%E5%9B%BE.jpeg)   
  
首先我们来从最开始的代码入手，获取SqlSession，然后调用其getMapper()方法，  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/getMapper.jpeg)  
  
此方法会自动跳转到SqlSession接口的实现类DefaultSqlSession的getMapper()方法，此方法会返回configuration.getMapper()方法。 
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/sessionGetMapper.jpeg)  
  
#### Configuration  
configuration.getMapper()方法返回MapperRegistry类的getMapper()方法，  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/configGetMapper.jpeg)
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/registryGetMapper.jpeg)  
  
此方法其中通过MapperProxyFactory类的newInstance()方法获取MapperProxy(此类实现了InvocationHandler接口)的实例(参数mapperInterface其实就是生成代理需要的类，也就是我们最开始在getMapper()传入的参数)，然后再以此实例作为参数，通过动态代理方法来获得Mapper的代理对象。  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/MapperProxyFactory.jpeg)  
  
此动态代理为阉割版的动态代理，正常的动态代理中，proxy中会保存有实现接口的类的target对象，invoke()方法可以将target对象和args作为参数来返回一个执行method.invoke(target,args)方法，但是MapperProxy中只有接口的类，没有其实现类(因为Mapper本身就不存在实现类，只有对应的xml文件)，无法执行method.invoke()方法。 
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/invoke.jpeg)  
  
由于是动态代理，每次我们操作任何方法时，都会触发代理类的invoke()方法，此方法会返回mapperMethod.execute()方法(mapperMethod是从methodCache这个ConcurrentHashMap中取出来的，而methodCache本身又是在MapperProxyFactory类中进行初始化，然后在MapperProxy获取实例的时候作为一个参数传入)，
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/execute.jpeg)  
  
其内部调用了SqlSession中的selectOne()方法。  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/selectOne.jpeg)  
  
#### Executor  
负责执行对数据库操作的方法。上述的SqlSession的实现类DefaultSqlSession中的selectOne()方法，会调用一个selectList()方法(DefaultSqlSession类中有包含了多种selectList()的重载方法来满足多种需要)，
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/selectList.jpeg)
  
selectList()方法中的executor.query()会执行操作数据库的方法。执行query()方法的实际上是Executor的实现类CachingExecutor，此类会判断是否带有缓存，若没有则会用一个名为delegate的SimpleExecutor实体类(这里用到了装饰器模式)的父类BeasExecutor的query()方法。  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/querydelegate.jpeg)
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/queryFather.jpeg)  
  
此方法若判断为没有缓存，则会调用queryfromDatabase()方法，并给methodCache插入一个占位符，后面的doQuery()方法会再次返回SimpleExecutor中。 
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/queryFromDatabase.jpeg)  
  
StatementHandler  
对操作数据库的statement进行操作的组件。SimpleExecutor的doQuery()方法会创建一个Configuration的实例，并且通过此实例获取一个StatementHandler对象，  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/doQuery.jpeg)  
  
之后又调用prepareStatement()方法获得一个Statement对象,(prepareStatement()方法中获取到了连接对象connection)，  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/preparementStatement.jpeg)  
  
并返回handler.query()方法进入RoutingStatementHandler的query()方法中，  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/query.jpeg)  
  
再进入PreparedStatementHandler的query()方法，此方法会获取一个PreparedStatement对象(所以Mybatis是可以防止注入攻击的)，  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/queryResultSet.jpeg)  
  
然后返回一个resultSetHandler.handleResultSets(),进入DefaultResultSetHandler的handleResultSets()方法中。  
      
ResultSetHandler  
处理ResultSet的组件(映射)。DefaultResultSetHandler的handleResultSets()方法对resultSet中所有的数据做处理，  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/handleResultSets.jpeg)  
  
其中调用本类的handleResultSet()方法，  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/handleResultSet.jpeg)  
  
此方法可以处理结果集中每一行中的value值，通过handleRowValuesForSimpleResultMap()方法中的getRowValue()来获取value，  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/handleRowValuesForSimpleResultMap.jpeg)
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/getRowValue.jpeg)  
  
用createResultObject()方法创建出结果对象，此时的对象的属性为null，  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/createResultObject.jpeg)  
  
再调用applyAutomaticMappings()给对象填充结果，完成映射。  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/applyAutomaticMapping1.jpeg)
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/applyAutomaticMapping2.jpeg)  
  
调用storeObject()方法进行保存。 
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/stroeObject.jpeg)  
  
执行其中调用本类的handleResultSet()方法finally块中的closeResultSet()方法,内部使用resultSet.close()方法关闭资源。
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/closeResultSet.jpeg)
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/closeResultSetBody.jpeg)  
  
调用collapseSingleResultList()方法返回结果。  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/collapseSingleResultLIst.jpeg)  
  
执行SimpleExecutor的doQuery()方法finally块中的closeStatement()方法，内部使用statement.close()方法关闭资源。  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/closeStatement.jpeg)
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/closeStatementBody.jpeg)  
  
queryfromDatabase()方法将，移除最开始放置的占位符，将结果存入methodCache中。  
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/localCache.jpeg)  
  
最后reset防止内存泄漏。  
  
#### 上述过程简易时序图：    
![](https://github.com/YufeizhangRay/image/blob/master/Mybatis/Mybatis%E7%AE%80%E6%98%93%E6%97%B6%E5%BA%8F%E5%9B%BE.jpeg)  
  
#### 总结
由源码可知，Mybatis不仅可以防止注入攻击，而且在资源的利用上也做的十分全面(缓存、连接的关闭)。底层技术依然是JDBC，但是对其进行了封装，隔绝了JDBC原有的代码操作，使开发者在进行数据库交互操作的时候更加的得心应手。
