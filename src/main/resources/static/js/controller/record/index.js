var pageIndex = 1;
$(window).scroll(function () {
    if ($(window).scrollTop() + $(window).height() > $(document).height() -50) {
        if ($('#recordBody')[0].children.length < Number($('#deployCount').text())) {
            pageIndex++
            getRecordList()
        }
    }
})

$(() => {
    getRecordList()
})


function getRecordList() {
    $.get("/record/list?pageIndex=" + pageIndex, (res) => {
        $('#deployCount').text(res.deployCount)
        $('#vueCount').text(res.vueCount)
        $('#springbootCount').text(res.springbootCount)
        res.recordList.forEach((item, index) => {
            $('<tr>' +
                '<td>' + item.typeName + '</td>' +
                '<td>' + item.host + '</td>' +
                '<td>' + item.name + '</td>' +
                '<td>' + item.createTime + '</td>' +
                '</tr>').appendTo('#recordBody')
        })
    })
}