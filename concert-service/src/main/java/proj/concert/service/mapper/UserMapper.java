package proj.concert.service.mapper;

import proj.concert.common.dto.UserDTO;
import proj.concert.service.domain.User;

public class UserMapper {
    private UserMapper(){};

    public static UserDTO toDTO(User user){
        return new UserDTO(user.getUsername(), user.getPassword());
    }
}
