package com.sivalabs.devzone.posts.domain.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_id_generator")
    @SequenceGenerator(name = "post_id_generator", sequenceName = "post_id_seq", allocationSize = 100)
    private Long id;

    @Column(nullable = false)
    @NotEmpty()
    private String title;

    @Column(nullable = false)
    @NotEmpty()
    private String url;

    private String content;

    @Column(nullable = false)
    private Long createdBy;

    @Column(updatable = false)
    private Instant createdAt;

    @Column(insertable = false)
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = Instant.now();
    }
}
