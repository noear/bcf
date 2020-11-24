<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 人员资源详情</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        body {
            user-select: none;
        }
         body,html{
             overflow: auto;
         }
        datagrid tbody tr:hover {
            background: none;
        }

        datagrid tbody tr:hover td {
            background: none;
        }

        #resources tr:hover {
            background: #ececec;
        }

        #resources tr.sel td {
            background: #ececec;
        }

        /* 右键菜单 */
        #menu {
            width: 125px;
            height: 75px;
            overflow: hidden;
            box-shadow: 0 1px 1px #888, 1px 0 1px #ccc;
            box-sizing: border-box;
            position: fixed;
            background-color: white;
            user-select: none;
            z-index: 9999;
            display: none;
        }

        .menu {
            width: 125px;
            height: 25px;
            line-height: 25px;
            padding: 0 10px;
        }

        .menu:hover {
            background-color: #ececec;
        }

        tbody.left td{
            text-align: left;
        }


    </style>

</head>
<body>
<div>
    <#--<div style="margin: 10px">-->
    <#--<tabbar>-->
    <#--<button id="add" class="edit">新增</button>-->
    <#--<button id="delete">删除</button>-->
    <#--</tabbar>-->
    <#--</div>-->
    <main>
        <datagrid>
            <table>
                <thead>
                <tr>
                    <#--<td>-->
                    <#--<checkbox><label><input type="checkbox" name="c2" /><a></a></label></checkbox>-->
                    <#--</td>-->
                    <td>RSID</td>
                    <td>CN_Name</td>
                    <td>PG_CN_Name</td>
                    <td>P_Expre</td>
                    <td>LK_OBJT</td>
                    <#--<td>操作</td>-->
                </tr>
                </thead>
                <tbody id="resources" class="left">
                </tbody>
            </table>
        </datagrid>
        <div id="menu">
            <div class="menu" id="copyResource">复制资源</div>
            <div class="menu" id="pasteResource">粘贴资源</div>
            <div class="menu" id="removeResource">移除资源</div>
        </div>
    </main>
</div>
</body>

<script>
    var lk_objt_id = getQueryString('puid')
    var tbody = $("#resources");
    $.ajax({
        type: 'get',
        // url: '/bcf/link/list',
        url: '/bcf/link/list/user_resource_all',
        data: {
            puid: lk_objt_id,
            // lk_objt: 2
        },
        success: function (data) {
            renderTable(data.data)
        },
        error: function () {
            top && top.layer.msg('网络错误，请重试')
        }
    })

    function renderTable(list) {
        console.log(list)
        if (!list || !list.length) {
            return;
        }
        var trs;
        tbody.empty();
        list.forEach(function (value) {
            trs += '<tr rsid="' + value.rsid + '">' +
                '                    <td>' + value.rsid + '</td>' +
                '                    <td>' + value.cn_name + '</td>' +
                '                    <td>' + (value.pg_cn_name || '') + '</td>' +
                '                    <td>'+ value.p_express +'</td>' +
                '                    <td>'+ value.lk_objt +'</td>' +
                '                </tr>';
        });
        tbody.append(trs)
    }

    tbody.on('click', '.delete', function () {
        var id = $(this).attr('data-id')
        layer.load(2, {time: 5*1000})
        $.ajax({
            type: 'post',
            url: '/bcf/link/remove/resource',
            traditional: true,
            data: {
                lk_objt: 7,
                lk_objt_id: lk_objt_id,
                columnIds: [id]
            },
            success: function (data) {

                window.location.reload()
            },
            error: function () {
                top && top.layer.msg('网络错误，请重试')
            }
        })
    })
    $("#delete").on('click', function () {
        if (!$("input[name=res]:checked").length) {
            top && top.layer.msg('资源未选择');
            return
        }

        top.layer.confirm('确定删除?', function () {
            top.layer.closeAll()

            var arr = []

            $("input[name=res]:checked").each(function () {
                var id = $(this).attr('rsid') - 0;
                arr.push(id)
            });
            layer.load(2, {time: 5*1000})
            $.ajax({
                type: 'post',
                url: '/bcf/link/remove/resource',
                traditional: true,
                data: {
                    lk_objt: 7,
                    lk_objt_id: lk_objt_id,
                    columnIds: arr
                },
                success: function (data) {
                    window.location.reload()
                },
                error: function () {
                    top && top.layer.msg('网络错误，请重试')
                }
            })

        })


    })

    $("#add").on('click', function () {
        top.layer.open({
            type: 2,
            area: ['700px', '450px'],
            title: '权限菜单',
            fixed: false, //不固定
            content: './permission_add?lk_objt_id=' + lk_objt_id
        });
    })


    /**
     * 右键菜单
     * @param e
     */
    $(document).on("contextmenu", function (e) {

        e.preventDefault();

        if ($(".active.sel").length <= 1) {
            var hoverTr = $(e.target).closest("tr");
            hoverTr.addClass("active sel");
            hoverTr.siblings().removeClass('active sel');
        }
        var menu = document.querySelector("#menu");

        var bodyHeight = window.innerHeight;
        var bodyWidth = document.body.offsetWidth;
        menu.style.left = e.clientX + 'px';
        menu.style.top = e.clientY + 'px';
        console.log(e.clientX)
        if (e.clientY + 25 * 3 > bodyHeight) {
            menu.style.top = e.clientY - 25 * 3 + 'px';
        }
        if (e.clientX + 125 > bodyWidth) {
            menu.style.left = e.clientX - 125 + 'px';
        }

        menu.style.display = 'block';

    });
    $(document).on('click', function (ev) {
        $("#menu").hide();
    })

    $("#copyResource").on('click', function () {
        $('#menu').hide()
        var arr = []
        $("#resources").find('.active').each(function () {
            var rsid = $(this).attr("rsid")
            arr.push(rsid)
        })

        if (!arr.length) {
            top && top.layer.msg('未选中要复制的资源')
        } else {
            top && top.layer.msg('复制成功')
        }

        sessionStorage.setItem('copyRes', JSON.stringify(arr));
    })

    $("#pasteResource").on('click', function () {
        $('#menu').hide()
        var arr = sessionStorage.getItem('copyRes') || [];
        if (!arr.length) {
            layer.msg("无可粘贴内容")
        }

        layer.load(2, {time: 5*1000})
        $.ajax({
            type: 'post',
            url: '/bcf/link/resource',
            data: {lk_objt_id: lk_objt_id, rsids: JSON.parse(arr), lk_objt: 7},
            traditional: true,
            success: function (data) {
                if (data.code === 1) {
                    layer.closeAll();
                    window.location.reload()
                }
            }
        });
    })

    $("#removeResource").on('click', function () {
        $("#menu").hide()
        var active = $("#resources").find('.active')
        if (!active || !active.length) {
            layer.msg('请选择需要删除的资源')
            return
        }
        var arr = []
        active.each(function () {
            arr.push($(this).attr("rsid") - 0)
        })
        layer.load(2, {time: 5*1000})
        $.ajax({
            type: 'post',
            url: '/bcf/link/remove/user',
            data: {"lk_objt": 7, "lk_objt_id": lk_objt_id, "columnIds": arr},
            traditional: true,
            success: function (data) {
                if (data.code === 1) {
                    layer.closeAll()
                    layer.msg('成功')
                    active.remove()
                } else {
                    layer.msg(data.msg)
                }
            }
        })


    })

    $("#resources").on('click', 'tr', function () {
        $(this).siblings().removeClass('active sel')
        $(this).addClass('active sel')


        // temprsId = $(this).attr('rsid')

    })
    $("#resources").on('click', 'tr', noKeyDownTrClick)

    var tempTr;

    var tempShift = {};
    var tempCtrl;

    function noKeyDownTrClick(ev) {
        var that = ev.currentTarget
        var shiftArr = []


        if (ev.ctrlKey || ev.metaKey) {
            console.log('按下ctrl')
            tempTr = $(that);
            tempShift = {};
            $(that).toggleClass('sel').toggleClass('active');

            return;
        }
        if (tempTr) {
            if (ev.shiftKey) {
                var preIndex = tempTr.index() <= $(that).index() ? tempTr.index() : $(that).index();
                var nextIndex = tempTr.index() <= $(that).index() ? $(that).index() + 1 : tempTr.index();
                var diffArr = [];

                var selectedTr = $('#resources tr').slice(preIndex, nextIndex);

                selectedTr.each(function () {
                    shiftArr.push($(this).attr('rsid'));
                });

                console.log('tempShift: ' + JSON.stringify(tempShift));
                if (tempShift && tempShift[preIndex]) {
                    console.log('getArrDiff: ' + getArrDiff(shiftArr, tempShift[preIndex]))


                    diffArr = getArrDiff(shiftArr, tempShift[preIndex]);


                    if (tempShift[preIndex].length >= shiftArr.length) {
                        // 删减元素
                        diffArr.forEach(function (val) {
                            $('#resources').find('tr[rsid=' + val + ']').removeClass('sel').removeClass('active');
                        });
                        tempShift[preIndex] = deepClone(shiftArr);

                    } else {
                        // 增加元素
                        diffArr.forEach(function (val) {
                            $('#resources').find('tr[rsid=' + val + ']').addClass('sel').addClass('active');
                        });
                        tempShift[preIndex] = deepClone(shiftArr);

                    }

                } else {
                    selectedTr.addClass('sel').addClass('active');

                    tempShift[preIndex] = deepClone(shiftArr);
                }


                return;
            }
        }


        tempShift = {};
        tempTr = $(that);
        $(that).siblings().removeClass('active');
        $(that).addClass('active')


        // temprsId = $(that).attr('rsid')

    }

    /**
     *  返回两个数组不同的元素
     * @param arr1
     * @param arr2
     * @returns {T[]}
     */
    function getArrDiff(arr1, arr2) {

        return arr1.concat(arr2).filter(function (v, i, arr) {

            return arr.indexOf(v) === arr.lastIndexOf(v);

        });

    }

    /**
     * 深克隆
     * @param obj
     * @returns {any}
     */
    function deepClone(obj) {
        var _tmp, result;
        _tmp = JSON.stringify(obj);
        result = JSON.parse(_tmp);
        return result;
    }
</script>
</html>