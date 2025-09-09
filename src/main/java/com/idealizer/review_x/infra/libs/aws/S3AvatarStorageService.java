package com.idealizer.review_x.infra.libs.aws;

import com.idealizer.review_x.domain.core.user.services.AvatarStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Logger;

@Component
public class S3AvatarStorageService implements AvatarStorageService {
    private static Logger logger = Logger.getLogger(S3AvatarStorageService.class.getName());

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    public S3AvatarStorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }


    @Override
    public String uploadAvatar(String username, byte[] imageData, String filename, String contentType) {
        String key = String.format("avatars/%s/%s_%s", username, UUID.randomUUID(), URLEncoder.encode(filename, StandardCharsets.UTF_8));

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(imageData));

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key);
    }

    @Override
    public void deleteAvatar(String userId, String filename) {
        String key = String.format("avatars/%s/%s", userId, URLEncoder.encode(filename, StandardCharsets.UTF_8));

        s3Client.deleteObject(builder -> builder.bucket(bucket).key(key));

        logger.info(String.format("Deleted avatar %s", key));
    }
}
