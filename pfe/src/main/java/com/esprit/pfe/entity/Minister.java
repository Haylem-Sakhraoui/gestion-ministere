package com.esprit.pfe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Minister implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private nameMinister name;
    @OneToOne
    @JsonIgnore
    private  Admin admin;

    @OneToMany(mappedBy = "minister",cascade = CascadeType.ALL)
    private Set<Employee>employees;

  }
