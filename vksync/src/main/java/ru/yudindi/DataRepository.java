package ru.yudindi;

import java.io.InputStream;
import java.util.List;

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
	List<MusicRecord> loadMusicRecords();

	/**
	 * Processes and saves a music file. Does renaming and IDv3 tag population
	 * if needed
	 * 
	 * @param file
	 * @return
	 */
	MusicRecord saveMusicFile(InputStream is, String name);

}
