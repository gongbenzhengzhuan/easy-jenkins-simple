let saveAndUpdateState = ''
let oldBranchName = ''

/**
 * 添加分支模态框
 * @param val
 */
function insertBranch(val) {
    saveAndUpdateState = 'Save'
    $('#addConnect').modal(val)
}

/**
 * 添加分支
 * @param val
 */
function addBranch(val) {

    if (saveAndUpdateState != 'Update') {
        $.ajax({
            url: '/branch/saveBranch',
            dataType: "json",
            async: false,
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify({"branchName": $("#branchName").val()}),
            type: "post",
            success: (res) => {
                if (res.code === 200) {
                    $('#addConnect').modal('hide')
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
    } else {

        let newBranchName = $('#branchName').val();
        if (newBranchName != oldBranchName) {
            $.get("/branch/editBranch/" + oldBranchName + "/" + newBranchName, (res) => {
                if (res.code === 200) {
                    $('#addConnect').modal('hide')
                    setTimeout(() => {
                        location.reload();
                    }, 1100)
                    $easyAlter.success('修改成功', 1500)
                } else {
                    $easyAlter.error(res.msg, 1500)
                }
            })
        } else {
            $easyAlter.success('修改成功', 1500)
            $('#addConnect').modal('hide')
        }

    }

}


function tableOnclick(branchName, num, _this) {
    switch (num) {
        case 1:
            $.get("/branch/switchPrimaryBranch/" + branchName, (res) => {
                if (res.code === 200) {
                    setTimeout(() => {
                        location.reload();
                    }, 1100)
                    $easyAlter.success('切换成功', 1500)
                } else {
                    $easyAlter.error(res.msg, 1500)
                }
            })
            break;
        case 2:
            saveAndUpdateState = 'Update'
            $('#addConnect').modal('show')
            $('#branchName').val(branchName);
            oldBranchName = branchName
            break;
        case 3:
            $.get("/branch/deleteBranch/" + branchName, (res) => {
                if (res.code === 200) {
                    setTimeout(() => {
                        location.reload();
                    }, 1100)
                    $easyAlter.success('删除成功', 1500)
                } else {
                    $easyAlter.error(res.msg, 1500)
                }
            })
            break;
    }
}