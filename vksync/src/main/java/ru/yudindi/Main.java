package ru.yudindi;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Main entry for the application
 * 
 * @author yudindi
 * 
 */
public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.scan("ru.yudindi");
		ctx.refresh();
	}

}
