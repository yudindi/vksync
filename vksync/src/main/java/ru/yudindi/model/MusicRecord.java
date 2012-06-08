package ru.yudindi.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Entity class for a downloaded music file
 * 
 * @author yudindi
 * 
 */
public class MusicRecord {

	private String remoteFileName;

	private String remoteTitle;

	private String remoteArtist;

	private String localFileName;

	private Date modifyDate;

	public MusicRecord() {
		// JAXB
	}

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

	@XmlAttribute
	public String getRemoteTitle() {
		return remoteTitle;
	}

	@XmlAttribute
	public String getRemoteArtist() {
		return remoteArtist;
	}

	public void setRemoteArtist(String remoteArtist) {
		this.remoteArtist = remoteArtist;
	}

	public void setRemoteTitle(String remoteTitle) {
		this.remoteTitle = remoteTitle;
	}

	@XmlAttribute
	public String getRemoteFileName() {
		return remoteFileName;
	}

	public void setRemoteFileName(String remoteFileName) {
		this.remoteFileName = remoteFileName;
	}

	@XmlAttribute
	public String getLocalFileName() {
		return localFileName;
	}

	public void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
		this.modifyDate = new Date();
	}

	@XmlAttribute
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
