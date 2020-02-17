package com.bbo.gmall.manage;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManageWebApplicationTests {

	@Test
	public void contextLoads() {
		try {
			// 配置fdfs的全局连接地址
			String tracker = GmallManageWebApplicationTests.class.getResource("/tracker.conf").getPath();
			ClientGlobal.init(tracker);

			TrackerClient trackerClient = new TrackerClient();
			// 获得一个TrackerServer的实例
			TrackerServer trackerServer = trackerClient.getTrackerServer();

			// 通过tracker获得一个Storage连接客户端
			StorageClient storageClient = new StorageClient(trackerServer, null);

			String[] uploadInfos = storageClient.upload_file("d://a.jpg", "jpg", null);
			String url = "http://192.168.45.180/";

			for (String uploadInfo : uploadInfos) {
				url += "/" + uploadInfo;
			}

			System.out.println(url);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (MyException e) {
			e.printStackTrace();
		}
	}

}
