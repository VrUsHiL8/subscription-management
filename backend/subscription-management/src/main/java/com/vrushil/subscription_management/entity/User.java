package com.vrushil.subscription_management.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users",
        indexes = {
                @Index(name = "idx_email", columnList = "email"),
                @Index(name = "idx_phone", columnList = "phone"),
                @Index(name = "idx_provider_id", columnList = "providerId")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String providerId; // OAuth provider ID (e.g., Google, Facebook)

    @NotNull
    private String name;

    @NotNull
    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    private String email;

    private String profilePicture; // Store provider profile image URL (optional)

    private String password;

    @Column(nullable = false)
    private Boolean isOAuthUser = false; // ✅ Ensure it's never null


    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits.")
    @Column(unique = true, nullable = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String provider; // Store provider name (e.g., GOOGLE, FACEBOOK)

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @Column(unique = true)
    private String resetToken;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JsonManagedReference("user-subscriptions")
    private List<Subscription> subscriptions;

    @PrePersist
    protected void prePersist() {
        if (this.role == null) {
            this.role = Role.USER;
        }
        if (this.name == null || this.name.isEmpty()) {
            this.name = "Google User"; // Fallback name
        }
        if (this.provider != null) {
            this.provider = this.provider.toUpperCase();
        }
        if (this.isOAuthUser == null) {
            this.isOAuthUser = false; // ✅ Ensure it's never null
        }
        if (this.emailVerified == null) {
            this.emailVerified = false; // ✅ Ensure it's never null
        }
    }
}
