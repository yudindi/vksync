package ru.yudindi.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import ru.yudindi.VkException;
import ru.yudindi.model.MusicRecord;
import ru.yudindi.model.SyncLog;
import ru.yudindi.service.DataRepository;

/**
 * Local FS data storage
 * 
 * @author yudindi
 * 
 */
@Repository("dataRepository")
public class LocalDataRepository implements DataRepository {
	private static final Log LOG = LogFactory.getLog(LocalDataRepository.class);

	private static final long DEFAULT_COPY_BUFFER_SIZE = 1024 * 1024 * 10;

	@Value("${music.synclog.path}")
	private String musicSyncFile;

	@Value("${music.store.path}")
	private String musicStorePath;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.yudindi.DataRepository#loadMusicRecords()
	 */
	public SyncLog loadMusicRecordsLog() {
		LOG.info("Loading list of existing records from the filesystem: "
				+ musicSyncFile);
		try {
			File syncLogFile = new File(musicSyncFile);
			if (!syncLogFile.exists()) {
				LOG.warn(String
						.format("Music sync log at '%s' not found, returning an empty result",
								musicSyncFile));
				return new SyncLog();
			}
			JAXBContext context = JAXBContext.newInstance(SyncLog.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			SyncLog log = (SyncLog) unmarshaller.unmarshal(syncLogFile);
			return log;
		} catch (JAXBException e) {
			throw new VkException(
					"Failed to demarshall list of synced records from an xml",
					e);
		}
	}

	public void saveMusicRecordsLog(SyncLog syncLog) {
		LOG.info("Saving list of existing records to the filesystem: "
				+ musicSyncFile);
		try {
			File syncLogFile = new File(musicSyncFile);
			JAXBContext context = JAXBContext.newInstance(SyncLog.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(syncLog, syncLogFile);
		} catch (JAXBException e) {
			throw new VkException(
					"Failed to marshall list of synced records to an xml", e);
		}
	}

	private File getDestinationFile(MusicRecord record) {
		String filename = record.getRemoteArtist() + " - "
				+ record.getRemoteTitle();
		filename = filename.replaceAll("[/\\\\]", "");
		return new File(musicStorePath + "/" + filename + ".mp3");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.yudindi.DataRepository#saveMusicFile(java.io.File)
	 */
	public MusicRecord saveMusicFile(MusicRecord record) {
		FileLock lock = null;
		FileOutputStream outputStream = null;
		FileChannel outputChannel = null;
		ReadableByteChannel inputChannel = null;
		InputStream is = null;
		try {
			is = new URL(record.getRemoteFileName()).openStream();
			File f = getDestinationFile(record);
			LOG.info(String.format("[%s]=>[%s]", record.getRemoteFileName(),
					f.getAbsolutePath()));
			outputStream = new FileOutputStream(f);
			outputChannel = outputStream.getChannel();
			inputChannel = Channels.newChannel(is);
			lock = outputChannel.lock();

			long len = 0;
			long pos = 0;
			while ((len = outputChannel.transferFrom(inputChannel, pos,
					DEFAULT_COPY_BUFFER_SIZE)) > 0) {
				pos += len;
			}

			record.setLocalFileName(f.getAbsolutePath());
		} catch (IOException e) {
			throw new VkException("Error saving file to a filesystem", e);
		} finally {
			if (lock != null) {
				try {
					lock.release();
				} catch (IOException e) {
					LOG.error(e);
				}
			}
			if (outputChannel != null) {
				try {
					outputChannel.close();
				} catch (IOException e) {
					LOG.error(e);
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					LOG.error(e);
				}
			}
			if (inputChannel != null) {
				try {
					inputChannel.close();
				} catch (IOException e) {
					LOG.error(e);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.error(e);
				}
			}
		}

		return record;
	}
}
