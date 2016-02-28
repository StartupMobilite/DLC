var server = require('./server');
var ds = server.dataSources.postgre;
var lbTables = ['User', 'AccessToken', 'ACL', 'RoleMapping', 'Role','utilisateur','distributeur'];
//var lbTables = ['dlcdistributeur'];
ds.automigrate(lbTables, function(er) {
  if (er) throw er;
  console.log('Loopback tables [' + lbTables + '] created in ', ds.adapter.name);
  ds.disconnect();
});