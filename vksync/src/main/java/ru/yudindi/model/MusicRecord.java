package ru.yudindi.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Entity class for a downloaded music file
 * 
 * @author yudindi
 * 
 */
@XmlRootElement
public class MusicRecord {

	@XmlAttribute
	private String remoteFileName;
	@XmlAttribute
	private String remoteTitle;
	@XmlAttribute
	private String remoteArtist;
	@XmlAttribute
	private String localFileName;
	@XmlAttribute
	private Date modifyDate;

	/**
	 * Creates a new Music Record
	 * 
	 * @param remoteFileName
	 *            file url at the web site
	 * @param romoteTitle
	 * @param romoteArtist
	 */
	public MusicRecord(String remoteFileName, String remoteTitle,
			String remoteArtist) {
		this.remoteFileName = remoteFileName;
		this.remoteTitle = remoteTitle;
		this.remoteArtist = remoteArtist;
		this.modifyDate = new Date();
	}

	public String getRemoteTitle() {
		return remoteTitle;
	}

	public String getRemoteArtist() {
		return remoteArtist;
	}

	public String getRemoteFileName() {
		return remoteFileName;
	}

	public void setRemoteFileName(String remoteFileName) {
		this.remoteFileName = remoteFileName;
	}

	public String getLocalFileName() {
		return localFileName;
	}

	public void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
		this.modifyDate = new Date();
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Override
	public int hashCode() {
		// Good enough
		return remoteTitle.hashCode() + remoteArtist.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MusicRecord)) {
			return false;
		}
		MusicRecord other = (MusicRecord) obj;
		return new EqualsBuilder().append(remoteTitle, other.getRemoteTitle())
				.append(remoteArtist, other.getRemoteArtist()).isEquals();
	}
}
