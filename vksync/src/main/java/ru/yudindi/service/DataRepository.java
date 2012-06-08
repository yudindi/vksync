package ru.yudindi.service;

import ru.yudindi.model.MusicRecord;
import ru.yudindi.model.SyncLog;

/**
 * Interface for all synced data load/store
 * 
 * @author yudindi
 * 
 */
public interface DataRepository {

	/**
	 * Loads list of all music records synced before that moment
	 * 
	 * @return
	 */
	SyncLog loadMusicRecordsLog();

	void saveMusicRecordsLog(SyncLog syncLog);

	/**
	 * Processes and saves a music file. Does renaming and IDv3 tag population
	 * if needed
	 * 
	 * @param file
	 * @return
	 */
	MusicRecord saveMusicFile(MusicRecord record);

}
