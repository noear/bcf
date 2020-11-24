<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 资源详情</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        body,html{
            overflow: auto;
        }

        tbody.left td{
            text-align: left;
        }
    </style>
</head>
<body>
<div>
    <detail>
        <form>
            <table id="detail">
                <#--<tr style="display: none">-->
                    <#--<td>r_pgid：</td>-->
                    <#--<td><input type="hidden" id="r_pgid" value=""/></td>-->
                <#--</tr>-->
                <#--<tr style="display: none">-->
                    <#--<td>note：</td>-->
                    <#--<td><input type="hidden" id="note" value=""/></td>-->
                <#--</tr>-->
                <tr>
                    <th>PUID：</th>
                    <td><input type="text" id="puid" disabled style="background-color: gainsboro"/></td>
                </tr>
                <tr>
                    <th class="t2">User_Id：</th>
                    <td><input type="text" id="user_id" value=""/></td>
                </tr>
                <tr>
                    <th class="t4">Pass_Wd：</th>
                    <td><input type="text" id="pass_wd" value=""/></td>
                </tr>
                <tr>
                    <th class="t1">Tags：</th>
                    <td><input type="text" id="tags"/></td>
                </tr>
                <tr>
                    <th><mark>OUT_OBJT：</mark></th>
                    <td><input type="text" id="out_objt"/></td>
                </tr>
                <tr>
                    <th><mark>OUT_OBJT_ID：</mark></th>
                    <td><input type="text" id="out_objt_id"/></td>
                </tr>
                <tr>
                    <th class="t2">CN_Name：</th>
                    <td><input type="text" id="cn_name"/></td>
                </tr>
                <tr>
                    <th>EN_Name：</th>
                    <td><input type="text" id="en_name"/></td>
                </tr>
                <tr>
                </tr>

                <tr>
                    <th>PW_Mail：</th>
                    <td><input type="text" class="txt longtxt" id="pw_mail"/></td>
                </tr>
                <tr>
                    <th></th>
                    <td>
                        <checkbox>
                            <label><input type="checkbox" id="is_disabled"
                                          name="is_disabled"/><a class="t4">Is_Disabled</a></label>
                        </checkbox>

                        <checkbox>
                            <label><input type="checkbox" id="is_visibled"
                                          name="is_visibled"/><a>Is_Visibled</a></label>
                        </checkbox>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <button onclick="save();return false;">保存</button>
                    </td>
                </tr>
            </table>
        </form>
    </detail>
</div>
</body>

<script>
    var puid = getQueryString('puid')
    var lk_objt_id = getQueryString('lk_objt_id')
    var p_pgid = getQueryString('pgid')

    if (puid == 0) {
        // 新建
        var thisNode = {
            "cn_name": "",
            "en_name": "",
            "is_disabled": false,
            "is_visibled": false,
            "out_objt": 0,
            "out_objt_id": 0,
            "pass_wd": "",
            "puid": 0,
            "pw_mail": "",
            "state": 0,
            "tags": "",
            "note": "",
            "user_id": "",
            "visibled": true
        }
        showRootNodeDetail(thisNode)
    } else {
        $.ajax({
            type: 'post',
            url: '/bcf/user/detail/' + puid,
            success: function (data) {
                if (data.code === 1) {
                    showRootNodeDetail(data.data)
                }
            },
            error: function (err) {
                top && top.layer.msg(err.statusText)
            }
        });
    }



    function showRootNodeDetail(treeNode) {
        var inputs = $("#detail").find('input')
        inputs.each(function () {
            try {
                if ($(this)[0].type === 'checkbox') {
                    if (!treeNode.hasOwnProperty($(this).attr('id'))) {
                        $(this).prop('checked', false);
                    }
                    $(this).prop('checked', treeNode[$(this).attr('id')]);
                } else {
                    $(this).val(treeNode[$(this).attr('id')]);
                }
            }catch(e){
                $(this).val('');
            }
        })

    }


    function save() {
        var inputs = $("#detail").find('input')
        var data = {}
        inputs.each(function () {
            if ($(this)[0].type === 'checkbox') {
                data[$(this).attr('id')] = $(this).prop('checked');
            } else {
                data[$(this).attr('id')] = $(this).val();

            }
        })

        data.pgid = p_pgid
        layer.load(2, {time: 5*1000})
        $.ajax({
            type: 'post',
            url: '/bcf/permission/add/user',
            data: data,
            success: function (data) {
                if (data.code === 1) {
                    layer.closeAll()
                    top && top.layer.msg(data.msg)
                    parent.window.showLinkResource({
                        isParent: false,
                        pgid: p_pgid
                    })

                    if (!data.psid) {
                        // 新增用户的时候会返回psid，如果没有psid或者为0，则为更新用户，不必做组关联
                        return
                    }
                    top && top.layer.msg(data.msg)
                    linkToGroup(data.psid);
                }
            },
            error: function (err) {
                top && top.layer.msg(err.statusText)
            }
        })

        function linkToGroup(psid) {
            $.ajax({
                type: 'post',
                url: '/bcf/link/user',
                data: {lk_objt_id: p_pgid, psids: [psid - 0], lk_objt: 2},
                traditional: true,
                success: function (data) {
                    if (data.code === 1) {
                        // top && top.layer.msg(data.msg)
                        // parent.window.reflash()
                    }
                }
            })
        }
    }
</script>
</html>