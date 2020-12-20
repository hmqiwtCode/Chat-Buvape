const express = require('express')
const app = express()

app.use((req, res, next) => {
    res.setHeader('Access-Control-Allow-Origin', '*')
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE')
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type,Authorization')
    res.setHeader('Access-Control-Allow-Credentials', true)
    next()
})



const port = process.env.PORT || 3001
app.use(express.json())


const db = require("./models/connect")
db.sequelize.sync()

// setup route
require("./routes/contact")(app)



const server = app.listen(port,() => {
    console.log(`Listen at port ${port}`)
})

const io = require('socket.io')(server)
require("./routes/loginqr")(app,io)