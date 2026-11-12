package com.event.event_manager.Service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.event.event_manager.Entity.Users;
import com.event.event_manager.Form.RegisterForm;
import com.event.event_manager.Repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor 
public class UsersService {

    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Optional <Users> serchUserByEmail(String email){
        
        return  repository.findByEmail(email);
    }

    public void RegisterUser(RegisterForm form){

        Users users = new Users();

        if(repository.existsByEmail(form.getEmail())){

            throw new IllegalArgumentException("このメールアドレスは既に登録されています");
        }

        users.setUsername(form.getUsername());

        users.setEmail(form.getEmail());

        users.setPasswordHash(passwordEncoder.encode(form.getPassword()));

        repository.save(users);
    }

    
}
