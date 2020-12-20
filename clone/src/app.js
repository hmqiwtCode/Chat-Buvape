const express = require('express')
const path = require('path')
const http = require('http')
const hbs = require('hbs')
const cookieParser = require('cookie-parser')
const userRouter  = require('./routes/user')
const adminRouter = require('./routes/admin')
const momentHandler = require('handlebars.moment')
const paginate = require('handlebars-paginate')

// const authen = require('./middleware/authen')

const app = express()
const server = http.createServer(app)
const port = process.env.PORT || 4000
app.use(cookieParser())

const publicPath = path.join(__dirname, '../public')
const viewsPath = path.join(__dirname, '../templates/views')

const io = require('socket.io')(server)

// io.on('connection',(socket) => {
//     console.log('Have a connection')
//     console.log(socket.id)
// })

require('./routes/socketspe')(io)



hbs.registerHelper('ifCond', function (v1, operator, v2, options) {

    switch (operator) {
        case '==':
            return (v1 == v2) ? options.fn(this) : options.inverse(this);
        case '===':
            return (v1 === v2) ? options.fn(this) : options.inverse(this);
        case '!=':
            return (v1 != v2) ? options.fn(this) : options.inverse(this);
        case '!==':
            return (v1 !== v2) ? options.fn(this) : options.inverse(this);
        case '<':
            return (v1 < v2) ? options.fn(this) : options.inverse(this);
        case '<=':
            return (v1 <= v2) ? options.fn(this) : options.inverse(this);
        case '>':
            return (v1 > v2) ? options.fn(this) : options.inverse(this);
        case '>=':
            return (v1 >= v2) ? options.fn(this) : options.inverse(this);
        case '&&':
            return (v1 && v2) ? options.fn(this) : options.inverse(this);
        case '||':
            return (v1 || v2) ? options.fn(this) : options.inverse(this);
        default:
            return options.inverse(this);
    }
})

hbs.registerHelper('paginate', paginate)
app.use(express.json())
app.set('view engine', 'hbs');
app.set('views', viewsPath)
app.use(express.static(publicPath))
momentHandler.registerHelpers(hbs);


// User
app.use(userRouter)
// Admin
app.use(adminRouter)

require("./routes/loginqr")(app,io,publicPath)


server.listen(port, () => {
    console.log(`listen host ${port}`)
})
