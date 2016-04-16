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
        var cn = store.getAt(0).get('cn');
        var idCard = store.getAt(0).get('idCard');
        var province = store.getAt(0).get('province');
        var city = store.getAt(0).get('city');
        var organization = store.getAt(0).get('organization');
        var institution = store.getAt(0).get('institution');
        var phone = store.getAt(0).get('phone');
        var address = store.getAt(0).get('address');
        var email = store.getAt(0).get('email');
        var status = store.getAt(0).get('status');
        Ext.getCmp('add_x509User_cn').setValue(cn);
        Ext.getCmp('add_x509User_idCard').setValue(idCard);
        Ext.getCmp('add_x509User_province').setValue(province);
        Ext.getCmp('add_x509User_city').setValue(city);
        Ext.getCmp('add_x509User_organization').setValue(organization);
        Ext.getCmp('add_x509User_institution').setValue(institution);
        Ext.getCmp('add_x509User_phone').setValue(phone);
        Ext.getCmp('add_x509User_address').setValue(address);
        Ext.getCmp('add_x509User_email').setValue(email);
        if(status=="1"){
            Ext.getCmp('check.request.info').setDisabled(true);
        }else{
            Ext.getCmp('check.down.info').setDisabled(true);
        }
    });

    var province_store = new Ext.data.SimpleStore({
            fields : ['id', 'name'],
            data : [
                ["河南省","河南省"],
                ["河北省","河北省"],
                ["山西省","山西省"],
                ["山东省","山东省"],
                ["湖南省","湖南省"],
                ["湖北省","湖北省"],
                ["四川省","四川省"],
                ["辽宁省","辽宁省"],
                ["吉林省","吉林省"],
                ["黑龙江省","黑龙江省"],
                ["广东省","广东省"],
                ["浙江省","浙江省"],
                ["福建省","福建省"],
                ["甘肃省","甘肃省"],
                ["江西省","江西省"],
                ["陕西省","陕西省"],
                ["海南省","海南省"],
                ["江苏省","江苏省"],
                ["安徽省","安徽省"],
                ["云南省","云南省"],
                ["贵州省","贵州省"],
                ["天津市","天津市"],
                ["北京市","北京市"],
                ["上海市","上海市"],
                ["重庆市","重庆市"],
                ["新疆维吾尔族自治区","新疆维吾尔族自治区"],
                ["内蒙古自治区","内蒙古自治区"],
                ["西藏自治区","西藏自治区"],
                ["广西壮族自治区","广西壮族自治区"],
                ["宁夏回族自治区","宁夏回族自治区"],
                ["香港特别行政区","香港特别行政区"],
                ["澳门特别行政区","澳门特别行政区"]
            ]
        });

    var apply_formPanel = new Ext.form.FormPanel({
        autoScroll: true,
        labelWidth: 150,
        labelAlign: 'right',
        defaultWidth: 450,
        autoWidth: true,
        layout: 'form',
        border: false,
        defaults: {
            width: 450,
            allowBlank: false,
            anchor:'95%',
            blankText: '该项不能为空！'
        },
        items: [
            new Ext.form.TextField({
                fieldLabel: '姓名',
                name: 'cn',
                id:"add_x509User_cn",
                emptyText:"请输入用户姓名",
                regex: /^[a-zA-Z0-9\u4e00-\u9fa5]+$/,
                regexText: '只能输入数字,字母,中文!',
                id: 'add_x509User_cn',
                allowBlank: false,
                blankText: "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel: '身份证号码',
                emptyText:"请输入有效的身份证号",
                id: 'add_x509User_idCard',
                regex: /^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})([0-9xX])$/,
                regexText: '请输入有效的身份证号',
                allowBlank: false,
                blankText: "请填写数字 ，不能为空，请正确填写",
                name: 'idCard'
            }),
            new Ext.form.ComboBox({
//                mode: 'remote',// 指定数据加载方式，如果直接从客户端加载则为local，如果从服务器断加载// 则为remote.默认值为：remote
                border: true,
                frame: true,
//                pageSize: 10,// 当元素加载的时候，如果返回的数据为多页，则会在下拉列表框下面显示一个分页工具栏，该属性指定每页的大小
                // 在点击分页导航按钮时，将会作为start及limit参数传递给服务端，默认值为0，只有在mode='remote'的时候才能够使用
                editable: false,
                fieldLabel: '省/行政区',
                emptyText: '请选择所在省/行政区',
                id: 'add_x509User_province',
//                hiddenName : 'x509User.province',
                triggerAction: "all",// 是否开启自动查询功能
                store: province_store,// 定义数据源
                displayField: "name", // 关联某一个逻辑列名作为显示值
                valueField: "name", // 关联某一个逻辑列名作为显示值
//                valueField: "id", // 关联某一个逻辑列名作为实际值
                mode : "local",// 如果数据来自本地用local 如果来自远程用remote默认为remote
                name: 'province',
                allowBlank: false,
                blankText: "请选择所在省/行政区"
            }),
            new Ext.form.TextField({
                fieldLabel: '城市/乡镇',
                name: 'city',
                emptyText:"请输入所在城市/乡镇",
                regex: /^[a-zA-Z0-9\u4e00-\u9fa5]+$/,
                regexText: '只能输入数字,字母,中文!',
                id: 'add_x509User_city',
                allowBlank: false,
                blankText: "不能为空，请正确填写所在城市/乡镇"
            }),
            new Ext.form.TextField({
                fieldLabel: '公司/团体',
                emptyText:"请输入所在公司/团体",
                allowBlank: false,
                id: 'add_x509User_organization',
                regex: /^[a-zA-Z0-9\u4e00-\u9fa5]+$/,
                regexText: '只能输入数字,字母,中文!',
                blankText: "不能为空，请正确填写",
                name: 'organization'
            }),
            new Ext.form.TextField({
                fieldLabel: '部门/机构',
                emptyText:"请输入所在部门/机构",
                id: 'add_x509User_institution',
                allowBlank: false,
                regex: /^[a-zA-Z0-9\u4e00-\u9fa5]+$/,
                regexText: '只能输入数字,字母,中文!',
                blankText: "不能为空，请正确填写",
                name: 'institution'
            }),
            new Ext.form.TextField({
                emptyText:"请输入联系电话",
                fieldLabel: '联系电话',
                name: 'phone',
                id: 'add_x509User_phone',
                regex: /^(1[3,5,8,7]{1}[\d]{9})|(((400)-(\d{3})-(\d{4}))|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{3,7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)$/,
                regexText: '请输入正确的电话号或手机号',
                allowBlank: false,
                blankText: "联系电话"
            }),
            new Ext.form.TextField({
                emptyText:"请输入联系地址",
                id: 'add_x509User_address',
                fieldLabel: '联系地址',
                regex: /^[a-zA-Z0-9\u4e00-\u9fa5]+$/,
                regexText: '只能输入数字,字母,中文!',
                name: 'address',
                allowBlank: false,
                blankText: "联系地址"
            }),
            new Ext.form.TextField({
                fieldLabel: '电子邮件',
                emptyText:"请输入电子邮件",
                regex: /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
                regexText: '请输入有效的邮件地址',
                name: 'email',
                id: 'add_x509User_email',
                allowBlank: false,
                blankText: "电子邮件"
            })
        ],
        buttons:[
            {
                id:'check.request.info',
                text:'证书申请',
                handler:function(){
                    if(apply_formPanel.form.isValid()){
                        apply_formPanel.getForm().submit({
                            url:'../CertificateServlet',
                            method :'POST',
                            params:{command:"requestCertificate"},
                            waitTitle:'信息',
                            waitMsg:'申请中,请稍后...',
                            success:function(form,action){
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:flag,
                                    animEl:'check.request.info',
                                    width:250,
                                    buttons:{'ok':'确定'},
                                    fn:function(){
                                        Ext.getCmp("check.request.info").setDisabled(true);
                                        Ext.getCmp("check.down.info").setDisabled(false);
                                    },
                                    icon:Ext.MessageBox.INFO,
                                    closable:false
                                });
                            },
                            failure:function(form, action){
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:flag,
                                    animEl:'check.request.info',
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
                id:'check.down.info',
                text:'证书下载',
                handler:function(){
                    if(apply_formPanel.form.isValid()){
                        apply_formPanel.getForm().submit({
                            url:'../CertificateServlet',
                            params:{command:"downCertificate"},
                            method :'POST',
                            waitTitle:'信息',
                            waitMsg:'正在下载,请稍后...',
                            success:function(form,action){
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:flag,
                                    animEl:'check.down.info',
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
                                    animEl:'check.down.info',
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
                id:'check.restore.info',
                text:'重新申请证书',
                handler:function(){
                    if(apply_formPanel.form.isValid()){
                        apply_formPanel.getForm().submit({
                            url:'../CertificateServlet',
                            params:{command:"restoreCertificate"},
                            method :'POST',
                            waitTitle:'信息',
                            waitMsg:'请稍后...',
                            success:function(form,action){
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:flag,
                                    animEl:'check.restore.info',
                                    width:250,
                                    buttons:{'ok':'确定'},
                                    fn:function(){
                                        Ext.getCmp("check.request.info").setDisabled(false);
                                        Ext.getCmp("check.down.info").setDisabled(true);
                                    },
                                    icon:Ext.MessageBox.INFO,
                                    closable:false
                                });
                            },
                            failure:function(form, action){
                                var flag = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:flag,
                                    animEl:'check.restore.info',
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

    var panel_cert = new Ext.Panel({
        frame:true,
        renderTo:"content",
        plain:true,
        border:true,
        height:setHeight(),
        autoScroll:true,
        items:[{
            xtype:'fieldset',
            title:'证书管理',
            items:[apply_formPanel]
        }]
    });
});

function setHeight(){
    return document.getElementById("content").offsetHeight;
}