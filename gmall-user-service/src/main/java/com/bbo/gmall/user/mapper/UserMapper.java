package com.bbo.gmall.user.mapper;

import com.bbo.gmall.bean.ums.UmsMember;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<UmsMember>{

    List<UmsMember> selectAllUser();

}
