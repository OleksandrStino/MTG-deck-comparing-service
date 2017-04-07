package MTG_DDS.repositories.implementation;

import MTG_DDS.entities.UserDTO;
import MTG_DDS.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private static final String KEY = "UserDTO";

	private RedisTemplate<String, UserDTO> redisTemplate;
	private HashOperations hashOps;

	@Autowired
	private UserRepositoryImpl(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOps = redisTemplate.opsForHash();
	}

	@Override
	public void saveUser(UserDTO userDTO) {
		hashOps.put(KEY, userDTO.getUserName(), userDTO);
	}

	@Override
	public UserDTO findUserByName(String userName) {
		return (UserDTO) hashOps.get(KEY, userName);
	}

	public void updateUser(UserDTO userDTO) {
		hashOps.put(KEY, userDTO.getUserName(), userDTO);
	}

	public UserDTO findUser(String id) {
		return (UserDTO) hashOps.get(KEY, id);
	}

	public void deleteUser(String id) {
		hashOps.delete(KEY, id);
	}

	public Map<Object, Object> findAllUsers() {
		return hashOps.entries(KEY);
	}
}
