package ru.yudindi;

/**
 * Generic application wide exception
 * 
 * @author yudindi
 * 
 */
public class VkException extends RuntimeException {

	public VkException(String message, Throwable cause) {
		super(message, cause);
	}

	public VkException(String message) {
		super(message);
	}

}
