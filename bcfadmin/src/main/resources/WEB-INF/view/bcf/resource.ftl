<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 资源管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <link rel="stylesheet" href="${js}/zTree/zTreeStyle.css" type="text/css">
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" />
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <script src="${js}/zTree/jquery.ztree.core.min.js"></script>

    <style>
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

        #resources tr.sel {
            background: #ececec;
        }

        #resources td {
            text-align: left;
            border-width: 0px 0px 1px 0px;
        }

        #resources ._disabled{
            color: #888;
            text-decoration:line-through;
        }


        .ul-resources {
            overflow-x: visible;
        }

        .ul-resources li {
            cursor: pointer;
            height: 28px;
            line-height: 28px;
            margin-left: -10px;
            padding-left: 10px;
            width: calc(100% + 10px);
        }

        .ul-resources li img {
            width: 20px;
            height: 20px;
        }

        .ul-resources li span {
            vertical-align: top;
        }

        .ul-resources li.active {
            background-color: #fff;
        }

        .ul-resources li:hover {
            background-color: #ececec;
        }

        .ul-resources li.active:hover {
            background-color: #fff;
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
            height: calc(100%);
            overflow-y: auto;
        }

        /* 右键菜单 */
        #menu {
            width: 125px;
            height: 125px;
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

        ._disabled {
            color: #b9b9b9;
            text-decoration: line-through;
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

    </style>

</head>
<body oncontextmenu="self.event.returnValue=false" onselectstart="return false;">
<main>
    <flex>
        <tile class="col-5">
            <div>
                <datagrid>
                    <table>
                        <thead>
                        <tr>
                            <td>分组</td>
                            <td>资源</td>
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
                        <#include "resource_group_base.ftl">
                    </div>
                </li>

                <li>
                    <div class="detail" id="resourceNodeDetail" style="display: none;">
                        <toolmenu style="margin-bottom: 0">
                            <tabbar>
                                <button id="baseIframeBtn" class="sel" onclick="toIframe(this,'base')">基本信息</button>

                                <button id="attachIframeBtn" onclick="toIframe(this,'attach')">附属信息</button>

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
            <div id="menu">
                <div class="menu" id="addChild">新建资源</div>
                <div class="menu" id="addResource">添加资源</div>
                <div class="menu" id="removeResource">移除资源</div>
                <hr/>
                <div class="menu" id="copyResource">复制资源</div>
                <div class="menu" id="pasteResource">粘贴资源</div>
            </div>

        </tile>
    </flex>
</main>
<footer>

</footer>
</body>
<script>
    var tempNode = {};// 缓存点击的组节点
    var tempNodeResource = [];// 缓存点击的组资源节点
    var temprsId = 0;
    var type = '';
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

    function toIframe(that, str) {
        type = str;
        $(that).siblings().removeClass('sel')
        $(that).addClass('sel')
        $(".loading").show()
        $('#iframe').hide()

        switch (str) {
            case 'base':
                var thisNode = {}
                tempNodeResource.forEach(function (value) {
                    if (value.rsid == temprsId) {
                        thisNode = value
                    }
                })
                $('#iframe').attr('src', './resource_base?data=' + encodeURIComponent(JSON.stringify(thisNode)) + '&lk_objt_id=' + tempNode.pgid)
                break;
            case 'attach':
                $('#iframe').attr('src', '/bcf/link/extra?lk_objt=7&rsid=' + temprsId)
                break;
            default:
                break;
        }
        $('#iframe').load(function () {
            $(".loading").hide()
            $('#iframe').show()
        })

    }

    function getAsyncUrl(treeId, treeNode) {
        if (!treeNode) {
            return "/bcf/group/list/4";
        }
        return "/bcf/group/list/" + treeNode.pgid;
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
        if (!childNodes.list.length) {
            return;
        }
        for (var i = 0, l = childNodes.list.length; i < l; i++) {
            childNodes.list[i].isParent = true;
            if (childNodes.list[i].is_disabled) {
                childNodes.list[i].font = {'text-decoration':'line-through !important'}
            }
        }
        return childNodes.list;
    }

    function zTreeOnClick(event, treeId, treeNode) {
        console.log(treeNode)
        // 判断点击层级，对应逻辑
        tempNode = treeNode
        // if (treeNode.isParent) {
        //     $("#addSibling").hide()
        //     $("#addChildBtn").show()
        // }else{
        //     // 子节点显示添加兄弟按钮
        //     $("#addSibling").show()
        //     $("#addChildBtn").hide()
        // }
        showRootNodeDetail(treeNode)
        // 展示右侧资源信息
        showLinkResource(treeNode)
    }

    function showLinkResource(treeNode) {
        // if (treeNode.isParent) {
        $("#resources").empty()
        // return
        // }

        $.ajax({
            type: 'get',
            url: '/bcf/link/list',
            data: {"lk_objt": 2, "lk_objt_id": treeNode.pgid},
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
            $("#resources").empty()
            return
        }
        tempNodeResource = list
        var str = ''
        list.forEach(function (li) {
            str += '<tr rsid=' + li.rsid + '><td>' +
                '                <img src="' + li.ico_path + '" alt="">' +
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
    $('.menu').on('click', function (e) {
        e.stopPropagation()
    })
    $("#addChild").on('click', function (e) {
        e.stopPropagation();
        $('#addResourceBtn').trigger("click");
    })
    $("#addChildBtn").on('click', function () {
        showRootNodeDetail({
            r_pgid: tempNode.hasOwnProperty('r_pgid') ? tempNode.r_pgid : 4,
            p_pgid: tempNode.hasOwnProperty('pgid') ? tempNode.pgid : 4,
            pgid: 0
        })
    })

    $("#addResourcesBtn").on('click', function () {
        $('#addResource').trigger("click");
    })
    $("#addSibling").on('click', function () {
        showRootNodeDetail({
            r_pgid: tempNode.hasOwnProperty('r_pgid') ? tempNode.r_pgid : 4,
            p_pgid: tempNode.hasOwnProperty('p_pgid') ? tempNode.p_pgid : 4,
            pgid: 0
        })
    })

    $("#pasteResource").on('click', function () {
        var arr = sessionStorage.getItem('copyRes') || [];
        if (!arr.length) {
            layer.msg("无可粘贴内容")
        }
        ;
        layer.load(2, {time: 5*1000})
        $.ajax({
            type: 'post',
            url: '/bcf/link/resource',
            data: {lk_objt_id: tempNode.pgid, rsids: JSON.parse(arr), lk_objt: 2},
            traditional: true,
            success: function (data) {
                if (data.code === 1) {
                    layer.closeAll();
                    window.reflash()
                }
            }
        });
    })
    $("#addResource").on('click', function () {
        layer.prompt({title: '添加资源', formType: 2}, function (text, index) {
            var arr = text.split(/,|，|;|\|/)
            layer.load(2, {time: 5*1000})
            $.ajax({
                type: 'post',
                url: '/bcf/link/resource',
                data: {lk_objt_id: tempNode.pgid, rsids: arr, lk_objt: 2},
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

    $("#removeResource").on('click', function () {
        $("#menu").hide()
        var active = $("#resources").find('.active')
        if (!active || !active.length) {
            layer.msg('请选择需要移除的资源')
            return
        }

        if(!confirm("确定要移除资源吗？")){
            return;
        }

        var arr = []
        active.each(function () {
            arr.push($(this).attr("rsid") - 0)
        })
        layer.load(2, {time: 5*1000})
        $.ajax({
            type: 'post',
            url: '/bcf/link/remove/resource',
            data: {"lk_objt": 2, "lk_objt_id": tempNode.pgid, "columnIds": arr},
            traditional: true,
            success: function (data) {
                if (data.code === 1) {
                    layer.closeAll();

                    layer.msg('成功')
                    active.remove()
                } else {
                    layer.msg(data.msg)
                }
            }
        })


    })

    $("#removeBtn").on('click', function () {
        $('#removeResource').trigger('click')
    })


    $("#addResourceBtn").on('click', function () {
        $(".detail").hide()
        $("#resourceNodeDetail").show()
        var thisNode = {
            "rsid": 0,
            "rs_code": "",
            "cn_name": "",
            "en_name": "",
            "uri_path": "",
            "uri_target": "",
            "ico_path": "",
            "order_index": "",
            "note": "",
            "tags": "",
            "is_disabled": false
        }

        $('#iframe').attr('src', './resource_base?data=' + encodeURIComponent(JSON.stringify(thisNode)) + '&lk_objt_id=' + tempNode.pgid)

    })

    $("#resources").on('click', 'tr', noKeyDownTrClick)


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
    /**
     * 右键菜单
     * @param e
     */
    $('#contextmenuWrap').on("contextmenu", function (e) {

        e.preventDefault();

        if ($(".active.sel").length <= 1) {
            var hoverTr = $(e.target).closest("tr");
            hoverTr.addClass("active sel");
            hoverTr.siblings().removeClass('active sel');
        }
        var menu = document.querySelector("#menu");

        menu.style.left = e.clientX + 'px';
        menu.style.top = e.clientY + 'px';

        menu.style.display = 'block';

    });
    $(document).on('click', function (ev) {
        $("#menu").hide();
    })
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

        // $("#baseIframeBtn").addClass('sel')
        // $("#baseIframeBtn").siblings().removeClass('sel')

        $(".detail").hide()
        $("#resourceNodeDetail").show()

        temprsId = $(that).attr('rsid')

        toIframe(that, type || 'base')
    }

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
        layer.load(2, {time: 5*1000})
        $.ajax({
            type: 'post',
            url: '/bcf/group/add',
            data: data,
            success: function (data) {
                if (data.code === 1) {
                    layer.closeAll()
                    layer.msg(data.msg)
                    window.location.reload()
                }
            }
        })
    }


    // 供子iframe通信调用
    window.reflash = function () {
        $('#' + tempNode.tId).find('a').trigger('click')
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