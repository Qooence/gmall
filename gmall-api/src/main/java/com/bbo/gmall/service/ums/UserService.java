package com.bbo.gmall.service.ums;

import com.bbo.gmall.bean.ums.UmsMember;
import com.bbo.gmall.bean.ums.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {

    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);
}
