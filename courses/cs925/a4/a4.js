/* Node.js Subnet Calculator

1. Serves formnjs.html page
2. Responds to AJAX call and returns subnet calculated results.
3. Returns 404 Status not found for any other request

Author: Stephen Chambers
*/

var http = require('http');
var file = require('fs');
var url = require('url');
var hello = file.readFileSync('./hello.txt');

http.createServer(
  function (request, response) 
  {
      //Serve the index.html page
      response.writeHead(200); 
      response.end(hello);
  }
).listen(1234)


