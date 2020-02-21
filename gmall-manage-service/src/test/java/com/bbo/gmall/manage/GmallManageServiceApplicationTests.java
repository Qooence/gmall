package com.bbo.gmall.manage;

import com.bbo.gmall.bean.pms.PmsBaseAttrValue;
import com.bbo.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.bbo.gmall.manage.util.CompareListBeanUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManageServiceApplicationTests {

	@Autowired
	private PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

	@Test
	public void contextLoads() {
		Map<String, List<PmsBaseAttrValue>> map = CompareListBeanUtil.compare(pmsBaseAttrValueMapper.findByAttrId("12"),null,"valueName");
		System.out.println(map.get("insert"));
		System.out.println(map.get("update"));
		System.out.println(map.get("delete"));
	}

}
