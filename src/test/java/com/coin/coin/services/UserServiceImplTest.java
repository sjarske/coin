package com.coin.coin.services;

import com.coin.coin.models.Exchange;
import com.coin.coin.models.User;
import com.coin.coin.repos.RoleRepo;
import com.coin.coin.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private RoleRepo roleRepo;
    private UserServiceImpl underTest;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(userRepo,roleRepo,passwordEncoder);
    }

    @Test
    void saveUser() {
        User user = new User(null,"John","John","12345",null,null);
        underTest.saveUser(user);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    void addExchangeToUser() {
        List<Exchange> exchanges = new ArrayList<>();

        User user = new User(null,"John","John","12345",null,exchanges);
        underTest.saveUser(user);
        underTest.addExchangeToUser("John","Binance","apikeyhere","secretkeyhere");
        assertThat(user.getExchanges().stream().count()==1);
    }

    @Test
    void getUser() {
        User user = new User(null,"John","John","12345",null,null);
        underTest.saveUser(user);
        underTest.getUser(user.getUsername());
        verify(userRepo).findByUsername("John");
    }

    @Test
    void getUsers() {
        underTest.getUsers();
        verify(userRepo).findAll();
    }
}