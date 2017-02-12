(function() {
window["JST"] = window["JST"] || {};

window["JST"]["post"] = function(obj) {
obj || (obj = {});
var __t, __p = '', __j = Array.prototype.join;
function print() { __p += __j.call(arguments, '') }
with (obj) {

_.each(books, function (book) {;
__p += '\r\n<div data-id="' +
((__t = (book.id)) == null ? '' : __t) +
'" class="w3-card-4 w3-margin-top">\r\n    <header class="w3-container w3-blue-gray">\r\n        <h3><a href="/book_detail" style="text-decoration: none">Book Title: ' +
((__t = (book.title)) == null ? '' : __t) +
'</a></h3>\r\n    </header>\r\n    <div class="w3-container w3-white">\r\n        <p>Author: ' +
((__t = (book.author)) == null ? '' : __t) +
'</p>\r\n        <p>Price: ' +
((__t = (book.price)) == null ? '' : __t) +
'</p>\r\n    </div>\r\n    <footer class="w3-container w3-blue-gray">\r\n        <button class="w3-margin-right w3-margin-top w3-btn w3-round w3-green">Contact seller</button>\r\n        <button id="delete_' +
((__t = (book.id)) == null ? '' : __t) +
'" class="w3-margin-right w3-margin-top w3-btn w3-round w3-green"\r\n                onclick="delete_post(\'' +
((__t = (book.id)) == null ? '' : __t) +
'\');">Delete\r\n        </button>\r\n        <script>\r\n            if (!sessionStorage.getItem(\'id\')) $(\'#delete_' +
((__t = (book.id)) == null ? '' : __t) +
'\').hide();\r\n            if (sessionStorage.getItem(\'id\') !== \'' +
((__t = (book.seller)) == null ? '' : __t) +
'\') $(\'#delete_' +
((__t = (book.id)) == null ? '' : __t) +
'\').hide();\r\n        </script>\r\n    </footer>\r\n</div>\r\n';
});;
__p += '\r\n';

}
return __p
}})();