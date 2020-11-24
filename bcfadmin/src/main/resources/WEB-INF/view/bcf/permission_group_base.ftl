<toolmenu style="margin-bottom: 0">
    <tabbar>
        <div>
            <button id="rootNodeDetailBtn1" class="sel" onclick="transToBase(this)">基本信息</button>
            <button id="rootNodeDetailBtn2" onclick="transToDetail(this)">资源信息</button>
        </div>

    </tabbar>
</toolmenu>

<div id="rootNode1">
<detail>
    <form>
        <table>
            <tr style="display: none">
                <th>r_pgid：</th>
                <td><input type="hidden" id="r_pgid" value=""/></td>
            </tr>
            <tr>
                <th>PGID：</th>
                <td><input type="text" id="pgid" value="" disabled style="background-color: gainsboro"/></td>
            </tr>
            <tr>
                <th style="color: red">R_PGID：</th>
                <td><input type="text" id="r_pgid" value=""/></td>
            </tr>
            <tr>
                <th class="t2">P_PGID：</th>
                <td><input type="text" id="p_pgid" value=""/></td>
            </tr>
            <tr>
                <th>PG_Code：</th>
                <td>
                    <input type="text" id="pg_code">
                </td>
            </tr>
            <tr>
                <th class="t1">Tags：</th>
                <td><input type="text" id="tags"/></td>
            </tr>
            <tr>

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
                <th>In_Level：</th>
                <td>
                    <input type="text" id="in_level"/>
                </td>
            </tr>
            <tr>
                <th>Order_Index：</th>
                <td>
                    <input type="text" id="order_index"/>
                </td>
            </tr>
            <tr>
                <th></th>
                <td>
                    <checkbox>
                        <label><input type="checkbox" id="is_branch" name="is_branch"/><a>Is_Branch</a></label>
                    </checkbox>

                    <checkbox>
                        <label><input type="checkbox" id="is_disabled" name="is_disabled"/><a class="t4">Is_Disabled</a></label>
                    </checkbox>

                    <checkbox>
                        <label><input type="checkbox" id="is_visibled" name="is_visibled"/><a>Is_Visibled</a></label>
                    </checkbox>
                </td>
            </tr>

            <tr>
                <th></th>
                <td>
                    <button onclick="save();return false;">保存</button>
                </td>
            </tr>
        </table>
    </form>
</detail>

</div>

<div id="rootNode2" style="min-height: 500px;display: none;">
    <datagrid>
        <table>
            <thead>
            <tr>
                <td>RSID</td>
                <td>CN_Name</td>
                <td>PG_CN_Name</td>
                <td>P_Express</td>
                <td>LK_OBJT</td>
            </tr>
            </thead>
            <tbody id="rootGroupDetailTbody" class="left">

            </tbody>
        </table>
    </datagrid>
</div>

<script>
    var globalGroupType = 1;
    function transToBase(that) {
        $("#rootNode1").show()
        $("#rootNode2").hide()
        $(that).siblings().removeClass('sel')
        $(that).addClass('sel')
        globalGroupType = 1
        $.ajax({
            type: 'get',
            url: '/bcf/permission/list_detail',
            data: {
                pgid: tempNode.pgid
            },
            success: function (data) {



                showRootNodeDetail(data.data)
            }
        })
    }

    function transToDetail(that) {
        $("#rootNode2").show()
        $("#rootNode1").hide()
        $(that).siblings().removeClass('sel')
        $(that).addClass('sel')
        globalGroupType = 2
        layer.load(2, {time: 5*1000})
        $.ajax({
            type: 'get',
            url: '/bcf/group/depart_get_resource',
            data: {
                pgid: tempNode.pgid
            },
            success: function (data) {
                layer.closeAll();


                var tbody = $("#rootGroupDetailTbody")

                tbody.empty();
                var trs = '';
                var d = data.data;
                for (var i =0; i < d.length; i++) {

                    // todo 接口
                    trs += '<tr rsid='+ d[i].rsid +'><td>'+ d[i].rsid +'</td><td>'+ d[i].cn_name +'</td>' +

                        '<td>'+ (d[i].pg_cn_name || "") +'</td><td>'+ d[i].lk_objt +'</td><td>'+ d[i].p_express +'</td></tr>'
                }
                tbody.append(trs);

            }
        })
    }

    $('#rootGroupDetailTbody').selects({
        sessionType: 'rsid'
    }, function () {

    })

    $('#rootNode2').contextmenu({
        menus: [
            {
                name: '复制资源',
                click: function () {

                    if (!getSelectedTrs2().length) {
                        return layer.msg("无可复制内容");
                    }
                    sessionStorage.setItem('copyRes', JSON.stringify(getSelectedTrs2()));
                    layer.msg("复制成功")
                }
            },
            {
                name: '粘贴资源',
                click: function () {
                    var user = JSON.parse(sessionStorage.getItem('copyRes'));
                    if (!user) {
                        layer.msg('无可粘贴内容')
                        return
                    }
                    layer.load(2, {time: 5*1000})
                    $.ajax({
                        type: 'post',
                        url: '/bcf/link/resource',
                        data: {
                            lk_objt_id: tempNode.pgid,
                            rsids: user,
                            lk_objt: 2
                        },
                        traditional: true,
                        success: function (data) {
                            if (data.code === 1) {
                                layer.closeAll()
                                transToDetail(document.getElementById("rootNodeDetailBtn2"))
                            }
                        }
                    });
                }
            },
            {
                name: '移除',
                click: function () {
                    var user = getSelectedTrs2();
                    if (!user.length) {
                        layer.msg('无可删除内容')
                        return
                    }
                    layer.load(2, {time: 5*1000})
                    $.ajax({
                        type: 'post',
                        url: '/bcf/link/remove/resource',
                        data: {
                            "lk_objt_id": tempNode.pgid,
                            "columnIds": getSelectedTrs2(),
                            lk_objt: 2
                        },
                        traditional: true,
                        success: function (data) {
                            if (data.code === 1) {
                                layer.closeAll()
                                layer.msg('成功')

                                $("#rootGroupDetailTbody .sel").remove()
                            } else {
                                layer.msg(data.msg)
                            }
                        }
                    })
                }
            }
        ]
    })
</script>