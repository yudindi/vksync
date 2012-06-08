package ru.yudindi;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.yudindi.service.SyncService;

/**
 * Main entry for the application
 * 
 * @author yudindi
 * 
 */
public class VkSyncMain {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		SyncService service = ctx.getBean(SyncService.class);
		service.sync();
	}
}
