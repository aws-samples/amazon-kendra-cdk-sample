const awsconfig = {
    Auth: {
        identityPoolId: `${process.env.REACT_APP_IDENTITY_POOL_ID}`,
        region: `${process.env.REACT_APP_REGION}`,
        identityPoolRegion: `${process.env.REACT_APP_REGION}`,
        userPoolId: `${process.env.REACT_APP_USER_POOL_ID}`,
        userPoolWebClientId: `${process.env.REACT_APP_USER_POOL_CLIENT_ID}`,
    }
};

export default awsconfig;