package life.chen.community.community.mapper;

import life.chen.community.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
     @Insert("insert into javauser (name,account_id,token,gmt_create,gmt_modified) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
     void insert(User user);

     @Select("select * from javauser where Token = #{token}")
     User findByToken(@Param("token") String token);
}
