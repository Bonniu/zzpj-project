package app.hotel.dbservices;

import app.database.entities.User;
import app.database.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return this.userRepository.findById(id);
    }

    public void insertUser(@RequestBody User user) {
        this.userRepository.insert(user);
    }

    public void updateUser(@RequestBody User user) {
        this.userRepository.save(user);
    }

    public void deleteUser(@RequestBody User user) {
        this.userRepository.delete(user);
    }
}
