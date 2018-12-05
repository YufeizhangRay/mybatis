package cn.zyf.mybatis.session;

import cn.zyf.mybatis.config.Configuration;
import cn.zyf.mybatis.executor.Executor;
import cn.zyf.mybatis.registory.MapperRegistory;

public class SqlSession {

	private Configuration configuration;
	private Executor executor;

	public SqlSession(Configuration configuration, Executor executor) {
		this.configuration = configuration;
		this.executor = executor;
	}

	public <T> T getMapper(Class<T> clazz) {
		return configuration.getMapper(clazz, this);
	}

	@SuppressWarnings("rawtypes")
	public <T> T selectOne(MapperRegistory.MapperData mapperData, String parameter) {
		return executor.query(mapperData, parameter);
	}
}
