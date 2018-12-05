package cn.zyf.mybatis.executor;

public abstract class AbstractFactory {

    public abstract SimpleExecutor getSimpleExecutor();
    
    public abstract CachingExecutor getCachingExecutor();

}
