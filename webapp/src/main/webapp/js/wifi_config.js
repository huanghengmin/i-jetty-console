Ext.onReady(function(){
    Ext.BLANK_IMAGE_URL = '../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';


    var record = new Ext.data.Record.create([
        {name:'ssid', mapping:'ssid'},
        {name:'pwd', mapping:'pwd'},
        {name:'secret', mapping:'secret'},
        {name:'status', mapping:'status'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../WifiServlet?command=find"
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
        var ssid = store.getAt(0).get('ssid');
        var pwd = store.getAt(0).get('pwd');
        var secret = store.getAt(0).get('secret');
        Ext.getCmp('wifi.ssid').setValue(ssid);
        Ext.getCmp('wifi.secret').setValue(secret);
        Ext.getCmp('wifi.pwd').setValue(pwd);
        if(secret=="0"){
            var pwd =  Ext.getCmp("wifi.pwd");
            pwd.getEl().up('.x-form-item').setDisplayed(false);
            var pwdInfo =  Ext.getCmp("wifi.pwd.info");
            pwdInfo.getEl().up('.x-form-item').setDisplayed(false);
            var pwdShow =  Ext.getCmp("wifi.show");
            pwdShow.getEl().up('.x-form-item').setDisplayed(false);
        }
        var status = store.getAt(0).get('status');
        if("on"==status){
            var checkbox = Ext.getCmp("wifi.start");
            checkbox.setValue(true);
        }
    });


    var pwdField = new Ext.form.TextField({
        fieldLabel : '密码',
        id:"wifi.pwd",
        name : 'pwd',
        minLength: 8,
        minLengthText:'密码不能少于8位',
        maxLength: 30,
        maxLengthText:'密码不能超过30位',
        inputType: 'password',    //密码
        allowBlank : true,
        blankText : "不能为空，请正确填写"
    });

    var secret_store = new Ext.data.JsonStore({
        fields:[ "id", "name" ],
        url:'../WifiServlet?command=secret',
        autoLoad:true,
        root:"rows" ,
        listeners : {
            load : function(store, records, options) {// 读取完数据后设定默认值
                var value =Ext.getCmp("wifi.secret").getValue();
                if(value!=null)
                    Ext.getCmp("wifi.secret").setValue(value);
            }
        }
    });

    var wifi_form = new Ext.form.FormPanel({
        plain:true,
        labelWidth:150,
        border:false,
        loadMask : { msg : '正在加载数据，请稍后.....' },
        labelAlign:"right",
        buttonAlign:'right',
        defaults : {
//            allowBlank : false,
            anchor:'95%',
            blankText : '该项不能为空！'
        },
        items: [
                new Ext.form.TextField({
                    fieldLabel : 'SSID',
                    name : 'ssid',
                    id:"wifi.ssid",
                    allowBlank : false,
                    blankText : "不能为空，请正确填写"
                }),
                new Ext.form.ComboBox({
                    border: true,
                    frame: true,
                    editable: false,
                    fieldLabel : '安全性',
                    id: 'wifi.secret',
                    triggerAction: "all",// 是否开启自动查询功能
                    store: secret_store,// 定义数据源
                    displayField: "name", // 关联某一个逻辑列名作为显示值
                    valueField: "id", // 关联某一个逻辑列名作为显示值
//                    mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
//                    name: 'secret',
                    hiddenName:'secret',
                    allowBlank: false,
                    blankText: "请选择",
                    listeners:{
                        select:function(){
                            if(this.getValue()=="0"){
                                var pwd =  Ext.getCmp("wifi.pwd");
                                pwd.getEl().up('.x-form-item').setDisplayed(false);
                                var pwdInfo =  Ext.getCmp("wifi.pwd.info");
                                pwdInfo.getEl().up('.x-form-item').setDisplayed(false);
                                var pwdShow =  Ext.getCmp("wifi.show");
                                pwdShow.getEl().up('.x-form-item').setDisplayed(false);
                            }else if(this.getValue()=="1"){
                                var pwd =  Ext.getCmp("wifi.pwd");
                                pwd.getEl().up('.x-form-item').setDisplayed(true);
                                var pwdInfo =  Ext.getCmp("wifi.pwd.info");
                                pwdInfo.getEl().up('.x-form-item').setDisplayed(true);
                                var pwdShow =  Ext.getCmp("wifi.show");
                                pwdShow.getEl().up('.x-form-item').setDisplayed(true);
                            }
                        }
                    }
                }),
                pwdField,
                new Ext.form.DisplayField({
                    id : "wifi.pwd.info",
                    value : "密码长度至少应包含8个字符,建议至少包含两种字符(小写、大写、数字或特殊字符)组合"
                }),
                new Ext.form.Checkbox({
                    id : "wifi.show",
                    name : "show",
                    autoScroll : false,
                    boxLabel : "显示密码",
                    inputValue : 1,
                    listeners : { "check" : function(obj,ischecked){
                        if (ischecked) {
                            pwdField.getEl().set({type:'text'});
                        } else {
                            pwdField.getEl().set({type:'password'});
                        }
                    }}
                }),
                new Ext.form.Checkbox({
                    id : "wifi.start",
                    name : "start",
                    autoScroll : false,
                    boxLabel : "启用WLAN热点",
                    inputValue : 1,
                    listeners : { "check" : function(obj,ischecked){
                        if (ischecked) {
                            if(wifi_form.form.isValid()){
                                wifi_form.getForm().submit({
                                    url: "../WifiServlet",
                                    method: "POST",
                                    params:{command:"startWifi"},
                                    waitTitle:'信息',
                                    waitMsg:'正在开启WLAN热点,请稍后...',
                                    success:function(form,action){
                                        var flag = action.result.msg;
                                        Ext.MessageBox.show({
                                            title:'信息',
                                            msg:flag,
                                            animEl:'wifi.save.info',
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
                                            animEl:'wifi.save.info',
                                            width:250,
                                            buttons:{'ok':'确定'},
                                            icon:Ext.MessageBox.INFO,
                                            closable:false
                                        });
                                    }
                                });
                            }
                        } else {
                            if(wifi_form.form.isValid()){
                                wifi_form.getForm().submit({
                                    url: "../WifiServlet",
                                    method: "POST",
                                    params:{command:"stopWifi"},
                                    waitTitle:'信息',
                                    waitMsg:'正在停止WLAN热点,请稍后...',
                                    success:function(form,action){
                                        var flag = action.result.msg;
                                        Ext.MessageBox.show({
                                            title:'信息',
                                            msg:flag,
                                            animEl:'wifi.save.info',
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
                                            animEl:'wifi.save.info',
                                            width:250,
                                            buttons:{'ok':'确定'},
                                            icon:Ext.MessageBox.INFO,
                                            closable:false
                                        });
                                    }
                                });
                            }
                        }
                    }}
                })
    ],
        buttons: [
            {
                id:'wifi.save.info',
                text:'保存WLAN热点配置',
                handler:function(){
                    if(wifi_form.form.isValid()){
                        wifi_form.getForm().submit({
                            url:'../WifiServlet',
                            params:{command:"saveWifi"},
                            method :'POST',
                            waitTitle:'信息',
                            waitMsg:'正在保存,请稍后...',
                            success:function(form,action){
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:flag,
                                    animEl:'wifi.save.info',
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
                                    animEl:'wifi.save.info',
                                    width:250,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false
                                });
                            }
                        });
                    }
                }
            }
        ]
    });

    var panel_wifi = new Ext.Panel({
        frame:true,
        renderTo:"content",
        plain:true,
        border:true,
        height:setHeight(),
        autoScroll:true,
        items:[{
            xtype:'fieldset',
            title:'WLAN热点配置',
            items:[wifi_form]
        }]
    });
});

function setHeight(){
    return document.getElementById("content").offsetHeight;
}
