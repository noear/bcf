<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 人员关系</title>
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
    <datagrid>
        <table>
            <thead>
            <tr>
                <td>PUID</td>
                <td>RL</td>
                <td>CN_Name</td>
            </tr>
            </thead>
            <tbody class="list left">
            <#list list as m>
                <tr>
                    <td>
                        ${m.puid!}
                    </td>
                    <td>
                        ${m.rl!}
                    </td>
                    <td>
                        ${m.cn_name!}
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </datagrid>
</div>
</body>

<script>
</script>
</html>