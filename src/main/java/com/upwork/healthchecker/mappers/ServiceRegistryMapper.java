package com.upwork.healthchecker.mappers;

import com.upwork.healthchecker.domain.ServiceRegistry;
import org.apache.ibatis.annotations.*;

import java.util.Collection;

/**
 * Created by rajaniy on 4/24/18.
 */
@Mapper
public interface ServiceRegistryMapper {
    @Select("SELECT * FROM service_registry")
    Collection<ServiceRegistry> findAll();

    @Select("SELECT * FROM service_registry where name = #{name}")
    ServiceRegistry findOne(@Param("name") String name);

    @Insert("INSERT INTO service_registry(name, servicetype, uri, query, username, password) values (#{name}, #{servicetype}, #{uri}, #{query}, #{username}, #{password})")
    @Options(useGeneratedKeys = true)
    int insert(ServiceRegistry serviceRegistry);

    @Delete("DELETE FROM service_registry WHERE name = #{name}")
    int delete(String name);
}
