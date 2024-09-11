package com.soulware.backend.domain.file.entity;

import com.soulware.backend.domain.common.Timestamp;
import com.soulware.backend.domain.post.entity.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "files")
@Entity
public class File extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public File(String fileName, Post post) {
        this.fileName = fileName;
        this.post = post;
    }

}
