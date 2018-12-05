package cn.zyf.mybatis;

public class Test {

	public static void main(String[] args) {
		SqlSession sqlSession = new SqlSession(new Configuration(), new SimpleExecutor());
		EntityMapper mapper = sqlSession.getMapper(EntityMapper.class);
		Entity entity = mapper.selectByPrimaryKey(1);
		System.out.println(entity);
	}
}