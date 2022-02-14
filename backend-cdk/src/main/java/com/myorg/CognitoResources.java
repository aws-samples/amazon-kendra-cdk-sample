package com.myorg;
import java.util.Arrays;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.cognito.*;
import software.amazon.awscdk.services.cognito.CfnUserPool.*;

/**
 * Class to provision the Cognito reosurces for the Amplify App
 * - User Pool
 * - User Pool Client
 * - Identity Pool
 * 
 */

public class CognitoResources extends Stack {
    // Attributes that can be accessed from the class
    final private String userPoolId; 
    final private String userPoolClientId;
    final private String identityPoolId;


    /**
     * Constructor for Congnito resources
     * @param scope
     */
    public CognitoResources(Construct scope){
        
        // Create a User Pool 
        CfnUserPool userPool = CfnUserPool.Builder.create(scope, "cognitoUserPool")
        .adminCreateUserConfig(
           AdminCreateUserConfigProperty.builder()
           .allowAdminCreateUserOnly(false)
           .build()
       )
        .autoVerifiedAttributes(Arrays.asList("email"))
        .build();
        
        this.userPoolId = userPool.getAtt("Ref").toString(); //Get the User Pool ID


        // Create a User Pool Client 
        CfnUserPoolClient userPoolClient = CfnUserPoolClient.Builder.create(scope,"cognitoUserPoolClient")
        .userPoolId(userPoolId)
        .build();

        this.userPoolClientId = userPoolClient.getAtt("Ref").toString(); //Get the User Pool Client ID

        // Create an Identity Pool 
        CfnIdentityPool identityPool = CfnIdentityPool.Builder.create(scope,"cognitoIdentityPool")
        .allowUnauthenticatedIdentities(false)
        .build();

        this.identityPoolId = identityPool.getAtt("Ref").toString(); //Get the Identity Pool ID 
    }

    /**
     * 
     * @return User Pool ID to be used in the Amplify App
     */
    public String getUserPoolId(){
        return this.userPoolId;
    }
    /**
     * 
     * @return User Pool Client ID to be used in the Amplify App
     */
    public String getUserPoolClientId(){
        return this.userPoolClientId;
    }
    /**
     * 
     * @return Identity Pool ID to be used in the Amplify App
     */
    public String getIdentityPoolId(){
        return this.identityPoolId;
    }


}