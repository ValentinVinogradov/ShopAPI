package com.shopapi.shop.impl;

import com.shopapi.shop.models.User;
import com.shopapi.shop.repository.UserRepository;
import com.shopapi.shop.services.AbstractService;
import com.shopapi.shop.services.UserService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends AbstractService<User, Long> implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);  // Вызов конструктора родительского класса
        this.userRepository = userRepository;
    }


    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public boolean userExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public void changePassword(long userId, String newPassword) {
        //todo change password
    }

    @Override
    public User authenticateUser(String email, String password) {
        return null; //todo в самом конце сделать аутентификацию
    }
}
