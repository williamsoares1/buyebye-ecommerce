package com.estudos.products.infra.s3.dtos;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;

@Builder
public record UploadObjectDTO(String id, MultipartFile file) {

}
