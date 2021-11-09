<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 资源附属</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script src="${js}/plugin.js"></script>
    <style>
        body,html{
            overflow: auto;
            height: 100%;
        }

        /* 右键菜单 */

        tbody tr.sel{
            background-color: #ececec;
        }

        tbody.left td{
            text-align: left;
        }
    </style>
</head>
<body rsid="${rsid!}">
<div>
    <datagrid>
        <table>
            <thead>
            <tr>
                <td>LK_OBJT</td>
                <td>LK_OBJT_ID</td>
                <td>LK_OBJT_Name</td>
                <td>P_Express</td>
            </tr>
            </thead>
            <tbody id="tbody" class="list left">
            <#list list as m>
                <tr puid="${m.lk_objt_id}">
                    <td>
                        ${m.lk_objt}
                    </td>
                    <td>
                        ${m.lk_objt_id}
                    </td>
                    <td>
                        ${m.lk_objt_name!}
                    </td>
                    <td>
                        ${m.p_express}
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>

</div>
</body>

<script>


$("body").contextmenu({
    isRightActive: false,
    menus: [
        {
            name: '复制用者',
            click: function () {

                if (!getSelectedTrs().length) {
                    return layer.msg("无可复制内容");
                }
                sessionStorage.setItem('copyUser', JSON.stringify(getSelectedTrs()));
                layer.msg("复制成功")
            }
        },
        {
            name: '粘贴用者',
            click: function () {
                var user = JSON.parse(sessionStorage.getItem('copyUser'));
                if (!user) {
                    layer.msg('无可粘贴内容')
                    return
                }
                layer.load(2, {time: 5*1000})
                $.ajax({
                    type: 'post',
                    url: '/bcf/link/userToResource',
                    data: {
                        rsid: $('body').attr('rsid'),
                        puids: user
                    },
                    traditional: true,
                    success: function (data) {
                        if (data.code === 1) {
                            layer.closeAll()
                            layer.msg('粘贴成功')
                            window.location.reload();
                        }
                    }
                });
            }
        },
        {
            name: '移除',
            click: function () {
                var user = getSelectedTrs();
                if (!user.length) {
                    layer.msg('无可删除内容')
                    return
                }
                layer.load(2, {time: 5*1000})
                $.ajax({
                    type: 'post',
                    url: '/bcf/link/remove/resource_user',
                    data: {
                        "rsid": $('body').attr('rsid'),
                        "puids": getSelectedTrs()
                    },
                    traditional: true,
                    success: function (data) {
                        if (data.code === 1) {
                            layer.closeAll()
                            layer.msg('成功')
                            $(".sel").remove()
                        } else {
                            layer.msg(data.msg)
                        }
                    }
                })
            }
        }
    ]
})

    $("#tbody").selects({
        sessionType: 'puid'
    }, function (ids) {
    })


    function getSelectedTrs() {
        var arr = []
        if (!$(".sel").length) {
            return [];
        }
        $(".sel").each(function () {
            arr.push($(this).attr('puid'))
        });
        return arr;
    }
</script>
</html>