Ext.onReady(function(){
    Ext.BLANK_IMAGE_URL = '../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';


    var record = new Ext.data.Record.create([
        {name:'SSLVPNIp', mapping:'SSLVPNIp'} ,
        {name:'strategyPort', mapping:'strategyPort'},
        {name:'connectPort', mapping:'connectPort'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../SSLVPNServlet?command=find"
    });

    var reader = new Ext.data.JsonReader({
        totalProperty:"totalCount",
        root:"root"
    }, record);

    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy:proxy,
        reader:reader
    });

    store.load();
    store.on('load',function(){
        var SSLVPNIp = store.getAt(0).get('SSLVPNIp');
        var strategyPort = store.getAt(0).get('strategyPort');
        var connectPort = store.getAt(0).get('connectPort');
        Ext.getCmp('sslvpn.SSLVPNIp').setValue(SSLVPNIp);
        Ext.getCmp('sslvpn.connectPort').setValue(connectPort);
        Ext.getCmp('sslvpn.strategyPort').setValue(strategyPort);
    });


    var sslvpn_form = new Ext.form.FormPanel({
        plain:true,
        labelWidth:150,
        border:false,
        loadMask : { msg : '正在加载数据，请稍后.....' },
        labelAlign:"right",
        buttonAlign:'right',
        defaults : {
            allowBlank : false,
            anchor:'95%',
            blankText : '该项不能为空！'
        },
        items: [
            new Ext.form.TextField({
                fieldLabel : 'SSLVPN服务IP',
                name : 'SSLVPNIp',
                regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                regexText:'请输入正确的IP地址',
                id:"sslvpn.SSLVPNIp",
                allowBlank : false,
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : 'SSLVPN服务端口',
                id:"sslvpn.connectPort",
                regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                regexText:'请输入正确的端口',
                name : 'connectPort',
                allowBlank : false,
                blankText : "不能为空，请正确填写"
            }),new Ext.form.TextField({
                fieldLabel : 'SSLVPN策略端口',
                id:"sslvpn.strategyPort",
                regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                regexText:'请输入正确的端口',
                name : 'strategyPort',
                allowBlank : false,
                blankText : "不能为空，请正确填写"
            })
        ],
        buttons:[
            {
                id:'sslvpn.down.info',
                text:'更新VPN配置',
                handler:function(){
                    if(sslvpn_form.form.isValid()){
                        sslvpn_form.getForm().submit({
                            url:'../SSLVPNServlet',
                            method :'POST',
                            params:{command:"downVPN"},
                            waitTitle:'信息',
                            waitMsg:'正在测试,请稍后...',
                            success:function(form,action){
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:flag,
                                    animEl:'sslvpn.down.info',
                                    width:250,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false
                                });
                            },
                            failure:function(form, action){
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:flag,
                                    animEl:'sslvpn.down.info',
                                    width:250,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false
                                });
                            }
                        });
                    }
                }
            },
            {
                id:'sslvpn.import.info',
                text:'导入VPN配置',
                handler:function(){
                    if(sslvpn_form.form.isValid()){
                        sslvpn_form.getForm().submit({
                            url:'../SSLVPNServlet',
                            params:{command:"importVPN"},
                            method :'POST',
                            waitTitle:'信息',
                            waitMsg:'正在测试,请稍后...',
                            success:function(form,action){
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:flag,
                                    animEl:'sslvpn.import.info',
                                    width:250,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false
                                });
                            },
                            failure:function(form, action){
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:flag,
                                    animEl:'sslvpn.import.info',
                                    width:250,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false
                                });
                            }
                        });
                    }
                }
            },{
                id:'sslvpn.start.info',
                text:'开启VPN连接',
                handler:function(){
                    if(sslvpn_form.form.isValid()){
                        sslvpn_form.getForm().submit({
                            url:'../SSLVPNServlet',
                            params:{command:"startVPN"},
                            method :'POST',
                            waitTitle:'信息',
                            waitMsg:'正在测试,请稍后...',
                            success:function(form,action){
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:flag,
                                    animEl:'sslvpn.start.info',
                                    width:250,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false
                                });
                            },
                            failure:function(form, action){
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:flag,
                                    animEl:'sslvpn.start.info',
                                    width:250,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false
                                });
                            }
                        });
                    }
                }
            },{
                id:'sslvpn.stop.info',
                text:'关闭VPN连接',
                handler:function(){
                    if(sslvpn_form.form.isValid()){
                        sslvpn_form.getForm().submit({
                            url:'../SSLVPNServlet',
                            params:{command:"stopVPN"},
                            method :'POST',
                            waitTitle:'信息',
                            waitMsg:'正在测试,请稍后...',
                            success:function(form,action){
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:flag,
                                    animEl:'sslvpn.stop.info',
                                    width:250,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false
                                });
                            },
                            failure:function(form, action){
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:flag,
                                    animEl:'sslvpn.stop.info',
                                    width:250,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false
                                });
                            }
                        });
                    }
                }
            }]
    });

    var panel_vpn = new Ext.Panel({
        frame:true,
        renderTo:"content",
        plain:true,
        border:true,
        height:setHeight(),
        autoScroll:true,
        items:[{
            xtype:'fieldset',
            title:'SSLVPN配置',
            items:[sslvpn_form]
        }]
    });
});

function setHeight(){
    return document.getElementById("content").offsetHeight;
}