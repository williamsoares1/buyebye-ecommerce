package com.estudos.products.infra.s3.service.impl;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.estudos.products.infra.s3.dtos.UploadObjectDTO;
import com.estudos.products.infra.s3.service.S3Service;

@Service
public class S3ServiceImpl implements S3Service{

    private final AmazonS3 amazonS3Client;

    @Value("${aws.bucket.products.name}")
    private String bucketName;

    public S3ServiceImpl(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public void uploadFile(UploadObjectDTO uploadObject) throws IOException {
        InputStream inputStream = uploadObject.file().getInputStream();
        ObjectMetadata metadata = new ObjectMetadata();
        String fileName = "id" + uploadObject.id() + getFileType(uploadObject.file().getOriginalFilename());
        
        metadata.setContentLength(inputStream.available());
        metadata.setContentType(uploadObject.file().getContentType());

        amazonS3Client.putObject(
                bucketName,
                fileName,
                inputStream, metadata);
    }

    @Override
    public String getFileType(String filename) {
        if (filename != null) {
            String[] parts = filename.split("\\.");
            if (parts.length > 1) {
                return "." + parts[parts.length - 1];
            }
        }
        return "";
    }
}
