package cn.zyf.mybatis;

public interface Executor {

	public <T> T query(String statement, String parameter);
		
}
