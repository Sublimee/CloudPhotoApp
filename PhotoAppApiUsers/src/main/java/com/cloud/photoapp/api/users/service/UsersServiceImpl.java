package com.cloud.photoapp.api.users.service;

import com.cloud.photoapp.api.users.data.UserEntity;
import com.cloud.photoapp.api.users.data.UsersRepository;
import com.cloud.photoapp.api.users.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsersServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        UserEntity createdUser = usersRepository.save(userEntity);
        return modelMapper.map(createdUser, UserDto.class);
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntityByEmail = usersRepository.findByEmail(email);
        if (userEntityByEmail == null) {
            throw new UsernameNotFoundException(email);
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(userEntityByEmail, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntityByEmail = usersRepository.findByEmail(username);
        if (userEntityByEmail == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(
                userEntityByEmail.getEmail(),
                userEntityByEmail.getEncryptedPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>()
        );
    }

}
