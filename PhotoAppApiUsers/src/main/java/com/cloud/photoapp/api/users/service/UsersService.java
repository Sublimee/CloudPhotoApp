package com.cloud.photoapp.api.users.service;

import com.cloud.photoapp.api.users.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserDetailsByEmail(String email);

}
