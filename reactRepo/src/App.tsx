import React from "react";
import LocalCredentialsBanner from "./services/helpers/LocalCredentialsBanner";
import MockDataWarning from "./services/helpers/MockDataWarning";
import Search from "./search/Search";
import { kendra, indexId, errors, s3 } from "./services/Kendra";
import { facetConfiguration } from "./search/configuration";
import { AuthState } from "@aws-amplify/ui-components";
// This is for the Amplify React App
import Amplify from "aws-amplify";
import { AmplifyAuthenticator, AmplifySignOut, withAuthenticator } from "@aws-amplify/ui-react";
 
import awsconfig from "./aws-config";
import "./App.css";
 
Amplify.configure(awsconfig);
 
 
 
function App() {
 return (
  
 
      
       <div className="App">
 
 
         <AmplifySignOut />
        
        
         {errors.length > 0 ? (
           <MockDataWarning errors={errors} />
         ) : (
           <LocalCredentialsBanner />
         )}
 
         <Search
           kendra={kendra}
           s3={s3}
           indexId={indexId}
           facetConfiguration={facetConfiguration}
         />
 
       </div>
  
 );
}
 
export default withAuthenticator(App);
