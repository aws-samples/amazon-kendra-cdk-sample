package com.myorg;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.amplify.*;
import software.amazon.awscdk.services.amplify.CfnApp.*;
import software.amazon.awscdk.services.codecommit.*;
import java.util.*;


/*

Class helps create the resources for the Amplify instance.
- CodeCommit Repo
- Amplify App
- Envrionment Variables for Amplify App

l
*/

public class AmplifyEnv extends Stack {

    final private String userPoolId; //The user pool id needed to connect the 
    final private String userPoolClientId; // The user pool client id; also needed for Amplify-Cognito hook up
    final private String identityPoolId; // Identity Pool ID passed into the env variable
    final private Construct scope; //Scope needed to only have one stack

    // Envrionment variables for Amplify isntance; have to double check is this is the best way to connect Cognito to Amplify
    final private CfnApp.EnvironmentVariableProperty identityPoolIdEnvVar; 
    final private CfnApp.EnvironmentVariableProperty userPoolIdEnvVar;
    final private CfnApp.EnvironmentVariableProperty userPoolClientIdEnvVar;
    final private CfnApp.EnvironmentVariableProperty regionEnvVar;


  /**
   * Constructor to provision the Amplify App resources
   * @param scope This tells the CDK which stack to write to 
   * @param userPoolId Needed to connect Amplify to Cognito 
   * @param userPoolClientId Needed to connect to Cognito
   * @param identityPoolId Needed to connect to Cognito
   */
    public AmplifyEnv(Construct scope, String userPoolId, String userPoolClientId, String identityPoolId){

        //Assign values to the attributes that were passed in from the constructor 
        this.userPoolId = userPoolId; 
        this.userPoolClientId = userPoolClientId; 
        this.identityPoolId = identityPoolId;
        this.scope = scope;

        // Create repo to host React front-end application
        CfnRepository reactRepo = CfnRepository.Builder.create(scope, "repoforreactapp")
        .repositoryName("reactRepo")
        .build();

        // HTTPS codecommit repo connection
        String repoConnection = reactRepo.getAtt("CloneUrlHttp").toString();

        // Env variables for Amplify App
        this.identityPoolIdEnvVar = CfnApp.EnvironmentVariableProperty.builder()
        .name("IDENTITY_POOL_ID")
        .value(identityPoolId)
        .build(); 

        this.userPoolIdEnvVar = CfnApp.EnvironmentVariableProperty.builder()
        .name("USER_POOL_ID")
        .value(userPoolId)
        .build(); 

        this.userPoolClientIdEnvVar = CfnApp.EnvironmentVariableProperty.builder()
        .name("USER_POOL_CLIENT_ID")
        .value(userPoolClientId)
        .build();

        this.regionEnvVar = CfnApp.EnvironmentVariableProperty.builder()
        .name("REGION")
        .value("us-east-1")
        .build();

        // Create the Amplify App resources with the CodeCommit repo already connected to it
        CfnApp reactApp = CfnApp.Builder.create(scope, "amplifyReactApp")
        .name("reactAppForKendra")
        .repository(repoConnection)
        .environmentVariables(Arrays.asList(identityPoolIdEnvVar,userPoolIdEnvVar,userPoolClientIdEnvVar, regionEnvVar))
        .build();

    }
}