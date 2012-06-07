package ru.yudindi.service;

import java.util.List;

import ru.yudindi.model.MusicRecord;

/**
 * Interface for all vk.com related activity
 * 
 * @author yudindi
 * 
 */
public interface VkService {

	List<MusicRecord> getRemoteMusicList();

}
