package com.electronic.store.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category extends BaseEntity{
    @Id
    private String categoryId;

    @Column(name = "category_title",length = 60,nullable = false)
    private String title;

    @Column(name = "category_desc",length = 50)
    private String description;

    @Column(name = "cover_image")
    private String coverImage;


}
