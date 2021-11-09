<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 权限附属</title>
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
    </style>
</head>
<body>
<div>
    <datagrid>
        <table>
            <thead>
            <tr>
                <td>PGID</td>
                <td>CN_Name</td>
                <td>R_PGID</td>
                <td>PG_Code</td>
            </tr>
            </thead>
            <tbody class="list left">
            <#list list as m>
                <tr>
                    <td>
                        ${m.pgid!}
                    </td>
                    <td>
                        ${m.cn_name!}
                    </td>
                    <td>
                        ${m.r_pgid!}
                    </td>
                    <td>
                        ${m.pg_code!}
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