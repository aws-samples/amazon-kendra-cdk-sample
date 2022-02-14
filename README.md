# Amazon-Kendra-CDK-Sample

## Customizable Kendra Search App CloudFormation Template (amazon-kendra-cdk-sample)

This solution is primarily for developers looking to have a customizable search application that indexes various data sources utilizing Kendra’s natural-language processing (NLP) algorithm by providing a Java repo that can add native AWS services with the AWS Cloud-Development Kit (AWS CDK) to an Amplify hosted react application.

 We will walk developers through how they can utilize [Amazon Kendra](https://aws.amazon.com/kendra/) and it’s pre-built natural-language processing algorithm to solve for search problems by using the provided CloudFormation template and Java repository. Customers looking to accelerate a Kendra Proof-of-Concept (PoC) now have a readily available template that they can use and scale. With the provided Java repository, developers that utilize the [AWS Cloud Development Kit (CDK)](https://aws.amazon.com/cdk/) can go ahead and add tailored data sources specific to their use case in addition to AWS services such as ([Amazon Lex](https://aws.amazon.com/lex/), [Amazon Textract](https://aws.amazon.com/textract/), [Amazon Transcribe](https://aws.amazon.com/transcribe/), etc.) to augment the provided CloudFormation template.

By utilizing the AWS CDK, developers are given the ability to add integration with Amazon RDS for MySQL and Amazon RDS for PostgreSQL databases, Textract, Transcribe, Lex and many other AWS services with a few lines of code. In addition to the repository for this Natural Language Processing (NLP) search application, a JSON CloudFormation template is also provided to allow the developers to spin up an Amazon S3 bucket, Cognito User and Identity pools, and a CodeCommit repository for the developer to commit the [react-client](https://github.com/aws-samples/amazon-kendra-cdk-sample/reactRepo) repo to host an search application optimized using NLP and hosted on AWS Amplify.

This search application is used to provide a real-time recommendation engine for developers looking to solve search problems that can be solved with NLP.

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

## Prerequisites

In order to follow this walk-through, you must have the [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html) installed and configured to allow you to add your own aws credentials that will be changed in the ‘local-dev-credentials.json’ file in the /services folder of the react-client repository.

* In the same ‘local-dev-credentials.json’ file - you will see an indexId that you will also need to manually input. This can be found in the ‘Index settings’ section in the Kendra Console.
* AWS CDK installation
* pip3
* node.js

NOTE: When looking to scale this search tool to production, it is not best-practices to keep your credential files in your repository. 

## CloudFormation Template

The cft directory contains the CloudFormation template used to deploy the resources needed to create a web hosted search application. After deploying the template in your AWS Console, you will have these resources provisioned in your AWS account:

* KendraSearchToolStack.template.json: This json file will deploy all the necessary resources to host your React.JS search application via Amplify

Once you have the resources deployed, it will provide:

* An Amazon S3 bucket for the search application to act as the data source for the Kendra index
    * Users will manually upload FAQs, PDFs, PPTs, etc. to the S3 bucket. This will allow Kendra’s NLP built-in algorithm to index the objects in the bucket when the user types in a specific domain in the search bar.
* An Amazon Kendra index
    * This index is automatically hooked up to the S3 bucket to reduce the effort of manual data source integration.
* An Amazon CodeCommit repo connected to Amplify
    * This repo is created so that when the developer commits the code to the repo it will automatically start building out the search application to be hosted on an Amplify hosted domain.
* Cognito User / Identity pool
    * This will act as the form of authentication to allow users to have the sign-up and sign-in functionality.
* NOTE: If you want to host the app on Amplify, you would have connect your repo manually to the Amplify app and provide the appropriate IAM privileges. The CloudFormation stack will not do this automatically. Make sure to run "npm update" before triggering the build.

## Repo Connection

* Step #1: Prerequisites:

We recommend the usage of the latest versions of Git and other prerequisite software. More information about minimum required versions of prerequisite software can be found here: Prerequisite software (https://docs.aws.amazon.com/console/codecommit/connect-tc-alert-np)

1. Install Python. To download and install the latest version of Python. View the Python website (https://www.python.org/)
2. Install and configure a Git client. View Git downloads page (http://git-scm.com/downloads)
3. Make sure the AWS cli user has the right permissions to the CodeCommit repository.

    * Learn how to create and configure an IAM user for accessing AWS CodeCommit (https://docs.aws.amazon.com/console/codecommit/connect-cli-user)
    * Learn how to configure connections for users with rotating credentials (https://docs.aws.amazon.com/console/codecommit/connect-temporary-np)
    * Learn how to add team members to an AWS CodeStar Project (https://docs.aws.amazon.com/console/codecommit/connect-codestar-team-np)

* Step #2: Install git-remote-codecommit ```pip3 install git-remote-codecommit```

* Step #3: Clone the CodeCommit repo ```git clone codecommit::us-east-1://reactRepo```

* Step #4: Change the placeholder credentials:

    1. Edit the ```'.env.local'``` file with the right credentials.
    2. Edit the ```"config"``` variable in the ```'/reactRepo/src/services/Kendra.ts'``` file on line 9 with the right credentials and the KendraIndexId (this can found in the CloudFormation console under the “outputs” tab of the stack).
    3. Edit the ```"IdentityPoolId"``` variable in the ```'/reactRepo/src/services/Kendra.ts'``` file on line 57 with the right credentials


Congratulations! You are now in possession of a intelligent search tool using AWS Kendra deployed via AWS CDK (Java) repository. Feel free to start adding your own data sources to S3 and test it out!

## Web-Crawler Search (Bonus Feature)

Also, as of today, there’s not a CDK library to add the Web Crawler programmatically as a data source; this step will have to be done from the Kendra console.

*Step #1:* Go to the Kendra console https://console.aws.amazon.com/kendra/home?region=us-east-1#welcome
*Step #2:* Add a new data source - WebCrawler. Click “Add Connector”
*Step #3:* Select the Web Crawler source URL(s) - this would be the starting point of the indexing
*Step #4:* Create a new IAM role with the role name ‘AmazonKendra-Web-Crawler-Role-XXX’. Proceed and select the number of layers for the Web Crawler to index (NOTE: the more layers selected, the longer the indexing step will take)

*Step #5*: The Web Crawler is now ready for use. Feel free to test out it out in the search bar.

## Contribute and scale 

Fork the AWS-CDK-Kendra-Web-Crawler-Search GitHub repository, please feel free to play around with the code by adding new services. From there, you can send pull requests to the team and we can merge new branches of architectures to make each new feature publicly available. Listed below are future services we plan on integrating to help anyone wanting to utilize the AWS CDK to scale out a natural-language processing search application.

* Add a chatbot functionality with Amazon Lex that indexes by integrating a QnABot with your Kendra index to allow users to search FAQs stored in the S3 bucket via chatbots on web UIs / Slack / etc. 
* Enhance your security posture by placing all your resources in an AWS VPC to ensure you are allowing access to the right people in your organization and restricting access to bad actors. This is AWS best-practices when considering to fully productionizing this search search tool. 
* Utilize the Kendra filtering functionality, relevance tuning, and add additional data source to increase the featurability of search tool.
* Enhance the search functionality of the tool by utilizing the power of Amazon AI services (https://aws.amazon.com/machine-learning/ai-services/)such as Transcribe, Comprehend, Polly or Textract to improve the search functionality and scale to use-case specific needs.

The team is continuously looking for ways to improve this search tool. If you have any requests or ideas - please do not hesitate to reach out to our team:

* Marco Punio, puniomp@amazon.com (Solutions Architect, Digital Native Business & Machine Learning)

* Div Shekhar, division@amazon.com (Solutions Architect, AWS Partner Org Tech Validations)

* Moe Alhassan, maalha@amazon.com (Manager, Partner Solutions Architecture)


