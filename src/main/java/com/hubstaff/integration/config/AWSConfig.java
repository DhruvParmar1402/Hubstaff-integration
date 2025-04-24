package com.hubstaff.integration.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AWSConfig {

    @Value("${aws.ACCESS_KEY}")
    private String accessKey;

    @Value("${aws.SECRET_KEY}")
    private String secretKey;

    @Value("${aws.ENDPOINT}")
    private String endpoint;

    @Value("${aws.REGION}")
    private String region;


    @Bean
    public DynamoDBMapper buildMapper(){
        return new DynamoDBMapper(getCredentials());
    }

    @Bean
    public AmazonDynamoDB getCredentials() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

        return  AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(endpoint, region)
                )
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
}
