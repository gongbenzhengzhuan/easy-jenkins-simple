/**
 * 编辑基本设置
 * @param val
 */
function editSetting(val) {
    $('#addConnect').modal(val)
}

/**
 * 弹框确认按钮
 * @param val
 */
function confirmEdit(val) {
    $.ajax({
        url: '/setting/editSetting',
        dataType: "json",
        async: false,
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify({
            "mavenPath": $('#mavenPath').val(),
            "host": $('#host').val(),
            "registryPath": $('#registryPath').val()
        }),
        type: "post",
        success: (res) => {
            if (res.code === 200) {
                $('#addConnect').modal('hide')
                $easyAlter.success("保存成功,项目端口号将在下次重启系统时生效")
                setTimeout(() => {
                    location.reload();
                }, 1100)
            } else {
                $easyAlter.error(res.msg, 1500)
            }
        },
        error: (err) => {
        }
    })
}