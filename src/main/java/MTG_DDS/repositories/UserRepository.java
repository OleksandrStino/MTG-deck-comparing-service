package MTG_DDS.repositories;


import MTG_DDS.entities.UserDTO;

public interface UserRepository {

	void saveUser(UserDTO userDTO);

	UserDTO findUserByName(String userName);

}
