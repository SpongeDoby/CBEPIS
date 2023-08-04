package com.example.cbepis.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.cbepis.entity.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoleMapper extends BaseMapper<Role> {
    @Select("select mid from role_menu where rid = #{roleId}")
    List<Integer> queryPermissionByRid(Integer roleId);

    @Delete("delete from role_menu where rid = #{rid}")
    void deleteRoleByRid(Integer rid);

    @Insert("insert into role_menu(rid,mid) values (#{rid},#{mid})")
    void saveMidByRoleId(Integer rid,Integer mid);

    @Select("select rid from user_role where uid=#{id}")
    List<Integer> getRoleByUid(Integer id);

    @Delete("delete from user_role where uid=#{uid}")
    void deleteRoleByUid(Integer uid);

    @Insert("insert into user_role(uid,rid) values (#{uid},#{rid})")
    void saveRidByUserId(Integer uid,Integer rid);

    @Select("select uid from user_role where rid=2")
    List<Integer> getTeachers();
}
