package com.myorg;
import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.Aws;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.iam.*;
import software.amazon.awscdk.services.kendra.*;
import software.amazon.awscdk.services.s3.Bucket;
import java.util.*;

/**
 * Class to provision the Kendra resources:
 * - S3 Bucket to store files of the type: .pdf, .docx, .ppt, .html, .txt
 * - Kendra Index 
 * - S3 Data Source...
        NOTE: You can choose from the following Data Sources: 
        Amazon S3 buckets

        Confluence instances

        Google Workspace Drives

        Amazon RDS for MySQL and Amazon RDS for PostgreSQL databases

        Confluence cloud and Confluence server

        Custom data sources

        Microsoft OneDrive for Business

        Microsoft SharePoint

        Salesforce sites

        ServiceNow instances


    
 * - IAM Roles & Policies for Kendra to S3

 */

public class KendraEnv extends Stack {

    // Attributes for the Kendra resources 
    final private String bucketName;
    final private String indexRoleArn;
    final private String dataSourceRoleArn;
    final private String indexId;
    final private Construct scope;
    final private String AWS_ACCOUNT_ID;


    /**
     * Constructor to instantiated a Kendra class
     * @param scope Needed to write to the right CDK stack
     */
    public KendraEnv(Construct scope){

            this.scope = scope;
            this.AWS_ACCOUNT_ID = Aws.ACCOUNT_ID; // Get Acount ID from AWS CLI

            // Create an empty S3 bucket
            Bucket s3Bucket = Bucket
            .Builder
            .create(scope, "s3BucketForDataSource")
            .build();

            // Declare the bucketName field for the kendraEnv
            this.bucketName = s3Bucket.getBucketName();

            // Call class method to create the IAM Roles
            String[] tempRoleArr = this.iamRolesCreation();

            // Assign values to the Roles
            this.indexRoleArn = tempRoleArr[0];
            this.dataSourceRoleArn = tempRoleArr[1];

            // Create Kendra index 
            CfnIndex kendraIndex = CfnIndex.Builder.create(scope,"kendraIndexid")
                                .edition("ENTERPRISE_EDITION")
                                .roleArn(indexRoleArn)
                                .description("Kendra index for search tool")
                                .name(bucketName)
                                .build();

            // Get the Kendra Index
            this.indexId = kendraIndex.getAtt("Ref").toString();

            // Create properties for S3 Data Source
            CfnDataSource.S3DataSourceConfigurationProperty s3Prop = CfnDataSource
            .S3DataSourceConfigurationProperty
            .builder()
            .bucketName(bucketName)
            .build();

            // Properties to create for Data Source 
            CfnDataSource.DataSourceConfigurationProperty dsProp = CfnDataSource
            .DataSourceConfigurationProperty
            .builder()
            .s3Configuration(s3Prop)
            .build();

            // Create actual Data Source
            CfnDataSource ds = CfnDataSource.Builder.create(scope, "DataSource")
                             .indexId(indexId) // Index ID
                             .name(bucketName) // Name for the Data Source
                             .type("S3") // Data Source type (e.g. S3, Google Drive, Confluence, etc.)
                             .dataSourceConfiguration(dsProp)
                             .roleArn(dataSourceRoleArn)//Role created above
                             .description("S3 data source for search tool")
                             .schedule("cron(0/2 * * * ? *)") //Synchronize Data Source every 2 minutes
                             .build();    

    }

    /**
     * 
     * @return an array with the two Roles' ARNs
     */
    private String[] iamRolesCreation(){

        // Create Kendra Index Role
        Role kendraIndexRole = Role.Builder.create(scope,"kendraIndexRole")
        .assumedBy(new ServicePrincipal("kendra.amazonaws.com"))
        .build();

        //Need to add four policy statements to our kendraIndexPole
        // You can find more about the needed policies here: https://docs.aws.amazon.com/kendra/latest/dg/iam-roles.html
        PolicyStatement kendraIndexPolStat1 = PolicyStatement.Builder.create()
        .effect(Effect.ALLOW)
        .resources(Arrays.asList("*"))
        .actions(Arrays.asList("cloudwatch:PutMetricData"))
        .conditions(new HashMap<String, Object>() {{
            put("StringEquals", new HashMap<String, String>() {{
                put("cloudwatch:namespace", "AWS/Kendra");                 
            }});
        }})
        .build();

        PolicyStatement kendraIndexPolStat2 = PolicyStatement.Builder.create()
        .effect(Effect.ALLOW)
        .resources(Arrays.asList("*"))
        .actions(Arrays.asList("logs:DescribeLogGroups"))
        .build();

        String roleFormat1 = String.format("arn:aws:logs:us-east-1:%s:log-group:/aws/kendra/*",this.AWS_ACCOUNT_ID);

        PolicyStatement kendraIndexPolStat3 = PolicyStatement.Builder.create()
        .effect(Effect.ALLOW)
        .resources(Arrays.asList("*")) 
        .actions(Arrays.asList("logs:CreateLogGroup"))
        .build();

        String roleFormat2 = String.format("arn:aws:logs:us-east-1:%s:log-group:/aws/kendra/*:log-stream:*",this.AWS_ACCOUNT_ID);
        PolicyStatement kendraIndexPolStat4 = PolicyStatement.Builder.create()
        .effect(Effect.ALLOW)
        .resources(Arrays.asList("*")) 
        .actions(Arrays.asList("logs:DescribeLogStreams", "logs:CreateLogStream", "logs:PutLogEvents")) // maybe use * temporarily 
        .build();

        //Add the Policy statements to the Role
        kendraIndexRole.addToPolicy(kendraIndexPolStat1);
        kendraIndexRole.addToPolicy(kendraIndexPolStat2);
        kendraIndexRole.addToPolicy(kendraIndexPolStat3);
        kendraIndexRole.addToPolicy(kendraIndexPolStat4);


        // Kendra Data Source role and policy creation
        Role kendraDataSourceRole = Role.Builder.create(scope,"kendraDataSourceRole")
        .assumedBy(new ServicePrincipal("kendra.amazonaws.com"))
        .build();

        PolicyStatement kendraDataSourcePolStat1 = PolicyStatement.Builder.create()
        .effect(Effect.ALLOW)
        .resources(Arrays.asList(String.format("arn:aws:s3:::%s/*", bucketName)))
        .actions(Arrays.asList("s3:GetObject"))
        .build();

        PolicyStatement kendraDataSourcePolStat2 = PolicyStatement.Builder.create()
        .effect(Effect.ALLOW)
        .resources(Arrays.asList(String.format("arn:aws:s3:::%s", bucketName)))
        .actions(Arrays.asList("s3:ListBucket"))
        .build();

        String roleFormat3 = String.format("arn:aws:kendra:us-east-1:%s:index/*",this.AWS_ACCOUNT_ID);
        PolicyStatement kendraDataSourcePolStat3 = PolicyStatement.Builder.create()
        .effect(Effect.ALLOW)
        .resources(Arrays.asList("*")) 
        .actions(Arrays.asList("kendra:BatchPutDocument", "kendra:BatchDeleteDocument"))
        .build();

        //Add policies to the role
        kendraDataSourceRole.addToPolicy(kendraDataSourcePolStat1);
        kendraDataSourceRole.addToPolicy(kendraDataSourcePolStat2);
        kendraDataSourceRole.addToPolicy(kendraDataSourcePolStat3);

        String indexRole = kendraIndexRole.getRoleArn().toString();
        String dsRole = kendraDataSourceRole.getRoleArn().toString();

        return new String[] {indexRole, dsRole};
        
    }

    /**
     * 
     * @return Kendra index ID
     */
    public String getKendraIndexId(){
        return this.indexId;
    }



}