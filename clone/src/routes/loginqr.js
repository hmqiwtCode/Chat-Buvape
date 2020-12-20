const qr = require('qrcode')
const uuid = require('uuid')
const { addUser } = require('./manage')
module.exports = (app, io, publicPath) => {
  let connect_id = null
  let abc_id = null
  io.on('connection', (socket) => {
    abc_id = uuid.v4()
    connect_id = socket.id
    console.log(abc_id + ": uuid")
    console.log(socket.id + " : connection")

    socket.on('getIMG', () => {
      var opts = {
        width: 200,
        height: 200,
        margin: 0.5,
        color: {
          dark: "#000",
          light: "#FFF"
        }
      }
      qr.toDataURL(connect_id, opts, (e, i) => {
        socket.emit("IMGQR", i)
      })

    })
    socket.on('scanPhone',data =>{
      const profileInfo = data.split('-__-')
      io.to(profileInfo[0]).emit('getReload',profileInfo)
    })

    socket.on('pass_login',(data) => {
      console.log(data)
      io.to(data.token.split('-_-')[1]).emit('save_token',{token:data.token.split('-_-')[0],data})
    })

    socket.on('cancel_login',(data) => {
      io.to(data.split('-_-')[1]).emit('reload_browser','reload')
    })

  })
  var router = require("express").Router();
  router.get("/loginQR", (req, resp) => {
    resp.render('qrcode')
  })
  app.use('', router);
}


