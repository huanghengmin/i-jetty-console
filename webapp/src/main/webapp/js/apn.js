/**
 * Created by Administrator on 14-9-26.
 */

Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 15;
    var record = new Ext.data.Record.create([
        {name: 'id', mapping: 'id'},
        {name: 'apn', mapping: 'apn'},
        {name: 'type', mapping: 'type'},
        {name: 'name', mapping: 'name'},
        {name: 'numeric', mapping: 'numeric'},
        {name: 'mcc', mapping: 'mcc'},
        {name: 'mnc', mapping: 'mnc'},
        {name: 'proxy', mapping: 'proxy'},
        {name: 'port', mapping: 'port'},
        {name: 'mmsproxy', mapping: 'mmsproxy'},
        {name: 'mmsport', mapping: 'mmsport'},
        {name: 'user', mapping: 'user'},
        {name: 'password', mapping: 'password'},
        {name: 'server', mapping: 'server'},
        {name: 'mmsc', mapping: 'mmsc'}/*,
        {name: 'authtype', mapping: 'authtype'}*/
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../ApnServlet?command=findAllApn"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty: "totalCount",
        root: "rows"
    }, record);
    var store_grid = new Ext.data.GroupingStore({
        id: "store_grid.info",
        proxy: proxy,
        reader: reader
    });
    store_grid.load({
        params: {
            start: start, limit: pageSize
        }
    });

    var tbar = new Ext.Toolbar({
        autoWidth: true,
        autoHeight: true,
        items: [
            {
                id: 'tbar.add.info',
                xtype: 'button',
                iconCls: 'add',
                text: '添加',
                handler: function () {
                    add(store_grid);
                }
            }
        ]
    });

    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        rowNumber,
        {header: '名称', dataIndex: 'name', align: 'center', sortable: true, menuDisabled: true},
        {header: "APN", dataIndex: "apn", align: 'center', sortable: true, menuDisabled: true},
        {header: "代理服务器", dataIndex: "proxy", align: 'center', sortable: true, menuDisabled: true},
        {header: "端口", dataIndex: "port", align: 'center', sortable: true, menuDisabled: true},
        //{header: "用户名", dataIndex: "user", align: 'center', sortable: true, menuDisabled: true},
        //{header: "密码", dataIndex: "password", align: 'center', sortable: true, menuDisabled: true},
        //{header: "服务器", dataIndex: "server", align: 'center', sortable: true, menuDisabled: true},
        {header: "MMSC", dataIndex: "mmsc", align: 'center', sortable: true, menuDisabled: true},
        {header: "MMS代理服务器", dataIndex: "mmsproxy", align: 'center', sortable: true, menuDisabled: true},
        {header: "MMS端口", dataIndex: "mmsport", align: 'center', sortable: true, menuDisabled: true},
        {header: "移动国家代码", dataIndex: "mcc", align: 'center', sortable: true, menuDisabled: true},
        {header: "移动网络代码", dataIndex: "mnc", align: 'center', sortable: true, menuDisabled: true},
        //{header: "认证类型", dataIndex: "authtype", align: 'center', sortable: true, menuDisabled: true},
        {header: "APN类型", dataIndex: "type", align: 'center', sortable: true, menuDisabled: true},
        {header: "号码", dataIndex: "numeric", align: 'center', sortable: true, menuDisabled: true},
        {header: "状态", dataIndex: "default", align: 'center', sortable: true, menuDisabled: true, renderer: show_status},
        {header: '操作标记', dataIndex: 'flag', align: 'center', sortable: true, menuDisabled: true, renderer: show_flag, width: 300}
    ]);
    var page_toolbar = new Ext.PagingToolbar({
        pageSize: pageSize,
        store: store_grid,
        displayInfo: true,
        displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg: "没有记录",
        beforePageText: "当前页",
        afterPageText: "共{0}页"
    });

    var grid_panel = new Ext.grid.GridPanel({
        id: 'grid.info',
        title: 'APN列表',
        plain: true,
        autoHeight: true,
        animCollapse: true,
        loadMask: {msg: '正在加载数据，请稍后...'},
        border: true,
        collapsible: false,
        cm: colM,
        tbar:tbar,
        store: store_grid,
        stripeRows: true,
        autoExpandColumn: 'Position',
        disableSelection: true,
        bodyStyle: 'width:100%',
        enableDragDrop: true,
        selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
        viewConfig: {
            forceFit: true,
            enableRowBody: true,
            getRowClass: function (record, rowIndex, p, store) {
                return 'x-grid3-row-collapsed';
            }
        },
        view: new Ext.grid.GroupingView({
            forceFit: true,
            groupingTextTpl: '{text}({[values.rs.length]}条记录)'
        }),
        bbar: page_toolbar
    });

    var panel_check = new Ext.Panel({
        frame: true,
        renderTo: "content",
        plain: true,
        border: true,
        height: setHeight(),
        autoScroll: true,
        items: [{
            xtype: 'fieldset',
            title: 'APN列表',
            labelWidth: 150,
            defaults: {
                anchor: '100%',
                allowBlank: false,
                blankText: '该项不能为空!'
            },
            items: [grid_panel]
        }]
    });
});

function setHeight() {
    return document.getElementById("content").offsetHeight;
}

function add(store) {
    var apnType_data = [
        {'id': 'default,mms', 'name': 'default,mms'},
        {'id': 'default', 'name': 'default'},
        {'id': 'mms', 'name': 'mms'}
    ];

    var apnType_store = new Ext.data.JsonStore({
        data: apnType_data,
        fields: ['id', 'name']
    });

    var formPanel = new Ext.form.FormPanel({
        frame: true,
        autoScroll: true,
        labelWidth: 100,
        labelAlign: 'right',
        defaultWidth: 450,
        autoWidth: true,
        layout: 'form',
        border: false,
        defaults: {
            width: 450,
            anchor: '95%'
        },
        items: [
            new Ext.form.TextField({
                fieldLabel: '名称',
                name: 'name',
                regex: /^[a-zA-Z0-9\u4e00-\u9fa5]+$/,
                regexText: '只能输入数字,字母,中文!',
                id: 'add_apn_name',
                allowBlank: false,
                blankText: "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel: 'APN',
                id: 'add_apn_apn',
                name: 'apn',
                allowBlank: false,
                blankText: "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel: '代理服务器',
                name: 'proxy',
                id: 'add_apn_proxy'
            }),
            new Ext.form.TextField({
                fieldLabel: '端口',
                name: 'port',
                id: 'add_apn_port'
            }),
            new Ext.form.TextField({
                fieldLabel: '用户名',
                name: 'user',
                id: 'add_apn_user'
            }),
            new Ext.form.TextField({
                fieldLabel: '密码',
                name: 'password',
                id: 'add_apn_password'
            }),
            new Ext.form.TextField({
                fieldLabel: '服务器',
                name: 'server',
                id: 'add_apn_server'
            }),
            new Ext.form.TextField({
                fieldLabel: 'MMSC',
                name: 'mmsc',
                id: 'add_apn_mmsc'
            }),
            new Ext.form.TextField({
                fieldLabel: 'MMS代理服务器',
                name: 'mmsproxy',
                id: 'add_apn_mmsproxy'
            }),
            new Ext.form.TextField({
                fieldLabel: 'MMS端口',
                name: 'mmsport',
                id: 'add_apn_mmsport'
            }),
            new Ext.form.TextField({
                fieldLabel: '移动国家代码',
                name: 'mcc',
                id: 'add_apn_mcc'
            }),
            new Ext.form.TextField({
                fieldLabel: '移动网络代码',
                name: 'mnc',
                id: 'add_apn_mnc'
            }),
            new Ext.form.ComboBox({
                border: true,
                frame: true,
                editable: false,
                fieldLabel: 'APN类型',
                id: 'add_apn_type',
                triggerAction: "all",// 是否开启自动查询功能
                store: apnType_store,// 定义数据源
                displayField: "name", // 关联某一个逻辑列名作为显示值
                valueField: "id", // 关联某一个逻辑列名作为显示值
                mode: "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
                name: 'type',
                //hiddenName: 'type',
                //allowBlank: false,
                blankText: "请选择APN类型"
            })
        ]
    });
    var win = new Ext.Window({
        title: "添加APN",
        width: 650,
        layout: 'fit',
        height: 450,
        modal: true,
        items: formPanel,
        bbar: [
            '->',
            {
                id: 'insert_win.info',
                text: '添加APN',
                handler: function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url: '../ApnServlet',
                            params: {command: "addApn"},
                            timeout: 20 * 60 * 1000,
                            method: 'POST',
                            waitTitle: '系统提示',
                            waitMsg: '正在连接...',
                            success: function (form, action) {
                                var json = Ext.decode(action.response.responseText);
                                var msg = json.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    buttons: Ext.MessageBox.OK,
                                    buttons: {'ok': '确定'},
                                    fn:function(e){
                                        store.reload();
                                        win.close();
                                    },
                                    icon: Ext.MessageBox.OK,
                                    closable: false
                                });
                            },
                            failure: function (form, action) {
                                var json = Ext.decode(action.response.responseText);
                                var msg = json.msg;
                                Ext.MessageBox.show({
                                    title: '信息',
                                    width: 250,
                                    msg: msg,
                                    buttons: Ext.MessageBox.OK,
                                    buttons: {'ok': '确定'},
                                    icon: Ext.MessageBox.ERROR,
                                    closable: false
                                });
                            }
                        });
                    }
                }
            }, {
                text: '重置',
                handler: function () {
                    formPanel.getForm().reset();
                }
            }
        ]
    }).show();
}


function show_flag(value, p, r) {
    return String.format(
        '<a id="del.info" href="javascript:void(0);" onclick="del();return false;" style="color: green;">删除</a>&nbsp;&nbsp;&nbsp;' +
        '<a id="start.info" href="javascript:void(0);" onclick="defaultApn();return false;" style="color: green;">启用默认APN</a>&nbsp;&nbsp;&nbsp;'
    );
};


function show_status(value, p, r) {
    if (r.get("default") == "1") {
        return String.format('<img src="../img/png/ok.png" alt="已启用" title="已启用" />');
    } else if (r.get("default") == "0") {
        return String.format('<img src="../img/png/off.png" alt="未启用" title="未启用" />');
    }
}

function del() {
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var id = recode.get("id");
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "确认删除吗？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: "../ApnServlet",
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    params: {command: "delApn", id: id},
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


function defaultApn(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var id = recode.get("id");
    if (!recode) {
        Ext.Msg.alert("提示", "请选择一条记录!");
    } else {
        Ext.Msg.confirm("提示", "设置此APN为默认APN？", function (sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url: "../ApnServlet",
                    timeout: 20 * 60 * 1000,
                    method: "POST",
                    params: {command: "defaultApn", id: id},
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