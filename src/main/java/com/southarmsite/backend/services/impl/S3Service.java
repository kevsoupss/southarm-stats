package com.southarmsite.backend.services.impl;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import java.time.Duration;

@Service
public class S3Service {
    private final String bucketName;
    private final S3Presigner presigner;

    public S3Service() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        this.bucketName = getenvOrDotenv("BUCKET_NAME", dotenv);
        String regionStr = getenvOrDotenv("AWS_REGION", dotenv);
        String accessKey = getenvOrDotenv("AWS_ACCESS_KEY_ID", dotenv);
        String secretKey = getenvOrDotenv("AWS_SECRET_ACCESS_KEY", dotenv);

        Region region = Region.of(regionStr);

        // Create credentials
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        this.presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    private String getenvOrDotenv(String key, Dotenv dotenv) {
        String val = System.getenv(key);
        if (val != null) {
            return val;
        }
        return dotenv.get(key);
    }

    public String generatePresignedUrl(String s3Key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofMinutes(60))
                .build();

        return presigner.presignGetObject(presignRequest).url().toString();
    }

    public void close() {
        presigner.close();
    }
}