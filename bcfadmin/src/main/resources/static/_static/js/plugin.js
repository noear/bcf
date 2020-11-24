/* bcf jq 插件库*/

/**
 * 右键菜单jquery插件
 * by - wjn
 * date - 20190506
 */
;(function($){

    function ContextMenu($element, options) {
        this.$element=$element
        this._defaults = {
            isRightActive: true,
            menus: [
                {
                    name: "新建资源1",
                    click: function () {
                        alert('23')
                    }
                },
                {
                    name: "新建资源2",
                    click: function () {
                        alert('46')
                    }
                }
            ]
        }
        this.options = $.extend({}, this._defaults, options || {});
        this.init();

    }
    ContextMenu.prototype = {
        // 初始化
        init: function () {
            var op = this.options;
            this.createMenu(op);
            this.bindEvent(op);
            this.createStyle();
            this.aopContextMenu(op);
        },
        //生产右键菜单dom
        createMenu: function (options) {
            var id = "menu" + ($('.menus').length + 2);

            this.tempId=id;

            var clsLength = $(".menu").length + 2;
            var menus = options.menus;
            var menuDoms = '';
            var menuDom = '';
            for (var i = 0;i < menus.length;i++) {
                menuDom += '<div class="menu" id="menuChild'+ (clsLength + i) +'">'+ menus[i].name +'</div>';
            }
            menuDoms = '<div class="menus" id="'+ id +'">' + menuDom + '</div>';
            $('body').append(menuDoms);
        },
        // // 生成样式
        createStyle: function() {
            var style = document.createElement("style")
            style.innerHTML = '#'+ this.tempId + '{height: '+ 25 * this.options.menus.length +'px;}.menus {\n' +
                '            width: 125px;\n' +
                '            height: 125px;\n' +
                '            overflow: hidden;\n' +
                '            box-shadow: 0 1px 1px #888, 1px 0 1px #ccc;\n' +
                '            box-sizing: border-box;\n' +
                '            position: fixed;\n' +
                '            background-color: white;\n' +
                '            user-select: none;\n' +
                '            z-index: 9999;\n' +
                '            display: none;\n' +
                '        }.menu {\n' +
                '            width: 125px;\n' +
                '            height: 25px;\n' +
                '            line-height: 25px;\n' +
                '            padding: 0 10px;\n' +
                '        }\n' +
                '\n' +
                '        .menu:hover {\n' +
                '            background-color: #ececec;\n' +
                '        }';
            document.body.appendChild(style);
        },
        // 绑定菜单事件
        bindEvent: function (options) {
            var parentDom = $('#' + this.tempId);
            parentDom.on('click', '.menu', function () {
                var index = $(this).index();
                options.menus[index].click();
                parentDom.hide()
            })
        },
        //截获右键事件
        aopContextMenu: function (options) {
            var $el = $(this.$element),
                $win = $(window),
                that = this;
            $el.on('contextmenu', function (e) {
                e.preventDefault();
                if ($el.find(".sel").length <= 1) {
                    var hoverTr = $(e.target).find("tr");
                    hoverTr.addClass("sel");
                    hoverTr.siblings().removeClass('sel');
                }
                var menu = $('#' + that.tempId)[0];

                var bodyHeight = document.body.offsetHeight;
                var bodyWidth = document.body.offsetWidth;
                menu.style.left = e.clientX + 'px';
                menu.style.top = e.clientY + 'px';
                console.log(e.clientX)
                if (e.clientY + 25 * options.menus.length > bodyHeight) {
                    menu.style.top = e.clientY - 25 * options.menus.length + 'px';
                }
                if (e.clientX + 125 > bodyWidth) {
                    menu.style.left = e.clientX - 125 + 'px';
                }

                menu.style.display = 'block';
            })

            $win.on('click', function () {
                $('#' + that.tempId).hide()
            })
        }
    }

    $.fn.contextmenu = function(options){
        return new ContextMenu(this[0], options);
    }
})(jQuery);


/**
 * 列表仿window无checkbox多选
 */
;(function ($) {

    function Selects($element, options, callback) {
        this.$element = $element
        this._defaults = {
            // 被选中的子元素标识class类名
            activeClass: 'sel',
            // 要多选的子元素标签类型，eg: tr, div, li
            childrenTag: 'tr',
            // 要被保存的id，eg: rsid, pgid
            sessionType: 'rsid'
        }
        this.callback = callback;
        this.options = $.extend({}, this._defaults, options || {});
        this.init();
    }
    Selects.prototype = {
        // 初始化
        init: function () {
            this.shift(this.callback)
        },
        getSelectedTrs: function () {
            var arr = []
            if (!$(this.$element).find('.sel').length) {
                return [];
            }
            $(this.$element).find('.sel').each(function () {
                arr.push($(this).attr('puid'))
            });
            return arr;
        },
        // shift键绑定
        shift: function (callback) {
            // 修改样式文字不可选
            this.$element.style.userSelect = 'none';
            var $el = $(this.$element),
                othis = this;
            $el.on('click', this.options.childrenTag, function (ev) {
                var currentDom = $(ev.currentTarget).closest(othis.options.childrenTag);
                var shiftArr = [];
                var ids = [];

                if (ev.ctrlKey || ev.metaKey) {
                    othis.tempTr = $(currentDom);
                    othis.tempShift = {};
                    $(currentDom).toggleClass(othis.options.activeClass);
                    $el.find(othis.options.childrenTag + "." + othis.options.activeClass).each(function (val) {
                        ids.push($(this).attr(othis.options.sessionType));
                    })

                    // 触发回调函数

                    callback(othis.getSelectedTrs())
                    return;
                }

                if (othis.tempTr) {
                    if (ev.shiftKey) {
                        var preIndex = othis.tempTr.index() <= $(currentDom).index() ? othis.tempTr.index() : $(currentDom).index();
                        var nextIndex = othis.tempTr.index() <= $(currentDom).index() ? $(currentDom).index() + 1 : othis.tempTr.index();
                        var diffArr = [];

                        var selectedTr = $el.find(othis.options.childrenTag).slice(preIndex, nextIndex);

                        selectedTr.each(function () {
                            shiftArr.push($(this).attr(othis.options.sessionType));
                        });

                        if (othis.tempShift && othis.tempShift[preIndex]) {
                            console.log('getArrDiff: ' + othis.getArrDiff(shiftArr, othis.tempShift[preIndex]))


                            diffArr = othis.getArrDiff(shiftArr, othis.tempShift[preIndex]);


                            if (othis.tempShift[preIndex].length >= shiftArr.length) {
                                // 删减元素
                                diffArr.forEach(function (val) {
                                    $el.find(othis.options.childrenTag+'['+ othis.options.sessionType + '=' + val +']').removeClass(othis.options.activeClass);
                                });
                                othis.tempShift[preIndex] = othis.deepClone(shiftArr);

                            } else {
                                // 增加元素
                                diffArr.forEach(function (val) {
                                    $el.find(othis.options.childrenTag+'['+ othis.options.sessionType + '=' + val +']').addClass(othis.options.activeClass);
                                });
                                othis.tempShift[preIndex] = othis.deepClone(shiftArr);

                            }

                        } else {
                            selectedTr.addClass(othis.options.activeClass);

                            othis.tempShift[preIndex] = othis.deepClone(shiftArr);
                        }
                        $el.find(othis.options.childrenTag + "." + othis.options.activeClass).each(function (val) {
                            ids.push($(this).attr(othis.options.sessionType));
                        })

                        // 触发回调函数
                        callback(othis.getSelectedTrs())

                        return;
                    }
                }


                othis.tempShift = {};
                othis.tempTr = $(currentDom);
                console.log(currentDom)
                $(currentDom).siblings().removeClass(othis.options.activeClass);
                $(currentDom).addClass(othis.options.activeClass)


                $el.find(othis.options.childrenTag + "." + othis.options.activeClass).each(function (val) {
                    ids.push($(this).attr(othis.options.sessionType));
                })

                // 触发回调函数
                callback(othis.getSelectedTrs())

            })
        },
        /**
         *  返回两个数组不同的元素
         * @param arr1
         * @param arr2
         * @returns {T[]}
         */
        getArrDiff: function (arr1, arr2) {

            return arr1.concat(arr2).filter(function (v, i, arr) {

                return arr.indexOf(v) === arr.lastIndexOf(v);

            });

        },

        /**
         * 深克隆
         * @param obj
         * @returns {any}
         */
        deepClone: function (obj) {
            var _tmp, result;
            _tmp = JSON.stringify(obj);
            result = JSON.parse(_tmp);
            return result;
        }
    }

    $.fn.selects = function(options, callback){
        return new Selects(this[0], options, callback);
    }
})(jQuery)