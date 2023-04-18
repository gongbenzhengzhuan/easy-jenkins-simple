let ws;
$(() => {
    //判断浏览器是否支持WebSocket
    var supportsWebSockets = 'WebSocket' in window || 'MozWebSocket' in window;
    if (supportsWebSockets) {
        ws = new WebSocket($('#wsUrl').val());
        ws.onopen = function () {
        }
        ws.onmessage = function (e) {
            if (e.data === '{}') {
                $('#large-header').css('display', "none")
            } else {
                $('#large-header').css('display', "block")
            }

            let data = JSON.parse(e.data)
            Object.keys(data).map(key => {
                let host = key.split("=")[0];
                let connectId = key.split("=")[1];
                let htmlElement = document.getElementById('win' + connectId);
                if (htmlElement) {
                    let app = liItem(data[host + '=' + connectId], connectId);
                    if (!isEmptyLi(connectId, app)) {
                        $('.ul_host_' + connectId).append(app)
                    }
                    if ($(app).text() === 'Successfully deployed') {
                        $('.cz_' + connectId).attr('disabled', false)
                    }
                    ($('.nav_ul').children("li:last-child")[0]).scrollIntoView();
                } else {
                    let html = '<div id="host_' + connectId + '" class="win-deploy">' +
                        '<div class= "nav_wrap">\n' +
                        '                <ul class= "nav_ul ul_host_' + connectId + '">\n' +
                        liItem(data[host + '=' + connectId], connectId) +
                        '                </ul>\n' +
                        '            </div>' +
                        '</div>';
                    createWinBox(host, connectId, html)
                }
            })
        }
        ws.onclose = function (e) {
            //当客户端收到服务端发送的关闭连接请求时，触发onclose事件
        }
        ws.onerror = function (e) {
            //如果出现连接、处理、接收、发送数据失败的时候触发onerror事件
        }
    } else {
        $easyAlter.message('您的浏览器不支持 WebSocket!')
    }

    function createWinBox(host, connectId, html) {
        new WinBox(host, {
            class: "my-theme",
            id: 'win' + connectId,
            html: html,
            x: getRandomInt(15,34)+'%',
            y: getRandomInt(10,26)+'%',
            left: '61px'
        });
    }

    function getRandomInt(min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }

    function liItem(key, connectId) {
        return '<li class="nav_li nav_li_' + connectId + '">' + key + '</li>'
    }

    function isEmptyLi(connectId, li) {
        let elementsByClassName = document.getElementsByClassName('nav_li_' + connectId);
        for (let e of elementsByClassName) {
            if (e.innerText === $(li).text()) {
                return true
            }
        }
        return false
    }
})