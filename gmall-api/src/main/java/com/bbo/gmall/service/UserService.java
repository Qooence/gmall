package com.bbo.gmall.service;

import com.bbo.gmall.bean.UmsMember;
import com.bbo.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {

    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);
}
