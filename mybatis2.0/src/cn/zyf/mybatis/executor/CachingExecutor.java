package cn.zyf.mybatis.executor;

import java.util.HashMap;
import java.util.Map;

import cn.zyf.mybatis.registory.MapperRegistory;
import cn.zyf.mybatis.registory.MapperRegistory.MapperData;

public class CachingExecutor implements Executor{
	
	private SimpleExecutor delegate;
	
	private static Map<String,Object> localCache = new HashMap<String,Object>();
	
	public CachingExecutor(SimpleExecutor delegate) {
		this.delegate = delegate;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> T query(MapperData mapperData, String parameter) {
		Object result = localCache.get(mapperData.getSql());
        if( null != result){
            System.out.println("缓存命中");
            return (T)result;
        }
        //代理模式
        result =  (T) delegate.query(mapperData,parameter);
        localCache.put(mapperData.getSql(),result);
        return (T)result;
	}

}
