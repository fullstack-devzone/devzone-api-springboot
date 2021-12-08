package com.sivalabs.devzone.domain.services;

import static com.sivalabs.devzone.domain.utils.AppConstants.ROLE_USER;

import com.sivalabs.devzone.domain.entities.Role;
import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.exceptions.DevZoneException;
import com.sivalabs.devzone.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.domain.models.ChangePasswordRequest;
import com.sivalabs.devzone.domain.models.UpdateUserRequest;
import com.sivalabs.devzone.domain.models.UserDTO;
import com.sivalabs.devzone.domain.repositories.RoleRepository;
import com.sivalabs.devzone.domain.repositories.UserRepository;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private static final String PROFILE_IMAGE_EXTN = ".png";
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(UserDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDTO createUser(UserDTO user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DevZoneException("Email " + user.getEmail() + " is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userEntity = user.toEntity();
        Optional<Role> roleUser = roleRepository.findByName(ROLE_USER);
        userEntity.setRoles(Collections.singleton(roleUser.orElse(null)));
        return UserDTO.fromEntity(userRepository.save(userEntity));
    }

    public UserDTO updateUser(UpdateUserRequest updateUserRequest) {
        User userEntity =
                userRepository
                        .findById(updateUserRequest.getId())
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "User with id "
                                                        + updateUserRequest.getId()
                                                        + " not found"));
        userEntity.setName(updateUserRequest.getName());
        userEntity.setBio(updateUserRequest.getBio());
        userEntity.setLocation(updateUserRequest.getLocation());
        userEntity.setGithubUsername(updateUserRequest.getGithubUsername());
        userEntity.setTwitterUsername(updateUserRequest.getTwitterUsername());
        userEntity.setSkills(updateUserRequest.getSkills());
        return UserDTO.fromEntity(userRepository.save(userEntity));
    }

    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresent(userRepository::delete);
    }

    public void changePassword(String email, ChangePasswordRequest changePasswordRequest) {
        User user =
                this.getUserByEmail(email)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "User with email " + email + " not found"));
        if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new DevZoneException("Current password doesn't match");
        }
    }

    public String updateUserProfilePic(Long userId, InputStream inputStream) {
        String fileName = userId + PROFILE_IMAGE_EXTN;
        fileStorageService.storeFile(inputStream, fileName);
        String imageUrl = "/api/users/" + userId + "/profile_pic";
        userRepository.updateImageUrl(userId, imageUrl);
        log.info("File uploaded to: {}", fileName);
        return imageUrl;
    }

    public Resource getUserImageUrl(Long userId) {
        String imageUrl = userRepository.getImageUrl(userId);
        if (imageUrl == null) {
            return null;
        }
        return fileStorageService.loadFileAsResource(userId + PROFILE_IMAGE_EXTN);
    }
}
