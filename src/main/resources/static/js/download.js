$(document).ready(() => {
    // 获取选择框的值
    const selectedFileType = $('#select').val();

    // 渲染文件
    $.ajax({
        url: '/api/files',
        type: 'GET',
        data: {filetype: selectedFileType}, // 传递选择的文件类型
        success(res) {
            let fileCount = 0;
            let fileGroupIndex = 0;
            let fileGroup = getFileGroup(fileGroupIndex);

            for (let i = 0; i < res.length; i++) {
                const filename = res[i];
                const fileLink = `<a href="/api/download?filename=${filename}" class="list-group-item list-group-item-action file-item my-2 btn">${filename}</a>`;

                fileGroup.append(fileLink);
                fileCount++;

                if (fileCount >= 10) {
                    fileCount = 0;
                    fileGroupIndex++;
                    fileGroup = getFileGroup(fileGroupIndex);
                }
            }
        },
        error(err) {
            $('#data').append(`<p>无法获取文件${err}</p>`);
        }
    });

    function getFileGroup(index) {
        let fileGroup = $(`#fileGroup${index}`);

        if (fileGroup.length === 0) {
            fileGroup = $(`<div id="fileGroup${index}" class="file-group mx-2"></div>`);
            $('#data').append(fileGroup);
        }

        return fileGroup;
    }

    $('#search').on('input', function () {
        const searchTerm = $(this).val();
        const selectedFileType = $('#select').val();

        $('#data').empty();

        $.ajax({
            url: '/api/files',
            type: 'GET',
            data: {filetype: selectedFileType, filename: searchTerm},
            success(res) {
                let fileCount = 0;
                let fileGroupIndex = 0;
                let fileGroup = getFileGroup(fileGroupIndex);

                for (let i = 0; i < res.length; i++) {
                    const filename = res[i];

                    if (filename.includes(searchTerm)) {
                        const fileLink = `<a href="/api/download?filename=${filename}" class="list-group-item list-group-item-action file-item my-2 btn">${filename}</a>`;
                        fileGroup.append(fileLink);
                        fileCount++;

                        if (fileCount >= 10) {
                            fileCount = 0;
                            fileGroupIndex++;
                            fileGroup = getFileGroup(fileGroupIndex);
                        }
                    }
                }
            },
            error(err) {
                $('#data').append(`<p>无法获取文件${err}</p>`);
            }
        });
    });


    // 监听选择框变化事件，重新加载文件
    $('#select').change(() => {
        const selectedFileType = $('#select').val();
        // 清空已有数据
        $('#data').empty();
        // 重新加载文件
        $.ajax({
            url: '/api/files',
            type: 'POST',
            data: {filetype: selectedFileType},
            success(res) {
                let fileCount = 0;
                let fileGroupIndex = 0;
                let fileGroup = getFileGroup(fileGroupIndex);

                for (let i = 0; i < res.length; i++) {
                    const filename = res[i];
                    const fileLink = `<a href="/api/download?filename=${filename}" class="list-group-item list-group-item-action file-item my-2 btn">${filename}</a>`;

                    fileGroup.append(fileLink);
                    fileCount++;

                    if (fileCount >= 10) {
                        fileCount = 0;
                        fileGroupIndex++;
                        fileGroup = getFileGroup(fileGroupIndex);
                    }
                }
            },
            error(err) {
                $('#data').append(`<p>can't get file${err}</p>`);
            }
        });
    });
});
