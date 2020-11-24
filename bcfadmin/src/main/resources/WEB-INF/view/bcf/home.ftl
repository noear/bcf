<!DOCTYPE HTML>
<html>
<head>
    <title>${app} - 管理工具</title>
    <link rel="shortcut icon" type="image/x-icon" href="/favicon.ico"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8 "/>
    <link rel="stylesheet" href="${css}/main.css"/>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" />
    <script src="/_session/domain.js"></script>
    <script src="${js}/lib.js"></script>
    <script src="${js}/layer.js"></script>

    <style>
        body { height: 100vh;}

        body > header label > div { line-height:50px!important; font-size:20px;}
        body > header aside .logout{display:inline-block; height:100%; border-left:1px solid #444; padding:0 15px; margin-left:15px;}
        body >header aside .uicon{line-height:50px;position:relative; top:7px; width:24px; border-radius:12px;margin-right:5px;}

        iframe{display: none;}
        iframe.sel{display: block;}

        nav > a{cursor: default;}
    </style>
    <script>
        function elemClick(el){
            if(el.length === 1){
                el[0].click();
            }
        }

        $(function (){
            var lskey = "bcf/home";

            var _sel = localStorage.getItem(lskey);

            $('nav a').each(function (){
                $(this).click(function (){
                    localStorage.setItem(lskey,this.id);

                    let ths = $(this);
                    let sel = $("nav a.sel");

                    if(ths.attr('frm') == sel.attr('frm')){
                        return ;
                    }

                    sel.removeClass("sel");
                    ths.addClass("sel");

                    $('#'+sel.attr('frm')).removeClass("sel");
                    $('#'+ths.attr('frm')).addClass("sel");
                });
            });

            if(_sel){
                var tmp = $('#'+_sel);
                if(tmp.length === 1){
                    tmp[0].click();
                }else{
                    elemClick($('nav a').first());
                }
            }else{
                elemClick($('nav a').first());
            }
        });
    </script>

</head>
<body>
<header>
    <flex>
        <left class="col-3"></left>
        <mid class="col-6 center">
            <nav>
                <a id="a1" frm="resource">资源管理</a>
                <a id="a2" frm="permission">权限管理</a>
            </nav>
        </mid>
        <right class="col-3">
            <aside>
                <a class="logout" href="/login"><i class="fa fa-fw fa-circle-o-notch"></i>退出</a>
            </aside>
        </right>
    </flex>
</header>
<main>
    <iframe src="./resource" id="resource"></iframe>
    <iframe src="./permission" id="permission"></iframe>
</main>
<footer>

</footer>
</body>
</html>