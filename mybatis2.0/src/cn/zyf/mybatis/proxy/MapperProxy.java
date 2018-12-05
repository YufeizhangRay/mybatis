package cn.zyf.mybatis.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import cn.zyf.mybatis.registory.MapperRegistory;
import cn.zyf.mybatis.session.SqlSession;

public class MapperProxy<T> implements InvocationHandler{

	private SqlSession sqlSession;
	
	public MapperProxy(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		MapperRegistory.MapperData mapperData = MapperRegistory.methodSqlMapping
				.get(method.getDeclaringClass().getName() + "." + method.getName());
		if(mapperData!=null) {
			System.out.println(String.format("SQL [ %s ], parameter [%s] ", mapperData.getSql(), args[0]));
            return sqlSession.selectOne(mapperData, String.valueOf(args[0]));
		}
		return method.invoke(this, args);
	}

}
