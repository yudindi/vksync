package ru.yudindi.service;

/**
 * Service interface for synchronization tasks
 * 
 * @author yudindi
 * 
 */
public interface SyncService {

	/**
	 * Sync all music records from vk.com to a local folder
	 */
	public void sync();

}
