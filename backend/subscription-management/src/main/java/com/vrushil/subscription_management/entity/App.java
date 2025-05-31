package com.vrushil.subscription_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "apps")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appId;

    @NotNull
    @Column(unique = true)
    private String appName;

    @NotNull
    private String logoUrl;

    @NotNull
    private String website;

    @OneToMany(mappedBy = "app", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("app")
    private List<Plan> plans;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties("apps")
    private Category category;
}
