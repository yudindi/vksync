package ru.yudindi;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

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
	private String romoteTitle;
	@XmlAttribute
	private String romoteArtist;
	@XmlAttribute
	private String localFileName;
	@XmlAttribute
	private Date modifyDate;

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
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}
