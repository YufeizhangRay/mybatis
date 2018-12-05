package cn.zyf.mybatis.test;

import cn.zyf.mybatis.config.Configuration;
import cn.zyf.mybatis.entity.Entity;
import cn.zyf.mybatis.executor.ExecutorFactory;
import cn.zyf.mybatis.mapper.EntityMapper;
import cn.zyf.mybatis.session.SqlSession;

public class Test {

	public static void main(String[] args) {
		Configuration configuration = new Configuration();
		ExecutorFactory executorFactory = new ExecutorFactory();
		SqlSession sqlSession = new SqlSession(configuration, 
				executorFactory.getCachingExecutor());
		EntityMapper entityMapper = sqlSession.getMapper(EntityMapper.class);
		Entity entity = entityMapper.selectByPrimaryKey(1);
		System.out.println(entity);
		Entity entity1 = entityMapper.selectByPrimaryKey(1);
		System.out.println(entity1);
	}
}
