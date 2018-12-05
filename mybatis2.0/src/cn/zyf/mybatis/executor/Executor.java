package cn.zyf.mybatis.executor;

import cn.zyf.mybatis.registory.MapperRegistory;
import cn.zyf.mybatis.registory.MapperRegistory.MapperData;

public interface Executor {

	@SuppressWarnings("rawtypes")
	public <T> T query(MapperRegistory.MapperData mapperData,String parameter);
}
