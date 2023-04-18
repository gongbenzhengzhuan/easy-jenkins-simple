function tableOnclick(connectId, num, _this, host) {
    switch (num) {
        case 1:
            $.get("/index/deploy/" + connectId, {}, (res) => {
                if (res.code === 200) {
                    $('.cz_' + connectId).attr('disabled', true)
                    $easyAlter.success("提交成功，请稍等")
                    setTimeout(()=>{
                        location.reload()
                    },1000)
                } else {
                    $easyAlter.error(res.msg)
                }
            })
            break;
        case 2:
            saveAndUpdateState = 'Update'
            $('#updateConnect').modal('show')
            $.get("/index/info/" + connectId, {}, (res) => {
                $('#connectIdUpdate').val(res.connectId)
                $('#localPathUpdate').val(res.localPath)
                $('#hostUpdate').val(res.host)
                $('#typeNameUpdate').val(res.typeName)
                $('#usernameUpdate').val(res.username)
                $('#nameUpdate').val(res.name)
                $('#passwordUpdate').val(res.password)
                $('#jarNameUpdate').val(res.jarName)
                $('#portUpdate').val(res.port)
                $('#vueRootLocalPathUpdate').val(res.vueRootLocalPath)
                $('#serverPathUpdate').val(res.serverPath)
                $('#projectPortUpdate').val(res.projectPort)
                $('#pomXmlPathUpdate').val(res.pomXmlPath)
                $('#createTimeUpdate').val(res.createTime)
                $('#mavenPathUpdate').val(res.mavenPath)
                $('#execUpdate').val(res.exec)
                if (res.typeName === 'springboot'){
                    $('#vueRootLocalPathUpdate-col').hide()
                    $('#jarNameUpdate-col-all').show()
                    $('#projectPortUpdate-col-all').show()
                    $('#pomXmlPathUpdate-col').show()
                }else {
                    $('#vueRootLocalPathUpdate-col').show()

                    $('#jarNameUpdate-col-all').hide()
                    $('#projectPortUpdate-col-all').hide()
                    $('#pomXmlPathUpdate-col').hide()
                }
            })
            break;
        case 3:
            $.get("/index/delete/" + connectId + "/" + currentBranch, {}, (res) => {
                if (res.code === 200) {
                    $(_this).closest('tr').remove()
                    $easyAlter.success("删除成功")
                } else {
                    $easyAlter.error(res.msg)
                }
            })
            break;
    }
}

let saveAndUpdateState = ''
let currentBranch = $('#currentBranch').text()

function openModal(val) {
    $("input[type=reset]").trigger("click");
    disabled('jarName', false)
    disabled('vueRootLocalPath', false)
    disabled('pomXmlPath', false)
    disabled('projectPort', false)
    saveAndUpdateState = ''
    $('#addConnect').modal(val)
}

function slash(pom) {
    var x = ''
    if (pom.indexOf('\\') !== -1) {
        x = '\\';
    } else if (pom.indexOf('\\\\') !== -1) {
        x = '\\\\';
    }
    return x;
}

function disabled(ele, blo) {
    if (typeof (blo) == "undefined") {
        blo = "disabled";
    }
    $('#' + ele).attr("disabled", blo)
    if (blo == 'disabled') {
        $('#' + ele).val('')
    }
}

$('#localPathUpdate').blur(() => {
    blur()
})

$("#localPath").blur(() => {
    blur()
})
$('#serverPath').on('input propertychange', function () {
    serverPathBlur()
});


$('#serverPathUpdate').on('input propertychange', function () {
    serverPathBlur()
})

function serverPathBlur() {
    var state = saveAndUpdateState
    if ($('#typeName' + state).val() === 'springboot') {
        if (!$("#serverPath" + state).val()) {
            $('#exec' + state).val('')
        } else {
            $('#exec' + state).val('')
            $('#exec' + state).val('nohup java -jar ' + $("#serverPath" + state).val() + '/' + $('#jarName' + state).val() + ' & tailf ' + $("#serverPath" + state).val() + '/nohup.out')
        }
    }
}

function blur() {
    var state = saveAndUpdateState
    var localPath = $("#localPath" + state).val()
    if (localPath) {
        var newPath = localPath
        newPath = newPath.substring(newPath.lastIndexOf('\\') + 1, newPath.lastIndexOf('.'))
        var pom = localPath
        pom = pom.substring(0, localPath.lastIndexOf('\\') + 1)
        if (localPath.indexOf('jar') !== -1) {
            disabled('jarName' + state, false)
            disabled('pomXmlPath' + state, false)
            disabled('projectPort' + state, false)
            disabled('exec' + state, false)
            $('#projectPort' + state).val(8080)
            $('#typeName' + state).val('springboot')
            $('#name' + state).val(newPath)
            $('#jarName' + state).val(newPath + '.jar')
            pom = pom + slash(pom) + 'pom.xml'
            pom = pom.replace("\\\\", slash(pom))
            pom = pom.replace("\\", slash(pom))
            pom = pom.replace("target\\\\\\", "").replace("target\\\\", "").replace("target\\", "")
            $('#pomXmlPath' + state).val(pom)
            disabled('vueRootLocalPath' + state)
        } else {
            $('#typeName' + state).val('vue')
            newPath = newPath.substring(0, newPath.lastIndexOf(slash(pom)))
            $('#vueRootLocalPath' + state).val(newPath)
            newPath = newPath.substring(newPath.lastIndexOf(slash(pom)) + 1)
            $('#name' + state).val(newPath)
            disabled('jarName' + state)
            disabled('vueRootLocalPath' + state, false)
            disabled('pomXmlPath' + state)
            disabled('projectPort' + state)
            disabled('exec' + state)
        }
    }
}

function okConnect(num) {
    var title = ''
    var stateConnect = ''
    var url
    if (num === 0) {
        title = '保存成功'
        url = '/index/save'
    } else {
        title = '修改成功'
        stateConnect = 'Update'
        url = '/index/update'
    }
    $.ajax({
        url: url,
        dataType: "json",
        async: false,
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify({
            "connectId": $('#connectId' + stateConnect).val(),
            "localPath": $('#localPath' + stateConnect).val(),
            "host": $('#host' + stateConnect).val(),
            "exec": $('#exec' + stateConnect).val(),
            "typeName": $('#typeName' + stateConnect).val(),
            "username": $('#username' + stateConnect).val(),
            "name": $('#name' + stateConnect).val(),
            "password": $('#password' + stateConnect).val(),
            "jarName": $('#jarName' + stateConnect).val(),
            "port": $('#port' + stateConnect).val(),
            "vueRootLocalPath": $('#vueRootLocalPath' + stateConnect).val(),
            "serverPath": $('#serverPath' + stateConnect).val(),
            "projectPort": $('#projectPort' + stateConnect).val(),
            "pomXmlPath": $('#pomXmlPath' + stateConnect).val(),
            "createTime": $('#createTime' + stateConnect).val(),
            "mavenPath": $('#mavenPath' + stateConnect).val()
        }),
        type: "post",
        success: (res) => {
            if (res.code === 200) {
                $('#addConnect').modal('hide')
                $('#updateConnect').modal('hide')
                $easyAlter.success(title)
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

$(document).ready(function() {
    var maxCheckedCount = 2;
    var checkedCount = 0;

    $('thead input[type="checkbox"]').click(function() {
        // 全选/取消全选
        $('tbody input[type="checkbox"]').prop('checked', $(this).prop('checked'));
        checkedCount = $('tbody input[type="checkbox"]:checked').length;
        limitCheckedCount();
    });

    $('tbody input[type="checkbox"]').click(function() {
        // 单个复选框的点击事件
        checkedCount = $('tbody input[type="checkbox"]:checked').length;
        limitCheckedCount();
    });

    $('#confirmButton').click(function() {
        // 确认按钮的点击事件
        var selectedItems = $('tbody input[type="checkbox"]:checked')
            .closest('tr').get();
        let firstText = $(selectedItems[0].children[0])[0].outerText
        let firstId = $(selectedItems[0].children[1])[0].outerText
        let lastText = $(selectedItems[1].children[0])[0].outerText
        let lastId = $(selectedItems[1].children[1])[0].outerText

        if (firstText.trim() === lastText.trim()){
            $easyAlter.error("错误的选择,部署类型不能相同", 1500)
        }else {
            let currentObj
            if (firstText === 'springboot'){
                currentObj = [{ name:lastText,connectId:lastId},{name:firstText, connectId:firstId}]
            }else {
                currentObj = [{name:firstText, connectId:firstId},{ name:lastText,connectId:lastId}]
            }
            $.ajax({
                url: "/index/batch/deploy",
                dataType: "json",
                async: false,
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify(currentObj),
                type: "post",
                success: (res) => {
                    if (res.code === 200) {
                        $easyAlter.success("提交成功，请稍等")
                        setTimeout(()=>{
                            location.reload()
                        },1000)
                    } else {
                        $easyAlter.error(res.msg, 1500)
                    }
                },
                error: (err) => {
                }
            })
        }
    });

    function limitCheckedCount() {
        // 当选择的数量超过最大限制时，禁用未选中的复选框
        if (checkedCount >= maxCheckedCount) {
            $('tbody input[type="checkbox"]:not(:checked)').prop('disabled', true);
        } else {
            $('tbody input[type="checkbox"]').prop('disabled', false);
        }

        // 如果已选择的数量为0或1，禁用“确认”按钮
        if (checkedCount === 0 || checkedCount === 1) {
            $('#confirmButton').prop('disabled', true);
        } else {
            $('#confirmButton').prop('disabled', false);
        }

        // 当选择的数量超过最大限制时，取消多余的选择
        if (checkedCount > maxCheckedCount) {
            $('tbody input[type="checkbox"]:checked:last').prop('checked', false);
            checkedCount = $('tbody input[type="checkbox"]:checked').length;
        }
    }
});
