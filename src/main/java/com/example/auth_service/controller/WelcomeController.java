package com.example.auth_service.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String welcome() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Leave Management System - Auth Service</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            line-height: 1.6;
                            max-width: 800px;
                            margin: 0 auto;
                            padding: 20px;
                            color: #333;
                        }
                        h1 {
                            color: #2c3e50;
                            border-bottom: 2px solid #3498db;
                            padding-bottom: 10px;
                        }
                        h2 {
                            color: #2980b9;
                        }
                        ul {
                            list-style-type: square;
                        }
                        .btn {
                            display: inline-block;
                            background: #3498db;
                            color: white;
                            padding: 10px 20px;
                            margin: 20px 0;
                            text-decoration: none;
                            border-radius: 5px;
                            font-weight: bold;
                        }
                        .btn:hover {
                            background: #2980b9;
                        }
                        .endpoints {
                            background: #f8f9fa;
                            padding: 15px;
                            border-radius: 5px;
                            border-left: 4px solid #3498db;
                        }
                    </style>
                </head>
                <body>
                    <h1>Welcome to the Leave Management System - Authentication Service</h1>
                    <p>This is the authentication microservice for the Leave Management System. This service handles user authentication and authorization using Google OAuth.</p>
                    
                    <h2>API Documentation</h2>
                    <p>For detailed API documentation, please visit:</p>
                    <a href="/swagger-ui/index.html" class="btn">Swagger UI</a>
                    
                    <h2>Endpoints</h2>
                    <div class="endpoints">
                        <ul>
                            <li><strong>/oauth2/authorization/google</strong> - Sign in with Google</li>
                            <li><strong>/auth/user</strong> - Get authenticated user details and JWT token</li>
                        </ul>
                    </div>
                    
                    <h2>Authentication</h2>
                    <p>This service uses Google OAuth2 for authentication. Users can sign in with their Google accounts.</p>
                    <a href="/oauth2/authorization/google" class="btn">Sign in with Google</a>
                </body>
                </html>
                """;
    }
}