package com.devmountain.noteApp.services;

import com.devmountain.noteApp.dtos.UserDto;
import com.devmountain.noteApp.entities.User;
import com.devmountain.noteApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * This adds a user to the database.
     * Normally, we would check for a duplicate using email address and other identifying parameters but this is just for practice.
     *
     * @param userDto the user's inputted data to create a User object.
     * @return a list string that lets the user know a user was added
     */
    @Override
    @Transactional
    public List<String> addUser(UserDto userDto) {
        // Create a new array list.
        List<String> response = new ArrayList<>();

        // We are creating a new user based on the user input (using userDto to transfer data).
        User user = new User(userDto);

        // This saves the user and 'flushes' them directly into our database right away.
        // The 'save' method will save the user in memory and won't 'flush' until you dictate it or the JPA will handle it when it gets to it.
        userRepository.saveAndFlush(user);

        // Then we add a string to our list to let the user know they were successful.
        // Now that we are adding the front end, I see we need the response to be in the form of an array list for its easy format and because we may want to display more info later.
        // In JS, this 'add' of ours will be [0] in our array.
        response.add("http://localhost:8080/login.html");

        return response;
    }

    /**
     * If user login is correct, pushes the user into the homepage and tracks user action via a cookie.
     *
     * @param userDto the inputted user info to check if they exist in our database.
     * @return a string based on whether the userDto matches a user in our database.
     */
    @Override
    public List<String> userLogin(UserDto userDto) {
        // Create a new array list.
        List<String> response = new ArrayList<>();

        // Used to find a username in our database without grabbing a null value that would break the app.
        Optional<User> userOptional = userRepository.findByUsername(userDto.getUsername());

        // Checking to see if the username entered exists in our database.
        if (userOptional.isPresent()) {

            // Checking to see if the password entered matches the username's password in our database.
            // If yes:
            if (passwordEncoder.matches(userDto.getPassword(), userOptional.get().getPassword())) {
                // This will be [0] of our JS code when we reference it later.
                response.add("http://localhost:8080/home.html");

                // Adds the username's string value into our array list.
                // This will be [1]of our JS code when we reference it later.
                response.add(String.valueOf(userOptional.get().getId()));
            } else {
                response.add("Username or password is incorrect.");
            }
        } else {
            response.add("Username or password is incorrect.");
        }

        return response;
    }
}
