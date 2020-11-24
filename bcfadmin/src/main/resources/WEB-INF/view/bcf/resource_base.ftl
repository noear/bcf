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
    </style>
</head>
<body>
<div>
    <detail>
        <form>
            <table id="detail">
                <tr>
                    <th>RSID：</th>
                    <td><input type="text" id="rsid" disabled style="background-color: gainsboro"/></td>
                </tr>
                <tr>
                    <th>RS_Code：</th>
                    <td><input type="text" id="rs_code" value=""/></td>
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
                    <th class="t1">Tags：</th>
                    <td><input type="text" id="tags"/></td>
                </tr>
                <tr>
                    <th>Note：</th>
                    <td><textarea style="height: 50px;" id="note"></textarea></td>
                </tr>

                <tr>
                    <th>Uri_Target：</th>
                    <td><input type="text" id="uri_target" /></td>
                </tr>
                <tr>
                    <th>Uri_Path：</th>
                    <td><input type="text" id="uri_path"  class="longtxt"/></td>
                </tr>
                <tr>
                    <th>Icon_Path：</th>
                    <td><input type="text" id="ico_path"  class="longtxt"/></td>
                </tr>

                <tr>
                    <th>Order_Index：</th>
                    <td><input type="text" id="order_index"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <checkbox>
                            <label><input type="checkbox" id="is_disabled"
                                          name="is_disabled"/><a style="color: red">Is_Disabled</a></label>
                        </checkbox>
                        <checkbox class="mar10-l">
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
    var data_str =decodeURIComponent(getQueryString('data'));
    var lk_objt_id = getQueryString('lk_objt_id')
    var data = JSON.parse(data_str)


    showRootNodeDetail(data)

    function showRootNodeDetail(treeNode) {
        var inputs = $("#detail").find('input')
        $("#note").val(treeNode.note)
        inputs.each(function () {
            if ($(this)[0].type === 'checkbox') {
                if (!treeNode.hasOwnProperty($(this).attr('id'))) {
                    $(this).prop('checked', false);
                }
                $(this).prop('checked', treeNode[$(this).attr('id')]);
            } else {
                $(this).val(treeNode[$(this).attr('id')]);
            }
        })

    }


    function save() {
        top && top.layer.msg('请稍后..', {
            timer: 1000
        })
        var inputs = $("#detail").find('input')
        var data = {}
        inputs.each(function () {
            if ($(this)[0].type === 'checkbox') {
                data[$(this).attr('id')] = $(this).prop('checked');
            } else {
                data[$(this).attr('id')] = $(this).val();

            }
        })

        data.pgid = lk_objt_id;

        data.note = $("#note").val()

        $.ajax({
            type: 'post',
            url: '/bcf/resource/update',
            data: data,
            success: function (data) {
                if (data.code === 1) {
                    top && top.layer.msg(data.msg)
                    parent.window.reflash()
                    // if (!data.rsid) {
                    //     return
                    // }
                    // linkToGroup(data.rsid);
                }
            },
            error: function (err) {
                top && top.layer.msg(err.statusText)
            }
        })

        function linkToGroup(rsid) {
            $.ajax({
                type: 'post',
                url: '/bcf/link/resource',
                data: {lk_objt_id: lk_objt_id, rsids: [rsid - 0], lk_objt: 2},
                traditional: true,
                success: function (data) {
                    if (data.code === 1) {
                        top && top.layer.msg(data.msg)
                        parent.window.reflash()
                    }
                }
            })
        }
    }
</script>
</html>