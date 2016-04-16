Ext.onReady(function(){
    Ext.BLANK_IMAGE_URL = '../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var record = new Ext.data.Record.create([
        {name:'ip', mapping:'ip'} ,
        {name:'port', mapping:'port'} ,
        {name:'cn', mapping:'cn'},
        {name:'idCard', mapping:'idCard'},
        {name:'province', mapping:'province'} ,
        {name:'city', mapping:'city'} ,
        {name:'organization', mapping:'organization'},
        {name:'institution', mapping:'institution'},
        {name:'phone', mapping:'phone'} ,
        {name:'address', mapping:'address'} ,
        {name:'email', mapping:'email'},
        {name:'status', mapping:'status'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../CertificateServlet?command=find"
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
        var ip = store.getAt(0).get('ip');
        var port = store.getAt(0).get('port');
        Ext.getCmp('check.ip.info').setValue(ip);
        Ext.getCmp('check.port.info').setValue(port);
    });


    var check_formPanel = new Ext.form.FormPanel({
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
        items:[{
            id:'check.ip.info',
            fieldLabel:'服务器IP',
            xtype:'textfield',
            name:'ip',
            regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
            regexText:'这个不是Ip',
            emptyText:'请输入Ip'
        },{
            id:'check.port.info',
            fieldLabel:'服务器端口',
            xtype:'textfield',
            name:'port',
            regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
            regexText:'这个不是端口类型1~65536',
            emptyText:'请输入端口1~65536'
        }],
        buttons:[
            {
                id:'check.test.info',
                text:'测试',
                handler:function(){
                    if(check_formPanel.form.isValid()){
                        check_formPanel.getForm().submit({
                            url:'../CertificateServlet',
                            method :'POST',
                            params:{command:"checkCertificate"},
                            waitTitle:'信息',
                            waitMsg:'正在测试,请稍后...',
                            success:function(form,action){
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:flag,
                                    animEl:'check.test.info',
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
                                    animEl:'check.test.info',
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
                id:'check.save.info',
                text:'保存',
                handler:function(){
                    if(check_formPanel.form.isValid()){
                        check_formPanel.getForm().submit({
                            url:'../CertificateServlet',
                            params:{command:"saveCertificate"},
                            method :'POST',
                            waitTitle:'信息',
                            waitMsg:'正在测试,请稍后...',
                            success:function(form,action){
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:flag,
                                    animEl:'check.save.info',
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
                                    animEl:'check.test.info',
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

    var panel_check = new Ext.Panel({
        frame:true,
        renderTo:"content",
        plain:true,
        border:true,
        height:setHeight(),
        autoScroll:true,
        items:[{
            xtype:'fieldset',
            title:'证书颁发服务器测试',
            items:[check_formPanel]
        }]
    });



});

function setHeight(){
    return document.getElementById("content").offsetHeight;
}