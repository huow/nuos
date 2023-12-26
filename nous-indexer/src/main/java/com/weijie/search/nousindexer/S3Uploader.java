package com.weijie.search.nousindexer;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class S3Uploader {

    private final S3Client s3Client;
    private final String bucketName;

    public S3Uploader() {
        s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        bucketName = "nous-dummy-note-index-bucket";
    }

    public void uploadIndex(String indexDirPath) {

        if (!isBucketExist(bucketName)) {
            createBucket(bucketName);
        }

        File indexDir = new File(indexDirPath);
        File[] indexFiles = indexDir.listFiles();

        if (indexFiles != null) {
            System.out.println("Start Uploading Index..");
            Long before = System.currentTimeMillis();
            // Delete Index files that exist in S3 but not in the local directory
            syncDeleteFromS3(indexFiles);
            // Upload Index files
            uploadIndexToS3(indexFiles);
            Long after = System.currentTimeMillis();
            System.out.println("Finish Uploading Index in " + (after - before) / 1000 + " seconds");
        }
    }

    private void syncDeleteFromS3(File[] indexFiles) {
        Set<String> s3Files = listS3BucketContents();
        for (File file : indexFiles) {
            s3Files.remove(file.getName());
        }
        for (String fileToSyncDelete : s3Files) {
            try {
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileToSyncDelete)
                        .build();

                s3Client.deleteObject(deleteObjectRequest);
                System.out.println("Deleted " + fileToSyncDelete + " from S3 bucket.");
            } catch (SdkException e) {
                System.err.println("Error deleting " + fileToSyncDelete + ": " + e.getMessage());
            }
        }
    }

    private Set<String> listS3BucketContents() {
        Set<String> fileNames = new HashSet<>();
        try {
            ListObjectsRequest listObjects = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .build();
            ListObjectsResponse res = s3Client.listObjects(listObjects);
            List<S3Object> objects = res.contents();

            for (S3Object obj : objects) {
                fileNames.add(obj.key());
            }
        } catch (SdkException e) {
            System.err.println(e.getMessage());
        }
        return fileNames;
    }

    private void uploadIndexToS3(File[] indexFiles) {
        // TODO: Upload speed of AWS S3 is 80 mb per second. If Index is too large, handle uploading in parallel
        if (indexFiles != null) {
            for (File file : indexFiles) {
                try {
                    PutObjectRequest request = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(file.getName())
                            .build();
                    s3Client.putObject(request, RequestBody.fromFile(file));
                } catch (S3Exception e) {
                    System.err.println(e.awsErrorDetails().errorMessage());
                }
            }
        } else {
            System.out.println("The directory path specified does not denote a directory or an I/O error occurred.");
        }
    }

    private boolean isBucketExist(String bucketName) {
        try {
            s3Client.headBucket(HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build());
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        } catch (SdkException e) {
            System.err.println("Error while checking if bucket exists: " + e.getMessage());
            return false;
        }
    }

    private void createBucket(String bucketName) {
        try {
            s3Client.createBucket(CreateBucketRequest
                    .builder()
                    .bucket(bucketName)
                    .build());
            System.out.println("Bucket created: " + bucketName);
        } catch (S3Exception e) {
            System.err.println("Error during bucket creation: " + e.awsErrorDetails().errorMessage());
        }
    }
}
