<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 权限管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <link rel="stylesheet" href="${js}/zTree/zTreeStyle.css" type="text/css">
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" />
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script src="${js}/zTree/jquery.ztree.core.min.js"></script>
    <script src="${js}/plugin.js"></script>
    <style>
        body,html{
            overflow: hidden;margin: 0px; padding: 0px;
        }
        .tile-bg {
            background-color: #eaedf1;
        }

        body {
            height: 100vh;
        }

        tile {
            height: calc(100vh);
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

        #resources td {
            text-align: left;
            border-width: 0px 0px 1px 0px;
        }

        #resource .active td {
            background-color: #ececec !important;
        }
        #resources {
            user-select: none;
        }

        #resources ._disabled{
            color: #888;
            text-decoration:line-through;
        }

        .iframe-wrap {
            width: 100%;
            height: calc(100vh - 90px);
        }

        #iframe {
            width: 100%;
            height: 100%;
        }

        /*ztree*/
        .ztree {
            padding: 0;
            height: calc(100vh - 90px);
            overflow: auto;
        }

        .ztree * {
            font-size: 14px;
            font-family: '微软雅黑', sans-serif;
        }

        .ztree li a {
            margin-left: -48px;
            width: 100%;
            padding: 0;
            padding-left: 48px;
            height: 27px;
            line-height: 27px;
            text-decoration: none !important;
        }

        .ztree li a.curSelectedNode {
            height: 27px;
            background-color: #ececec;
            border: none;
            opacity: 1;
        }

        .ztree li span.button.switch {
            width: 28px;
            height: 28px;
        }

        .ztree li span.button.noline_close {
            background: url("${img}/add.png");
            background-position: center;
            background-size: 50%;
            background-repeat: no-repeat;
            position: relative;
        }

        .ztree li span.button.noline_open {
            background: url("${img}/reduce.png");
            background-position: center;
            background-size: 50%;
            background-repeat: no-repeat;
            position: relative;

        }

        .ztree li span.button.ico_loading {
            margin-right: 2px;
            background: url(${js}/zTree/img/loading.gif) no-repeat scroll 0 0 transparent;
            vertical-align: middle
        }

        /*todo ovflow bug解决*/
        .ovfh {
            height: calc(100vh - 80px);
            overflow-y: auto;
        }


        @-webkit-keyframes loading {
            from {opacity: 1}
            to {opacity: 0.25}
        }
        div.spinner {
            position: relative;
            top:50%;
            left:50%;
            display: inline-block;
        }
        div.spinner div {
            width: 3px;
            height:7px;
            background: #10110a;
            border:1px solid #fff;
            position: absolute;
            left: 100%;
            top: 100%;
            opacity: 0;

            -webkit-animation: loading 2s linear infinite;
            -webkit-border-radius:60%;
        }
        div.spinner div.bar1 {-webkit-transform:rotate(0deg) translate(0, -13px); -webkit-animation-delay: -0s;}
        div.spinner div.bar2 {-webkit-transform:rotate(30deg) translate(0, -13px); -webkit-animation-delay: -1.8313s;}
        div.spinner div.bar3 {-webkit-transform:rotate(60deg) translate(0, -13px); -webkit-animation-delay: -1.6668s;}
        div.spinner div.bar4 {-webkit-transform:rotate(90deg) translate(0, -13px); -webkit-animation-delay: -1.5002s;}
        div.spinner div.bar5 {-webkit-transform:rotate(120deg) translate(0, -13px); -webkit-animation-delay: -1.3336s;}
        div.spinner div.bar6 {-webkit-transform:rotate(150deg) translate(0, -13px); -webkit-animation-delay: -1.167s;}
        div.spinner div.bar7 {-webkit-transform:rotate(180deg) translate(0, -13px); -webkit-animation-delay: -1.0004s;}
        div.spinner div.bar8 {-webkit-transform:rotate(210deg) translate(0, -13px); -webkit-animation-delay: -0.8338s;}
        div.spinner div.bar9 {-webkit-transform:rotate(240deg) translate(0, -13px); -webkit-animation-delay: -0.6672s;}
        div.spinner div.bar10 {-webkit-transform:rotate(270deg) translate(0, -13px); -webkit-animation-delay: -0.5006s;}
        div.spinner div.bar11 {-webkit-transform:rotate(300deg) translate(0, -13px); -webkit-animation-delay: -0.313s;}
        div.spinner div.bar12 {-webkit-transform:rotate(330deg) translate(0, -13px); -webkit-animation-delay: -0.1674s;}

        tr.sel td{
            background-color: #ececec;
        }
        tr.sel:hover{
            background-color: #ececec;
        }
    </style>

</head>
<body class="tile-bg" oncontextmenu="self.event.returnValue=false" onselectstart="return false;">

<main>
    <flex>
        <tile class="col-5">
            <div>
                <datagrid>
                    <table>
                        <thead>
                        <tr>
                            <td>部门</td>
                            <td>人员</td>
                        </tr>
                        <tr>
                            <td style="background-color: white;text-align: left">
                                <button id="addChildBtn">添加子级</button>
                                <button id="addSibling">添加兄弟</button>
                            </td>
                            <td style="background-color: white;text-align: left">
                                <button id="addResourcesBtn">批量添加</button>
                                <button id="removeBtn">移除</button>
                                <button id="addResourceBtn" class="edit">新建</button>
                            </td>
                        </tr>
                        </thead>
                        <tbody style="overflow: hidden;">
                        <tr>
                            <td style="width: 50%;">
                                <ul id="treeResource" class="ztree"></ul>
                            </td>
                            <td id="contextmenuWrap" style="position: relative;overflow: auto;width: 50%;">
                                <table style="position: absolute;left: 0;top: 0;">
                                    <tbody id="resources">
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </datagrid>
            </div>
        </tile>
        <tile class="col-7" style="overflow: hidden;">
            <ul style="background-color: #fff;padding: 0;">
                <li style="margin-bottom: 0">
                    <div class="detail" id="rootNodeDetail">
                        <#include "permission_group_base.ftl">
                    </div>
                </li>

                <li>
                    <div class="detail observer" out-id="" type="" id="resourceNodeDetail" style="display: none;">
                        <toolmenu style="margin-bottom: 0">
                            <tabbar>
                                <div>
                                    <button data-id="1" class="sel" onclick="toIframe(this,1)">基本信息</button>
                                    <button data-id="2" onclick="toIframe(this,2)">扩展资源</button>
                                    <button data-id="3" onclick="toIframe(this,3)">资源信息</button>
                                    <button data-id="4" onclick="toIframe(this,4)">组织关系</button>
                                    <button data-id="5" onclick="toIframe(this,5)">人员关系</button>
                                </div>

                            </tabbar>
                        </toolmenu>

                        <div class="iframe-wrap" id="iframe-wrap">
                            <iframe id="iframe" src="" frameborder="0"></iframe>
                            <div class="spinner loading" style="display: none;">
                                <div class="bar1">
                                </div>
                                <div class="bar2">
                                </div>
                                <div class="bar3">
                                </div>
                                <div class="bar4">
                                </div>
                                <div class="bar5">
                                </div>
                                <div class="bar6">
                                </div>
                                <div class="bar7">
                                </div>
                                <div class="bar8">
                                </div>
                                <div class="bar9">
                                </div>
                                <div class="bar10">
                                </div>
                                <div class="bar11">
                                </div>
                                <div class="bar12">
                                </div>
                            </div>
                        </div>
                    </div>
                </li>

            </ul>
        </tile>
    </flex>
</main>

</body>
<script>
    var tempNode = {};// 缓存点击的组节点
    var tempCopyArr = [];

    function Observer(options, callback) {
        this.callback = callback
        this.options = options
        this.init()
    }
    Observer.prototype = {
        init: function () {
            var observerDom = document.querySelector(this.options.el);
            var _this = this;
            var observer = new MutationObserver(function (mutations) {
                _this.mutations = mutations;

                // 变动的值不存在
                if (!_this.getAttr(_this.options.attr)) {
                    return
                }
                // 获取变动前的attr值
                var attrOldValue = _this.getAttr(_this.options.attr).oldValue;
                // 获取变动后的attr值
                var attrNewValue = observerDom.getAttribute(_this.options.attr)
                // if (attrNewValue == attrOldValue) {
                //     return
                // }
                _this.callback(observerDom.getAttribute(_this.options.attr));
            })
            observer.observe(observerDom, {
                attributes: true,
                attributeOldValue: true
            })
        },
        getAttr: function (attr) {
            return this.mutations.find(function (value) {
                return value.attributeName == attr
            })
        }
    }

    var o = new Observer({
        // 监听的元素
        el: '.observer',
        // 要监听的属性值
        attr: 'type'
    }, function (attrType) {

        var attrNewValue = $(".observer").attr('out-id');
        tranIframe(attrType, attrNewValue)
    })

    var ob = new Observer({
        // 监听的元素
        el: '.observer',
        // 要监听的属性值
        attr: 'out-id'
    },function (attrNewValue) {

        var type = $(".observer").attr('type');

        tranIframe(type, attrNewValue)
    })

    function tranIframe(type, value) {
        $(".detail").hide();
        $("#resourceNodeDetail").show();

        switch (type-0) {
            case 1:
                $('#iframe').attr('src', './permission_user_base?puid=' + value + '&pgid=' + tempNode.pgid);
                break;
            case 2:
                $('#iframe').attr('src', './permission_user_extra?puid=' + value);
                break;
            case 3:
                $('#iframe').attr('src', './permission_list?puid=' + value);
                break;
            case 4:
                $('#iframe').attr('src', '/bcf/group/getGroupConn?puid=' + value);
                break;
            case 5:
                $('#iframe').attr('src', '/bcf/group/getUserConn?puid=' + value);
                break;
            default:
                break;
        }
    }
    function toIframe(that, str) {
        $(that).siblings().removeClass('sel')
        $(that).addClass('sel')
        $(".observer").attr("type", str);
        $(".loading").show()
        $('#iframe').hide()
        $('#iframe').load(function () {
            $(".loading").hide()
            $('#iframe').show()
        })
    }





    var setting = {
        view: {
            showIcon: false,
            showLine: false,
            fontCss: getFont,
            nameIsHTML: true
        },
        data: {
            simpleData: {
                enable: true,
                idKey: "pgid",
                name: "cn_name",
                isParent: "isParent"
            },
            key: {
                name: "cn_name"
            }
        },
        async: {
            enable: true,
            url: getAsyncUrl,
            dataFilter: filter
        },
        callback: {
            onClick: zTreeOnClick,
            onAsyncSuccess: zTreeOnAsyncSuccess
        }

    }


    function getAsyncUrl(treeId, treeNode) {
        if (!treeNode) {
            return "/bcf/permission/list?pGroupId=0";
        }
        return "/bcf/permission/list?pGroupId=" + treeNode.pgid;
    }
    function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
        if (!JSON.parse(msg).list.length) {
            var treeObj = $.fn.zTree.getZTreeObj("treeResource");
            treeNode.isParent = false;
            treeObj.updateNode(treeNode);
            return false;
        }
    }
    function getFont(treeId, node) {
        return node.font ? node.font : {};
    }
    function filter(treeId, parentNode, childNodes) {
        if (!childNodes) return null;
        for (var i = 0, l = childNodes.list.length; i < l; i++) {
            childNodes.list[i].isParent = true;
            if (childNodes.list[i].is_disabled) {
                childNodes.list[i].font = {'text-decoration':'line-through !important'}
            }

        }
        return childNodes.list;
    }

    function zTreeOnClick(event, treeId, treeNode) {
        // 判断点击层级，对应逻辑
        tempNode = treeNode
        $(".detail").hide()
        $("#rootNodeDetail").show()
        if (globalGroupType == 1) {

            $("#rootNodeDetailBtn1").click();

            // showRootNodeDetail(treeNode);
        } else if (globalGroupType == 2) {
            $("#rootNodeDetailBtn2").click();
        }
        // 展示右侧资源信息
        showLinkResource(treeNode);
    }

    function showLinkResource(treeNode) {
        // if (treeNode.isParent) {
        //     return
        // }

        $("#resources").empty();
        $.ajax({
            type: 'get',
            url: '/bcf/permission/user/list',
            data: {"lk_objt": 7, "lk_objt_id": treeNode.pgid},
            success: function (data) {
                if (data.code === 1) {
                    renderResourceList(data.list)
                } else {
                    layer.msg(data.msg)
                }
            }
        })

    }

    function renderResourceList(list) {
        if (!list || !list.length) {
            return
        }
        var str = ''
        list.forEach(function (li) {
            str += '<tr rsid=' + li.puid + '><td>' +
                '                <span class="' + (li.is_disabled ? "_disabled" : "") + '">' + li.cn_name + '</span>' +
                '            </td></tr>'
        })
        $("#resources").empty()
        $("#resources").append(str)
    }

    function showRootNodeDetail(treeNode) {
        $(".detail").hide()
        $("#rootNodeDetail").show()
        var inputs = $("#rootNodeDetail").find('input')
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


    $(document).ready(function () {
        $.fn.zTree.init($("#treeResource"), setting);
    });
</script>
<script>
    $("#addChildBtn").on('click', function () {
        showRootNodeDetail({
            r_pgid: tempNode.hasOwnProperty('r_pgid') ? tempNode.r_pgid : 4,
            p_pgid: tempNode.hasOwnProperty('pgid') ? tempNode.pgid : 4,
            pgid: 0
        })
    })
    $("#addSibling").on('click', function () {
        showRootNodeDetail({
            r_pgid: tempNode.hasOwnProperty('r_pgid') ? tempNode.r_pgid : 4,
            p_pgid: tempNode.hasOwnProperty('p_pgid') ? tempNode.p_pgid : 4,
            pgid: 0
        })
    })
    $("#addResourcesBtn").on('click', function () {
        layer.prompt({title: '添加资源', formType: 2}, function (text, index) {
            var arr = text.split(/,|，|;|\|/)
            layer.load(2, {time: 5*1000})
            $.ajax({
                type: 'post',
                url: '/bcf/link/user',
                data: {lk_objt_id: tempNode.pgid, psids: arr, lk_objt: 2},
                traditional: true,
                success: function (data) {
                    if (data.code === 1) {
                        layer.closeAll()
                        layer.close(index);
                        window.reflash()
                    }
                }
            })
        });
    })

    $("#removeBtn").on('click', function () {
        var active = $("#resources").find('.sel')
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
            url: '/bcf/link/remove/userFromGroup',
            data: {"lk_objt": 2, "lk_objt_id": tempNode.pgid, "columnIds": arr},
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


    function save() {
        var inputs = $("#rootNodeDetail").find('input')
        var data = {}
        inputs.each(function () {
            if ($(this)[0].type === 'checkbox') {
                data[$(this).attr('id')] = $(this).prop('checked');
            } else {
                data[$(this).attr('id')] = $(this).val();

            }
        })

        layer.load(2, {time: 5*1000});

        $.ajax({
            type: 'post',
            url: '/bcf/group/add',
            data: data,
            success: function (data) {
                if (data.code === 1) {
                    layer.closeAll();
                    layer.msg(data.msg);

                    if(!data.pgid) {
                        window.location.reload();
                    }
                }
            }
        })
    }

    $("#addResourceBtn").on('click', function () {
        $(".detail").hide();
        $("#resourceNodeDetail").show();
        $(".observer").attr('out-id', 0)
        $(".observer").attr('type', 1)
    })

    $('#contextmenuWrap').contextmenu({
        menus: [
            {
                name: '新建',
                click: function () {
                    $("#addResourceBtn").trigger('click')
                }
            },
            {
                name: '添加',
                click: function () {
                    $("#addResourcesBtn").trigger('click')
                }
            },
            {
                name: '移除',
                click: function () {
                    $("#removeBtn").trigger('click')
                }
            },
            {
                name: '复制',
                click: function () {
                    var tempCopyArr = getSelectedTrs();
                    if (!tempCopyArr.length) {
                        return layer.msg('无可用复制项')
                    }
                    sessionStorage.setItem('copyUser', JSON.stringify(tempCopyArr));
                    layer.msg('复制成功')
                }
            },
            {
                name: '粘贴',
                click: function () {
                    var arr = sessionStorage.getItem('copyUser') || [];
                    if (!arr.length) {
                        layer.msg("无可粘贴内容")
                    }

                    layer.load(2, {time: 5*1000})
                    $.ajax({
                        type: 'post',
                        url: '/bcf/link/user',
                        data: {lk_objt_id: tempNode.pgid, psids: JSON.parse(arr), lk_objt: 2},
                        traditional: true,
                        success: function (data) {
                            if (data.code === 1) {
                                layer.closeAll();
                                window.reflash()
                            }
                        }
                    });
                }
            },
        ]
    })

    $('#resources').selects({
        sessionType: 'rsid'
    }, function (ids) {
        tempCopyArr = getSelectedTrs()
    })
    $("#resources").on('click', 'tr', function (ev) {

        // 变更outid
        $(".observer").attr('out-id', $(ev.currentTarget).closest('tr').attr('rsid'))
        $(".observer").attr('type', $(".observer").attr('type') || 1)
    })
    // 供子iframe通信调用
    window.reflash = function () {
        $('#' + tempNode.tId).find('a').trigger('click')
    }
    function getSelectedTrs() {
        var arr = []
        if (!$('#resources').find('.sel').length) {
            return [];
        }
        $('#resources').find('.sel').each(function () {
            arr.push($(this).attr('rsid'))
        });
        return arr;
    }

    function getSelectedTrs2() {
        var arr = []
        if (!$('#rootGroupDetailTbody').find('.sel').length) {
            return [];
        }
        $('#rootGroupDetailTbody').find('.sel').each(function () {
            arr.push($(this).attr('rsid'))
        });
        return arr;
    }
</script>
</html>