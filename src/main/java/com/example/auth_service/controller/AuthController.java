package com.example.auth_service.controller;

import com.example.auth_service.entity.User;
import com.example.auth_service.entity.Role;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "APIs for user authentication and token generation")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Get authenticated user details", description = "Authenticates the user via OAuth2 and returns user details along with a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user details and token",
                    content = @Content(mediaType="application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated",
                    content = @Content)
    })
    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String picture = principal.getAttribute("picture");

        Optional<User> userOpt = userRepository.findByEmail(email);
        User user;
        if (userOpt.isEmpty()) {
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setProfilePicture(picture);

            long userCount = userRepository.count();

            if (userCount == 0) {
                // Make the first user an admin
                user.setRole(Role.ADMIN);
                System.out.println("First user created as ADMIN: " + email);
            } else {
                // Default role for subsequent users
                user.setRole(Role.STAFF);
            }

            user.setDepartmentId("1"); // Default department ID as string now
            userRepository.save(user);
        } else {
            user = userOpt.get();
        }

        String token = jwtUtil.generateToken(email, user.getRole().name(), user.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getId());
        response.put("email", user.getEmail());
        response.put("name", user.getName());
        response.put("profilePicture", user.getProfilePicture());
        response.put("role", user.getRole().name());
        return response;
    }
}