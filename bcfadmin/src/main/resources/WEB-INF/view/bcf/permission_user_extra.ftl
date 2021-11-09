<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 资源扩展</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>
    <style>
        body,html{
            overflow: auto;
        }

        tbody.left td{
            text-align: left;
        }

        #opsx{height: 300px;}
    </style>
</head>
<body>
<div>
    <detail id="detail">
        <form>
            <textarea id="opsx"></textarea>
            <button style="display: block;" id="submit" onclick="save();return false;">保存</button>
        </form>
    </detail>
</div>
</body>

<script>
    var lk_objt_id = getQueryString('puid')

            $.ajax({
                type: 'get',
                url: '/bcf/show/opsx?lk_obj_id=' + lk_objt_id + '&lk_objt=' + 7,
                success: function (data) {
                    if (data.code === 1) {
                        if (data.opsx.opsx) {
                            $("#opsx").val(data.opsx.opsx)
                            // document.getElementById('detail').innerHTML = JSON.stringify(JSON.parse(data.opsx.opsx));

                        }
                    }
                }
            })
    
    function save() {

        if (!$("#opsx").val()) {
           return
        }
        layer.load(2, {time: 5*1000})
        $.ajax({
            type: 'get',
            url: '/bcf/show/set_opsx',
            data: {
                lk_obj_id: lk_objt_id,
                lk_objt: 7,
                opsx: $("#opsx").val()
            },
            success: function (data) {
                if (data.code === 1) {
                    layer.closeAll()
                    layer.msg('成功')
                    window.location.reload();
                }
            }
        });
    }

</script>
</html>