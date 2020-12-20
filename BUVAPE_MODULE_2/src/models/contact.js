const Contact = (sequelize, Sequelize) => {
    const contactSchema = sequelize.define("contact", {
        'from_uid': {
            type: Sequelize.INTEGER,
            unique : 'actions_unique'
        },
        'to_uid' : {
            type: Sequelize.INTEGER,
            unique : 'actions_unique'
        }
    }, {
        modelName: 'contact',
        timestamps: false
    },{
        uniqueKeys: {
            actions_unique: {
                fields: ['from_uid','to_uid']
            }
        }
      })
   // contactSchema.removeAttribute('id')
    return contactSchema

}
module.exports = Contact