(function() {
window["JST"] = window["JST"] || {};

window["JST"]["post"] = function(obj) {
obj || (obj = {});
var __t, __p = '', __j = Array.prototype.join;
function print() { __p += __j.call(arguments, '') }
with (obj) {

_.each(books, function (book) {;
__p += '\r\n<div class="w3-card-4 w3-margin-top">\r\n    <header>\r\n        <ul class="w3-navbar w3-border w3-blue-gray w3-large">\r\n            <li>\r\n                <a style="text-transform: uppercase;" class="w3-hover-blue-gray w3-text-white w3-hover-text-theme"\r\n                   href="/book_detail?book_id=' +
((__t = (book.id)) == null ? '' : __t) +
'">' +
((__t = (book.title)) == null ? '' : __t) +
'</a>\r\n            </li>\r\n            <li id="delete_' +
((__t = (book.id)) == null ? '' : __t) +
'" class="w3-right">\r\n                <a class="w3-hover-blue-gray w3-text-white w3-hover-text-theme" href=""\r\n                   onclick="confirm_before_delete_post(\'' +
((__t = (book.id)) == null ? '' : __t) +
'\'); return false;">Delete</a>\r\n            </li>\r\n            <li id="contact_' +
((__t = (book.id)) == null ? '' : __t) +
'" class="w3-right">\r\n                <a class="w3-hover-blue-gray w3-text-white w3-hover-text-theme" href="/contact"\r\n                   onclick="sessionStorage.setItem(\'book_id\', \'' +
((__t = (book.id)) == null ? '' : __t) +
'\')">Contact Seller</a>\r\n            </li>\r\n        </ul>\r\n        <script>\r\n            if (!sessionStorage.getItem(\'id\')) {\r\n                $(\'#delete_' +
((__t = (book.id)) == null ? '' : __t) +
'\').hide();\r\n            }\r\n            if (sessionStorage.getItem(\'id\') !== \'' +
((__t = (book.seller)) == null ? '' : __t) +
'\') {\r\n                $(\'#delete_' +
((__t = (book.id)) == null ? '' : __t) +
'\').hide();\r\n            } else {\r\n                $(\'#contact_' +
((__t = (book.id)) == null ? '' : __t) +
'\').hide();\r\n            }\r\n        </script>\r\n    </header>\r\n    <div class="w3-container w3-white">\r\n        <p>Author: ' +
((__t = (book.author)) == null ? '' : __t) +
'</p>\r\n        <p>Price: ' +
((__t = (book.price)) == null ? '' : __t) +
'</p>\r\n        <p>' + ((__t = (book.views)) == null ? '' : __t) +
' views' +
'</p>\r\n    </div>\r\n</div>\r\n';
});;
__p += '\r\n\r\n';

}
return __p
}})();