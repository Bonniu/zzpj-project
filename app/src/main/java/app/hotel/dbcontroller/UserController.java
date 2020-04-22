package app.hotel.dbcontroller;

import app.database.entities.User;
import app.database.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public Optional<User> getUserById(@PathVariable("id") String id) {
        return this.userRepository.findById(id);
    }

    @PostMapping
    public void insertUser(@RequestBody User user) {
        this.userRepository.insert(user);
    }

    @PutMapping
    public void updateUser(@RequestBody User user) {
        this.userRepository.save(user);
    }

    @DeleteMapping
    public void deleteUser(@RequestBody User user) {
        this.userRepository.delete(user);
    }
}
