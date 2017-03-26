// transfers sessionStorage from one tab to another
var sessionStorage_transfer = function(event) {
    if(!event) { event = window.event; } // ie suq
    if(!event.newValue) return;          // do nothing if no value to work with
    if (event.key == 'getSessionStorage') {
        // another tab asked for the sessionStorage -> send it
        localStorage.setItem('sessionStorage', JSON.stringify(sessionStorage));
        // the other tab should now have it, so we're done with it.
        localStorage.removeItem('sessionStorage'); // <- could do short timeout as well.
    } else if (event.key == 'sessionStorage' && !sessionStorage.length) {
        // another tab sent data <- get it
        var data = JSON.parse(event.newValue);
        for (var key in data) {
            sessionStorage.setItem(key, data[key]);
        }
        if(sessionStorage.getItem("id")){
            window.location.reload();
        }   //reload page after receiving data to update the content
    } else if (event.key == 'logout'){
        sessionStorage.clear();
        window.location.reload();
    }
};

// listen for changes to localStorage
if(window.addEventListener) {
    window.addEventListener("storage", sessionStorage_transfer, false);
} else {
    window.attachEvent("onstorage", sessionStorage_transfer);
};


// Ask other tabs for session storage (this is ONLY to trigger event)
if (!sessionStorage.length) {
    localStorage.setItem('getSessionStorage', 'foobar');
    localStorage.removeItem('getSessionStorage');
};

function shareLogout(){
    localStorage.setItem('logout', true);
    localStorage.removeItem('logout');
}

function getFormData(form) {
    var data = {};
    form.serializeArray().map(function(x){data[x.name] = x.value;});
    data = JSON.stringify(data);
    return data;
}

function getParameterByName(name, url) {
    if (!url) {
        url = window.location.href;
    }
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

var bookIdToBeDeleted = null;

function confirm_before_delete_post(bookId) {
    bookIdToBeDeleted = bookId;
    document.getElementById('modal-confirm-deletion').style.display='block';
}

function delete_post(){
    var bookId = bookIdToBeDeleted;
    bookIdToBeDeleted = null;
    $.get('/api/book/delete/' + sessionStorage.getItem('id') + '/' + bookId, function (result) {
        if (result === 'SUCCESS') {
            window.location.reload();
        } else {
            alert("please try again");
        }
    });
}