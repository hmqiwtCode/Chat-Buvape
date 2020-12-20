const express = require('express')
const db = require("./db/dbdynamo")
const http = require('http')
const app = express()
const server = http.createServer(app)
const port = process.env.PORT || 3002
const io = require('socket.io')(server)

app.use((req, res, next) => {
    res.setHeader('Access-Control-Allow-Origin', '*')
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE')
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type,Authorization')
    res.setHeader('Access-Control-Allow-Credentials', true)
    next()
})

app.use(express.json())

require("./routes/message")(app)
require("./routes/chat")(app,io)


server.listen(port,() => {
    console.log(`Listen at port ${port}`)
})