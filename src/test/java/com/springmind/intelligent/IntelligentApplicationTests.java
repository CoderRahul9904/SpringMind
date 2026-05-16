package com.springmind.intelligent;

import com.springmind.intelligent.helper.Helper;
import com.springmind.intelligent.service.ChatClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class IntelligentApplicationTests {

	@Autowired
	public ChatClientService chatClientService;

	@Test
	public void checkDataStoreInVectorDB(){
		System.out.println("Putting Data in Vector DB - MariaDB");
		this.chatClientService.storeDocumentedData(Helper.getData());
		System.out.println("Data is Stored in MariaDB");
	}




}
