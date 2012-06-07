package ru.yudindi.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ru.yudindi.VkException;
import ru.yudindi.model.MusicRecord;
import ru.yudindi.service.VkService;

@Service("vkService")
public class DefaultVkService implements VkService {
	@Value("vk.user")
	private String user;
	@Value("vk.password")
	private String password;
	@Value("vk.id")
	private String userId;// 980785

	private DefaultHttpClient httpClient = new DefaultHttpClient();
	private HttpContext context;

	public List<MusicRecord> getRemoteMusicList() {
		List<MusicRecord> records = new ArrayList<MusicRecord>();

		String response = execute("http://vk.com/audio.php",
				getMusicListParams(userId));
		Pattern p = Pattern.compile("\"all\"\\:\\[\\[(.*)\\]\\]");
		Matcher m = p.matcher(response);
		if (!m.find()) {
			throw new VkException("Error parsing music list");
		}
		String innerText = m.group(1);
		String[] recs = innerText.split("\\],\\[");
		Pattern recPattern = Pattern
				.compile("'\\d*','\\d*','(.*?)','.*?','.*?','(.*?)','(.*?)'.*");
		for (String rec : recs) {
			Matcher recMatcher = recPattern.matcher(rec);
			if (!recMatcher.find()) {
				throw new VkException("Error parsing music record: " + rec);
			}

			MusicRecord record = new MusicRecord(recMatcher.group(1),
					StringEscapeUtils.unescapeHtml4(recMatcher.group(2)),
					StringEscapeUtils.unescapeHtml4(recMatcher.group(3)));
			records.add(record);
		}
		return records;
	}

	private Map<String, String> getMusicListParams(String id) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("edit", "0");
		params.put("gid", "0");
		params.put("act", "load_audios_silent");
		params.put("al", "1");
		params.put("id", id);
		return params;
	}

	private String execute(String url, Map<String, String> params) {
		authorizeIfNeeded();

		try {
			HttpPost post = new HttpPost("http://vk.com/audio");

			List<NameValuePair> httpParams = new ArrayList<NameValuePair>();
			for (Entry<String, String> entry : params.entrySet()) {
				httpParams.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(httpParams);
			post.setEntity(entity);

			ResponseHandler responseHandler = new BasicResponseHandler();
			String response = httpClient.execute(new HttpHost("vk.com"), post,
					responseHandler, context);
			return response;
		} catch (IOException e) {
			throw new VkException("Failed to retrieve music list", e);
		}
	}

	/**
	 * checks authorizations status and logs in if needed
	 */
	private void authorizeIfNeeded() {
		try {
			// had to hack this one due to HTTP POST redirects breaking the
			// standards
			httpClient.setRedirectStrategy(new DefaultRedirectStrategy() {
				@Override
				public boolean isRedirected(HttpRequest request,
						HttpResponse response, HttpContext context)
						throws ProtocolException {
					if (response == null) {
						throw new IllegalArgumentException(
								"HTTP response may not be null");
					}

					int statusCode = response.getStatusLine().getStatusCode();
					String method = request.getRequestLine().getMethod();
					Header locationHeader = response.getFirstHeader("location");
					switch (statusCode) {
					case HttpStatus.SC_MOVED_TEMPORARILY:
						return (method.equalsIgnoreCase(HttpGet.METHOD_NAME)
								|| method
										.equalsIgnoreCase(HttpHead.METHOD_NAME) || method
									.equalsIgnoreCase(HttpPost.METHOD_NAME))
								&& locationHeader != null;
					case HttpStatus.SC_MOVED_PERMANENTLY:
					case HttpStatus.SC_TEMPORARY_REDIRECT:
						return method.equalsIgnoreCase(HttpGet.METHOD_NAME)
								|| method
										.equalsIgnoreCase(HttpHead.METHOD_NAME)
								|| method
										.equalsIgnoreCase(HttpPost.METHOD_NAME);
					case HttpStatus.SC_SEE_OTHER:
						return true;
					default:
						return false;
					} // end of switch
				}
			});
			CookieStore cookieStore = new BasicCookieStore();
			context = new BasicHttpContext();
			context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			HttpPost post = new HttpPost("http://login.vk.com");

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("act", "login"));
			params.add(new BasicNameValuePair("q", "1"));
			params.add(new BasicNameValuePair("al_frame", "1"));
			params.add(new BasicNameValuePair("from_host", "vk.com"));
			params.add(new BasicNameValuePair("email", "shmasser@gmail.com"));
			params.add(new BasicNameValuePair("pass", "Uhjntcr"));

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
			post.setEntity(entity);

			HttpResponse response = httpClient.execute(post, context);
			response.getEntity().writeTo(System.out);
		} catch (IOException e) {
			throw new VkException("Error during authorization", e);
		}
	}
}
