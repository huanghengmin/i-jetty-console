/**
 * Created by Administrator on 14-9-26.
 */

Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var formPanel = new Ext.form.FormPanel({
        plain: true,
        labelAlign: 'right',
        labelWidth: 150,
        defaultType: 'textfield',
        defaults: {
            anchor: '100%',
            allowBlank: false,
            blankText: '该项不能为空!'
        },
        items: [
            {
                xtype: 'fieldset',
                collapsible: true,
                title: '重启设备',
                defaultType: 'textfield',
                labelWidth: 150,
                defaults: {
                    anchor: '100%',
                    allowBlank: false,
                    blankText: '该项不能为空!'
                },
                items: [
                    {
                        xtype: 'button',
                        text: '重启设备',
                        id: 'system.reboot',
                        name: 'system_reboot',
                        handler: function () {
                            Ext.Ajax.request({
                                url: "../SystemManagerServlet",
                                method: "POST",
                                params: {command: "reboot"},
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
                    }
                ]
            },
            {
                xtype: 'fieldset',
                collapsible: true,
                title: '关闭设备',
                defaultType: 'textfield',
                labelWidth: 150,
                defaults: {
                    anchor: '100%',
                    allowBlank: false,
                    blankText: '该项不能为空!'
                },
                items: [
                    {
                        id: 'system.shutdown',
                        text: '关闭设备',
                        name: 'system_shutdown',
                        xtype: 'button',
                        handler: function () {
                            Ext.Ajax.request({
                                url: "../SystemManagerServlet",
                                method: "POST",
                                params: {command: "shutdown"},
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
                    }
                ]
            },
            {
                xtype: 'fieldset',
                collapsible: true,
                title: '日志下载',
                defaultType: 'textfield',
                labelWidth: 150,
                defaults: {
                    anchor: '100%',
                    allowBlank: false,
                    blankText: '该项不能为空!'
                },
                items: [
                    {
                        id: 'system.log',
                        text: '日志下载',
                        name: 'system.log',
                        xtype: 'button',
                        handler: function () {
                            if (!Ext.fly('test')) {
                                var frm = document.createElement('form');
                                frm.id = 'test';
                                frm.style.display = 'none';
                                document.body.appendChild(frm);
                            }
                            Ext.Ajax.request({
                                url: "../SystemManagerServlet?command=downLog",
                                method: "GET",
                                form: Ext.fly('test'),
                                isUpload: true
                            });
                        }
                    }
                ]
            }
        ]
    });


    var panel_check = new Ext.Panel({
        frame: true,
        renderTo: "content",
        plain: true,
        border: true,
        height:setHeight(),
        autoScroll: true,
        items: [
            {
                xtype: 'fieldset',
                title: '系统管理',
                items: [formPanel]
            }
        ]
    });
});

function setHeight(){
    return document.getElementById("content").offsetHeight;
}