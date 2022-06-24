package proj.concert.service.mapper;

import proj.concert.common.dto.UserDTO;
import proj.concert.service.domain.User;

//A class which maps a user to a userDTO

public class UserMapper {
    private UserMapper(){};

    public static UserDTO toDTO(User user){
        return new UserDTO(user.getUsername(), user.getPassword());
    }
}
