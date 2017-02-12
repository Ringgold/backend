function getFormData(form) {
    var data = {};
    form.serializeArray().map(function(x){data[x.name] = x.value;});
    data = JSON.stringify(data);
    return data;
}

function delete_post(bookId) {
    $.get('/api/book/delete/' + sessionStorage.getItem('id') + '/' + bookId, function (result) {
        if (result === 'SUCCESS') {
            window.location.reload();
        } else {
            alert("please try again");
        }
    });
}