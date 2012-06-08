package ru.yudindi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Stores information about a whole sync event
 * 
 * @author yudindi
 * 
 */
@XmlRootElement(name = "synchronization")
public class SyncLog {

	@XmlAttribute
	private Date lastDate;

	@XmlElement
	private List<MusicRecord> records;

	public SyncLog() {
		records = new ArrayList<MusicRecord>();
	}

	public Date getLastDate() {
		return lastDate;
	}

	public List<MusicRecord> getRecords() {
		return records;
	}

	public void setRecords(Collection<MusicRecord> records) {
		this.records = new ArrayList<MusicRecord>(records);
	}

}
