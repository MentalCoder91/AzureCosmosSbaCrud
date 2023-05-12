package com.cosmos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cosmos.model.User;
import com.cosmos.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<User> save(User user) {
    	log.info("User::=>{}",user);
        return this.userRepository.save(user);
    }

    @Override
    public Mono<User> delete(String id) {
        return this.userRepository
                .findById(id)
                .flatMap(p -> this.userRepository.deleteById(p.getId())
                                    .thenReturn(p))
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }

    @Override
    public Mono<User> update(String id, User user) {

        return this.userRepository.findById(id)
            .flatMap(u -> {
                   u.setId(id);
                   u.setAddress(user.getAddress());
                   u.setFirstName(user.getFirstName());
                   u.setLastName(user.getLastName());
                   return save(u);
                     }).switchIfEmpty(Mono.empty());
    }

    @Override
    public Flux<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        return this.userRepository.findById(id);
    }
}