const axios = require("axios")
const uuid = require('uuid')
const { AWS, docClient, dynamodb, tableName } = require('../db/dbdynamo')
exports.create = (async (req, resp) => {
    try {
        const params = {
            TableName: tableName,
            Item: {
                maso: uuid.v4(),
                ...req.body
            }
        }
        docClient.put(params, (e, d) => {
            if (e) {
                return resp.status(500).send(e)
            }
            return resp.status(200).send({ ...req.body })
        })
    } catch (error) {
        return resp.status(500).send(error)
    }
})
exports.getAll = (async (req, resp) => {
    const idSend = req.params.id
    try {
        const param = {
            TableName: tableName
        }
    
        const results = []
        let item;
    
        item = await docClient.scan(param).promise()
    
        do {
            item.Items.forEach(sv => {
                if(sv.type === undefined){
                    if(sv.sender === idSend || sv.receiver === idSend ){
                        results.push(sv)
                    }
                }else{
                    if(sv.sender === idSend){
                        results.push(sv)
                    }else{
                        sv.receiver.forEach(e => {
                            if(e.id === idSend){
                                results.push(sv)
                            }
                        })
                    }
                }
                
            });
        } while (typeof item.LastEvaluatedKey != 'undefined')
    
        console.log(results[0])
        return resp.status(200).send(results)
    } catch (error) {
        console.log(error)
        return resp.status(500).send(error)
    }
})

// get (/message/:id) //   34?id=1 || 1?id=34
exports.getMessageByUser = (async (req, resp) => {
    const user_id = req.params.id  //1
    const user_friend = req.query.id //34
    //(#s = :sendID AND #rv = :receiveID) OR 
    //":sendID": user_id, ":receiveID": user_friend,
    const params = {
        TableName: tableName,
        FilterExpression: " (#s = :sendID AND #rv = :receiveID) OR  (#s = :receiveID1 AND #rv = :sendID1)",
        ExpressionAttributeNames: {
            "#s": "sender",
            "#rv": "receiver"
        },
        ExpressionAttributeValues: { ":sendID": user_id, ":receiveID": user_friend, ":sendID1": user_id, ":receiveID1": user_friend }
    }
    try {
        let scanResults = [];
        let items;
        do {
            items = await docClient.scan(params).promise();
            items.Items.forEach((item) => scanResults.push(item));
            params.ExclusiveStartKey = items.LastEvaluatedKey;
        } while (typeof items.LastEvaluatedKey != "undefined");

        resp.status(200).send(scanResults[0])

    } catch (error) {
        resp.status(500).send(error)
    }
})


exports.updateMessageObject = (async (req, resp) => {
    try {
        const params = {
            TableName: tableName,
            Key: {maso: req.params.id},
            ReturnValues: 'ALL_NEW',
            UpdateExpression: 'set #ur = :un_read ,#messages = list_append(if_not_exists(#messages, :empty_list), :message)',
            ExpressionAttributeNames: {
                '#messages': 'messages',
                '#ur'      : 'un_read'
            },
            ExpressionAttributeValues: {
                ':un_read' : req.body.un_read,
                ':message': [req.body.message],
                ':empty_list': []
            }
        }
        docClient.update(params, (e, d) => {
            if (e) {
                return resp.status(500).send(e)
            }else{
                return resp.status(200).send({ ...req.body })
            }
        })
    } catch (error) {
        resp.status(500).send(error)
    }

})


// get (/messages/:id)

exports.getMessageById = (async (req,resp) => {
    const pr = {
        TableName : tableName,
        Key : {
            maso : req.params.id
        }
    }

    docClient.get(pr,(e,d) => {
        if(e){
            return resp.status(500).send(e)
        }
        return resp.status(200).send(d.Item)
    })

})


// get (/message/unread/:id)

exports.updateUnreadMessage = (async (req,resp) => {
    const pr = {
        TableName : tableName,
        Key : {
            maso : req.params.id
        },
        UpdateExpression: 'set #ur = :un_read',
        ExpressionAttributeNames: {
            '#ur'      : 'un_read'
        },
        ExpressionAttributeValues: {
            ':un_read' : 0
        }
    }

    try{
        docClient.update(pr, (e, d) => {
            if (e) {
                return resp.status(500).send(e)
            }else{
                return resp.status(200).send()
            }
        })
    }catch(error){
        resp.status(500).send(error)
    }

})

