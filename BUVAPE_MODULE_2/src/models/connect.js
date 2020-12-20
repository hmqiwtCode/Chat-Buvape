const dbConfig = require("../db/mysql")
const Sequelize = require("sequelize")

const sequelize = new Sequelize(dbConfig.DB, dbConfig.USER, dbConfig.PASSWORD, {
  host: dbConfig.HOST,
  dialect: dbConfig.dialect,
  define : {charset: 'utf8mb4'},
  dialectOptions: {
    useUTC: false,
    dateStrings: true,
    typeCast: true,
    timezone: "+07:00"
  },
    timezone: "+07:00",
  operatorsAliases: false,

  pool: {
    max: dbConfig.pool.max,
    min: dbConfig.pool.min,
    acquire: dbConfig.pool.acquire,
    idle: dbConfig.pool.idle
  }
})

const db = {}

db.Sequelize = Sequelize
db.sequelize = sequelize


db.Contact = require("../models/contact")(sequelize, Sequelize);

module.exports = db