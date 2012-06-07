package ru.yudindi;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ru.yudindi.service.SyncService;

/**
 * Main entry for the application
 * 
 * @author yudindi
 * 
 */
public class VkSyncMain {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.scan("ru.yudindi");
		ctx.refresh();
		SyncService service = ctx.getBean(SyncService.class);
		service.sync();
	}

}
