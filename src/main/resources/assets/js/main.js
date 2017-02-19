function getFormData(form) {
    var data = {};
    form.serializeArray().map(function(x){data[x.name] = x.value;});
    data = JSON.stringify(data);
    return data;
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