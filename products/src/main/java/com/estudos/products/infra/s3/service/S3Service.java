package com.estudos.products.infra.s3.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.estudos.products.infra.s3.dtos.UploadObjectDTO;

@Service
public interface S3Service {
    void uploadFile(UploadObjectDTO uploadObject) throws IOException;
    String getFileType(String filename);
}
