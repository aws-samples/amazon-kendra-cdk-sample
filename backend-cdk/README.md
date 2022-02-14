# Welcome to your CDK Java project!

This is a blank project for Java development with CDK.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

It is a [Maven](https://maven.apache.org/) based project, so you can open this project with any Maven compatible Java IDE to build and run tests.

## Useful commands

 * `mvn package`     compile and run tests
 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation

## AWS Cloud Development Kit (CDK)

The [AWS Cloud Development Kit (AWS CDK)](https://aws.amazon.com/cdk/) is an open source software development framework to define cloud application resources using familiar programming languages.

This aws sample provides customers the ability to scale out the current CloudFormation template located in the /cft folder to attach various database resources and AWS services to customize the template as necessary for specified use cases. 

The AWS CDK is powerful because it allows customers to add and tune the template to use case specific services to scale the existing template that was created in CloudFormation.

In the provided Java repository you can locate the /backend-cdk/src/main/java/com/myorg folder to see that we have separated all of the resources in the provided CFT template in a modular fashion and have placed all of our modular constructs in the /KendraSearchToolStack.java file.
	- After locating the /backend-cdk/src/main/java/com/myorg folder, you will be able to see the 4 L2 constructs that we have created to create the provided cloudformation template that was created in the /cft folder.

With this provided repository, customers can now go ahead and add their own constructs to the template by creating new java files with additional AWS resources in the form of AWS CDK constructs and implement their changes to the KendraSearchToolStack.java file. After implementing new CDK constructs to the /src/main/java/com/myorg folder - developers can run the 'cdk synth' command to emit a newly synthesized and customized CloudFormation template!

Here are a few resources that developers can check out to learn how to use the AWS CDK to scale out the provided CloudFormation template located in the /cft folder:
   - [What is the AWS CDK?](https://docs.aws.amazon.com/cdk/v2/guide/home.html)  
   - [Getting Started Documentation](https://docs.aws.amazon.com/cdk/v2/guide/getting_started.html)     
   - [Concept of Constructs](https://docs.aws.amazon.com/cdk/v2/guide/constructs.html)  
   - [Class Construct: AWS Developer Documentation!](https://docs.aws.amazon.com/cdk/api/v1/docs/@aws-cdk_core.Construct.html)  


Enjoy!
