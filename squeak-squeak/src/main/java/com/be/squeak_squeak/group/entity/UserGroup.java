package com.be.squeak_squeak.group.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_group")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String image;

    private String description;

    private int totalMemberCount;

    private String inviteCode;

    public void updateGroup(String name, String image, String description){
        this.name = name;
        this.image = image;
        this.description = description;
    }
}
