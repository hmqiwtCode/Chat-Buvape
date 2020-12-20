const axios = require('axios')
const uuid = require('uuid')
module.exports = (app, io) => {
  sockets = [];
  people = {};



  function findUserByName(name) {
    for (socketId in people) {
      if (people[socketId].name === name) {
        return socketId;
      }
    }
    return false;
  }


  io.on('connection', (socket) => {
    console.log("connected", socket.id)
    sockets.push(socket)
    const hostName = 'http://module3-bv.us-east-2.elasticbeanstalk.com' // module 3
    console.log(hostName)

    socket.on('addJoin', function (name) {
      people["people_" + name] = socket;
     // console.log(people)
    })

    socket.on('addRoom', async (data) => {
      const socketReceiver = people["people_" + data.id]
      if (socketReceiver) { // if socket receive online|active
        const roomID = socket.id + "-_-" + socketReceiver.id
        socket.join(roomID)
        socketReceiver.join(roomID)
        socket.broadcast.to(roomID).emit('message', data)
        const messageUser = await axios.get(hostName+`/message/${data.sender}?id=${data.id}`)
        console.log(messageUser.data)
        if (!messageUser.data) { //if have no message
          axios.post(hostName+'/message', {
            sender: data.sender,
            receiver: data.id,
            un_read: 1,
            messages: [
              {
                id_gen: uuid.v4(),
                id: data.sender,
                message: data.message,
                time: data.time
              }
            ]
          }).then((result) => {
            //  console.log(result)
          }).catch((err) => {
            console.log(err)
          });
        } else { // add message object to messages array
          axios.patch(hostName+`/message/${messageUser.data.maso}`, {
            un_read: messageUser.data.un_read + 1,
            message: {
              id_gen: uuid.v4(),
              id: data.sender,
              message: data.message,
              time: data.time
            }
          }).then((result) => {
            console.log(result)
          }).catch((err) => {
            console.log(err)
          });
        }
      } else { // if receive offline| not active   
        const messageUser = await axios.get(hostName+`/message/${data.sender}?id=${data.id}`)
        console.log(messageUser.data)
        if (!messageUser.data) { //if have no message
          axios.post(hostName+'/message', {
            sender: data.sender,
            receiver: data.id,
            un_read: 1,
            messages: [
              {
                id_gen: uuid.v4(),
                id: data.sender,
                message: data.message,
                time: data.time
              }
            ]
          }).then((result) => {
            //  console.log(result)
          }).catch((err) => {
            console.log(err)
          });
        } else { // add message object to messages array
          axios.patch(hostName+`/message/${messageUser.data.maso}`, {
            un_read: messageUser.data.un_read + 1,
            message: {
              id_gen: uuid.v4(),
              id: data.sender,
              message: data.message,
              time: data.time
            }
          }).then((result) => {
            console.log(result)
          }).catch((err) => {
            console.log(err)
          });
        }
      }

    })

    socket.on('createGroup',async (data) => {
      console.log(socket.handshake.headers.host)
      axios.post(hostName+'/message', {
            sender: data.sender,
            receiver: data.receiver,
            un_read: 1,
            name : data.name,
            type : 'group',
            messages: [{
              id_gen: uuid.v4(),
              id: data.sender,
              message: "Hi",
              time: data.time
            }]
          }).then((result) => {
            //  console.log(result)
          }).catch((err) => {
            console.log(err)
          });
    })

    socket.on('addGroup',async (data) => {
      const message = await axios.get(hostName+'/messages/' + data.idMessage)
      if(message){
        const isUserOnline = []
        const arrIdUserReceiver = []
        message.data.receiver.forEach(element => {
          arrIdUserReceiver.push(element.id)
        });

        arrIdUserReceiver.push(message.data.sender)

        arrIdUserReceiver.forEach(e => {
          const socketReceiver = people["people_" + e]
          if(socketReceiver){
            isUserOnline.push(socketReceiver)
          }
        })

        let nameRoom = ""
        isUserOnline.forEach(name => {
          nameRoom += name.id
        })
        isUserOnline.forEach(socketUser => {
          socketUser.join(nameRoom)
        })

        socket.broadcast.to(nameRoom).emit('message', data)

        axios.patch(hostName+`/message/${data.idMessage}`, {
            un_read: message.data.un_read + 1,
            message: {
              id_gen: uuid.v4(),
              id: data.sender,
              message: data.message,
              time: data.time
            }
          }).then((result) => {
            console.log(result)
          }).catch((err) => {
            console.log(err)
          });



      }

    })

    socket.on('disconnect', function () {
      console.log("Disconnect", socket.id)
    });
  })


  const router = require("express").Router();
  app.use('', router);
}