package MTG_DDS.services.RedisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

	@Autowired
	private RedisTemplate< String, Object > template;

	public Object getValue(final String key) {
		return template.opsForValue().get(key);
	}

	public void setValue(final String key, final String value) {
		template.opsForValue().set(key, value);

		// set a expire for a message
		template.expire(key, 5, TimeUnit.SECONDS);
	}

}