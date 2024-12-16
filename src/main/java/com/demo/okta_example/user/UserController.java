package com.demo.okta_example.user;

import com.okta.sdk.client.Client;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserBuilder;
import com.okta.sdk.resource.user.UserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public Client client;

    @GetMapping("/list")
    public UserList getUsers() {
        return client.listUsers();
    }

    @GetMapping("/find")
    public UserList searchUserByEmail(@RequestParam String query) {
        //filter users using the firstName, lastName, or email as query parameters
        return client.listUsers(query, null, null, null, null);
    }

    @PostMapping("/create")
    public User createUser(@RequestBody UserRegisterDto userRegisterDto) {
        return UserBuilder.instance()
                .setEmail(userRegisterDto.email())
                .setFirstName(userRegisterDto.firstName())
                .setLastName(userRegisterDto.lastName())
                .setPassword(userRegisterDto.password().toCharArray())
                .setActive(userRegisterDto.active())
                .buildAndCreate(client);
    }

    @GetMapping("/random-user")
    public User generateRandomUser() {
        char[] tempPassword = {'1','6','$','a','w','7','d','8'};
        return UserBuilder.instance()
                .setEmail("eduardo.roque@email.com")
                .setFirstName("Eduardo")
                .setLastName("Roque")
                .setPassword(tempPassword)
                .setActive(true)
                .buildAndCreate(client);
    }

    @GetMapping("/{userId}/show")
    public User getUser(@PathVariable String userId) {
        return  client.getUser(userId);
    }

    @PutMapping("/{userId}/update")
    public User updateUser(@PathVariable String userId, @RequestBody UserUpdateDto userUpdateDto) throws Exception {
        // Obtén el usuario existente
        User user = client.getUser(userId);

        if (Objects.isNull(user)) {
            throw new Exception("User not found");
        }

        // Actualiza los atributos del usuario si no son nulos
        if (userUpdateDto.email() != null) {
            user.getProfile().setEmail(userUpdateDto.email());
            user.getProfile().setLogin(userUpdateDto.email());
        }
        if (userUpdateDto.firstName() != null) {
            user.getProfile().setFirstName(userUpdateDto.firstName());
        }
        if (userUpdateDto.lastName() != null) {
            user.getProfile().setLastName(userUpdateDto.lastName());
        }

        // Guarda los cambios
        user.update();

        return user;
    }

    @GetMapping("/{userId}/deactivate")
    public User deactivateUser(@PathVariable String userId) throws Exception {
        User user = client.getUser(userId);

        if (Objects.isNull(user)) {
            throw new Exception("User not found");
        }

        user.deactivate();
        return getUser(userId);
    }

    @GetMapping("/{userId}/delete")
    public User deleteUser(@PathVariable String userId) throws Exception {
        User user = deactivateUser(userId);
        user.delete();
        return user;
    }
}
