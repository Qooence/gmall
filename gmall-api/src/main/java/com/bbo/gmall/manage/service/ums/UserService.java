package com.bbo.gmall.manage.service.ums;

import com.bbo.gmall.manage.bean.ums.UmsMember;
import com.bbo.gmall.manage.bean.ums.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {

    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);
}
