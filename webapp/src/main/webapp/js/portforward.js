/**
 * Created by Administrator on 14-9-26.
 */

Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';


    /*var record = new Ext.data.Record.create([
        {name: 'serverPort', mapping: 'serverPort'},
        {name: 'status', mapping: 'status'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url: "../PortForwardServlet?command=find"
    });

    var reader = new Ext.data.JsonReader({
        totalProperty: "totalCount",
        root: "root"
    }, record);

    var store = new Ext.data.GroupingStore({
        id: "store.info",
        proxy: proxy,
        reader: reader
    });

    store.load();
    store.on('load', function () {
        var serverPort = store.getAt(0).get('serverPort');
        var status = store.getAt(0).get('status');
        Ext.getCmp('port.serverPort').setValue(serverPort);
        if ("on" == status) {
            var checkbox = Ext.getCmp("port.show");
            checkbox.setValue(true);
        }
    });*/

    var grid_start = 0;
    var grid_pageSize = 15;
    var grid_record = new Ext.data.Record.create([
        {name: 'bindIp', mapping: 'bindIp'} ,
        {name: 'bindPort', mapping: 'bindPort'} ,
        {name: 'accessIp', mapping: 'accessIp'} ,
        {name: 'accessPort', mapping: 'accessPort'} ,
        {name: 'protocol', mapping: 'protocol'} ,
        {name: 'status', mapping: 'status'},
        {name: 'desc', mapping: 'desc'},
        {name: 'pId', mapping: 'pId'}
    ]);

    var grid_proxy = new Ext.data.HttpProxy({
        url: "../PortForwardServlet?command=findRule",
        timeout: 10 * 1000
    });

    var grid_reader = new Ext.data.JsonReader({
        totalProperty: "totalCount",
        root: "root"
    }, grid_record);

    var grid_store = new Ext.data.GroupingStore({
        id: "grid_store.info",
        proxy: grid_proxy,
        reader: grid_reader
    });

    grid_store.load({
        params: {
            start: grid_start, limit: grid_pageSize
        }
    });

    var grid_boxM = new Ext.grid.CheckboxSelectionModel({singleSelect: true});   //复选框单选

    var grid_rowNumber = new Ext.grid.RowNumberer();         //自动编号

    var grid_colM = new Ext.grid.ColumnModel([
        grid_boxM,
        grid_rowNumber,
        {header: "监听IP", dataIndex: "bindIp", align: 'center', sortable: true, menuDisabled: true, sort: true} ,
        {header: "监听端口", dataIndex: "bindPort", align: 'center', sortable: true, menuDisabled: true, sort: true} ,
        {header: "访问IP", dataIndex: "accessIp", align: 'center', sortable: true, menuDisabled: true, sort: true} ,
        {header: "访问端口", dataIndex: "accessPort", align: 'center', sortable: true, menuDisabled: true, sort: true} ,
        {header: "协议", dataIndex: "protocol", align: 'center', sortable: true, menuDisabled: true, sort: true} ,
        {header: "状态", dataIndex: "status", align: 'center', sortable: true, menuDisabled: true, renderer: show_status},
        {header: '操作标记', dataIndex: 'flag', align: 'center', sortable: true, menuDisabled: true, renderer: show_flag, width: 300}
    ]);

    var grid_page_toolbar = new Ext.PagingToolbar({
        pageSize: grid_pageSize,
        store: grid_store,
        displayInfo: true,
        displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg: "没有记录",
        beforePageText: "当前页",
        afterPageText: "共{0}页"
    });


    var grid_tb = new Ext.Toolbar({
        autoWidth: true,
        autoHeight: true,
        items: [
            {
                id: 'add_resource.info',
                xtype: 'button',
                text: '新增',
                iconCls: 'add',
                handler: function () {
                    add(grid_store);
                }
            }
        ]
    });



    var find_data = [
        {'id': 'Tcp', 'name': 'Tcp'},
        {'id': 'Udp', 'name': 'Udp'}
    ];

    var find_store = new Ext.data.JsonStore({
        data: find_data,
        fields: ['id', 'name']
    });

    var grid_tbar = new Ext.Toolbar({
        autoWidth: true,
        autoHeight: true,
        items: [
            '监听IP',
                new Ext.form.TextField({
                id: 'find.bindIp',
                regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                regexText:'请输入正确的IP地址',
                allowBlank : false,
                blankText : "不能为空，请正确填写"
            }), '监听端口',
            new Ext.form.TextField({
                id: 'find.bindPort',
                regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                regexText:'请输入正确的端口',
                allowBlank : false,
                blankText : "不能为空，请正确填写"
            }),  '协议',
            new Ext.form.ComboBox({
                border: true,
                frame: true,
                editable: false,
                fieldLabel: '协议',
                id: 'modify.protocol',
                triggerAction: "all",// 是否开启自动查询功能
                store: find_store,// 定义数据源
                displayField: "name", // 关联某一个逻辑列名作为显示值
                valueField: "id", // 关联某一个逻辑列名作为显示值
                mode: "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
//                name: 'protocol',
//                hiddenName: 'protocol',
                id:'find.protocol',
                allowBlank: false,
                blankText: "请选择"
            }),  {
                id: 'tb.tbar.info',
                xtype: 'button',
                iconCls: 'select',
                text: '查询',
                handler: function () {
                    var bindIp = Ext.getCmp('find.bindIp').getValue();
                    var bindPort = Ext.getCmp('find.bindPort').getValue();
                    var protocol = Ext.getCmp('find.protocol').getValue();
                    grid_store.setBaseParam('bindIp', bindIp.trim());
                    grid_store.setBaseParam('bindPort', bindPort.trim());
                    grid_store.setBaseParam('protocol', protocol.trim());
                    grid_store.load({
                        params: {
                            start: grid_start,
                            limit: grid_pageSize
                        }
                    });
                }
            }]
    });

    var grid_panel = new Ext.grid.GridPanel({
        id: 'grid.info',
        plain: true,
        autoHeight: true,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: true,
        cm: grid_colM,
        sm: grid_boxM,
        store: grid_store,
        tbar: grid_tb,
        listeners: {
            render: function () {
                grid_tbar.render(this.tbar);
            }
        },
        bbar: grid_page_toolbar
    });


    var formPanel = new Ext.form.FormPanel({
        plain: true,
        labelAlign: 'right',
        labelWidth: 150,
        autoScroll: true,
        defaultType: 'textfield',
        defaults: {
            anchor: '95%',
            allowBlank: false,
            blankText: '该项不能为空!'
        },
        items: [
            {
                fieldLabel: '转发服务端口',
                id: 'port.serverPort',
                name: 'serverPort',
                regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                regexText:'这个不是端口类型1~65536',
                emptyText:'请输入端口1~65536'
            },
            new Ext.form.Checkbox({
                id: "port.show",
                name: "show",
                boxLabel: "开启端口映射",
                listeners: {
                    "check": function (obj, ischecked) {
                        if (ischecked) {
                            var serverPort = Ext.getCmp("port.serverPort");
                            Ext.Ajax.request({
                                url: "../PortForwardServlet",
                                method: "POST",
                                waitTitle: '信息',
                                waitMsg: '正在开启服务,请稍后...',
                                params: {command: "start", serverPort: serverPort},
                                success: function (response, options) {
                                    var reText = Ext.util.JSON.decode(response.responseText);
                                    var flag = reText.msg;
                                    Ext.MessageBox.show({
                                        title: '信息',
                                        msg: flag,
                                        width: 250,
                                        buttons: {'ok': '确定'},
                                        icon: Ext.MessageBox.INFO,
                                        closable: false
                                    });
                                },
                                failure: function (response, options) {
                                    var reText = Ext.util.JSON.decode(response.responseText);
                                    var flag = reText.msg;
                                    Ext.MessageBox.show({
                                        title: '信息',
                                        msg: flag,
                                        width: 250,
                                        buttons: {'ok': '确定'},
                                        icon: Ext.MessageBox.INFO,
                                        closable: false
                                    });
                                }
                            });
                        }
                        else {
                            var serverPort = Ext.getCmp("port.serverPort");
                            Ext.Ajax.request({
                                url: "../PortForwardServlet",
                                method: "POST",
                                waitTitle: '信息',
                                waitMsg: '正在关闭服务,请稍后...',
                                params: {command: "stop", serverPort: serverPort},
                                success: function (response, options) {
                                    var reText = Ext.util.JSON.decode(response.responseText);
                                    var flag = reText.msg;
                                    Ext.MessageBox.show({
                                        title: '信息',
                                        msg: flag,
                                        width: 250,
                                        buttons: {'ok': '确定'},
                                        icon: Ext.MessageBox.INFO,
                                        closable: false
                                    });
                                },
                                failure: function (response, options) {
                                    var reText = Ext.util.JSON.decode(response.responseText);
                                    var flag = reText.msg;
                                    Ext.MessageBox.show({
                                        title: '信息',
                                        msg: flag,
                                        width: 250,
                                        buttons: {'ok': '确定'},
                                        icon: Ext.MessageBox.INFO,
                                        closable: false
                                    });
                                }
                            });
                        }
                    }}
            })
        ],
        buttons: [
            {
                id: 'port.save.info',
                text: '保存',
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: '../PortForwardServlet',
                            params: {command: "save"},
                            method: 'POST',
                            waitTitle: '信息',
                            waitMsg: '正在保存,请稍后...',
                            success: function (form, action) {
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    msg: flag,
                                    animEl: 'port.save.info',
                                    width: 250,
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false
                                });
                            },
                            failure: function (form, action) {
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    msg: flag,
                                    animEl: 'port.save.info',
                                    width: 250,
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false
                                });
                            }
                        });
                    }
                }
            },{
                id: 'port.reset.info',
                text: '重置',
                handler: function () {
                    formPanel.getForm().reset();
                }
            }
        ]
    });

    var panel_port = new Ext.Panel({
        frame: true,
        renderTo: "content",
        plain: true,
        border: true,
        height: setHeight(),
        autoScroll: true,
        items: [
            {
                xtype: 'fieldset',
                title: '端口转发',
                items: [
                    /*{
                        xtype: 'fieldset',
                        title: '转发服务',
                        items: [formPanel]
                    } ,*/
                    {
                        xtype: 'fieldset',
                        title: '转发规则',
                        items: [grid_panel]
                    }]
            }
        ]
    });
});

function setHeight() {
    return document.getElementById("content").offsetHeight;
}


function show_flag(value, p, r) {
    if (r.get("status") == "0") {
        return String.format(
                '<a id="del.info" href="javascript:void(0);" onclick="del();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;' +
                '<a id="update.info" href="javascript:void(0);" onclick="update();return false;" style="color: green;">更新</a>&nbsp;&nbsp;&nbsp;' +
                '<a id="start.info" href="javascript:void(0);" onclick="start();return false;" style="color: green;">启用</a>&nbsp;&nbsp;&nbsp;'
        );
    } else if (r.get("status") == "1") {
        return String.format(
                '<a id="del.info" href="javascript:void(0);" onclick="del();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;' +
                '<a id="update.info" href="javascript:void(0);" onclick="update();return false;" style="color: green;">更新</a>&nbsp;&nbsp;&nbsp;' +
                '<a id="stop.info" href="javascript:void(0);" onclick="stop();return false;" style="color: green;">停用</a>&nbsp;&nbsp;&nbsp;'
        );
    }
};

function show_status(value, p, r) {
    if (r.get("status") == "1") {
        return String.format('<img src="../img/png/ok.png" alt="已启用" title="已启用" />');
    } else if (r.get("status") == "0") {
        return String.format('<img src="../img/png/off.png" alt="未启用" title="未启用" />');
    }
}

function del() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var bindIp = recode.get("bindIp");
    var bindPort = recode.get("bindPort");
    var protocol = recode.get("protocol");
    var pId = recode.get("pId");
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确认删除吗？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: "../PortForwardServlet",
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    params: {command: "delRule",
                        bindIp: bindIp,
                        bindPort: bindPort,
                        protocol:protocol,
                        pId: pId
                    },
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 250,
                            msg: msg,
                            buttons: Ext.MessageBox.OK,
                            buttons: {'ok': '确定'},
                            fn:function(e){
                                grid_panel.getStore().reload();
                            },
                            icon: Ext.MessageBox.INFO,
                            closable: false
                        });
                    },
                    failure: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 250,
                            msg: msg,
                            buttons: Ext.MessageBox.OK,
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.INFO,
                            closable: false
                        });
                    }
                });
            }
        });
    }
}

function update() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();

    var oldBindIp = recode.get("bindIp");
    var oldBindPort = recode.get("bindPort");
    var oldProtocol = recode.get("protocol");
    var pId = recode.get("pId");
    var protocol_data = [
        {'id': 'Tcp', 'name': 'Tcp'},
        {'id': 'Udp', 'name': 'Udp'}
    ];

    var protocol_store = new Ext.data.JsonStore({
        data: protocol_data,
        fields: ['id', 'name']
    });

    var formPanel = new Ext.form.FormPanel({
        frame: true,
        autoScroll: true,
        labelWidth: 150,
        labelAlign: 'right',
        defaultWidth: 300,
        autoWidth: true,
        layout: 'form',
        border: false,
        defaults: {
            width: 250,
            allowBlank: false,
            blankText: '该项不能为空！'
        },
        items: [
            new Ext.form.TextField({
                fieldLabel: '监听IP',
                name: 'bindIp',
                id: 'modify.bindIp',
                value: recode.get("bindIp"),
                emptyText: "请输入正确的Ip地址",
                regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                regexText:'请输入正确的IP地址',
                allowBlank : false,
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel: '监听端口',
                name: 'bindPort',
                id: 'modify.bindPort',
                value: recode.get("bindPort"),
                emptyText: "请输入正确的端口",
                regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                regexText:'请输入正确的端口',
                allowBlank : false,
                blankText : "不能为空，请正确填写",
                listeners:{
                    blur:function () {
                        var value = this.getValue();
                        if((0<=value&&value<=1024)||value==8080||value == 7505||value>=65535){
                            Ext.MessageBox.show({
                                title:'提示',
                                width:400,
                                msg:'0-1024端口可能被系统占用,端口不能为系统服务端口8080,7505,且端口不能大于65535!',
                                buttons:Ext.MessageBox.OK,
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.INFO,
                                closable:false,
                                fn:function(e){
                                    if(e=='ok'){
                                        this.setValue('');
                                    }
                                }
                            });
                        }
                    }
                }
            }),
            new Ext.form.TextField({
                fieldLabel: '访问IP',
                name: 'accessIp',
                id: 'modify.accessIp',
                value: recode.get("accessIp"),
                emptyText: "请输入正确的Ip地址",
                regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                regexText:'请输入正确的IP地址',
                allowBlank : false,
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel: '访问端口',
                name: 'accessPort',
                value: recode.get("accessPort"),
                id: 'modify.accessPort',
                emptyText: "请输入正确的端口",
                regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                regexText:'请输入正确的端口',
                allowBlank : false,
                blankText : "不能为空，请正确填写"
            }),

            new Ext.form.ComboBox({
                border: true,
                frame: true,
                editable: false,
                fieldLabel: '协议',
                id: 'modify.protocol',
                value: recode.get("protocol"),
                triggerAction: "all",// 是否开启自动查询功能
                store: protocol_store,// 定义数据源
                displayField: "name", // 关联某一个逻辑列名作为显示值
                valueField: "id", // 关联某一个逻辑列名作为显示值
                mode: "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
                name: 'protocol',
                hiddenName: 'protocol',
                allowBlank: false,
                blankText: "请选择"
            }),
            new Ext.form.TextField({
                fieldLabel: '描述',
                name: 'desc',
                value: recode.get("desc"),
                id: 'modify.desc',
                allowBlank: false,
                emptyText: "请输入规则描述",
                blankText: "不能为空，请正确填写"
            })
        ]
    });
    var win = new Ext.Window({
        title: "更新",
        width: 500,
        layout: 'fit',
        height: 340,
        modal: true,
        items: formPanel,
        bbar: [
            '->',
            {
                id: 'modify_win.info',
                text: '更新',
                width: 50,
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: '../PortForwardServlet',
                            params: {
                                command: "modifyRule",
                                oldBindIp:oldBindIp,
                                oldBindPort:oldBindPort,
                                oldProtocol:oldProtocol,
                                pId:pId
                            },
                            timeout: 20 * 60 * 1000,
                            method: 'POST',
                            waitTitle: '系统提示',
                            waitMsg: '正在更新...',
                            success: function (form, action) {
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    msg: flag,
                                    animEl: 'wifi.modify.info',
                                    width: 250,
                                    buttons: {'ok': '确定'},
                                    fn:function(e){
                                        win.close();
                                        grid_panel.getStore().reload();
                                    },
                                    icon: Ext.MessageBox.INFO,
                                    closable: false
                                });
                            },
                            failure: function (form, action) {
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    msg: flag,
                                    animEl: 'wifi.modify.info',
                                    width: 250,
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 200,
                            msg: '请填写完成再提交!',
                            buttons: Ext.MessageBox.OK,
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.ERROR,
                            closable: false
                        });
                    }
                }
            }, {
                text: '重置',
                width: 50,
                handler: function () {
                    formPanel.getForm().reset();
                }
            }
        ]
    }).show();
}

function stop() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var bindIp = recode.get("bindIp");
    var bindPort = recode.get("bindPort");
    var protocol = recode.get("protocol");
    var pId = recode.get("pId");
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确认停用吗？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: "../PortForwardServlet",
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    params: {command: "disableRule",
                        bindIp: bindIp,
                        bindPort: bindPort,
                        protocol:protocol,
                        pId: pId
                    },
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 250,
                            msg: msg,
                            buttons: Ext.MessageBox.OK,
                            buttons: {'ok': '确定'},
                            fn:function(e){
                                grid_panel.getStore().reload();
                            },
                            icon: Ext.MessageBox.INFO,
                            closable: false
                        });
                    },
                    failure: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 250,
                            msg: msg,
                            buttons: Ext.MessageBox.OK,
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.INFO,
                            closable: false
                        });
                    }
                });
            }
        });
    }
}

function start() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var bindIp = recode.get("bindIp");
    var bindPort = recode.get("bindPort");
    var accessIp = recode.get("accessIp");
    var accessPort = recode.get("accessPort");
    var protocol = recode.get("protocol");
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确认开启吗？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: "../PortForwardServlet",
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    params: {command: "enableRule",
                        bindIp: bindIp,
                        bindPort: bindPort,
                        accessIp: accessIp,
                        accessPort: accessPort,
                        protocol: protocol
                    },
                    success: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 250,
                            msg: msg,
                            buttons: Ext.MessageBox.OK,
                            buttons: {'ok': '确定'},
                            fn:function(e){
                                grid_panel.getStore().reload();
                            },
                            icon: Ext.MessageBox.INFO,
                            closable: false
                        });
                    },
                    failure: function (r, o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 250,
                            msg: msg,
                            buttons: Ext.MessageBox.OK,
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.INFO,
                            closable: false
                        });
                    }
                });
            }
        });
    }
}

function add(store) {

    var protocol_data = [
        {'id': 'Tcp', 'name': 'Tcp'},
        {'id': 'Udp', 'name': 'Udp'}
    ];

    var protocol_store = new Ext.data.JsonStore({
        data: protocol_data,
        fields: ['id', 'name']
    });

    var formPanel = new Ext.form.FormPanel({
        frame: true,
        autoScroll: true,
        labelWidth: 150,
        labelAlign: 'right',
        defaultWidth: 300,
        autoWidth: true,
        layout: 'form',
        border: false,
        defaults: {
            width: 250,
            allowBlank: false,
            blankText: '该项不能为空！'
        },
        items: [
            new Ext.form.TextField({
                fieldLabel: '监听IP',
                name: 'bindIp',
                id: 'add.bindIp',
                emptyText: "请输入正确的Ip地址",
                regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                regexText:'请输入正确的IP地址',
                allowBlank : false,
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel: '监听端口',
                name: 'bindPort',
                id: 'add.bindPort',
                emptyText: "请输入正确的端口",
                regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                regexText:'请输入正确的端口',
                allowBlank : false,
                blankText : "不能为空，请正确填写",
                listeners:{
                    blur:function () {
                        var value = this.getValue();
                        if((0<=value&&value<=1024)||value==8080||value == 7505||value>=65535){
                            Ext.MessageBox.show({
                                title:'提示',
                                width:400,
                                msg:'0-1024端口可能被系统占用,端口不能为系统服务端口8080,7505,且端口不能大于65535!',
                                buttons:Ext.MessageBox.OK,
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.INFO,
                                closable:false,
                                fn:function(e){
                                    if(e=='ok'){
                                        this.setValue('');
                                    }
                                }
                            });
                        }
                    }
                }
            }),
            new Ext.form.TextField({
                fieldLabel: '访问IP',
                name: 'accessIp',
                id: 'add.accessIp',
                emptyText: "请输入正确的Ip地址",
                regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                regexText:'请输入正确的IP地址',
                allowBlank : false,
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel: '访问端口',
                name: 'accessPort',
                id: 'add.accessPort',
                emptyText: "请输入正确的端口",
                regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                regexText:'请输入正确的端口',
                allowBlank : false,
                blankText : "不能为空，请正确填写"
            }),

            new Ext.form.ComboBox({
                border: true,
                frame: true,
                editable: false,
                fieldLabel: '协议',
                id: 'add.protocol',
                triggerAction: "all",// 是否开启自动查询功能
                store: protocol_store,// 定义数据源
                displayField: "name", // 关联某一个逻辑列名作为显示值
                valueField: "id", // 关联某一个逻辑列名作为显示值
                mode: "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
                name: 'protocol',
                hiddenName: 'protocol',
                allowBlank: false,
                blankText: "请选择"
            }),
            new Ext.form.TextField({
                fieldLabel: '描述',
                name: 'desc',
                id: 'add.desc',
                allowBlank: false,
                emptyText: "请输入规则描述",
                blankText: "不能为空，请正确填写"
            })
        ]
    });
    var win = new Ext.Window({
        title: "新增",
        width: 500,
        layout: 'fit',
        height: 340,
        modal: true,
        items: formPanel,
        bbar: [
            '->',
            {
                id: 'add_win.info',
                text: '新增',
                width: 50,
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: '../PortForwardServlet',
                            params: {command: "addRule"},
                            timeout: 20 * 60 * 1000,
                            method: 'POST',
                            waitTitle: '系统提示',
                            waitMsg: '正在保存...',
                            success: function (form, action) {
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    msg: flag,
                                    animEl: 'wifi.save.info',
                                    width: 250,
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false,
                                    fn:function(e){
                                        store.reload();
                                        win.close();
                                    }
                                });
                            },
                            failure: function (form, action) {
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    msg: flag,
                                    animEl: 'wifi.save.info',
                                    width: 250,
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.INFO,
                                    closable: false
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title: '信息',
                            width: 200,
                            msg: '请填写完成再提交!',
                            buttons: Ext.MessageBox.OK,
                            buttons: {'ok': '确定'},
                            icon: Ext.MessageBox.ERROR,
                            closable: false
                        });
                    }
                }
            }, {
                text: '重置',
                width: 50,
                handler: function () {
                    formPanel.getForm().reset();
                }
            }
        ]
    }).show();
}