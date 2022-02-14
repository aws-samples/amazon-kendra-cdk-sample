package com.myorg;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.core.*;

/**
 * Class to define the CDK stack with the required resources.
 * 
 * 
 */
public class KendraSearchToolStack extends Stack {
    public KendraSearchToolStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public KendraSearchToolStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // The code that defines your stack goes here


         // Create Kendra resources 
         KendraEnv kendraResources = new KendraEnv(this);

         // Create Cognito resources
         CognitoResources cognitoResources = new CognitoResources(this);

         // Create Amplify Resources
         AmplifyEnv amplifyEnv = new AmplifyEnv(this,
         cognitoResources.getUserPoolId(),
         cognitoResources.getUserPoolClientId(),
         cognitoResources.getIdentityPoolId());

         // Output Kendra Index ID into CloudFormation Console
        CfnOutput.Builder.create(this, "KendraIndexId")
         .value(kendraResources.getKendraIndexId())
         .build();

        // Output Identity Pool ID into CloudFormation Console
         CfnOutput.Builder.create(this, "IdentityPoolId")
         .value(cognitoResources.getIdentityPoolId())
         .build();



         

    }
}
