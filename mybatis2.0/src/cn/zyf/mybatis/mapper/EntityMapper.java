package cn.zyf.mybatis.mapper;

import cn.zyf.mybatis.entity.Entity;

public interface EntityMapper {
	
	public Entity selectByPrimaryKey(Integer userId);
}
