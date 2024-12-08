package com.shopapi.shop.impl;

import com.shopapi.shop.models.User;
import com.shopapi.shop.repository.UserRepository;
import com.shopapi.shop.services.AbstractService;
import com.shopapi.shop.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends AbstractService<User, Long> implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);  // Вызов конструктора родительского класса
        this.userRepository = userRepository;
    }


    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUsername(Long userId, String username) {
        userRepository.updateUsername(userId, username);
    }

    @Override
    public void updatePassword(Long userId, String password) {
    }

    @Override
    public void updateEmail(Long userId, String email) {
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public boolean userExists(String email) {
        return userRepository.findByEmail(email) != null;
    }



    @Override
    @Transactional
    public User authenticateUser(String email, String password) {
        return null; //todo в самом конце сделать аутентификацию
    }
}
