/* Node.js Subnet Calculator

1. Serves formnjs.html page
2. Responds to AJAX call and returns subnet calculated results.
3. Returns 404 Status not found for any other request

Author: Stephen Chambers
*/

var https = require('https');
var file = require('fs');
var url = require('url');
var hello = file.readFileSync('./hello.txt');

var options = {
	  key: file.readFileSync('rb2.key'),
	  cert: file.readFileSync('rb2.crt')
};

https.createServer(options,
  function (request, response) 
  {
      //Serve the index.html page
      response.writeHead(200); 
      response.end(hello);
  }
).listen(1234)


