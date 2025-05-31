package com.vrushil.subscription_management.service;

import com.vrushil.subscription_management.entity.User;
import com.vrushil.subscription_management.exception.UserNotFoundException;
import com.vrushil.subscription_management.repository.UserRepository;
import com.vrushil.subscription_management.request.ResetPasswordRequest;
import com.vrushil.subscription_management.request.UserRegisterRequest;
import com.vrushil.subscription_management.response.ApiResponse;
import com.vrushil.subscription_management.response.UserResponse;
import com.vrushil.subscription_management.util.Mapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public ApiResponse<UserResponse> getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + id));
        return new ApiResponse<>("SUCCESS", "User retrieved successfully", Mapper.toUserResponse(user,"EMAIL_PASSWORD",null));
    }

    @Override
    public ApiResponse<Page<UserResponse>> getAllUsers(String search,Pageable pageable) {
        Page<User> users;

        if (search != null && !search.isBlank()){
            users = userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        Page<UserResponse> response = users
                .map(user -> Mapper.toUserResponse(user, "EMAIL_PASSWORD", null));
        return new ApiResponse<>("SUCCESS", "All users retrieved successfully", response);
    }

    @Override
    @Transactional
    public ApiResponse<UserResponse> updateUser(Long id, UserRegisterRequest updateUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + id));

        if (updateUser.getName() != null) {
            existingUser.setName(updateUser.getName());
        }
        if (updateUser.getEmail() != null && !updateUser.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(updateUser.getEmail())) {
                throw new IllegalArgumentException("Email already in use");
            }
            existingUser.setEmail(updateUser.getEmail());
        }
        if (updateUser.getPhone() != null) {
            existingUser.setPhone(updateUser.getPhone());
        }
        if (updateUser.getPassword() != null && !updateUser.getPassword().isBlank()) {
            existingUser.setPassword(bCryptPasswordEncoder.encode(updateUser.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        return new ApiResponse<>("SUCCESS", "User updated successfully", Mapper.toUserResponse(updatedUser, "EMAIL_PASSWORD", null));
    }

    @Override
    public ApiResponse<Void> deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with Id: " + id);
        }
        userRepository.deleteById(id);
        return new ApiResponse<>("SUCCESS", "User deleted successfully", null);
    }

    @Override
    public ApiResponse<Void> sendResetLink(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return new ApiResponse<>("ERROR", "User not found with email: " + email, null);
        }

        User user = optionalUser.get();
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.save(user);

        String resetLink = "http://localhost:4200/reset-password?token="+token;
        sendEmail(user.getEmail(), "Click the link: "+resetLink);

        return new ApiResponse<>("SUCCESS", "Password reset link sent successfully", null);
    }

    @Override
    @Transactional
    public ApiResponse<Void> resetPassword(ResetPasswordRequest request){
        Optional<User> optionalUser = userRepository.findByResetToken(request.getToken());

        if (optionalUser.isEmpty()) {
            return new ApiResponse<>("ERROR", "Invalid or expired token", null);
        }

        User user = optionalUser.get();
        user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        userRepository.save(user);

        return new ApiResponse<>("SUCCESS", "Password reset successfully", null);
    }

    private void sendEmail(String to, String content){
        SimpleMailMessage message =new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset Your Password");
        message.setText(content);
        javaMailSender.send(message);
    }
}
