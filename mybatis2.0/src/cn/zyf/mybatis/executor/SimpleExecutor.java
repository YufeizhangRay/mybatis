package cn.zyf.mybatis.executor;

import cn.zyf.mybatis.handler.StatementHandler;
import cn.zyf.mybatis.registory.MapperRegistory;
import cn.zyf.mybatis.registory.MapperRegistory.MapperData;

public class SimpleExecutor implements Executor{

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T query(MapperRegistory.MapperData mapperData, String parameter) {
        //初始化StatementHandler --> ParameterHandler --> ResultSetHandler
        StatementHandler handler = new StatementHandler();
        return (T) handler.query(mapperData, parameter);
    }
}
