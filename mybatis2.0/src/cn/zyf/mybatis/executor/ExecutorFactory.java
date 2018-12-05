package cn.zyf.mybatis.executor;

//抽象工厂模式
public class ExecutorFactory extends AbstractFactory{

	@Override
	public SimpleExecutor getSimpleExecutor() {
		return new SimpleExecutor();
	}

	@Override
	public CachingExecutor getCachingExecutor() {
		return new CachingExecutor(getSimpleExecutor());
	}

}
