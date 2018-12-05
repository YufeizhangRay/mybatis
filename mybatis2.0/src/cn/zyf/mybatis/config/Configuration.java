package cn.zyf.mybatis.config;


import cn.zyf.mybatis.registory.MapperRegistory;
import cn.zyf.mybatis.session.SqlSession;

public class Configuration {
	
	private MapperRegistory mapperRegistory = new MapperRegistory();
	
	public <T> T getMapper(Class<T> clazz,SqlSession sqlSession) {
		return mapperRegistory.getMapper(clazz, sqlSession);
	}
}
