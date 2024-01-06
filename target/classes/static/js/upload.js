$(document).ready(() => {
    $('body').css('display', 'block');
    const fileName = $(".file-name")
    const fileSize = $(".file-size")
    const bar = $('.progress-bar')
    // 文件上传
    $("#select-file").change(function () {
        var files = this.files;
        $('.progress-bar').width(0)
        bar.removeClass('progress-bar-success');
        if (files.length >= 2) {
            var totalSize = 0;
            for (var i = 0; i < files.length; i++) {
                totalSize += files[i].size;
            }
            fileName.text("已选择: " + files.length + "个文件");
            fileSize.text("文件大小: " + formatFileSize(totalSize));
        } else if (files.length === 1) {
            fileName.text("文件名: " + files[0].name);
            fileSize.text("文件大小: " + formatFileSize(files[0].size));
        } else {
            fileName.text("未选择文件");
            fileSize.text("");
        }
    });

    function formatFileSize(bytes) {
        if (bytes === 0) return '0 Bytes';
        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    }

    $('#upload').on('click', function () {
        const files = $('#select-file')[0].files;
        let percentComplete = 0;
        fileName.text("上传中...")
        if (files.length > 0) {
            const formData = new FormData();

            $.each(files, function (index, file) {
                formData.append('files', file);
            });

            $.ajax({
                url: '/api/upload', // 替换为实际的上传文件的服务器端地址
                type: 'POST', data: formData, processData: false, contentType: false, xhr: function () {
                    const xhr = new window.XMLHttpRequest();

                    xhr.upload.addEventListener('progress', function (event) {
                        if (event.lengthComputable) {
                            percentComplete = (event.loaded / event.total) * 100;
                            bar.width(percentComplete + '%');
                        }
                    }, false);

                    return xhr;
                }, success: function (data) {
                    bar.addClass('progress-bar-success');
                    fileName.text(data);
                }, error: function (error) {
                    console.log(error);
                    alert('上传失败');
                }
            });
        } else {
            alert('请选择文件');
        }

    });

});
