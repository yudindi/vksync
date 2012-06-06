package ru.yudindi;

import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("vkService")
public class DefaultVkService implements VkService {
	@Value("vk.user")
	private String user;
	@Value("vk.password")
	private String password;
	@Value("vk.id")
	private String userId;

	private HttpClient httpClient = new DefaultHttpClient();

	public List<MusicRecord> getRemoteMusicList() {
		String response = execute("http://vk.com/audio.php"); // TODO
		return null;
	}

	private String execute(String url) {
		authorizeIfNeeded();
		return ""; // TODO
	}

	private void authorizeIfNeeded() {
		// TODO Auto-generated method stub

	}

}
