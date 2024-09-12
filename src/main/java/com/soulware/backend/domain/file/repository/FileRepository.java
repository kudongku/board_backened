package com.soulware.backend.domain.file.repository;

import com.soulware.backend.domain.file.entity.File;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByPostId(Long postId);

}
