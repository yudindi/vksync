package ru.yudindi.service;

import java.util.Set;

import ru.yudindi.model.MusicRecord;

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
	Set<MusicRecord> loadMusicRecordsLog();

	void saveMusicRecordsLog(Set<MusicRecord> records);

	/**
	 * Processes and saves a music file. Does renaming and IDv3 tag population
	 * if needed
	 * 
	 * @param file
	 * @return
	 */
	MusicRecord saveMusicFile(MusicRecord record);

}
