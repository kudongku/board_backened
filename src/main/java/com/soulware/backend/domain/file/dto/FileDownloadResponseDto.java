package com.soulware.backend.domain.file.dto;

import org.springframework.core.io.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileDownloadResponseDto {

    private String fileName;
    private Resource resource;

}
