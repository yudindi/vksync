package ru.yudindi.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yudindi.VkException;
import ru.yudindi.model.MusicRecord;
import ru.yudindi.model.SyncLog;
import ru.yudindi.service.DataRepository;
import ru.yudindi.service.SyncService;
import ru.yudindi.service.VkService;

/**
 * Default implementation of SyncService
 * 
 * @author yudindi
 * 
 */
@Service("syncService")
public class DefaultSyncService implements SyncService {

	private static final Log LOG = LogFactory.getLog(DefaultSyncService.class);

	@Autowired
	private DataRepository repository;

	@Autowired
	private VkService vkService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.yudindi.service.SyncService#sync()
	 */
	public void sync() {
		LOG.info("Starting sync");
		SyncLog syncLog = repository.loadMusicRecordsLog();
		Set<MusicRecord> existingRecords = new HashSet<MusicRecord>(
				syncLog.getRecords());
		Set<MusicRecord> avaialbleRecords = new HashSet<MusicRecord>(
				vkService.getRemoteMusicList());
		avaialbleRecords.removeAll(existingRecords);
		LOG.info(String.format("%d new records found", avaialbleRecords.size()));
		for (MusicRecord record : avaialbleRecords) {
			try {
				repository.saveMusicFile(record);
				existingRecords.add(record);
			} catch (VkException e) {
				LOG.error(e);
			}
		}
		syncLog.setRecords(existingRecords);
		repository.saveMusicRecordsLog(syncLog);
		LOG.info("Sync finished");
	}

}
