const AWS = require('aws-sdk')
const tableName = 'message'

AWS.config.update({
    accessKeyId : 'your access key',
    secretAccessKey : 'your private key',
    region : 'us-east-2'
})


const dynamodb = new AWS.DynamoDB()
const docClient = new AWS.DynamoDB.DocumentClient()

const param = {
    TableName : tableName,
    KeySchema : [
        {AttributeName : 'maso',KeyType: 'HASH'}
    ],
    AttributeDefinitions : [
        {AttributeName : 'maso', AttributeType : 'S'}
    ],
    ProvisionedThroughput: {       
        ReadCapacityUnits: 1, 
        WriteCapacityUnits: 1
    }
}

// dynamodb.createTable(param, function(err, data) {
//     if (err) {
//         console.error("Unable to create table. Error JSON:", JSON.stringify(err, null, 2));
//     } else {
//         console.log("Created table. Table description JSON:", JSON.stringify(data, null, 2));
//     }
// })

module.exports = {
    AWS,
    docClient,
    dynamodb,
    tableName
}