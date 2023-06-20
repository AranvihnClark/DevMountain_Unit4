package com.devmountain.noteApp.entities;

import com.devmountain.noteApp.dtos.UserDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (unique = true)
    private String username;

    @Column
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    // Prevents infinite recursion along with the 'JsonBackReference' it seems.
    @JsonManagedReference
    private Set<Note> noteSet = new HashSet<>();

    public User (UserDto userdto) {
        if (userdto.getUsername() != null) {
            this.username = userdto.getUsername();
        }
        if (userdto.getPassword() != null) {
            this.password = userdto.getPassword();
        }
    }
}
