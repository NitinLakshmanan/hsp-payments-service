package com.bhs.payments.client;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.DecryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import java.util.Base64;

public class AWSSecretsClient {

  public static void main(String[] args) {
    getSecret();
  }
  private static void getSecret() {

    String secretName = "dev-analytics-redshift";
    String region = "ap-south-1";

    // Create a credentials provider with you IAM access key and secret Key in AwsCredentials.properties file
    AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();

    // Create a Secrets Manager client
    AWSSecretsManager client  = AWSSecretsManagerClientBuilder.standard()
        .withRegion(region)
        .withCredentials(credentialsProvider)
        .build();


    String secret, decodedBinarySecret;

    GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
        .withSecretId(secretName);
    GetSecretValueResult getSecretValueResult = null;

    try {
      getSecretValueResult = client.getSecretValue(getSecretValueRequest);
    } catch (DecryptionFailureException e) {
      // Secrets Manager can't decrypt the protected secret text using the provided KMS key.
      // Deal with the exception here, and/or rethrow at your discretion.
      throw e;
    } catch (InternalServiceErrorException e) {
      // An error occurred on the server side.
      // Deal with the exception here, and/or rethrow at your discretion.
      throw e;
    } catch (InvalidParameterException e) {
      // You provided an invalid value for a parameter.
      // Deal with the exception here, and/or rethrow at your discretion.
      throw e;
    } catch (InvalidRequestException e) {
      // You provided a parameter value that is not valid for the current state of the resource.
      // Deal with the exception here, and/or rethrow at your discretion.
      throw e;
    } catch (ResourceNotFoundException e) {
      // We can't find the resource that you asked for.
      // Deal with the exception here, and/or rethrow at your discretion.
      throw e;
    }

    // Decrypts secret using the associated KMS CMK.
    // Depending on whether the secret is a string or binary, one of these fields will be populated.
    if (getSecretValueResult.getSecretString() != null) {
      secret = getSecretValueResult.getSecretString();
    }
    else {
      decodedBinarySecret = new String(
          Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
    }

    // Your code goes here.
  }
}
