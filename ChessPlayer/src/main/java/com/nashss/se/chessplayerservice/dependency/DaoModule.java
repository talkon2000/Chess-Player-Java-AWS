package com.nashss.se.chessplayerservice.dependency;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class DaoModule {

    /**
     * Provides a dynamoDBMapper with a custom DynamoDBMapper config.
     * @return DynamoDBMapper
     */
    @Singleton
    @Provides
    public DynamoDBMapper provideDynamoDBMapper() {
        return new DynamoDBMapper(
                AmazonDynamoDBClientBuilder
                        .standard()
                        .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                        .withRegion(Regions.US_EAST_2)
                        .build(),
                DynamoDBMapperConfig.builder()
                        .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES)
                        .build());
    }
}
