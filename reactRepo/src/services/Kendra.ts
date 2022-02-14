import Kendra from "aws-sdk/clients/kendra";
import S3 from "aws-sdk/clients/s3";
import { AWSCloudWatchProvider } from "aws-amplify";
import AWS from "aws-sdk";

const _loadingErrors = [];


let config = JSON.parse(`{
  "accessKeyId": "",
  "secretAccessKey": "",
  "region": "",
  "indexId": ""
}`);

if (config) {
  if (!config.accessKeyId) {
    _loadingErrors.push(
      `accessKeyId missing.`
    );
  }
  if (!config.secretAccessKey) {
    _loadingErrors.push(
      `secretAccessKey missing.`
    );
  }
  if (!config.region) {
    _loadingErrors.push(
      `region missing`
    );
  }
  if (!config.indexId || config.indexId.length === 0) {
    _loadingErrors.push(
      `indexId missing.`
    );
  }
}

const hasErrors = _loadingErrors.length > 0;
if (hasErrors) {
  console.error(JSON.stringify(_loadingErrors));
}

export const errors = _loadingErrors;




export const indexId = config.indexId;





AWS.config.credentials =   new AWS.CognitoIdentityCredentials(
      {
        IdentityPoolId:''
      }
    )     
    
AWS.config.update({
      accessKeyId: config.accessKeyId,
      secretAccessKey: config.secretAccessKey
    })
    
const creds = AWS.config.credentials;

export const kendra = !hasErrors
  ? new Kendra({
      credentials: creds,
      region: 'us-east-1'

  })
  : undefined;

export const s3 = !hasErrors
  ? new S3({
      credentials: creds,
      region: 'us-east-1'
      
    })
  : undefined;
