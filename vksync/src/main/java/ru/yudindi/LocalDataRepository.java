package ru.yudindi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Local FS data storage
 * 
 * @author yudindi
 * 
 */
@Repository("dataRepository")
public class LocalDataRepository implements DataRepository {
	private static final Log LOG = LogFactory.getLog(LocalDataRepository.class);

	@Value("music.synclog.path")
	private String musicSyncFile;

	@Value("music.store.path")
	private String musicStorePath;

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.yudindi.DataRepository#loadMusicRecords()
	 */
	public List<MusicRecord> loadMusicRecords() {
		try {
			File syncLogFile = new File(musicSyncFile);
			if (!syncLogFile.exists()) {
				LOG.warn(String
						.format("Music sync log at '%s' not found, returning an empty result",
								musicSyncFile));
				return Collections.emptyList();
			}
			JAXBContext context = JAXBContext.newInstance(MusicRecord.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			List<MusicRecord> list = (List<MusicRecord>) unmarshaller
					.unmarshal(syncLogFile);
			return list;
		} catch (JAXBException e) {
			throw new VkException(
					"Failed to demarshall list of synced records from an xml",
					e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ru.yudindi.DataRepository#saveMusicFile(java.io.File)
	 */
	public MusicRecord saveMusicFile(InputStream is, String name) {
		try {
			File f = new File(musicStorePath + "/" + name);
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(f));
			byte[] buf = new byte[10000];
			int len = 0;
			while ((len = is.read(buf)) > 0) {
				bos.write(buf, 0, len);
			}
			bos.close();
		} catch (IOException e) {
			throw new VkException("Error saving downloaded file", e);
		}
		return new MusicRecord(); // TODO
	}
}
